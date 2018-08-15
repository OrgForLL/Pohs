package com.md.order.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.order.constant.OrderStatus;
import com.md.order.dao.OrderMapper;
import com.md.order.model.Order;
import com.md.order.service.IOrderService;
import com.stylefeng.guns.core.page.Page;
import com.stylefeng.guns.core.util.ToolUtil;

@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
	@Resource
	OrderMapper orderMapper;

	@Override
	public List<Map<String, Object>> find(Order order) {
		Wrapper<Order> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(order)) {
			if (ToolUtil.isNotEmpty(order.getSn())) {
				wrapper.eq("sn", order.getSn());
			}
			if (ToolUtil.isNotEmpty(order.getStatus())) {
				wrapper.eq("status", order.getStatus());
			}else {
				wrapper.ne("status", OrderStatus.DELETE.getCode());
			}
			if (ToolUtil.isNotEmpty(order.getMemberId())) {
				wrapper.eq("memberId", order.getMemberId());
			}
			if (ToolUtil.isNotEmpty(order.getPhoneNum())) {
				wrapper.eq("phoneNum", order.getPhoneNum());
			}
			if (ToolUtil.isNotEmpty(order.getConsigneeName())) {
				wrapper.eq("consigneeName", order.getConsigneeName());
			}
			if (ToolUtil.isNotEmpty(order.getShopId())) {
				wrapper.eq("shopId", order.getShopId());
			}
		}
		wrapper.orderBy("createTime", false);
		return orderMapper.selectMaps(wrapper);
	}

	@Override
	public Long add(Order order) {
		orderMapper.insert(order);
		return order.getId();
	}

	@Override
	public void update(Order order) {
		orderMapper.updateById(order);
	}

	@Override
	public Order getById(Long id) {
		return orderMapper.selectById(id);
	}

	@Override
	public void editRemark(Long orderId, String remark) {
		Order order = getById(orderId);
		order.setRemark(remark);
		orderMapper.updateById(order);
	}

	@Override
	public void editStatus(Long orderId, OrderStatus status) {
		Order order = getById(orderId);
		order.setStatus(status.getCode());
		orderMapper.updateById(order);
	}

	@Override
	public List<Order> findListByPage(Long memberId, Integer status, Integer index) {
		Wrapper<Order> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(memberId)) {
			wrapper.eq("memberId", memberId);
		}
		if (ToolUtil.isNotEmpty(status)) {
			wrapper.eq("status", status);
		}else {
			wrapper.ne("status", OrderStatus.DELETE.getCode());
		}
		wrapper.orderBy("createTime",false);
		Integer begin = (index - 1) * Page.PAGESIZE.getCode();
		RowBounds rowBounds = new RowBounds(begin,Page.PAGESIZE.getCode());
		return orderMapper.selectPage(rowBounds, wrapper);
	}

	@Override
	public List<Order> getListByCancel() {
		Wrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.where("status = 0 and TO_DAYS( NOW( ) ) - TO_DAYS(createTime) >= 1");
		return orderMapper.selectList(wrapper);
	}

	@Override
	public List<Order> findOfSn(String sn) {
		Wrapper<Order> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(sn)) {
			wrapper.eq("sn", sn);
			wrapper.orderBy("createTime", false);
			return orderMapper.selectList(wrapper);
		}
		return null;
		
	}

	@Override
	public List<Order> getListByEvaluation() {
		// TODO 自动生成的方法存根
		Wrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.where("status = 6 and TO_DAYS( NOW( ) ) - TO_DAYS(createTime) >= 7");
		return orderMapper.selectList(wrapper);
	}

	@Override
	public List<Order> getListByPayTimescale(String tiemscale) {
		// TODO 自动生成的方法存根
		Wrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.where("TO_DAYS(paytime) = TO_DAYS("+tiemscale+")");
		return orderMapper.selectList(wrapper);
	}

	@Override
	public Integer selectOrderCount(Long memberId, Integer status) {
		// TODO 自动生成的方法存根
		Wrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.eq("memberId", memberId);
		wrapper.eq("status", status);
		return orderMapper.selectCount(wrapper);
	}

	@Override
	public List<Order> findListByCondition(Timestamp startTime, Timestamp endTime, Long memberId, Long orderId,
			Integer status, Integer index) {
		
		Wrapper<Order> wrapper = new EntityWrapper<>();
		if(ToolUtil.isNotEmpty(startTime)) {
			wrapper.ge("createTime", startTime);
		}
		if(ToolUtil.isNotEmpty(endTime)) {
			wrapper.le("createTime", endTime);
		}
		if (ToolUtil.isNotEmpty(memberId)) {
			wrapper.eq("memberId", memberId);
		}
		if(orderId != 0l) {
			wrapper.eq("id", orderId);
		}
		if (ToolUtil.isNotEmpty(status)) {
			wrapper.eq("status", status);
		}else {
			wrapper.ne("status", OrderStatus.DELETE.getCode());
		}
		wrapper.orderBy("createTime",false);
		Integer begin = (index - 1) * Page.PAGESIZE.getCode();
		RowBounds rowBounds = new RowBounds(begin,Page.PAGESIZE.getCode());
		return orderMapper.selectPage(rowBounds, wrapper);
	}
}
