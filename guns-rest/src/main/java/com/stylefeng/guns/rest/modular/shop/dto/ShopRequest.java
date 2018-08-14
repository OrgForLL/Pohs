package com.stylefeng.guns.rest.modular.shop.dto;

/**
 * 门店
 * @author 54476
 *
 */
public class ShopRequest {

	private Long shopId;
	
	private long proId;
	
	private long cityId;
	
	private double lng;
	
	private double lat;

	public long getProId() {
		return proId;
	}

	public void setProId(long proId) {
		this.proId = proId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
}
