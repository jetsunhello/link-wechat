package com.linkwechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.linkwechat.domain.WeFlowerCustomerTagRel;

import java.util.List;

public interface IWeFlowerCustomerTagRelService extends IService<WeFlowerCustomerTagRel> {

    /**
     * 批量保存或更新
     *
     * @param tagRels
     */
    void batchAddOrUpdate(List<WeFlowerCustomerTagRel> tagRels);


    /**
     * 获取当天新增的标签
     *
     * @param externalUserId
     * @param userId
     * @return
     */
    List<WeFlowerCustomerTagRel> findNowAddWeFlowerCustomerTagRel(String externalUserId, String userId);


    /**
     * 获取移除的标签
     *
     * @param externalUserId
     * @param userId
     * @return
     */
    List<WeFlowerCustomerTagRel> findRemoveWeFlowerCustomerTagRel(String externalUserId, String userId);

    /**
     * 通过标签Id和用户Id获取客户的数量 （不去重）
     *
     * @param tagIds  标签Id
     * @param userIds 用户Id
     * @return {@link List<String>}
     * @author WangYX
     * @date 2023/06/13 16:32
     */
    List<String> getCountByTagIdAndUserId(List<String> tagIds, List<String> userIds);


    /**
     * 通过标签Id和用户Id获取客户 （不去重）
     *
     * @param tagIds  标签Id集合
     * @param userIds 企微用户Id集合
     * @return {@link List<WeFlowerCustomerTagRel>}
     * @author WangYX
     * @date 2023/06/30 18:35
     */
    List<WeFlowerCustomerTagRel> getListByTagIdAndUserId(List<String> tagIds, List<String> userIds);


}
