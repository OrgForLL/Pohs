package com.md.notice.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("shop_notice")
public class ShopNotice {

	/**
	 * 主键Id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 通知类型
	 */
	private Integer type;
	/**
	 * 通知内容
	 */
	private String content;
	/**
	 * 客户id
	 */
	private Long memberId;
	/**
	 * 门店id
	 */
	private Long shopId;
	/**
	 * 链接
	 */
	private String url;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 状态
	 */
	private Integer status;

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

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ShopNotice {id=" + id + ", title=" + title + ", type=" + type + ", content=" + content + ", memberId="
				+ memberId + ", shopId=" + shopId + ", url=" + url + ", createTime=" + createTime
				+ ", status=" + status + "}";
	}

}
