package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.wecom.domain.WeCustomerPortrait;
import com.linkwechat.wecom.domain.WeTagGroup;
import com.linkwechat.wecom.service.IWeCustomerService;
import com.linkwechat.wecom.service.IWeTagGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 客户画像相关controller
 * @author: HaoN
 * @create: 2021-03-03 15:10
 **/
@RestController
@RequestMapping("/wecom/portrait")
public class WeCustomerPortraitController extends BaseController {


    @Autowired
    private IWeCustomerService iWeCustomerService;


    @Autowired
    private IWeTagGroupService weTagGroupService;


    /**
     * 根据客户id和当前企业员工id获取员工详细信息
     * @param externalUserid
     * @param operUserid
     * @return
     */
    @GetMapping(value = "/findWeCustomerInfo")
    public AjaxResult findWeCustomerInfo(String externalUserid, String operUserid){

        return AjaxResult.success(
                iWeCustomerService.findCustomerByOperUseridAndCustomerId(externalUserid,operUserid)
        );
    }


    /**
     * 客户画像资料更新
     * @param weCustomerPortrait
     * @return
     */
    @PostMapping(value = "/updateWeCustomerInfo")
    public AjaxResult updateWeCustomerInfo(@RequestBody WeCustomerPortrait weCustomerPortrait){




        iWeCustomerService.updateWeCustomerPortrait(weCustomerPortrait);


        return AjaxResult.success();
    }



    /**
     * 获取当前系统所有可用标签
     * @return
     */
    @GetMapping(value = "/findAllTags")
    public AjaxResult findAllTags(){

        return AjaxResult.success(
                weTagGroupService.selectWeTagGroupList(
                        WeTagGroup.builder()
                                .build()
                )
        );

    }



}
