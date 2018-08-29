package com.stylefeng.guns.rest.modular.notice;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.md.notice.model.ShopNotice;
import com.md.notice.service.IShopNoticeService;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.rest.modular.notice.dto.NoticeRequest;

import io.swagger.annotations.ApiOperation;

/**
 * 通知
 * 
 * @author 54476
 *
 */
@RestController
@RequestMapping("/notice")
public class ApiNoticeController extends BaseController {

	@Resource
	IShopNoticeService shopNoticeService;
	/**
	 * 消息已读接口
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "通知消息已读接口", notes = "通知消息已读接口")
	@RequestMapping(value = "/setNoticeRead", method = RequestMethod.POST)
	public ResponseEntity<?> setNoticeRead(@RequestBody NoticeRequest noticeRequest) {

		ShopNotice shopNotice =  shopNoticeService.selectById(noticeRequest.getNoticeId());
		shopNotice.setStatus(1);
		boolean flag = shopNoticeService.updateById(shopNotice);
		if(!flag){
			return ResponseEntity.ok(ERROR);
		}
		return ResponseEntity.ok(SUCCESS);
	}
	/**
	 * 获取消息详情
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "获取消息详情", notes = "获取消息详情")
	@RequestMapping(value = "/getNotice", method = RequestMethod.POST)
	public ResponseEntity<?> getNotice(@RequestBody NoticeRequest noticeRequest) {
		
		ShopNotice shopNotice =  shopNoticeService.selectById(noticeRequest.getNoticeId());
		return ResponseEntity.ok(shopNotice);
	}
	
	/**
	 * 获取消息通知列表
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "获取消息通知列表", notes = "获取消息通知列表")
	@RequestMapping(value = "/selectMyNoticeList", method = RequestMethod.POST)
	public ResponseEntity<?> selectMyNoticeList(@RequestBody NoticeRequest noticeRequest) {
		
		List<ShopNotice> shopNoticeList =  shopNoticeService.selectNoticeListByMember(noticeRequest.getMemberId());
		return ResponseEntity.ok(shopNoticeList);
	}

	/**
	 * 获取消息通知列表
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "获取未读消息数量", notes = "获取未读消息数量")
	@RequestMapping(value = "/noticeCount", method = RequestMethod.POST)
	public ResponseEntity<?> getNoticeCount(@RequestBody NoticeRequest noticeRequest) {
		
		Integer count =  shopNoticeService.getNoticeCount(noticeRequest.getMemberId());
		return ResponseEntity.ok(count);
	}
	
	/**
	 * 删除消息
	 * 
	 * @param memberId
	 * @return
	 */
	@ApiOperation(value = "删除消息", notes = "删除消息")
	@RequestMapping(value = "/removeNotice", method = RequestMethod.POST)
	public ResponseEntity<?> removeNotice(@RequestBody NoticeRequest noticeRequest) {
		
		ShopNotice shopNotice =  shopNoticeService.selectById(noticeRequest.getNoticeId());
		shopNotice.setStatus(2);
		boolean flag = shopNoticeService.updateById(shopNotice);
		if(flag){
			return ResponseEntity.ok(ERROR);
		}
		return ResponseEntity.ok(SUCCESS);
	}

}
