package com.md.share.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("share_relation_member")
public class RelationMember {
	/**
	 * 主键Id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 门店id
	 */
	private Long shopId;
	/**
	 * 绑定截止时间
	 */
	private String endtime;
	/**
	 * 用户id
	 */
	private Long memberId;
	/**
	 * 分销员id
	 */
	private Long shareId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getShareId() {
		return shareId;
	}

	public void setShareId(Long shareId) {
		this.shareId = shareId;
	}

	@Override
	public String toString() {
		return "Relation {id=" + id + ", shopId=" + shopId + ", endtime=" + endtime + ", memberId=" + memberId
				+ ", shareId=" + shareId + "}";
	}

}
