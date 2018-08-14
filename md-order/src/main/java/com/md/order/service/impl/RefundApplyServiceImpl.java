package com.md.order.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.SystemUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.pool.vendor.SybaseExceptionSorter;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.order.dao.RefundApplyMapper;
import com.md.order.model.Order;
import com.md.order.model.RefundApply;
import com.md.order.service.IRefundApplyService;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.ToolUtil;

@Service
@Transactional
public class RefundApplyServiceImpl extends ServiceImpl<RefundApplyMapper, RefundApply> implements IRefundApplyService {

	@Resource
	RefundApplyMapper refundApplyMapper;

	@Override
	public boolean add(Order order, String applyWhy, Integer refundType, Long orderItemId) {
		// TODO 自动生成的方法存根
		Wrapper<RefundApply> wrapper = new EntityWrapper<>();
		wrapper.where("(`status` = 7 OR `status` = 9)");
		wrapper.eq("orderId", order.getId());
		if (refundApplyMapper.selectList(wrapper).size() == 0) {
			RefundApply refundApply = new RefundApply();
			refundApply.setOrderId(order.getId());
			refundApply.setRefundType(refundType);
			refundApply.setOrderItemId(orderItemId);
			if (refundType == 1) {
				refundApply.setStatus(9);
			} else {
				refundApply.setStatus(7);
			}
			refundApply.setMemberId(order.getMemberId());
			refundApply.setApplyWhy(applyWhy);
			refundApply.setCreateTime(DateUtil.getTime());
			refundApplyMapper.insert(refundApply);
			return true;
		}
		return false;
	}

	@Override
	public List<Map<String, Object>> find(RefundApply refundApply) {
		// TODO 自动生成的方法存根
		Wrapper<RefundApply> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(refundApply)) {
			if (ToolUtil.isNotEmpty(refundApply.getOrderId())) {
				wrapper.eq("orderId", refundApply.getOrderId());
			}
			if (ToolUtil.isNotEmpty(refundApply.getStatus())) {
				wrapper.eq("status", refundApply.getStatus());
			}
			if (ToolUtil.isNotEmpty(refundApply.getStatus())) {
				wrapper.eq("orderId", refundApply.getOrderId());
			}
			if (ToolUtil.isNotEmpty(refundApply.getStatus())) {
				wrapper.eq("orderItemId", refundApply.getOrderItemId());
			}
		}
		wrapper.orderBy("createTime");
		return refundApplyMapper.selectMaps(wrapper);
	}

	@Override
	public List<Map<String, Object>> findMemberRefundApply(Long memberId) {
		// TODO 自动生成的方法存根
		Wrapper<RefundApply> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(memberId)) {
			wrapper.eq("memberId", memberId);
		}
		wrapper.orderBy("createTime");
		return refundApplyMapper.selectMaps(wrapper);
	}

	@Override
	public List<RefundApply> findMemberRefundApplyToo(Long memberId) {
		// TODO 自动生成的方法存根
		Wrapper<RefundApply> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(memberId)) {
			wrapper.eq("memberId", memberId);
		}
		wrapper.orderBy("createTime");
		return refundApplyMapper.selectList(wrapper);
	}

	@Override
	public RefundApply selectByRefundOrderSn(String out_refund_no) {
		Wrapper<RefundApply> wrapper = new EntityWrapper<>();
		if (ToolUtil.isNotEmpty(out_refund_no)) {
			wrapper.eq("refundOrderSn", out_refund_no);
			List<RefundApply> refundApplyList = refundApplyMapper.selectList(wrapper);
			if (refundApplyList.size() == 0) {
				return null;
			}
			return refundApplyList.get(0);
		}
		return null;

	}

	@Override
	public List<RefundApply> findRefundApplyByOrder(Long orderId, Long orderItemId) {
		// TODO 自动生成的方法存根
		Wrapper<RefundApply> wrapper = new EntityWrapper<>();
		wrapper.eq("orderId", orderId);
		wrapper.eq("orderItemId", orderItemId);
		return refundApplyMapper.selectList(wrapper);
	}

	@Override
	public Integer selectOrderCount(Long memberId, String statusArray) {
		// TODO 自动生成的方法存根
		Wrapper<RefundApply> wrapper = new EntityWrapper<>();
		wrapper.eq("memberId", memberId);
		wrapper.in("status", statusArray);
		return refundApplyMapper.selectCount(wrapper);
	}

}
