package com.md.share.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("share_default_configure")
public class DefaultConfigure {
	/**
	 * 主键Id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
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
	 * 分销利润(%)
	 */
	private Double profit;
	/**
	 * 时效
	 */
	private String time;

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

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

	public Double getProfit() {
		return profit;
	}

	public void setProfit(Double profit) {
		this.profit = profit;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "DefaultConfigure {id=" + id + ", title=" + title + ", type=" + type + ", content=" + content
				+ ", profit=" + profit + ", time=" + time + "}";
	}

}
