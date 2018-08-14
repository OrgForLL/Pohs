package com.md.share.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.share.dao.RecodeMapper;
import com.md.share.model.Recode;
import com.md.share.service.IRecodeService;

@Service
@Transactional
public class RecodeServiceImpl extends ServiceImpl<RecodeMapper,Recode> implements IRecodeService {

	

}
