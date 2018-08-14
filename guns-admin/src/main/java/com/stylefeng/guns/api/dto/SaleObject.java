package com.stylefeng.guns.api.dto;

import java.util.List;

public class SaleObject {

	private List<SaleOrder> dtos;
	
	private Param param;

	public List<SaleOrder> getDtos() {
		return dtos;
	}

	public void setDtos(List<SaleOrder> dtos) {
		this.dtos = dtos;
	}

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}

}
