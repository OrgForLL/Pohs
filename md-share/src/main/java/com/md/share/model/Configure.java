package com.md.share.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("share_configure")
public class Configure {
	/**
	 * 主键Id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 关联实体id
	 */
	private Long associatedEntityId;
	/**
	 * 分享标题
	 */
	private String title;
	/**
	 * 类型：（1 商城，2 活动，3单品）
	 */
	private Integer type;
	/**
	 * 分享内容
	 */
	private String content;
	/**
	 * 分享图片
	 */
	private String imgurl;
	/**
	 * 是否默认（1 是 0 否）
	 */
	private Long isDefault;
	/**
	 * 分销利润(%)
	 */
	private Double profit;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public Long getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Long isDefault) {
		this.isDefault = isDefault;
	}

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public Long getAssociatedEntityId() {
		return associatedEntityId;
	}

	public void setAssociatedEntityId(Long associatedEntityId) {
		this.associatedEntityId = associatedEntityId;
	}

	@Override
	public String toString() {
		return "Configure {id=" + id + ",associatedEntityId=" + associatedEntityId + ", title=" + title + ", type="
				+ type + ", content=" + content + ", imgurl=" + imgurl + ", isDefault=" + isDefault + ", profit="
				+ profit + "}";
	}

}
