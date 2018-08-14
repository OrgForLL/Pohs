package com.stylefeng.guns.rest.modular.share.dto;

public class ShareRequest {
	/**
	 * 门店id数组
	 */
	private String shopIds;
	/**
	 * 姓名
	 */
	private String realName;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 分享类型
	 */
	private Integer type;
	/**
	 * 分享实体id
	 */
	private Long associatedEntityId;
	/**
	 * 被绑定用户Id
	 */
	private Long boundMemberId;
	/**
	 * 分享人Id
	 */
	private Long shareMemberId;
	

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Long getBoundMemberId() {
		return boundMemberId;
	}

	public void setBoundMemberId(Long boundMemberId) {
		this.boundMemberId = boundMemberId;
	}

	public Long getShareMemberId() {
		return shareMemberId;
	}

	public void setShareMemberId(Long shareMemberId) {
		this.shareMemberId = shareMemberId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getAssociatedEntityId() {
		return associatedEntityId;
	}

	public void setAssociatedEntityId(Long associatedEntityId) {
		this.associatedEntityId = associatedEntityId;
	}

	public String getShopIds() {
		return shopIds;
	}

	public void setShopIds(String shopIds) {
		this.shopIds = shopIds;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
