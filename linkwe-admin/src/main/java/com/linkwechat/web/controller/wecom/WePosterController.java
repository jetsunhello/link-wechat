package com.linkwechat.web.controller.wecom;

import com.linkwechat.common.core.controller.BaseController;
import com.linkwechat.common.core.domain.AjaxResult;
import com.linkwechat.common.utils.SnowFlakeUtil;
import com.linkwechat.wecom.domain.WePoster;
import com.linkwechat.wecom.domain.WePosterFont;
import com.linkwechat.wecom.domain.WePosterSubassembly;
import com.linkwechat.wecom.service.IWePosterFontService;
import com.linkwechat.wecom.service.IWePosterService;
import com.linkwechat.wecom.service.IWePosterSubassemblyService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "wecom/poster/")
public class WePosterController extends BaseController {


    @Resource
    private IWePosterService wePosterService;

    @Resource
    private IWePosterSubassemblyService wePosterSubassemblyService;

    @Resource
    private IWePosterFontService wePosterFontService;

    @PostMapping(value = "insert")
    @Transactional(rollbackFor = RuntimeException.class)
    public AjaxResult insert(@RequestBody WePoster poster){
        wePosterService.generateSimpleImg(poster);
        poster.setId(SnowFlakeUtil.nextId());
        poster.setDelFlag(1);
        wePosterService.saveOrUpdate(poster);
        poster.getPosterSubassemblyList().forEach(wePosterSubassembly -> {
            wePosterSubassembly.setDelFlag(1);
            wePosterSubassembly.setId(SnowFlakeUtil.nextId());
            wePosterSubassembly.setPosterId(poster.getId());
        });
        wePosterSubassemblyService.saveBatch(poster.getPosterSubassemblyList());
        return AjaxResult.success("创建成功");
    }


    @PutMapping(value = "update")
    public AjaxResult update(@RequestBody WePoster poster){
        if(poster.getId() == null){
            return AjaxResult.error("id为空");
        }
        wePosterService.generateSimpleImg(poster);
        wePosterService.saveOrUpdate(poster);
        List<WePosterSubassembly> posterSubassemblyList = wePosterSubassemblyService.lambdaQuery().eq(WePosterSubassembly::getPosterId,poster.getId()).eq(WePosterSubassembly::getDelFlag,1).list();
        Map<Long,WePosterSubassembly> posterSubassemblyMap = posterSubassemblyList.stream().collect(Collectors.toMap(WePosterSubassembly::getId,p->p));
        List<WePosterSubassembly> insertList = new ArrayList<>();
        List<WePosterSubassembly> updateList = new ArrayList<>();
        poster.getPosterSubassemblyList().forEach(wePosterSubassembly -> {
            if(wePosterSubassembly.getId() == null){
                wePosterSubassembly.setId(SnowFlakeUtil.nextId());
                wePosterSubassembly.setPosterId(poster.getId());
                wePosterSubassembly.setDelFlag(1);
                insertList.add(wePosterSubassembly);
            }else {
                posterSubassemblyMap.remove(wePosterSubassembly.getId());
                updateList.add(wePosterSubassembly);
            }
        });
        if(!CollectionUtils.isEmpty(insertList)) {
            wePosterSubassemblyService.saveBatch(insertList);
        }
        if(!CollectionUtils.isEmpty(updateList)) {
            wePosterSubassemblyService.updateBatchById(updateList);
        }
        List<WePosterSubassembly> deleteList = new ArrayList<>(posterSubassemblyMap.values());
        if(!CollectionUtils.isEmpty(deleteList)){
            deleteList.forEach(wePosterSubassembly -> wePosterSubassembly.setDelFlag(0));
            wePosterSubassemblyService.updateBatchById(deleteList);
        }
        return AjaxResult.success("修改成功");
    }

    @GetMapping(value = "list")
    public AjaxResult list(){
        List<WePoster> fontList = wePosterService.lambdaQuery()
                .eq(WePoster::getDelFlag,1)
                .orderByDesc(WePoster::getCreateTime)
                .list();
        return AjaxResult.success(fontList);
    }

    @GetMapping(value = "entity/{id}")
    public AjaxResult entity(@PathVariable Long id){
        return AjaxResult.success(wePosterService.selectOne(id));
    }

    @GetMapping(value = "page")
    public AjaxResult page(){
        startPage();
        List<WePoster> fontList = wePosterService.lambdaQuery()
                .eq(WePoster::getDelFlag,1)
                .orderByDesc(WePoster::getCreateTime)
                .list();
        return AjaxResult.success(getDataTable(fontList));
    }

    @DeleteMapping(value = "delete/{id}")
    public AjaxResult deletePosterFont(@PathVariable Long id){
        wePosterService.lambdaUpdate().set(WePoster::getDelFlag,0).eq(WePoster::getId,id);
        wePosterSubassemblyService.lambdaUpdate().set(WePosterSubassembly::getDelFlag,0).eq(WePosterSubassembly::getPosterId,id).eq(WePosterSubassembly::getFontId,1);
        return AjaxResult.success("删除成功");
    }



}
