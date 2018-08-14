package com.md.notice.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.md.notice.model.ShopNotice;

public interface IShopNoticeService extends IService<ShopNotice> {
	/**
	 * 订单发货
	 * @param content
	 * @param memberId
	 * @return
	 */
	boolean addOnOrderSend (String content, Long memberId );
	/**
	 * 订单定时关闭未付款订单
	 * @param content
	 * @param memberId
	 * @return
	 */
	boolean addOnOrderScheduled (String content, Long memberId );
	/**
	 * 订单审核
	 * @param content
	 * @param memberId
	 * @return
	 */
	boolean addOnOrderCheck (String content, Long memberId );
	/**
	 * 退款
	 * @param content
	 * @param memberId
	 * @return
	 */
	boolean addOnRefundPay (String content, Long memberId );
	/**
	 * 客服消息
	 * @param content
	 * @param memberId
	 * @return
	 */
	boolean addOnKfMsg (String content, Long memberId );
	/**
	 * 获取消息列表
	 * @param shopNotice
	 * @return
	 */
	List<ShopNotice> selectNoticeList(ShopNotice shopNotice);
	
	/**
	 * 获取消息列表
	 * @param memberId 用户id
	 * @return
	 */
	List<ShopNotice> selectNoticeListByMember(Long memberId);
	
	/**
	 * 获取未读消息数量
	 * @param memberId
	 * @return
	 */
	Integer getNoticeCount(Long memberId);
	
	
}
