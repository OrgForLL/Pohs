package com.stylefeng.guns.api;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.md.member.service.IMemberService;
import com.md.order.model.Order;
import com.md.order.service.IOrderService;
import com.md.pay.service.IWeixinService;
import com.md.pay.service.IwxPayService;
import com.stylefeng.guns.core.base.controller.BaseController;

import weixin.popular.bean.paymch.MchBaseResult;
import weixin.popular.bean.paymch.MchPayNotify;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;


@Controller
@RequestMapping("/api/weixin")
public class ApiWeixinController extends BaseController{

	@Resource
	IMemberService memberService;
	
	@Resource
	IWeixinService weixinService;
	
	@Resource
	IOrderService orderServiceImpl;
	 
	@Resource
	IwxPayService wxPayServiceImpl;
	private static final String REDIRECT_INDEX = "redirect:http://www.baidu.com";  //商城首页
	
	@RequestMapping(value = "/redirectUrl", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public ModelAndView redirectUrl(String code, String state, String configKey) {
		String openid = weixinService.getOpenId(code, state,configKey);
		return new ModelAndView(REDIRECT_INDEX+"?openId="+openid);
	}
}
