package com.md.share.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.share.dao.ShareAmountMapper;
import com.md.share.model.ShareAmount;
import com.md.share.service.IShareAmountService;

@Service
@Transactional
public class ShareAmountServiceImpl extends ServiceImpl<ShareAmountMapper,ShareAmount> implements IShareAmountService {

	@Resource
	ShareAmountMapper shareAmountMapper;

	@Override
	public List<ShareAmount> selectByShareMemberId(Long shareMemberId,Integer status) {
		// TODO 自动生成的方法存根
		List<ShareAmount> shareAmounts = new ArrayList<>();
		Wrapper<ShareAmount> wrapper = new EntityWrapper<>();
		wrapper.eq("shareMemberId", shareMemberId);	
		wrapper.eq("status", status);	
		shareAmounts = shareAmountMapper.selectList(wrapper);
		return shareAmounts;
	}

}
