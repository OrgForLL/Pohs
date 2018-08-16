package com.stylefeng.guns.rest.modular.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.cart.model.Cart;
import com.md.cart.model.CartItem;
import com.md.cart.service.ICartItemService;
import com.md.cart.service.ICartService;
import com.md.goods.model.PriceTag;
import com.md.goods.model.Product;
import com.md.goods.model.Shop;
import com.md.goods.service.IGoodsService;
import com.md.goods.service.IPriceTagService;
import com.md.goods.service.IProductService;
import com.md.goods.service.IShopService;
import com.md.goods.service.IUploadFileService;
import com.md.member.service.IMemberService;
import com.md.order.constant.OrderStatus;
import com.md.order.model.Evaluation;
import com.md.order.model.Order;
import com.md.order.model.OrderItem;
import com.md.order.model.RefundApply;
import com.md.order.model.Shipping;
import com.md.order.service.IEvaluationService;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.md.order.service.IRefundApplyService;
import com.md.order.service.IShippingService;
import com.md.order.warpper.OrderItemWarpper;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.HttpPostUrl;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.rest.modular.order.dto.EvaluationRequest;
import com.stylefeng.guns.rest.modular.order.dto.OrderRequest;

import io.swagger.annotations.ApiOperation;

/**
 * 订单控制器
 * @author 54476
 *
 */
@Controller
@RequestMapping("/order")
public class ApiOrderController extends BaseController{

	public static final String user = "1851588926@qq.com";
	public static final String pwd="463111";
	public static final String account= "900003";
	public static final String appKey = "2588c2dc-d622-4e8f-9852-69d3a56fa3cd";
	public static final String appSecret="asq2q2";
	
	
	@Resource
	IEvaluationService evaluationService;
	
	@Resource
	IOrderItemService orderItemService;
	
	@Resource
	IOrderService orderService;
	
	@Resource
	IProductService productService;
	
	@Resource
	IShopService shopService;
	
	@Resource
	IGoodsService goodsService;
	
	@Resource
	IPriceTagService priceTagService;
	
	@Resource
	IUploadFileService uploadFileService;
	
	@Resource
	IRefundApplyService refundApplyService;
	
	@Resource
	ICartService cartService;
	
	@Resource
	ICartItemService cartItemService;
	
	@Resource
	IMemberService memberService;
	
	@Resource
	IShippingService shippingService;
	
	@ApiOperation(value = "添加评价", notes = "添加评价")
	@RequestMapping(value = "/addEvaluation", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> addEvaluation(@RequestBody OrderRequest orderRequest) {
		for(EvaluationRequest evalution : orderRequest.getEvaluationList()) {
			OrderItem orderItem = orderItemService.selectById(evalution.getOrderItemId());
			Order order = orderService.getById(orderItem.getOrderId());
			Product product = productService.findById(orderItem.getProductId());
			Evaluation evaluation = new Evaluation();
			evaluation.setCreateTime(DateUtil.format(new Date()));
			evaluation.setDetail(evalution.getDetail());
			evaluation.setLevel(evalution.getLevel());
			evaluation.setGoodsId(orderItem.getGoodsId());
			evaluation.setMemberId(orderRequest.getMemberId());
			evaluation.setOrderItemId(evalution.getOrderItemId());
			evaluation.setShopId(order.getShopId());
			evaluation.setSpecName(product.getName());
			evaluationService.insert(evaluation);
			order.setStatus(OrderStatus.TRADE_SUCCESS.getCode());
			orderService.updateById(order);
			Map<String, String> mapParam = new HashMap<String, String>();
	  		String data = "{\"MsgTypeID\":3100,\"CreateID\":3100,\"MsgJson\":{\"orderId\":"+order.getId()+",\"status\":4},\"RequestID\":\"\"}";
	  		mapParam.put("data", data);
	  		HttpPostUrl.sendPost("", mapParam);
		}
		
		return ResponseEntity.ok("success");
	}
	
	@ApiOperation(value = "获取评价列表", notes = "获取评价列表")
	@RequestMapping(value = "/getEvaluationList", method = RequestMethod.POST)
	public ResponseEntity<?> getEvaluationList(@RequestBody OrderRequest orderRequest) {
		List<Evaluation> list = evaluationService.findListByPage(orderRequest.getGoodsId(),orderRequest.getShopId(),orderRequest.getIndex());
		return ResponseEntity.ok(list);
	}
	
	@ApiOperation(value = "获取订单列表", notes = "获取订单列表")
	@RequestMapping(value = "/getOrderList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getOrderList(@RequestBody OrderRequest orderRequest) {
		JSONObject jb = new JSONObject();
		if (ToolUtil.isEmpty(orderRequest.getMemberId())) {
			jb.put("data", "");
			return ResponseEntity.ok(jb);
		}
		List<Order> orderResult = orderService.findListByPage(orderRequest.getMemberId(), orderRequest.getStatus(), orderRequest.getIndex());
		if(orderResult.size()>0) {
			for(Order order : orderResult) {
				Shop shop = shopService.findById(order.getShopId());
				order.setShop(shop);
				List<Map<String, Object>> itemResult = orderItemService.getListByOrderId(order.getId());
				order.setItemObject(super.warpObject(new OrderItemWarpper(itemResult)));
			}
		}
		jb.put("data", orderResult);
		return ResponseEntity.ok(jb);
	}
	
	@ApiOperation(value = "获取退款申请列表", notes = "获取退款申请列表")
	@RequestMapping(value = "/getRefundApplyList", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getRefundApplyList(@RequestBody OrderRequest orderRequest) {
		JSONObject jb = new JSONObject();
		if (ToolUtil.isEmpty(orderRequest.getMemberId())) {
			jb.put("data", "");
			return ResponseEntity.ok(jb);
		}
		List<RefundApply> refundApplys = refundApplyService.findMemberRefundApplyToo(orderRequest.getMemberId());
		List<Order> orders = new ArrayList<>();
		for(RefundApply refundApply : refundApplys){
			Order order = orderService.selectById(refundApply.getOrderId());
			order.setRefundApply(refundApply);
			Shop shop = shopService.findById(order.getShopId());
			order.setShop(shop);
			List<Map<String, Object>> itemResult = orderItemService.getListByOrderId(order.getId());
			order.setItemObject(super.warpObject(new OrderItemWarpper(itemResult)));
			orders.add(order);
		}
		jb.put("data", orders);
		return ResponseEntity.ok(jb);
	}
	
	@ApiOperation(value = "修改订单状态", notes = "修改订单状态")
	@RequestMapping(value = "/changeOrderStatus", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> changeOrderStatus(@RequestBody OrderRequest orderRequest) {
		JSONObject jb = new JSONObject();
		Order order = orderService.getById(orderRequest.getOrderId());
		//订单状态判断  需要新增
		order.setStatus(orderRequest.getStatus());
		orderService.update(order);
		if(orderRequest.getStatus() == OrderStatus.WAIT_EVALUATE.getCode()) {
			Shipping shipping = shippingService.findByOrderId(order.getId()).get(0);
			shipping.setType(1);
			shippingService.updateById(shipping);
			order.setGainsTime(DateUtil.format(new Date()));
			orderService.update(order);
		}
		if(orderRequest.getStatus() == OrderStatus.TRADE_CLOSE.getCode()) {
			for(OrderItem item : orderItemService.findByOrderId(orderRequest.getOrderId())) {
				priceTagService.addInventory(item.getProductId(), order.getShopId(), item.getQuantity());
			}
		}
		Map<String, String> mapParam = new HashMap<String, String>();
  		String data = "{\"MsgTypeID\":3100,\"CreateID\":3100,\"MsgJson\":{\"orderId\":"+order.getId()+",\"status\":"+orderRequest.getStatus()+"},\"RequestID\":\"\"}";
  		mapParam.put("data", data);
  		HttpPostUrl.sendPost("", mapParam);
		jb.put("data", "success");
		return ResponseEntity.ok(jb);
	}
	
	@ApiOperation(value = "获取订单详情", notes = "获取订单详情")
	@RequestMapping(value = "/getOrderDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getOrderDetail(@RequestBody OrderRequest orderRequest) {
		JSONObject jb = new JSONObject();
		Order order = orderService.getById(orderRequest.getOrderId());
		Shop shop = shopService.findById(order.getShopId());
		order.setShop(shop);
		List<Map<String, Object>> itemResult = orderItemService.getListByOrderId(order.getId());
		order.setItemObject(super.warpObject(new OrderItemWarpper(itemResult)));
		List<Shipping> shipping = shippingService.findByOrderId(orderRequest.getOrderId());
		if(ToolUtil.isNotEmpty(shipping)) {
			jb.put("shipping", shipping.get(0));
		}else {
			jb.put("shipping", new Shipping());
		}
		jb.put("data", order);
		return ResponseEntity.ok(jb);
	}
	
	@ApiOperation(value = "获取退款详情", notes = "获取退款详情")
	@RequestMapping(value = "/getRefundDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getRefundDetail(@RequestBody OrderRequest orderRequest) {
		JSONObject jb = new JSONObject();
		RefundApply refundApply = refundApplyService.selectById(orderRequest.getRefundApplyId());
		Order order = orderService.getById(refundApply.getOrderId());
		Shop shop = shopService.findById(order.getShopId());
		order.setShop(shop);
		order.setRefundApply(refundApply);
		List<Map<String, Object>> itemResult = orderItemService.getListByOrderId(order.getId(),refundApply.getOrderItemId());
		order.setItemObject(super.warpObject(new OrderItemWarpper(itemResult)));
		jb.put("data", order);
		return ResponseEntity.ok(jb);
	}
	
	@ApiOperation(value = "获取各状态订单数量", notes = "获取各状态订单数量")
	@RequestMapping(value = "/getOrderQuantity", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> getOrderQuantity(@RequestBody OrderRequest orderRequest) {
		List<Integer> quantity = new ArrayList<>();
		quantity.add(orderService.selectOrderCount(orderRequest.getMemberId(),0));
		quantity.add(orderService.selectOrderCount(orderRequest.getMemberId(),2));
		quantity.add(orderService.selectOrderCount(orderRequest.getMemberId(),3));
		quantity.add(orderService.selectOrderCount(orderRequest.getMemberId(),6));
		quantity.add(refundApplyService.selectOrderCount(orderRequest.getMemberId(),"7,9,10"));
		return ResponseEntity.ok(quantity);
	}
	
	@ApiOperation(value = "批量提交订单", notes = "批量提交订单")
	@RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
	public ResponseEntity<?> submitOrder(@RequestBody OrderRequest orderRequest) throws Exception {
		List<Long> idList = new ArrayList<>();
		String sn =new Date().getTime()+ String.valueOf((int)((Math.random()* 9 + 1) * 100000));	
		for(Order order : orderRequest.getOrderList()) {
			Cart cart = cartService.findById(order.getMemberId());
			if(orderRequest.isCart()) {
				cart.setQuantity(cart.getQuantity() - order.getPackages());
				cartService.updateById(cart);
				orderRequest.setCart(false);
			}
			order.setSn(sn);
			orderService.add(order);
			idList.add(order.getId());
			for(OrderItem item:order.getOrderItems()) {
				priceTagService.reduceInventory(item.getProductId(), order.getShopId(), item.getQuantity());
				item.setOrderId(order.getId());
				orderItemService.insert(item);
				PriceTag tag = priceTagService.findByShopAndProduct(item.getProductId(), order.getShopId());
				CartItem cartItem = cartItemService.findByTagId(tag.getId(), cart.getId());
				if(ToolUtil.isNotEmpty(cartItem)) {
					cartItemService.deleteById(cartItem.getId());
				}
			}
			Map<String, String> mapParam = new HashMap<String, String>();
	  		String data = "{\"MsgTypeID\":3100,\"CreateID\":3100,\"MsgJson\":{\"orderId\":"+order.getId()+",\"status\":0},\"RequestID\":\"\"}";
	  		mapParam.put("data", data);
	  		HttpPostUrl.sendPost("", mapParam);
		}
		
		return ResponseEntity.ok(idList);
	}
	
}
