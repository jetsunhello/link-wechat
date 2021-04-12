package com.linkwechat.wecom.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cn.hutool.core.util.ArrayUtil;
import com.linkwechat.common.config.RuoYiConfig;
import com.linkwechat.common.constant.Constants;
import com.linkwechat.common.constant.WeConstans;
import com.linkwechat.common.core.domain.entity.SysRole;
import com.linkwechat.common.core.domain.entity.SysUser;
import com.linkwechat.common.utils.SecurityUtils;
import com.linkwechat.system.mapper.SysRoleMapper;
import com.linkwechat.system.service.ISysUserService;
import com.linkwechat.wecom.service.IWeAccessTokenService;
import com.linkwechat.wecom.service.IWeCorpAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.linkwechat.wecom.mapper.WeCorpAccountMapper;
import com.linkwechat.common.core.domain.entity.WeCorpAccount;

/**
 * 企业id相关配置Service业务层处理
 * 
 * @author ruoyi
 * @date 2020-08-24
 */
@Service
public class WeCorpAccountServiceImpl implements IWeCorpAccountService {



    @Autowired
    private IWeAccessTokenService iWeAccessTokenService;


    @Autowired
    private IWeCorpAccountService iWeCorpAccountService;

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private RuoYiConfig ruoYiConfig;


    @Autowired
    private SysRoleMapper roleMapper;

    /**
     * 查询企业id相关配置
     * 
     * @param id 企业id相关配置ID
     * @return 企业id相关配置
     */
    @Override
    public WeCorpAccount selectWeCorpAccountById(Long id)
    {
        return iWeCorpAccountService.selectWeCorpAccountById(id);
    }

    /**
     * 查询企业id相关配置列表
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 企业id相关配置
     */
    @Override
    public List<WeCorpAccount> selectWeCorpAccountList(WeCorpAccount wxCorpAccount)
    {
        return iWeCorpAccountService.selectWeCorpAccountList(wxCorpAccount);
    }

    /**
     * 新增企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @Override
    public int insertWeCorpAccount(WeCorpAccount wxCorpAccount)
    {
        int returnCode = iWeCorpAccountService.insertWeCorpAccount(wxCorpAccount);

        if(Constants.SERVICE_STATUS_ERROR<returnCode){
            iSysUserService.insertUser(
                    SysUser.builder()
                            .userName(wxCorpAccount.getCropAccount())
                            .nickName(wxCorpAccount.getCompanyName())
                            .userType(Constants.USER_TYOE_CORP_ADMIN)
                            .roleIds(ArrayUtil.toArray(roleMapper.selectRoleList(SysRole.builder()
                                    .roleKey(Constants.DEFAULT_WECOME_CORP_ADMIN)
                                    .build()).stream().map(SysRole::getRoleId).collect(Collectors.toList()), Long.class))
                            .password(SecurityUtils.encryptPassword(ruoYiConfig.getWeUserDefaultPwd()))
                            .build()
            );
        }

        return returnCode;
    }

    /**
     * 修改企业id相关配置
     * 
     * @param wxCorpAccount 企业id相关配置
     * @return 结果
     */
    @Override
    public int updateWeCorpAccount(WeCorpAccount wxCorpAccount)
    {

        int returnCode = iWeCorpAccountService.updateWeCorpAccount(wxCorpAccount);
        if(Constants.SERVICE_RETURN_SUCCESS_CODE<returnCode){


            iWeAccessTokenService.removeToken();

        }

        return returnCode;
    }



    /**
     * 获取有效的企业id
     *
     * @return 结果
     */
    @Override
    public WeCorpAccount findValidWeCorpAccount() {
        return iWeCorpAccountService.findValidWeCorpAccount();
    }


    /**
     * 启用有效的企业微信账号
     * @param corpId
     */
    @Override
    public int startVailWeCorpAccount(String corpId) {

        int returnCode = iWeCorpAccountService.startVailWeCorpAccount(corpId);

        if(Constants.SERVICE_RETURN_SUCCESS_CODE<returnCode){


            iWeAccessTokenService.removeToken();

        }


        return returnCode;
    }

    @Override
    public int startCustomerChurnNoticeSwitch(String status) {
        WeCorpAccount validWeCorpAccount = findValidWeCorpAccount();
        validWeCorpAccount.setCustomerChurnNoticeSwitch(status);
        return iWeCorpAccountService.updateWeCorpAccount(validWeCorpAccount);
    }

    @Override
    public String getCustomerChurnNoticeSwitch() {
        WeCorpAccount validWeCorpAccount = iWeCorpAccountService.findValidWeCorpAccount();
        String noticeSwitch = Optional.ofNullable(validWeCorpAccount).map(WeCorpAccount::getCustomerChurnNoticeSwitch)
                .orElse(WeConstans.DEL_FOLLOW_USER_SWITCH_CLOSE);
        return noticeSwitch;
    }


    @Override
    public WeCorpAccount findWeCorpByAccount(String corpAccount) {
        return iWeCorpAccountService.findWeCorpByAccount(corpAccount);
    }


}
