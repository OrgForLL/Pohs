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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.md.delivery.dao.DeliveryModeMapper;
import com.md.delivery.model.DeliveryCost;
import com.md.delivery.model.DeliveryMode;
import com.md.goods.model.Product;
import com.md.goods.service.IGoodsService;
import com.md.goods.service.IProductService;
import com.md.member.dao.AddressMapper;
import com.md.member.model.Address;
import com.md.member.service.IMemberService;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.md.settlement.service.IAccountService;
import com.md.settlement.service.impl.AccountServiceImpl;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.HttpPostUrl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
	IAccountService accountServiceImpl;
	
	@Resource
	IGoodsService goodsService;
	
	@Autowired
	GunsProperties gunsProperties;
	
	@Resource
	IProductService productService;
	
	@Resource
	DeliveryModeMapper deliveryModeMapper;
	@Resource
	AddressMapper addressMapper;
	
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
		Product product = productService.findById(312l);
		String data = "{\"MsgTypeID\":3102,\"CreateID\":3100,\"MsgJson\":{\"productId\":"+333+
				",\"shopId\":"+2+",\"goodsId\":"+222+",\"sn\":"+"\"sn123\""+",\"productbarcode\":"+"\""+product.getBarcode()+"\""+
				",\"productname\":"+"\""+product.getName()+"\""+
				",\"inventory\":"+12+",\"threshold\":"+12+"},\"RequestID\":\"\"}";
		mapParam.put("data", data);
		String result = HttpPostUrl.sendPost(gunsProperties.getMessagePath(), mapParam);
		System.out.println(result);
		jb.put("data", result);
		return jb;
	}
	
	@ApiOperation(value = "配送结算测试", notes = "配送结算测试")
	@RequestMapping(value = "/getDeliveryCost", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getDeliveryCost(
			@ApiParam("deliveryModeId") @RequestParam(value = "deliveryModeId", required = true)Long deliveryModeId,
			@ApiParam("addressId") @RequestParam(value = "addressId", required = true)Long addressId){
		JSONObject jb = new JSONObject();
		DeliveryMode deliveryMode = deliveryModeMapper.selectById(deliveryModeId);
		Address address = addressMapper.selectById(addressId);
		Long shopId = new Long(1);
		DeliveryCost deliveryCost =  accountServiceImpl.getDeliveryCost(deliveryMode, address, shopId);
		jb.put("data", deliveryCost);
		return jb;
	}
	



}
