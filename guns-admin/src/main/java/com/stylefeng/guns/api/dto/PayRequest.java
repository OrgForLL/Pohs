package com.stylefeng.guns.api.dto;

import java.util.List;

public class PayRequest {
	private Long memberId;
	private List<Long> orderIdList;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public List<Long> getOrderIdList() {
		return orderIdList;
	}

	public void setOrderIdList(List<Long> orderIdList) {
		this.orderIdList = orderIdList;
	}

	

}
