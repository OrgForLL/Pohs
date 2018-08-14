package com.md.share.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.md.share.model.ShareBind;

public interface IShareBindService extends IService<ShareBind> {

	List<ShareBind> selectByBoundMemberId(Long boundMemberId);

	
}
