package com.stylefeng.guns.api.dto;

public class Param {

	private boolean isAutoCreateMember = false;
	
	private boolean isPromotion = false;
	
	private Integer unitType = 0;

	public boolean isAutoCreateMember() {
		return isAutoCreateMember;
	}

	public void setAutoCreateMember(boolean isAutoCreateMember) {
		this.isAutoCreateMember = isAutoCreateMember;
	}

	public boolean isPromotion() {
		return isPromotion;
	}

	public void setPromotion(boolean isPromotion) {
		this.isPromotion = isPromotion;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}
	
}
