package com.stylefeng.guns.api;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.md.coupon.constant.PromotionStatus;
import com.md.coupon.model.Promotion;
import com.md.coupon.service.IPromotionService;
import com.md.goods.model.Product;
import com.md.goods.service.IPriceTagService;
import com.md.goods.service.IProductService;
import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.md.member.service.imp.MemberServiceImpl;
import com.md.notice.service.IShopNoticeService;
import com.md.order.constant.OrderStatus;
import com.md.order.model.Evaluation;
import com.md.order.model.Order;
import com.md.order.model.OrderItem;
import com.md.order.model.Shipping;
import com.md.order.service.IEvaluationService;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.md.order.service.IShippingService;
import com.md.order.service.impl.ShippingServiceImpl;
import com.md.share.model.Configure;
import com.md.share.model.DefaultConfigure;
import com.md.share.model.ShareAmount;
import com.md.share.model.ShareBind;
import com.md.share.service.IConfigureService;
import com.md.share.service.IDefaultConfigureService;
import com.md.share.service.IShareAmountService;
import com.md.share.service.IShareBindService;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.ToolUtil;


/**
 * 定时任务
 * @author 54476
 *
 */
@Configuration
@EnableScheduling
public class ApiQuartz {

    @Resource 
    IOrderService orderService;
    
    @Resource
    IPromotionService promotionService;
    
    @Resource
    IShopNoticeService shopNoticeServiceImpl;
    
    @Resource 
    IOrderItemService orderItemService;
    
    @Resource
    IPriceTagService priceTagService;
    
    @Resource
    IShippingService shippingServiceImpl;
    
	@Resource
	IEvaluationService evaluationService;
	@Resource
	IProductService productService;
	@Resource
	IMemberService memberService;
	@Resource
	IShareBindService shareBindService;
	@Resource
	IConfigureService configureService;
	@Resource
	IDefaultConfigureService defaultConfigureService;
	@Resource
	IShareAmountService shareAmountService;

    /**
     * 24小时后未支付则取消订单
     */
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分钟执行一次，取消订单
    public void orderTask() {
    	List<Order> list = orderService.getListByCancel();
    	System.out.println("开始刷订单");
		for(Order order : list) {
			System.out.println("订单号"+order.getId()+"被自动取消,交易关闭");
			shopNoticeServiceImpl.addOnOrderScheduled("您的订单"+order.getSn()+"因为24小时未付款，订单已取消，给您带来的不便请谅解！", order.getMemberId());
			order.setStatus(OrderStatus.TRADE_CLOSE.getCode());
			for(OrderItem item : orderItemService.findByOrderId(order.getId())) {
				priceTagService.addInventory(item.getProductId(), order.getShopId(), item.getQuantity());
			}
    		orderService.updateById(order);
    	}
    }
    /**
     * 15天自动确认收货
     */
    @Scheduled(cron = "0 0 4 * * ?") // 每天4点执行一次
    public void AutoOrderGains() {
    	List<Shipping> list = shippingServiceImpl.getListByWaitGains(15);
    	for(Shipping shipping : list) {
    		Order order = orderService.selectById(shipping.getOrderId());
    		order.setGainsTime(DateUtil.format(new Date()));
    		order.setStatus(OrderStatus.WAIT_EVALUATE.getCode());
    		orderService.updateById(order);
    		shipping.setType(1);
    		shippingServiceImpl.updateById(shipping);
    	}
    }
    /**
     * 7天自动好评
     */
    @Scheduled(cron = "0 0 5 * * ?") // 每天5点执行一次
    public void AutoOrderEvaluation() {
    	List<Order> list = orderService.getListByEvaluation();
    	for(Order order : list) {
    		order.setStatus(OrderStatus.TRADE_SUCCESS.getCode());
    		for(OrderItem orderItem : orderItemService.findByOrderId(order.getId())) {
    			Product product = productService.findById(orderItem.getProductId());
    			Evaluation evaluation = new Evaluation();
    			evaluation.setCreateTime(DateUtil.format(new Date()));
    			evaluation.setDetail("默认好评。");
    			evaluation.setLevel(0);
    			evaluation.setGoodsId(orderItem.getGoodsId());
    			evaluation.setMemberId(order.getMemberId());
    			evaluation.setOrderItemId(orderItem.getId());
    			evaluation.setShopId(order.getShopId());
    			evaluation.setSpecName(product.getName());
    			evaluationService.insert(evaluation);
    		}
    		orderService.updateById(order);
    	}
    }
    /**
     * 处理分享订单
     */
    @Scheduled(cron = "0 0 3 * * ?") // 每天3点执行一次
    public void shareOrder() {
    	List<Shipping> list = shippingServiceImpl.getListByWaitGains(30);
    	for(Shipping shipping : list) {
    		Order order = orderService.selectById(shipping.getOrderId());
    		List<ShareBind> shareBinds = shareBindService.selectByBoundMemberId(order.getMemberId());
    		ShareBind shareBind = new ShareBind();
    		if(shareBinds.size() != 0){
    			shareBind = shareBinds.get(0);
    			if(ToolUtil.isEmpty(shareBind.getShareMemberId())){
    				continue;
    			}
    		}else{
    			continue;
    		}		
    		for(OrderItem orderItem : orderItemService.findByOrderId(order.getId())) {
    			List<Configure> configures = configureService.selectByAssociatedEntity(orderItem.getGoodsId(), 3);
    			Configure configure = new Configure();
    			if(configure.getIsDefault() == 0){
    				ShareAmount shareAmount = new ShareAmount();
    				shareAmount.setBoundMemberId(shareBind.getBoundMemberId());
    				shareAmount.setShareMemberId(shareBind.getShareMemberId());
    				shareAmount.setGoodsId(orderItem.getGoodsId());
    				shareAmount.setProductId(orderItem.getProductId());
    				shareAmount.setAmount(orderItem.getAmount());
    				shareAmount.setMoistenAmount(orderItem.getAmount().multiply(new BigDecimal(configure.getProfit())));
    				shareAmount.setOrderId(orderItem.getOrderId());
    				shareAmount.setOrderItemId(orderItem.getId());
    				shareAmountService.insert(shareAmount);
    				continue;
    			}else{
    				List<DefaultConfigure> defaultConfigures = defaultConfigureService.selectByType(3);
    	    		if(defaultConfigures.size() != 0){
    	    			DefaultConfigure defaultConfigure = defaultConfigures.get(0);
    	    			ShareAmount shareAmount = new ShareAmount();
    					shareAmount.setBoundMemberId(shareBind.getBoundMemberId());
    					shareAmount.setShareMemberId(shareBind.getShareMemberId());
    					shareAmount.setGoodsId(orderItem.getGoodsId());
    					shareAmount.setProductId(orderItem.getProductId());
    					shareAmount.setAmount(orderItem.getAmount());
    					shareAmount.setMoistenAmount(orderItem.getAmount().multiply(new BigDecimal(defaultConfigure.getProfit())));
    					shareAmount.setOrderId(orderItem.getOrderId());
        				shareAmount.setOrderItemId(orderItem.getId());
    					shareAmountService.insert(shareAmount);
    					continue;
    	    		}
    			}
    		}
    	}
    }
    /**
     * 修改促销活动状态
     */
    @Scheduled(cron = "0 0/10 * * * ?") // 每10分钟执行一次
    public void promotionTask() {
    	List<Promotion>  resultStart = promotionService.getListByStart();
    	List<Promotion>  resultEnd = promotionService.getListByEnd();
    	System.out.println("开始刷活动");
		for(Promotion promotion : resultStart) {
			System.out.println("活动编号"+promotion.getId()+"自动开始");
			promotion.setStatus(PromotionStatus.RUNNING.getCode());
			promotionService.updateById(promotion);
    	}
		for(Promotion promotion : resultEnd) {
			System.out.println("活动编号"+promotion.getId()+"自动结束");
			promotion.setStatus(PromotionStatus.FINISH.getCode());
			promotionService.updateById(promotion);
    	}
    }
    
}
