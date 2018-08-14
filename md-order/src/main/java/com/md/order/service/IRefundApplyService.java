package com.md.order.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.md.order.model.Order;
import com.md.order.model.RefundApply;

public interface IRefundApplyService extends IService<RefundApply>{

	boolean add(Order order, String applyWhy,Integer refundType, Long orderItemId);
	
	List<Map<String, Object>> find(RefundApply refundApply);

	List<Map<String, Object>> findMemberRefundApply(Long memberId);

	RefundApply selectByRefundOrderSn(String out_refund_no);

	List<RefundApply> findMemberRefundApplyToo(Long memberId);
	
	List<RefundApply> findRefundApplyByOrder(Long orderId , Long orderItemId);

	Integer selectOrderCount(Long memberId, String statusArray);

	
}
