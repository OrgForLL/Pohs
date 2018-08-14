package com.md.notice.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.notice.dao.ShopNoticeMapper;
import com.md.notice.model.ShopNotice;
import com.md.notice.service.IShopNoticeService;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.ToolUtil;

@Service
@Transactional
public class ShopNoticeServiceImpl extends ServiceImpl<ShopNoticeMapper,ShopNotice> implements IShopNoticeService {

	@Resource
	ShopNoticeMapper shopNoticeMapper;
	
	@Override
	public boolean addOnOrderSend(String content, Long memberId) {
		// TODO 自动生成的方法存根
		ShopNotice shopNotice = new ShopNotice();
		shopNotice.setContent(content);
		shopNotice.setMemberId(memberId);
		shopNotice.setCreateTime(DateUtil.getTime());
		shopNotice.setType(1);
		shopNotice.setStatus(0);
		shopNotice.setTitle("订单发货");
		
		Integer num = shopNoticeMapper.insert(shopNotice);
		if(num == 1){
			return true;
		}
		return false;
	}

	@Override
	public boolean addOnOrderScheduled(String content, Long memberId) {
		// TODO 自动生成的方法存根
		ShopNotice shopNotice = new ShopNotice();
		shopNotice.setContent(content);
		shopNotice.setMemberId(memberId);
		shopNotice.setCreateTime(DateUtil.getTime());
		shopNotice.setType(1);
		shopNotice.setStatus(0);
		shopNotice.setTitle("订单关闭");
		
		Integer num = shopNoticeMapper.insert(shopNotice);
		if(num == 1){
			return true;
		}
		return false;
	}

	@Override
	public boolean addOnOrderCheck(String content, Long memberId) {
		// TODO 自动生成的方法存根
		ShopNotice shopNotice = new ShopNotice();
		shopNotice.setContent(content);
		shopNotice.setMemberId(memberId);
		shopNotice.setCreateTime(DateUtil.getTime());
		shopNotice.setType(1);
		shopNotice.setStatus(0);
		shopNotice.setTitle("订单审核关闭");
		
		Integer num = shopNoticeMapper.insert(shopNotice);
		if(num == 1){
			return true;
		}
		return false;
	}

	@Override
	public boolean addOnRefundPay(String content, Long memberId) {
		// TODO 自动生成的方法存根
		ShopNotice shopNotice = new ShopNotice();
		shopNotice.setContent(content);
		shopNotice.setMemberId(memberId);
		shopNotice.setCreateTime(DateUtil.getTime());
		shopNotice.setType(1);
		shopNotice.setStatus(0);
		shopNotice.setTitle("退款");
		
		Integer num = shopNoticeMapper.insert(shopNotice);
		if(num == 1){
			return true;
		}
		return false;
	}

	@Override
	public boolean addOnKfMsg(String content, Long memberId) {
		// TODO 自动生成的方法存根
		ShopNotice shopNotice = new ShopNotice();
		shopNotice.setContent(content);
		shopNotice.setMemberId(memberId);
		shopNotice.setCreateTime(DateUtil.getTime());
		shopNotice.setType(1);
		shopNotice.setStatus(0);
		shopNotice.setTitle("您收到一个客服消息！");
		
		Integer num = shopNoticeMapper.insert(shopNotice);
		if(num == 1){
			return true;
		}
		return false;
	}

	@Override
	public List<ShopNotice> selectNoticeList(ShopNotice shopNotice) {
		// TODO 自动生成的方法存根
		Wrapper<ShopNotice> wrapper = new EntityWrapper<>();
		if(ToolUtil.isNotEmpty(shopNotice)){
		}
		
		return shopNoticeMapper.selectList(wrapper);
	}

	@Override
	public List<ShopNotice> selectNoticeListByMember(Long memberId) {
		// TODO 自动生成的方法存根
		Wrapper<ShopNotice> wrapper = new EntityWrapper<>();
		wrapper.eq("memberId", memberId);
		wrapper.ne("status", 2);
		return shopNoticeMapper.selectList(wrapper);
	}

	@Override
	public Integer getNoticeCount(Long memberId) {
		Wrapper<ShopNotice> wrapper = new EntityWrapper<>();
		wrapper.eq("memberId", memberId);
		wrapper.eq("status", 0);
		return shopNoticeMapper.selectCount(wrapper);
	}


}
