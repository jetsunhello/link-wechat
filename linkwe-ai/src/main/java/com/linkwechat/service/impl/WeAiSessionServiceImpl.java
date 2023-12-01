package com.linkwechat.service.impl;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.exception.wecom.WeComException;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.WeAiMsgQuery;
import com.linkwechat.service.HunYuanService;
import com.linkwechat.service.IWeAiSessionService;
import com.linkwechat.utils.WeAiSessionUtil;
import com.tencentcloudapi.hunyuan.v20230901.models.ChatStdResponse;
import com.tencentcloudapi.hunyuan.v20230901.models.Choice;
import com.tencentcloudapi.hunyuan.v20230901.models.Delta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@Service
public class WeAiSessionServiceImpl implements IWeAiSessionService {

    @Autowired
    private HunYuanService hunYuanService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Override
    public void createSseConnect() {
        String sessionId = IdUtil.simpleUUID();
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(60 * 1000L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(sessionId));

        WeAiSessionUtil.add(sessionId, sseEmitter);
        log.info("创建新的sse连接，当前session：{}", sessionId);

        try {
            sseEmitter.send(SseEmitter.event().id("sessionId").data(sessionId));
        } catch (IOException e) {
            log.error("SseEmitterServiceImpl[createSseConnect]: 创建长链接异常，客户端ID:{}", sessionId, e);
            throw new WeComException("创建连接异常！");
        }
    }

    @Override
    public void closeSseConnect(String sessionId) {
        WeAiSessionUtil.removeAndClose(sessionId);
    }

    @Override
    public void sendMsg(WeAiMsgQuery query) {
        SseEmitter sseEmitter = WeAiSessionUtil.get(query.getSessionId());
        if (Objects.nonNull(sseEmitter)) {
            sendAiMsg(query);
        } else {
            rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getAiMsgEx(), "", JSONObject.toJSONString(query));
        }
    }


    private Runnable completionCallBack(String sessionId) {
        return () -> {
            log.info("结束连接：{}", sessionId);

            WeAiSessionUtil.removeAndClose(sessionId);
        };
    }


    public void sendAiMsg(WeAiMsgQuery query) {
        hunYuanService.sendMsg(query.getMsgList(), (data) -> {
            SseEmitter sseEmitter = WeAiSessionUtil.get(query.getSessionId());
            if (Objects.nonNull(sseEmitter)) {
                ChatStdResponse response = JSONObject.parseObject(data, ChatStdResponse.class);
                Delta delta = Arrays.stream(response.getChoices()).map(Choice::getDelta).findFirst().orElseGet(null);
                try {
                    sseEmitter.send(SseEmitter.event().name("msg").data(delta));
                } catch (IOException e) {
                    log.error("发送客户端异常 query：{}", JSONObject.toJSONString(query), e);
                }
            }
        });
    }
}
