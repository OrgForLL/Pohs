package com.stylefeng.guns.adminapi;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.goods.model.Shop;
import com.md.goods.service.IShopService;
import com.md.order.model.Order;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.md.order.warpper.OrderItemWarpper;
import com.stylefeng.guns.core.base.controller.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 订单接口
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/adminapi/order")
public class AdminApiOrderController extends BaseController {

	@Resource
	IOrderService orderService;
	
	@Resource
	IOrderItemService orderItemService;
	
	@Resource
	IShopService shopService;
	
	@ApiOperation(value = "根据订单ID获取订单详情", notes = "根据订单ID获取订单详情    \r\n"
			+ "actualPay:实际支付金额;  \r\n"
			+ "address:收货地址;  \r\n"
			+ "consigneeName:收件人姓名;  \r\n"
			+ "couponId:用户券ID;  \r\n"
			+ "createTime:订单创建时间;  \r\n"
			+ "diliveryId:物流方式;  \r\n"
			+ "diliveryPay:物流费用;  \r\n"
			+ "gainsTime:确认收货时间;  \r\n"
			+ "due:订单金额;  \r\n"
			+ "id:订单编号;  \r\n"
			+ "unitPrice:单价;  \r\n"
			+ "quantity:数量;  \r\n"
			+ "productId:产品编号;  \r\n"
			+ "actualPrice:实际支付金额;  \r\n"
			+ "goodsId:商品编号;  \r\n"
			+ "orderId:订单编号;  \r\n"
			+ "imageUrl:产品图片地址;  \r\n"
			+ "goodsName:商品名称;  \r\n"
			+ "productName:规格名称;  \r\n"
			+ "memberId:用户编号;  \r\n"
			+ "weight:订单重量;  \r\n"
			+ "shopId:门店编号;  \r\n"
			+ "remark:备注;  \r\n"
			+ "status:订单状态(0, \"待付款\";1, \"待审核\";2, \"待发货\";3,\"待收货\";6,\"待评价\";4, \"交易完成\";-1, \"删除订单\";9,\"待同意退货\";10,\"退货中\";7, \"待退款\";8, \"已退款\";5, \"交易关闭\")\"")
	@RequestMapping(value = "/getOrderDetail", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getOrderDetail (
			@ApiParam("订单Id") @RequestParam(value = "orderId", required = true) @RequestBody Long orderId){
		JSONObject jb = new JSONObject();
		Order order = orderService.getById(orderId);
		Shop shop = shopService.findById(order.getShopId());
		order.setShop(shop);
		List<Map<String, Object>> itemResult = orderItemService.getListByOrderId(order.getId());
		order.setItemObject(super.warpObject(new OrderItemWarpper(itemResult)));
		jb.put("data", order);
		return jb;
	}
	
	@ApiOperation(value = "读取指定时间、指定ID、指定状态、用户id订单信息", notes = "读取指定时间、指定ID、指定状态、用户id订单信息  \r\n"
			+ "actualPay:实际支付金额;  \r\n"
			+ "address:收货地址;  \r\n"
			+ "consigneeName:收件人姓名;  \r\n"
			+ "couponId:用户券ID;  \r\n"
			+ "createTime:订单创建时间;  \r\n"
			+ "diliveryId:物流方式;  \r\n"
			+ "diliveryPay:物流费用;  \r\n"
			+ "gainsTime:确认收货时间;  \r\n"
			+ "due:订单金额;  \r\n"
			+ "id:订单编号;  \r\n"
			+ "unitPrice:单价;  \r\n"
			+ "quantity:数量;  \r\n"
			+ "productId:产品编号;  \r\n"
			+ "actualPrice:实际支付金额;  \r\n"
			+ "goodsId:商品编号;  \r\n"
			+ "orderId:订单编号;  \r\n"
			+ "imageUrl:产品图片地址;  \r\n"
			+ "goodsName:商品名称;  \r\n"
			+ "productName:规格名称;  \r\n"
			+ "memberId:用户编号;  \r\n"
			+ "weight:订单重量;  \r\n"
			+ "shopId:门店编号;  \r\n"
			+ "remark:备注;  \r\n"
			+ "status:订单状态(0, \"待付款\";1, \"待审核\";2, \"待发货\";3,\"待收货\";6,\"待评价\";4, \"交易完成\";-1, \"删除订单\";9,\"待同意退货\";10,\"退货中\";7, \"待退款\";8, \"已退款\";5, \"交易关闭\")\"")
	@RequestMapping(value = "/getOrderListByCondition", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getOrderListByCondition(
			@ApiParam("开始时间") @RequestParam(value = "startTime", required = false) @RequestBody Timestamp startTime,
			@ApiParam("结束时间") @RequestParam(value = "endTime", required = false) @RequestBody Timestamp endTime,
			@ApiParam("用户id") @RequestParam(value = "memberId", required = true) @RequestBody Long memberId,
			@ApiParam("订单id") @RequestParam(value = "orderId", required = true) @RequestBody Long orderId,
			@ApiParam("订单状态") @RequestParam(value = "status", required = false) @RequestBody Integer status,
			@ApiParam("当前页") @RequestParam(value = "index", required = true) @RequestBody Integer index) {
		JSONObject jb = new JSONObject();
		List<Order> orderResult = orderService.findListByCondition(startTime,endTime,memberId,orderId,status,index);
		if(orderResult.size()>0) {
			for(Order order : orderResult) {
				Shop shop = shopService.findById(order.getShopId());
				order.setShop(shop);
				List<Map<String, Object>> itemResult = orderItemService.getListByOrderId(order.getId());
				order.setItemObject(super.warpObject(new OrderItemWarpper(itemResult)));
			}
		}
		jb.put("data", orderResult);
		return jb;
	}
	
	@ApiOperation(value = "修改订单状态", notes = "修改订单状态")
	@RequestMapping(value = "/changeOrderStatus", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeOrderStatus(
			@ApiParam("订单id") @RequestParam(value = "orderId", required = true) @RequestBody Long orderId,
			@ApiParam("状态") @RequestParam(value = "status", required = true) @RequestBody Integer status) {
		JSONObject jb = new JSONObject();
		Order order = orderService.getById(orderId);
		//订单状态判断  需要新增
		order.setStatus(status);
		orderService.update(order);
		jb.put("data", "SUCCESS");
		return jb;
	}
}
