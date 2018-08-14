package com.stylefeng.guns.rest.modular.order.dto;

public class EvaluationRequest {

	// 评价等级
	private Integer level;
	// 订单项id
	private Long orderItemId;
	// 评价详情
	private String detail;
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	
}
