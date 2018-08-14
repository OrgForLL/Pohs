package com.stylefeng.guns.rest.modular.share;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.md.order.model.Order;
import com.md.order.model.OrderItem;
import com.md.order.model.RefundApply;
import com.md.order.model.Shipping;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.md.order.service.IRefundApplyService;
import com.md.order.service.IShippingService;
import com.md.share.model.Configure;
import com.md.share.model.DefaultConfigure;
import com.md.share.model.ShareAmount;
import com.md.share.model.ShareBind;
import com.md.share.model.SharePayApply;
import com.md.share.service.IConfigureService;
import com.md.share.service.IDefaultConfigureService;
import com.md.share.service.IShareAmountService;
import com.md.share.service.IShareBindService;
import com.md.share.service.ISharePayApplyService;
import com.md.share.service.impl.ShareAmountServiceImpl;
import com.md.share.service.impl.ShareBindServiceImpl;
import com.md.share.service.impl.SharePayApplyServiceImpl;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.rest.modular.share.dto.ShareRequest;

import io.swagger.annotations.ApiOperation;

/**
 * 分销管理
 * 
 * @author 54476
 *
 */
@RestController
@RequestMapping("/share")
public class ApiShareController extends BaseController {
	
	@Resource
	IMemberService memberServiceImpl;
	@Resource
	IConfigureService configureServiceImpl;
	@Resource
	IDefaultConfigureService defaultConfigureService;
	@Resource
	IShareBindService shareBindService;
	@Resource
	IShareAmountService shareAmountService;
    @Resource
    IShippingService shippingServiceImpl;
    @Resource 
    IOrderService orderService;
    @Resource 
    IOrderItemService orderItemService;
	@Resource
	IConfigureService configureService;
	@Resource
	ISharePayApplyService sharePayApplyService;
	@Resource
	IRefundApplyService refundApplyService;
	
    /**
     * 成为分销员
     */
	@ApiOperation(value = "分销员注册", notes = "注册")
    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ResponseEntity<?> register( @RequestBody ShareRequest shareRequest) {
		Member member = new Member();
    	List<Member> members = memberServiceImpl.selectByPhoneNum(shareRequest.getPhone());
    	if(members.size() != 0){
    		member = members.get(0);
    		member.setType("分销员");
    		member.setShopIds(shareRequest.getShopIds());
    		member.setRealName(shareRequest.getRealName());
    		member.setStatus(3);
    		memberServiceImpl.updateById(member);
    		return ResponseEntity.ok(SUCCESS);
    	}
    	return ResponseEntity.ok(ERROR);
    	
    }
	
	/**
	 * 获取分享配置
	 */
	@ApiOperation(value = "获取分享配置", notes = "获取分享配置")
	@RequestMapping(value = "/getShareConfigure", method = { RequestMethod.POST })
	public ResponseEntity<?> getShareConfigure( @RequestBody ShareRequest shareRequest) {
		List<Configure> configures = configureServiceImpl.selectByAssociatedEntity(shareRequest.getAssociatedEntityId(),shareRequest.getType());
		if(configures.size() != 0){
			Configure configure = configures.get(0);
			if(configure.getIsDefault() == 0){
				return ResponseEntity.ok(configure);
			}
		}
		List<DefaultConfigure> defaultConfigures = defaultConfigureService.selectByType(shareRequest.getType());
		if(defaultConfigures.size() != 0){
			return ResponseEntity.ok(defaultConfigures.get(0));
		}
		return ResponseEntity.ok("");
	}
	
	/**
	 * 分润提现申请
	 */
	@ApiOperation(value = "分润提现申请", notes = "分润提现申请")
	@RequestMapping(value = "/getSharePayApply", method = { RequestMethod.POST })
	public ResponseEntity<?> getSharePayApply( @RequestBody SharePayApply sharePayApply) {
		Member member = memberServiceImpl.selectById(sharePayApply.getMemberId());
		BigDecimal moistenAmount = new BigDecimal(0);
		if(member.getType().equals("分销员")){
			//计算可提现分润金额
			List<ShareAmount> shareAmounts = shareAmountService.selectByShareMemberId(member.getId(),0);
			if(shareAmounts.size() != 0){
				for(ShareAmount shareAmount : shareAmounts){
					moistenAmount.add(shareAmount.getMoistenAmount());
					shareAmount.setStatus(1);
					shareAmountService.updateById(shareAmount);
				}
			}
		}
		sharePayApply.setAmount(moistenAmount);
		sharePayApply.setCreateTime(DateUtil.format(new Date()));
		sharePayApply.setStatus(0);
		sharePayApplyService.insert(sharePayApply);
		return ResponseEntity.ok(SUCCESS);
	}
	
	/**
	 * 绑定被分享用户
	 */
	@ApiOperation(value = "绑定被分享用户", notes = "绑定被分享用户")
	@RequestMapping(value = "/bindMember", method = { RequestMethod.POST})
	public ResponseEntity<?> bindMember( @RequestBody ShareRequest shareRequest) {
		ShareBind shareBind = new ShareBind();
		List<ShareBind> shareBinds = shareBindService.selectByBoundMemberId(shareRequest.getBoundMemberId());
		if(shareBinds.size() == 0){
			shareBind.setBoundMemberId(shareRequest.getBoundMemberId());
			shareBind.setShareMemberId(shareRequest.getShareMemberId());
			shareBind.setBindTime(DateUtil.format(new Date()));
			shareBindService.insert(shareBind);
		}else{
			shareBind = shareBinds.get(0);
			if(shareBind.getShareMemberId().equals(shareRequest.getShareMemberId())){
				shareBind.setBindTime(DateUtil.format(new Date()));
				shareBindService.updateById(shareBind);
			}else{
				Calendar cld = Calendar.getInstance();
				cld.setTime(shareBind.getBindTime());
				cld.add(Calendar.DATE, 15);
				Timestamp timestamp = DateUtil.format(cld.getTime());
				if (!timestamp.before(new Date())) {
					shareBind.setShareMemberId(shareRequest.getShareMemberId());
					shareBind.setBindTime(DateUtil.format(new Date()));
					shareBindService.updateById(shareBind);
				}
			}
		}
		return ResponseEntity.ok(SUCCESS);
	}
	
	/**
	 * 获取分润详情
	 */
	@ApiOperation(value = "获取分润详情", notes = "获取分润详情")
	@RequestMapping(value = "/getShareAmount", method = { RequestMethod.POST })
	public ResponseEntity<?> getShareAmount( @RequestBody ShareRequest shareRequest) {
		Map<String, Object> result = new HashMap<>();
		Member member = memberServiceImpl.selectById(shareRequest.getShareMemberId());
		BigDecimal amount = new BigDecimal(0);
		BigDecimal moistenAmount = new BigDecimal(0);
		BigDecimal unMoistenAmount = new BigDecimal(0);
		BigDecimal total = new BigDecimal(0);
		if(member.getType().equals("分销员")){
			//计算可提现分润金额与被分享人购买的总数量
			List<ShareAmount> shareAmounts = shareAmountService.selectByShareMemberId(member.getId(),0);
			if(shareAmounts.size() != 0){
				for(ShareAmount shareAmount : shareAmounts){
					amount.add(shareAmount.getAmount());
					moistenAmount.add(shareAmount.getMoistenAmount());
				}
			}
			//计算分润还不能提现部分
			List<Shipping> list = shippingServiceImpl.getListByWaitGains(30);
	    	for(Shipping shipping : list) {
	    		Order order = orderService.selectById(shipping.getOrderId());
	    		List<ShareBind> shareBinds = shareBindService.selectByBoundMemberId(order.getMemberId());
	    		ShareBind shareBind = new ShareBind();
	    		if(shareBinds.size() != 0){
	    			shareBind = shareBinds.get(0);
	    			if(ToolUtil.isEmpty(shareBind.getShareMemberId()) || shareBind.getShareMemberId().equals(shareRequest.getShareMemberId())){
	    				continue;
	    			}
	    		}else{
	    			continue;
	    		}		
	    		for(OrderItem orderItem : orderItemService.findByOrderId(order.getId())) {
	    			RefundApply refundApply = new RefundApply();
	    			List<RefundApply> refundApplies =  refundApplyService.findRefundApplyByOrder(orderItem.getOrderId(),orderItem.getId());
	    			if(refundApplies.size() != 0){
	    				refundApply = refundApplies.get(0);
	    			}
	    			List<Configure> configures = configureService.selectByAssociatedEntity(orderItem.getGoodsId(), 3);
		    		if(configures.size() != 0){
		    			Configure configure = configures.get(0);
		    			if(configure.getIsDefault() == 0){
		    				if(ToolUtil.isNotEmpty(refundApply.getMoney())){
		    					unMoistenAmount.add(orderItem.getAmount().subtract(new BigDecimal(refundApply.getMoney())).multiply(new BigDecimal(configure.getProfit())));
		    				}else{
		    					unMoistenAmount.add(orderItem.getAmount().multiply(new BigDecimal(configure.getProfit())));
		    				}
		    				continue;
		    			}
	    			}
    				List<DefaultConfigure> defaultConfigures = defaultConfigureService.selectByType(3);
    	    		if(defaultConfigures.size() != 0){
    	    			DefaultConfigure defaultConfigure = defaultConfigures.get(0);
    	    			if(ToolUtil.isNotEmpty(refundApply.getMoney())){
    	    				unMoistenAmount.add(orderItem.getAmount().subtract(new BigDecimal(refundApply.getMoney())).multiply(new BigDecimal(defaultConfigure.getProfit())));
	    				}else{
	    					unMoistenAmount.add(orderItem.getAmount().multiply(new BigDecimal(defaultConfigure.getProfit())));
	    				}
    					continue;
    	    		}
	    		}
	    	}
	    	//已经被领取的金额
	    	List<SharePayApply> sharePayApplies = sharePayApplyService.selectAmountByStatus(member.getId(),1);
	    	if(sharePayApplies.size() != 0) {
	    		for(SharePayApply sharePayApply : sharePayApplies) {
	    			total.add(sharePayApply.getAmount());
	    		}
	    		
	    	}
	    	result.put("total", total);
	    	result.put("amount", amount);
			result.put("moistenAmount", moistenAmount); 
			result.put("unMoistenAmount", unMoistenAmount);
			return ResponseEntity.ok(result);
		}
		result.put("resultMsg", "用户不是分销员！");
		return ResponseEntity.ok(result);
	}
	
	
}
