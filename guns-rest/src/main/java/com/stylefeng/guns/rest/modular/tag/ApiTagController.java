package com.stylefeng.guns.rest.modular.tag;


import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.md.goods.model.Tag;
import com.md.goods.service.ITagRelationService;
import com.md.goods.service.ITagService;
import com.stylefeng.guns.core.base.controller.BaseController;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/tag")
public class ApiTagController extends BaseController{

	@Resource
	ITagService tagService;
	
	@Resource
	ITagRelationService tagRelationService;

	@ApiOperation(value = "获取标签列表", notes = "获取标签列表")
	@RequestMapping(value = "/getTagList",method = RequestMethod.POST)
	public ResponseEntity<?> getTagList() {
		return ResponseEntity.ok(tagService.list(new Tag()));
	}
}
