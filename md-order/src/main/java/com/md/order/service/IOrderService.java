package com.md.order.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.md.order.constant.OrderStatus;
import com.md.order.model.Order;

public interface IOrderService extends IService<Order> {
	/**
	 * 查找订单
	 * 
	 * @param order
	 * @return
	 */
	List<Map<String, Object>> find(Order order);
	/**
	 * 根据订单编号查找订单
	 * 
	 * @param order
	 * @return
	 */
	List<Order> findOfSn(String sn);

	/**
	 * 新增订单
	 */
	Long add(Order order);

	/**
	 * 修改订单
	 */
	void update(Order order);

	/**
	 * 通过id查找订单
	 */
	Order getById(Long id);

	/**
	 * 修改订单的备注
	 * 
	 * @param orderId
	 * @param remark
	 */
	void editRemark(Long orderId, String remark);

	/**
	 * 修改订单的状态
	 * 
	 * @param orderId
	 * @param status
	 */
	void editStatus(Long orderId, OrderStatus status);

	List<Order> findListByPage(Long memberId, Integer status, Integer index);
	
	/**
	 * 获取未支付需要改成交易关闭的订单列表
	 * @param status
	 * @return
	 */
	List<Order> getListByCancel();
	/**
	 * 获取未评价的订单列表
	 * @param status
	 * @return
	 */
	List<Order> getListByEvaluation();
	/**
	 * 获取支付时间某一时间段的订单
	 * @param beforDay
	 * @return
	 */
	List<Order> getListByPayTimescale(String tiemscale);
	/**
	 * 获取用户的各个状态下的订单数量
	 * @param memberId
	 */
	Integer selectOrderCount(Long memberId,Integer status);
	/**
	 * 获取订单列表
	 * @param startTime
	 * @param endTime
	 * @param memberId
	 * @param orderId
	 * @param status
	 * @param index
	 * @return
	 */
	List<Order> findListByCondition(Timestamp startTime, Timestamp endTime, String memberId, String orderId, Integer status,
			Integer index,Integer pageSize);

}
