package com.md.pay.service;

import com.baomidou.mybatisplus.service.IService;
import com.md.member.model.Member;
import com.md.pay.model.WeiXin;

import weixin.popular.bean.user.User;


public interface IWeixinService extends IService<WeiXin>{

	/**
	 * 获取accessToken
	 * @return
	 */
	String getAccessToken ();
	
	/**
	 * 获取详情
	 * @return
	 */
	WeiXin getInfo();

	/**
	 * 获取授权通用户的openid
	 * @return
	 */
	String getOpenId(String code,String state);
	/**
	 * 获取用户微信信息
	 * @param access_token
	 * @param openid
	 * @return
	 */
    User getWxUserInfo(String access_token,String openid) ;
    
    /**
     * 获取商户支付api密钥
     * @return
     */
	String getApiKey();
    
}
