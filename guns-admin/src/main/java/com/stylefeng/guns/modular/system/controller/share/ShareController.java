package com.stylefeng.guns.modular.system.controller.share;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.md.goods.service.IShopService;
import com.md.member.factory.MemberFactory;
import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.md.member.warpper.MemberTooWarpper;
import com.md.member.warpper.MemberWarpper;
import com.md.order.constant.OrderStatus;
import com.md.order.model.Order;
import com.md.order.model.Shipping;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderLogService;
import com.md.order.service.IOrderService;
import com.md.order.service.IShippingItemService;
import com.md.order.service.IShippingService;
import com.md.share.model.Configure;
import com.md.share.model.DefaultConfigure;
import com.md.share.model.SharePayApply;
import com.md.share.service.IConfigureService;
import com.md.share.service.IDefaultConfigureService;
import com.md.share.service.ISharePayApplyService;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.exception.GunsException;

/**
 * 分销控制器
 *
 * @author fudaqian
 * @Date 2017年2月17日20:27:22
 */
@Controller
@RequestMapping("/share")
public class ShareController extends BaseController {
	
    @Resource
    private GunsProperties gunsProperties;
    @Resource
    IMemberService memberServiceImpl;
	@Resource
	IConfigureService configureService;
	@Resource
	IDefaultConfigureService defaultConfigureService;
	@Resource
	ISharePayApplyService sharePayApplyService;
	
	private String PREFIX = "/share/";
	
    /**
     * 跳转到分销申请审核
     */
    @RequestMapping("/apply")
    public String apply(Model model) {
        return PREFIX + "share_apply.html";
    }
    
    /**
     * 跳转到分销申请审核
     */
    @RequestMapping("/payListView")
    public String payListView(Model model) {
    	return PREFIX + "share_pay_apply.html";
    }
    
    /**
     * 跳转到活动分享配置管理
     */
    @RequestMapping("/activity")
    public String activityShare(Model model) {
    	return PREFIX + "activity.html";
    }
    
    /**
     * 跳转到商品分享配置管理
     */
    @RequestMapping("/goods")
    public String goodsShare(Model model) {
    	return PREFIX + "goods.html";
    }
    
    /**
     * 跳转到商品分享配置页
     */
    @RequestMapping("/shareView/{goodsId}")
    public String shareGoodsView(@PathVariable Long goodsId ,Model model) {
    	Configure configure = new Configure(); 
    	configure.setAssociatedEntityId(goodsId);
    	List<Configure> configures = configureService.selectByAssociatedEntity(goodsId, 3);
    	if(configures.size() != 0){
    		configure = configures.get(0);
    	}
    	model.addAttribute("configure", configure);
    	return PREFIX + "share_goods_view.html";
    }
    /**
     * 跳转到活动分享配置页
     */
    @RequestMapping("/shareActivity/{activityId}")
    public String shareActivityView(@PathVariable Long activityId ,Model model) {
    	Configure configure = new Configure(); 
    	configure.setAssociatedEntityId(activityId);
    	List<Configure> configures = configureService.selectByAssociatedEntity(activityId, 2);
    	if(configures.size() != 0){
    		configure = configures.get(0);
    	}
    	model.addAttribute("configure", configure);
    	return PREFIX + "share_activity_view.html";
    }
    /**
     * 跳转到商品分享默认配置页
     */
    @RequestMapping("/shareGoodsView")
    public String shareGoodsView(Model model) {
    	DefaultConfigure defaultConfigure = new DefaultConfigure(); 
    	List<DefaultConfigure> defaultConfigures = defaultConfigureService.selectByType(3);
    	if(defaultConfigures.size() != 0){
    		defaultConfigure = defaultConfigures.get(0);
    	}
    	model.addAttribute("defaultConfigure", defaultConfigure);
    	return PREFIX + "default_share_goods_view.html";
    }
    /**
     * 跳转到活动分享默认配置页
     */
    @RequestMapping("/shareActivityView")
    public String shareActivityView(Model model) {
    	DefaultConfigure defaultConfigure = new DefaultConfigure(); 
    	List<DefaultConfigure> defaultConfigures = defaultConfigureService.selectByType(2);
    	if(defaultConfigures.size() != 0){
    		defaultConfigure = defaultConfigures.get(0);
    	}
    	model.addAttribute("defaultConfigure", defaultConfigure);
    	return PREFIX + "default_share_activity_view.html";
    }
    
    /**
     * 跳转到商城分享默认配置页
     */
    @RequestMapping("/shop")
    public String shareShopView(Model model) {
    	DefaultConfigure defaultConfigure = new DefaultConfigure(); 
    	List<DefaultConfigure> defaultConfigures = defaultConfigureService.selectByType(1);
    	if(defaultConfigures.size() != 0){
    		defaultConfigure = defaultConfigures.get(0);
    	}
    	model.addAttribute("defaultConfigure", defaultConfigure);
    	return PREFIX + "default_share_shop_view.html";
    }
    
    /**
     * 获取分销员列表
     */
    @RequestMapping(value = "/applyList")
    @ResponseBody
    public Object applyList(Integer status) {
    	List<Map<String, Object>> shareUsers = memberServiceImpl.findShareUser(status);
    	return new MemberWarpper(shareUsers).warp();
    }
    /**
     * 获取分润提现申请列表
     */
    @RequestMapping(value = "/payApplyList")
    @ResponseBody
    public Object payApplyList(Integer status) {
    	List<Map<String, Object>> sharePayApplys = sharePayApplyService.selectAmountByStatusToo(null, status);
    	return new MemberTooWarpper(sharePayApplys).warp();
    }
    
    /**
     * 分润提现结果记录
     */
    @RequestMapping(value = "/agreeWithPay")
    @ResponseBody
    public Object agreeWithPay(Long payApplyId,Integer status) {
    	SharePayApply sharePayApply = sharePayApplyService.selectById(payApplyId);
    	sharePayApply.setStatus(status);
    	sharePayApplyService.updateById(sharePayApply);
    	if(status == 1){
    		//	SmsUtil
    	}
    	return SUCCESS;
    }
    /**
     * 分销员审核结果记录
     */
    @RequestMapping(value = "/agreeWithShareUser")
    @ResponseBody
    public Object agreeWithShareUser(Long shareUserId,Integer status) {
    	Member member = memberServiceImpl.selectById(shareUserId);
    	member.setStatus(status);
    	memberServiceImpl.updateById(member);
    	if(status == 1){
    		//	SmsUtil
    	}
    	return SUCCESS;
    }
    
    /**
     * 记录默认分享配置
     */
    @RequestMapping(value = "/addDefaultShareDefaultConfigure")
    @ResponseBody
    public Object addDefaultShareGoods(DefaultConfigure defaultConfigure) {
    	List<DefaultConfigure> defaultConfigures = defaultConfigureService.selectByType(defaultConfigure.getType());
    	if(defaultConfigures.size() == 0){
    		defaultConfigureService.insert(defaultConfigure);
    	}else{
    		defaultConfigure.setId(defaultConfigures.get(0).getId());
    		defaultConfigureService.updateById(defaultConfigure);
    	}
    	return SUCCESS;
    }

    /**
     * 记录分享配置
     */
    @RequestMapping(value = "/addShareConfigure")
    @ResponseBody
    public Object addShareGoods(Configure configure) {
    	List<Configure> configures = configureService.selectByAssociatedEntity(configure.getAssociatedEntityId(), configure.getType());
    	if(configures.size() == 0){
    		configureService.insert(configure);
    	}else{
    		configure.setId(configures.get(0).getId());
    		configureService.updateById(configure);
    	}
    	return SUCCESS;
    }
    
    /**
     * 上传分享图片
     */
    @RequestMapping(method = RequestMethod.POST, path = "/upload")
    @ResponseBody
    public String upload(@RequestPart("file") MultipartFile picture) {
        String pictureName = UUID.randomUUID().toString() + ".jpg";
        try {
            String fileSavePath = gunsProperties.getFileUploadPath();
            picture.transferTo(new File(fileSavePath + pictureName));
        } catch (Exception e) {
            throw new GunsException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return pictureName;
    }
    
}
