package com.stylefeng.guns.modular.system.controller.payment;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.md.goods.service.IShopService;
import com.md.member.service.IMemberService;
import com.md.notice.service.IShopNoticeService;
import com.md.notice.service.impl.ShopNoticeServiceImpl;
import com.md.order.constant.OrderStatus;
import com.md.order.model.Order;
import com.md.order.model.RefundApply;
import com.md.order.model.Shipping;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderLogService;
import com.md.order.service.IOrderService;
import com.md.order.service.IRefundApplyService;
import com.md.order.service.IShippingItemService;
import com.md.order.service.IShippingService;
import com.md.order.warpper.RefundApplyWarpper;
import com.md.pay.model.PaymentOrder;
import com.md.pay.service.IPaymentOrderService;
import com.md.pay.service.IRefundOrderService;
import com.md.pay.service.IwxPayService;
import com.stylefeng.guns.core.base.controller.BaseController;

import io.swagger.annotations.ApiImplicitParam;
import weixin.popular.bean.paymch.SecapiPayRefundResult;

/**
 * 退款申请控制器
 *
 */
@Controller
@RequestMapping("/refundApply")
public class RefundApplyController extends BaseController {

    private String PREFIX = "/payment/refundApply/";

    @Resource
    IRefundApplyService refundApplyServiceImpl;
    @Resource
    IOrderService orderServiceImpl;
    @Resource
    IRefundOrderService refundOrderServiceImpl;
    @Resource
    IwxPayService wxPayServiceImpl;
    @Resource
    IPaymentOrderService paymentOrderServiceImpl;
    @Resource
    IShopNoticeService shopNoticeService;
	@Resource
	IOrderItemService orderItemService;
	@Resource
	IShopService shopService;
	@Resource
	IMemberService memberService;
	@Resource
    IOrderLogService orderLogService;
	@Resource
    IShippingService shippingService;
	@Resource
    IShippingItemService shippingItemService;
    
    /**
     * 跳转管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "list.html";
    }
    
    @RequestMapping("/refund_view/{refundApplyId}")
    public String refund(@PathVariable Long refundApplyId, Model model) {
    	model.addAttribute("refundApplyId", refundApplyId);
    	return PREFIX + "refund.html";
    }
    
    /**
     * 获取退款申请记录列表
     */
    @RequestMapping("/list")
    @ResponseBody
    @ApiImplicitParam(name = "refundApply", value = "付款订单信息", required = true, dataType = "refundApply", paramType = "body")
    public Object list(RefundApply refundApply , Model model) {	
    	List<Map<String, Object>> refundApplys = refundApplyServiceImpl.find(refundApply);
    	return super.warpObject(new RefundApplyWarpper(refundApplys));
    }
    
    /**
     * 发起退款
     */
    @RequestMapping("/refund")
    @ResponseBody
    public Object refund(Long refundApplyId,String money) {
    	JSONObject jb = new JSONObject();
    	RefundApply refundApply = refundApplyServiceImpl.selectById(refundApplyId);
    	Order order = orderServiceImpl.selectById(refundApply.getOrderId());
    	PaymentOrder paymentOrder = paymentOrderServiceImpl.selectByOrdersn(order.getSn());
    	SecapiPayRefundResult secapiPayRefundResult = wxPayServiceImpl.wxPayRefund(order, paymentOrder, money, refundApply);
		System.out.println("修改订单状态-----000000");
		refundApply.setStatus(8);
		refundApply.setMoney(Double.valueOf(money));
		refundApplyServiceImpl.updateById(refundApply);
		shopNoticeService.addOnRefundPay("您的订单"+order.getSn()+"已退款，请注意查收。", order.getMemberId());
    	jb.put("code", secapiPayRefundResult.getReturn_code());
		jb.put("msg", secapiPayRefundResult.getReturn_msg());
		return jb;
    }
    
    /**
     * 同意退货
     */
    @RequestMapping("/agreeToReturn")
    @ResponseBody
    public Object agreeToReturn(Long refundApplyId) {
    	RefundApply refundApply = refundApplyServiceImpl.selectById(refundApplyId);
    	refundApply.setStatus(10);
    	boolean flag = refundApplyServiceImpl.updateById(refundApply);
    	if(flag) {
    		return SUCCESS;
    	}
    	return ERROR;
    }
    
    /**
     * 已退货
     */
    @RequestMapping("/returnSuccess")
    @ResponseBody
    public Object returnSuccess(Long refundApplyId) {
    	RefundApply refundApply = refundApplyServiceImpl.selectById(refundApplyId);
    	refundApply.setStatus(7);
    	boolean flag = refundApplyServiceImpl.updateById(refundApply);
    	if(flag) {
    		return SUCCESS;
    	}
    	return ERROR;
    }
    
    /**
     * 跳转到订单详情页面
     */
    @RequestMapping("/detail/{refundApplyId}")
    public String detail(@PathVariable Long refundApplyId, Model model) {
    	RefundApply refundApply = refundApplyServiceImpl.selectById(refundApplyId);
        Order order = orderServiceImpl.getById(refundApply.getOrderId());
        order.setOrderItems(orderItemService.findByOrderId(refundApply.getOrderId()));
        order.setShop(shopService.findById(order.getShopId()));
        order.setMember(memberService.findById(order.getMemberId()));
        model.addAttribute("refundApply",refundApply);
        model.addAttribute("order",order);
        model.addAttribute("status",OrderStatus.valueOf(order.getStatus()));
        model.addAttribute("orderItems",JSONArray.toJSON(order.getOrderItems()));
        //封装物流对象
        //调用shippingService里的findByOrderId方法,将结果封装到model里
        List<Shipping> shippings=shippingService.findByOrderId(refundApply.getOrderId());
        shippings.stream().forEach(s->{
            s.setShippingItems(shippingItemService.findByShippingId(s.getId()));
        });
        model.addAttribute("shippings",shippings);
        return PREFIX + "order/order_detail.html";
    }
}
