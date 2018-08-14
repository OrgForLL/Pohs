package com.stylefeng.guns.api.dto;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

public class RefundApplyRequest {
	// 用户id
	private Long memberId;
	// 订单id
	private Long orderId;
	// 退款申请id
	private Long refundApplyId;
	// 退款理由
	private String applyWhy;
	// 退款类型(0:仅退款 1：退货退款)
	private Integer refundType;
	// 退款类型(0:仅退款 1：退货退款)
	private String logisticsMsg;
	//退款的商品订单编号
	private Long orderItemId;

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public Long getRefundApplyId() {
		return refundApplyId;
	}

	public void setRefundApplyId(Long refundApplyId) {
		this.refundApplyId = refundApplyId;
	}

	public String getLogisticsMsg() {
		return logisticsMsg;
	}

	public void setLogisticsMsg(String logisticsMsg) {
		this.logisticsMsg = logisticsMsg;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getApplyWhy() {
		return applyWhy;
	}

	public void setApplyWhy(String applyWhy) {
		this.applyWhy = applyWhy;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

}

