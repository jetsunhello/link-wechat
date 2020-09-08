package com.linkwechat.wecom.service;

import java.util.List;
import com.linkwechat.wecom.domain.WeTagGroup;

/**
 * 标签组Service接口
 * 
 * @author ruoyi
 * @date 2020-09-07
 */
public interface IWeTagGroupService 
{
    /**
     * 查询标签组
     * 
     * @param id 标签组ID
     * @return 标签组
     */
    public WeTagGroup selectWeTagGroupById(Long id);

    /**
     * 查询标签组列表
     * 
     * @param weTagGroup 标签组
     * @return 标签组集合
     */
    public List<WeTagGroup> selectWeTagGroupList(WeTagGroup weTagGroup);

    /**
     * 新增标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    public int insertWeTagGroup(WeTagGroup weTagGroup);

    /**
     * 修改标签组
     * 
     * @param weTagGroup 标签组
     * @return 结果
     */
    public int updateWeTagGroup(WeTagGroup weTagGroup);

    /**
     * 批量删除标签组
     * 
     * @param ids 需要删除的标签组ID
     * @return 结果
     */
    public int deleteWeTagGroupByIds(Long[] ids);

    /**
     * 删除标签组信息
     * 
     * @param id 标签组ID
     * @return 结果
     */
    public int deleteWeTagGroupById(Long id);
}