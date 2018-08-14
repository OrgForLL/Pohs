package com.stylefeng.guns.rest.modular.pay.dto;

import java.util.List;

public class PayRequest {
	private Long memberId;
	private List<Long> orderIdList;
	
	private String href;

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

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	

}
