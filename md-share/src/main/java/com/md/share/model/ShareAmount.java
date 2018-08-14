package com.md.share.model;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

@TableName("share_amount")
public class ShareAmount {
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
	 * 商品id
	 */
	private Long goodsId;
	/**
	 * 订单id
	 */
	private Long orderId;
	/**
	 * 订单id
	 */
	private Long orderItemId;
	/**
	 * 商品规格id
	 */
	private Long productId;
	/**
	 * 订单商品金额
	 */
	private BigDecimal amount;
	/**
	 * 分润金额
	 */
	private BigDecimal moistenAmount;
	/**
	 * 状态
	 * @return
	 */
	private Integer status;
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
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
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getMoistenAmount() {
		return moistenAmount;
	}
	public void setMoistenAmount(BigDecimal moistenAmount) {
		this.moistenAmount = moistenAmount;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}


}
