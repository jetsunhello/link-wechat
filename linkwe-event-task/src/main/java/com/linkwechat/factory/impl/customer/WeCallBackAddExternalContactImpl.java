package com.linkwechat.factory.impl.customer;

import com.alibaba.fastjson.JSONObject;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.enums.WelcomeMsgTypeEnum;
import com.linkwechat.common.utils.StringUtils;
import com.linkwechat.config.rabbitmq.RabbitMQSettingConfig;
import com.linkwechat.domain.wecom.callback.WeBackBaseVo;
import com.linkwechat.domain.wecom.callback.WeBackCustomerVo;
import com.linkwechat.factory.WeEventStrategy;
import com.linkwechat.service.IWeCustomerService;
import com.linkwechat.service.IWeTaskFissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author danmo
 * @description 新增客户事件
 * @date 2021/1/20 23:18
 **/
@Slf4j
@Component("add_external_contact")
public class WeCallBackAddExternalContactImpl extends WeEventStrategy {

    @Autowired
    private IWeCustomerService weCustomerService;

    @Autowired
    private IWeTaskFissionService weTaskFissionService;

    @Autowired
    private RabbitMQSettingConfig rabbitMQSettingConfig;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void eventHandle(WeBackBaseVo message) {
        WeBackCustomerVo customerInfo = (WeBackCustomerVo) message;

        try {
            weCustomerService.addCustomer(customerInfo.getExternalUserID(),customerInfo.getUserID(),customerInfo.getState());
            if(StringUtils.isNotEmpty(customerInfo.getWelcomeCode())){
                //发送欢迎语
                rabbitTemplate.convertAndSend(rabbitMQSettingConfig.getWeWelcomeMsgEx(),rabbitMQSettingConfig.getWeCustomerWelcomeMsgRk(), JSONObject.toJSONString(customerInfo));
            }
        } catch (Exception e) {
            log.error("添加外部联系人异常 params:{}",JSONObject.toJSONString(message),e);
        }


        //任务宝裂变客户处理
        if (StringUtils.isNotEmpty(customerInfo.getState()) && isFission(customerInfo.getState())) {
            try {
                String fissionRecordId = customerInfo.getState().substring(WelcomeMsgTypeEnum.FISSION_PREFIX.getType().length());
                weTaskFissionService.addCustomerHandler(customerInfo.getExternalUserID(),customerInfo.getUserID(),fissionRecordId);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("添加外部联系人异常 任务宝裂变客户处理 params:{}",JSONObject.toJSONString(message),e);
            }
        }

    }

}
