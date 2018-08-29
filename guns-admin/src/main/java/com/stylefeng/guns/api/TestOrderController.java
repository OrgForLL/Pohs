package com.stylefeng.guns.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.goods.service.IGoodsService;
import com.md.member.service.IMemberService;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.HttpPostUrl;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/testOrder")
public class TestOrderController extends BaseController{

	@Resource
	IOrderService orderService;
	
	@Resource
	IMemberService memberService;
	
	@Resource
	IOrderItemService orderItemService;
	
	@Resource
	IGoodsService goodsService;
	
	@Autowired
	GunsProperties gunsProperties;
	
	public static final String user = "1851588926@qq.com";
	public static final String pwd="463111";
	public static final String account= "900003";
	public static final String appKey = "2588c2dc-d622-4e8f-9852-69d3a56fa3cd";
	public static final String appSecret="asq2q2";
	
	
	@ApiOperation(value = "测试", notes = "测试")
	@RequestMapping(value = "/saleDelivery", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject saleDelivery(){
		JSONObject jb = new JSONObject();
		Map<String, String> mapParam = new HashMap<String, String>();
		String data = "{\"MsgTypeID\":3101,\"CreateID\":3100,\"MsgJson\":{\"id\":201801},\"RequestID\":\"\"}";
		mapParam.put("data", data);
		String result = HttpPostUrl.sendPost(gunsProperties.getMessagePath(), mapParam);
		System.out.println(result);
		jb.put("data", result);
		return jb;
	}
	



}
