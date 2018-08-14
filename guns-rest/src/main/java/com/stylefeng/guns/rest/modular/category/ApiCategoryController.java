package com.stylefeng.guns.rest.modular.category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.md.goods.model.Category;
import com.md.goods.service.ICategoryRelationService;
import com.md.goods.service.ICategoryService;
import com.stylefeng.guns.rest.modular.category.dto.CategoryRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 分类接口
 * @author 54476
 *
 */
@RestController
public class ApiCategoryController{

	@Resource
	ICategoryService categoryService;
	
	@Resource
	ICategoryRelationService categoryRelationService;
	
	@RequestMapping(value = "/category/getCategoryList",method = RequestMethod.POST)
	public ResponseEntity<?> getCategoryList(@RequestBody CategoryRequest categoryRequest) {
		List<Category> result = categoryService.findCategoryList(categoryRequest.getPid());
		if(categoryRequest.getPid() != 0 ) {
			for(Category category : result ) {
				List<Category> list = categoryService.findCategoryList(category.getId());
				category.setList(list);
			}
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/category/getAllCategoryList",method = RequestMethod.POST)
	public ResponseEntity<?> getAllCategoryList(@RequestBody CategoryRequest categoryRequest) {
		List<Category> result = categoryService.findCategoryList(categoryRequest.getPid());
		for(Category category : result ) {
			List<Category> list = categoryService.findCategoryList(category.getId());
			if(list.size() > 0) {
				for(Category categorys : list ) {
					List<Category> lists = categoryService.findCategoryList(categorys.getId());
					categorys.setList(lists);
				}
			}
			category.setList(list);
		}
		return ResponseEntity.ok(result);
	}

}
