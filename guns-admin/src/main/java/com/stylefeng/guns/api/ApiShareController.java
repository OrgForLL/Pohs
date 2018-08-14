package com.stylefeng.guns.api;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.md.share.model.Configure;
import com.md.share.model.DefaultConfigure;
import com.md.share.model.ShareBind;
import com.md.share.service.IConfigureService;
import com.md.share.service.IDefaultConfigureService;
import com.md.share.service.IShareBindService;
import com.stylefeng.guns.api.dto.ShareRequest;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.util.DateUtil;

import io.swagger.annotations.ApiOperation;

/**
 * 分销管理
 * 
 * @author 54476
 *
 */
@RestController
@RequestMapping("/apiShare")
public class ApiShareController extends BaseController {
	
	@Resource
	IMemberService MemberServiceImpl;
	@Resource
	IConfigureService configureServiceImpl;
	@Resource
	IDefaultConfigureService defaultConfigureService;
	@Resource
	IShareBindService shareBindService;
	
    /**
     * 成为分销员
     */
	@ApiOperation(value = "分销员注册", notes = "注册")
    @RequestMapping(value = "/register", method = { RequestMethod.POST })
    public ResponseEntity<?> register( @RequestBody ShareRequest shareRequest) {
		Member member = new Member();
    	List<Member> members = MemberServiceImpl.selectByPhoneNum(shareRequest.getPhone());
    	if(members.size() != 0){
    		member = members.get(0);
    		member.setType("分销员");
    		member.setShopIds(shareRequest.getShopIds());
    		member.setStatus(3);
    		MemberServiceImpl.updateById(member);
    		return ResponseEntity.ok(SUCCESS);
    	}
    	return ResponseEntity.ok(ERROR);
    	
    }
	
	/**
	 * 获取分享配置
	 */
	@ApiOperation(value = "获取分享配置", notes = "获取分享配置")
	@RequestMapping(value = "/getShareConfigure", method = { RequestMethod.POST })
	public ResponseEntity<?> getShareConfigure( @RequestBody ShareRequest shareRequest) {
		List<Configure> configures = configureServiceImpl.selectByAssociatedEntity(shareRequest.getAssociatedEntityId(),shareRequest.getType());
		if(configures.size() != 0){
			Configure configure = configures.get(0);
			if(configure.getIsDefault() == 0){
				return ResponseEntity.ok(configure);
			}
		}
		List<DefaultConfigure> defaultConfigures = defaultConfigureService.selectByType(shareRequest.getType());
		if(defaultConfigures.size() != 0){
			return ResponseEntity.ok(defaultConfigures.get(0));
		}
		return ResponseEntity.ok("");
	}
	
	/**
	 * 绑定被分享用户
	 */
	@ApiOperation(value = "绑定被分享用户", notes = "绑定被分享用户")
	@RequestMapping(value = "/bindMember", method = { RequestMethod.POST })
	public ResponseEntity<?> bindMember( @RequestBody ShareRequest shareRequest) {
		ShareBind shareBind = new ShareBind();
		List<ShareBind> shareBinds = shareBindService.selectByBoundMemberId(shareRequest.getBoundMemberId());
		if(shareBinds.size() == 0){
			shareBind.setBoundMemberId(shareRequest.getBoundMemberId());
			shareBind.setShareMemberId(shareRequest.getShareMemberId());
			shareBind.setBindTime(DateUtil.format(new Date()));
			shareBindService.insert(shareBind);
		}else{
			shareBind = shareBinds.get(0);
			if(shareBind.getShareMemberId().equals(shareRequest.getShareMemberId())){
				shareBind.setBindTime(DateUtil.format(new Date()));
				shareBindService.updateById(shareBind);
			}else{
				Calendar cld = Calendar.getInstance();
				cld.setTime(shareBind.getBindTime());
				cld.add(Calendar.DATE, 15);
				Timestamp timestamp = DateUtil.format(cld.getTime());
				if (!timestamp.before(new Date())) {
					shareBind.setShareMemberId(shareRequest.getShareMemberId());
					shareBind.setBindTime(DateUtil.format(new Date()));
					shareBindService.updateById(shareBind);
				}
			}
		}
		return ResponseEntity.ok(SUCCESS);
	}
	
	
}
