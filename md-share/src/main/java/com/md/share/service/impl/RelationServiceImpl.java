package com.md.share.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.md.share.dao.RelationMemberMapper;
import com.md.share.model.RelationMember;
import com.md.share.service.IRelationService;

@Service
@Transactional
public class RelationServiceImpl extends ServiceImpl<RelationMemberMapper,RelationMember> implements IRelationService {

	

}
