package com.md.pay.service.impl;


import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.pay.dao.WeiXinMapper;
import com.md.pay.model.WeiXin;
import com.md.pay.service.IWeixinService;
import com.stylefeng.guns.core.util.ToolUtil;

import weixin.popular.api.SnsAPI;
import weixin.popular.api.TokenAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.sns.SnsToken;
import weixin.popular.bean.token.Token;
import weixin.popular.bean.user.User;

@Service
public class WeixinServiceImpl extends ServiceImpl<WeiXinMapper, WeiXin>  implements IWeixinService {

	@Resource
	WeiXinMapper weiXinMapper;
	
	@Override
	public String getAccessToken() {
		Token token = new Token();
		WeiXin weiXin = weiXinMapper.selectById(1);
		String access_token =  weiXin.getAccessToken();
		Long expiresIn = weiXin.getExpiresIn();
		Long nowDate = new Date().getTime();
		if (ToolUtil.isEmpty(access_token) || expiresIn <= nowDate) {
			token = TokenAPI.token(weiXin.getAppid(),weiXin.getSecret());
			weiXin.setAccessToken(token.getAccess_token());
			weiXin.setExpiresIn(new Date().getTime() + token.getExpires_in()*1000);
			weiXinMapper.updateById(weiXin);
			return token.getAccess_token();
		}
		return weiXin.getAccessToken();
	}

	@Override
	public User getWxUserInfo(String access_token, String openid) {
		User user = UserAPI.userInfo(access_token, openid);
		return user;
	}

	@Override
	public WeiXin getInfo() {
		return weiXinMapper.selectById(1);
	}

	@Override
	public String getOpenId(String code, String state) {
		WeiXin weiXin = weiXinMapper.selectById(1);
		SnsToken snsToken = SnsAPI.oauth2AccessToken(weiXin.getAppid(), weiXin.getSecret(), code);
		return snsToken.getOpenid();
	}

	@Override
	public String getApiKey() {
		// TODO 自动生成的方法存根
		WeiXin weiXin = weiXinMapper.selectById(1);
		return weiXin.getApikey();
	}
	
}
