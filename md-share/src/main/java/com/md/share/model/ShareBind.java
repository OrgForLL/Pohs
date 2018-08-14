package com.md.share.model;

import java.sql.Timestamp;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("share_user_bind")
public class ShareBind {
	/**
	 * 主键Id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;
	/**
	 * 被绑定用户Id
	 */
	private Long boundMemberId;
	/**
	 * 分享人Id
	 */
	private Long shareMemberId;
	/**
	 * 绑定时间
	 */
	private Timestamp bindTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Timestamp getBindTime() {
		return bindTime;
	}

	public void setBindTime(Timestamp bindTime) {
		this.bindTime = bindTime;
	}


}
