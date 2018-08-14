package com.md.share.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.md.share.model.SharePayApply;

public interface ISharePayApplyService extends IService<SharePayApply> {

	List<SharePayApply> selectAmountByStatus(Long memberId, Integer status);
	List<Map<String, Object>> selectAmountByStatusToo(Long memberId, Integer status);

}
