package com.md.order.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("shop_refund_apply")
public class RefundApply {
	/**
	 * 主键id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 用户id
	 */
	private Long memberId;
	/**
	 * 订单id
	 */
	private Long orderId;
	/**
	 * 订单详情id
	 */
	private Long orderItemId;
	/**
	 * 申请原因
	 */
	private String applyWhy;
	/**
	 * 退款金额
	 */
	private Double money;
	/**
	 * 物流信息
	 */
	private String logisticsMsg;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 退款类型(0:仅退款 1：退货退款)
	 */
	private Integer refundType;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 退款单编号
	 */
	private String refundOrderSn;
	
	
	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

	public String getRefundOrderSn() {
		return refundOrderSn;
	}

	public void setRefundOrderSn(String refundOrderSn) {
		this.refundOrderSn = refundOrderSn;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getLogisticsMsg() {
		return logisticsMsg;
	}

	public void setLogisticsMsg(String logisticsMsg) {
		this.logisticsMsg = logisticsMsg;
	}

	@Override
	public String toString() {
		return "RefundApply {id=" + id + ",orderItemId="+orderItemId+" money=" + money + ",logisticsMsg=" + logisticsMsg + ",  memberId=" + memberId + ", orderId=" + orderId
				+ ", applyWhy=" + applyWhy + ", status=" + status + ", createTime=" + createTime + "}";
	}

	

}
