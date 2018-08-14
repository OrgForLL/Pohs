package com.md.share.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.md.share.model.ShareAmount;

public interface IShareAmountService extends IService<ShareAmount> {

	List<ShareAmount> selectByShareMemberId(Long shareMemberId, Integer status);

}
