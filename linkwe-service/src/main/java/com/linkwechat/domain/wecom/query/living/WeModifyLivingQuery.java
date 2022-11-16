package com.linkwechat.domain.wecom.query.living;

import com.linkwechat.domain.wecom.query.WeBaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author danmo
 * @description 直播接口入参
 * @date 2022/10/10 10:27
 **/
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class WeModifyLivingQuery extends WeBaseQuery {

    /**
     * 直播id，仅允许修改预约状态下的直播id
     */
    @ApiModelProperty("直播id，仅允许修改预约状态下的直播id")
    private String livingid;

    /**
     * 直播的标题，最多支持20个utf8字符
     */
    @ApiModelProperty("直播的标题，最多支持20个utf8字符")
    private String theme;

    /**
     * 直播开始时间的unix时间戳
     */
    @ApiModelProperty("直播开始时间的unix时间戳")
    private Long living_start;

    /**
     * 直播持续时长
     */
    @ApiModelProperty("直播持续时长")
    private Long living_duration;

    /**
     * 直播的类型，0：通用直播，1：小班课，2：大班课，3：企业培训，4：活动直播，默认 0。其中大班课和小班课仅k12学校和IT行业类型能够发起
     */
    @ApiModelProperty("直播的类型，0：通用直播，1：小班课，2：大班课，3：企业培训，4：活动直播，默认 0")
    private Integer type;

    /**
     * 直播的简介，最多支持300个字节，“活动直播”简介通过activity_detail.description控制
     */
    @ApiModelProperty("直播的简介，最多支持300个字节，“活动直播”简介通过activity_detail.description控制")
    private String description;

    /**
     * 指定直播开始前多久提醒用户，相对于living_start前的秒数，默认为0
     */
    @ApiModelProperty("指定直播开始前多久提醒用户，相对于living_start前的秒数，默认为0")
    private Integer remind_time;

}
