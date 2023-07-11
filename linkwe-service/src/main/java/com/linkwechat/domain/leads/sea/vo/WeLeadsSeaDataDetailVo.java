package com.linkwechat.domain.leads.sea.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;


/**
 * @author caleb
 * @since 2023/5/9
 */
@Data
@HeadRowHeight(30)
@ColumnWidth(20)
public class WeLeadsSeaDataDetailVo {

    //    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ExcelProperty(value = "日期", index = 0)
    private String dateTime;

    @ExcelProperty(value = "总线索量", index = 1)
    private int totalNum;

    @ExcelProperty(value = "总跟进量", index = 2)
    private int allFollowNum;

    @ExcelProperty(value = "当日领取量", index = 3)
    private int todayReceiveNum;

    @ExcelProperty(value = "当日有效跟进", index = 4)
    private int todayFollowNum;

    @ExcelProperty(value = "当日跟进率", index = 5)
    private double todayFollowRatio;

}
