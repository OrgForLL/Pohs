/*package com.stylefeng.guns.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.goods.model.Goods;
import com.md.goods.service.IGoodsService;
import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.md.order.model.Order;
import com.md.order.model.OrderItem;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.stylefeng.guns.api.dto.Customer;
import com.stylefeng.guns.api.dto.Inventory;
import com.stylefeng.guns.api.dto.Param;
import com.stylefeng.guns.api.dto.SaleObject;
import com.stylefeng.guns.api.dto.SaleOrder;
import com.stylefeng.guns.api.dto.SaleOrderDetails;
import com.stylefeng.guns.api.dto.Unit;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.DateUtil;

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
	IGoodsService goodsService;
	
	public static final String user = "1851588926@qq.com";
	public static final String pwd="463111";
	public static final String account= "900003";
	public static final String appKey = "2588c2dc-d622-4e8f-9852-69d3a56fa3cd";
	public static final String appSecret="asq2q2";
	
	
	*//**
	 * 测试
	 * 
	 * @param memberId
	 * @return
	 * @throws Exception 
	 *//*
	@ApiOperation(value = "销货单", notes = "销货单")
	@RequestMapping(value = "/saleDelivery", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject saleDelivery(
			@ApiParam("订单Id，必填") @RequestParam(value = "sn", required = true) @RequestBody String sn) throws Exception {
		JSONObject jb = new JSONObject();
		SaleObject object = new SaleObject(); 
		Param param = new Param();
		List<SaleOrder> dtos = new ArrayList<>();
		List<Order> orderResult = orderService.findOfSn(sn);
		for(Order order :orderResult) {
			SaleOrder saleD = new SaleOrder();
			Member member = memberService.findById(order.getMemberId());
			saleD.setAddress(order.getAddress());
			saleD.setContactPhone(order.getPhoneNum());
			Customer customer = new Customer();
			customer.setCode(member.getCustomer());
			saleD.setCustomer(customer);
			saleD.setExternalCode(order.getId().toString());
			saleD.setLinkMan(order.getConsigneeName());
			saleD.setMemo("");
			saleD.setVoucherDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
			List<SaleOrderDetails> saleOrderDetails = new ArrayList<>();
			for(OrderItem item : orderItemService.findByOrderId(order.getId())) {
				SaleOrderDetails saleDetial = new SaleOrderDetails();
				saleDetial.setOrigPrice(item.getUnitPrice());
				saleDetial.setQuantity(new BigDecimal(item.getQuantity()));
				Unit unit = new Unit();
				Goods good = goodsService.findById(item.getGoodsId());
				unit.setName(good.getUnit());
				saleDetial.setUnit(unit);
				Inventory inventory = new Inventory();
				inventory.setCode("T_123");
				saleDetial.setInventory(inventory);
				saleOrderDetails.add(saleDetial);
			}
			saleD.setSaleOrderDetails(saleOrderDetails);
			dtos.add(saleD);
		}
		object.setParam(param);
		object.setDtos(dtos);
		String jsonStr = "";
		String json = JSONObject.toJSONString(object);
		System.out.println(json);
//		openAPI api = new openAPI("http://222.77.181.10:8001/tplus/api/v1/", appKey, appSecret);
//		jsonStr = api.get("Authorization/Logout"); //登出方法
		//jsonStr = api.Login(user, pwd, account);
//		jsonStr = api.getData("saleOrder/CreateBatch", json);
		System.out.println(jsonStr);
		JSONObject result = JSONObject.parseObject(jsonStr);
		JSONObject data = JSONObject.parseObject(result.get("data").toString());
		System.out.println(data.get("StatusCode"));
		return jb;
	}

}
*/