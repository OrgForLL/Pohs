package com.stylefeng.guns.rest.modular.order.dto;

import java.math.BigDecimal;

public class SaleOrderDetails {

	private Inventory Inventory;		//库存
	
	private Unit Unit;		//单位
	
	private BigDecimal  Quantity;    //数量
	
	private BigDecimal OrigPrice;		//商品标价   可为空

	public Inventory getInventory() {
		return Inventory;
	}

	public void setInventory(Inventory inventory) {
		Inventory = inventory;
	}

	public Unit getUnit() {
		return Unit;
	}

	public void setUnit(Unit unit) {
		Unit = unit;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getOrigPrice() {
		return OrigPrice;
	}

	public void setOrigPrice(BigDecimal origPrice) {
		OrigPrice = origPrice;
	}
}
