package com.md.order.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.order.dao.ShippingMapper;
import com.md.order.model.Order;
import com.md.order.model.Shipping;
import com.md.order.service.IShippingService;

@Service
@Transactional
public class ShippingServiceImpl extends ServiceImpl<ShippingMapper, Shipping> implements IShippingService {
	@Resource
	ShippingMapper shippingMapper;

	@Override
	public List<Shipping> findByOrderId(Long orderId) {
		Wrapper<Shipping> wrapper = new EntityWrapper<>();
		wrapper.eq("orderId", orderId);
		return shippingMapper.selectList(wrapper);
	}

	@Override
	public void add(Shipping shipping) {
		shippingMapper.insert(shipping);
	}

	@Override
	public List<Shipping> getListByWaitGains(Integer num) {
		// TODO 自动生成的方法存根
		Wrapper<Shipping> wrapper = new EntityWrapper<>();
		wrapper.where("type = 0 and TO_DAYS( NOW( ) ) - TO_DAYS(createTime) >= " + num);
		return shippingMapper.selectList(wrapper);
	}
	
	@Override
	public List<Shipping> getListByWaitGainsLe(Integer num) {
		// TODO 自动生成的方法存根
		Wrapper<Shipping> wrapper = new EntityWrapper<>();
		wrapper.where("type = 0 and TO_DAYS( NOW( ) ) - TO_DAYS(createTime) <= " + num);
		return shippingMapper.selectList(wrapper);
	}

}
