package com.stylefeng.guns.api;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;
import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.md.order.model.Order;
import com.md.order.model.RefundApply;
import com.md.order.service.IOrderService;
import com.md.order.service.IRefundApplyService;
import com.md.pay.service.IWeixinService;
import com.md.pay.service.IwxPayService;
import com.stylefeng.guns.api.dto.PayRequest;
import com.stylefeng.guns.api.dto.RefundApplyRequest;
import com.stylefeng.guns.common.exception.BizExceptionEnum;
import com.stylefeng.guns.config.properties.GunsProperties;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.exception.ApiException;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.ToolUtil;

import io.swagger.annotations.ApiOperation;
import weixin.popular.bean.paymch.MchBaseResult;
import weixin.popular.bean.paymch.MchPayNotify;
import weixin.popular.bean.paymch.MchPayRefundNotify;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.util.PayUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;

/**
 * 客户信息
 * 
 * @author 54476
 *
 */
@Controller
@RequestMapping("/api/pay")
public class ApiPayController extends BaseController {

	@Resource
	IOrderService orderServiceImpl;
	@Resource
	IMemberService MemberServiceImpl;
	@Resource
	IwxPayService wxPayServiceImpl;
	@Resource
	IRefundApplyService refundApplyService;
	@Resource
	IWeixinService weixinService;
	@Resource
	private GunsProperties gunsProperties;
	
	
	/**
	 * 微信支付
	 */
	@ApiOperation(value = "微信支付", notes = "微信支付")
	@RequestMapping(value = "/wxpay", method = { RequestMethod.POST })
	public ResponseEntity<?> wxpay(@RequestBody PayRequest payRequest, HttpServletRequest request) {
		JSONObject jb = new JSONObject();
		List<Order> orderList = new ArrayList<>();
		List<Long> orderidList = payRequest.getOrderIdList();
		if (ToolUtil.isEmpty(orderidList)) {
			return ResponseEntity.ok(new ApiException(BizExceptionEnum.ORDER_NULL));
		}
		BigDecimal amount = new BigDecimal(0);
		String orderSn = orderServiceImpl.selectById(orderidList.get(0)).getSn();
		for (Long orderid : orderidList) {
			Order order = orderServiceImpl.selectById(orderid);
			orderList.add(order);
			amount = amount.add(order.getActualPay());
		}
		Member member = MemberServiceImpl.selectById(payRequest.getMemberId());
		String memberIp = request.getRemoteAddr();
		UnifiedorderResult unifiedorderResult = wxPayServiceImpl.wxPayUnifiedorder(amount, orderSn, memberIp,
				member.getOpenId(),payRequest.getTradeType(),gunsProperties.getNorifyUrl());		
		if(("SUCCESS").equals(unifiedorderResult.getReturn_code())){
			jb.put("code", unifiedorderResult.getReturn_code());
			jb.put("msg", unifiedorderResult.getReturn_msg());
			jb.put("data", PayUtil.generateMchPayJsRequestJson(unifiedorderResult.getPrepay_id(), unifiedorderResult.getAppid(),weixinService.getApiKey(payRequest.getPayType())));
		}else{
			jb.put("code", unifiedorderResult.getReturn_code());
			jb.put("msg", unifiedorderResult.getReturn_msg());
		}
		return ResponseEntity.ok(jb);
	}
	
	/**
	 * 微信支付成功通知接口(微信端)
	 * 
	 */
	@ApiOperation(value = "微信支付成功通知接口(微信端)", notes = "")
	@RequestMapping(value = "/webwxPayNotify", produces = "text/html;charset=UTF-8")
	public void webwxPayNotify(HttpServletRequest request,HttpServletResponse response) {
		System.out.println("微信支付回调成功");
		// 获取请求数据
		String xmlData = null;
		try {
			System.out.println("开始解析====");
			xmlData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
			// 将XML转为MAP,确保所有字段都参与签名验证
			Map<String, String> mapData = XMLConverUtil.convertToMap(xmlData);
			// 转换数据对象
			MchPayNotify payNotify = XMLConverUtil.convertToObject(MchPayNotify.class, xmlData);
			payNotify.buildDynamicField(mapData);
			List<Order> orderList = orderServiceImpl.findOfSn(String.valueOf(mapData.get("out_trade_no")));
			System.out.println(String.valueOf(mapData.get("out_trade_no")));
			System.out.println(orderList.toString());
			String result = wxPayServiceImpl.webwxPayNotify(mapData, payNotify ,orderList);
			System.out.println("result = " + result);
			if ("success".equals(result)) {
				MchBaseResult baseResult = new MchBaseResult();
				baseResult.setReturn_code("SUCCESS");
				baseResult.setReturn_msg("OK");
				response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
			} else {
				MchBaseResult baseResult = new MchBaseResult();
				baseResult.setReturn_code("FAIL");
				baseResult.setReturn_msg("ERROR");
				response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			System.out.println("抛异常了=======");
			e.printStackTrace();
		}

	}
	
	/**
	 * 微信退款成功通知接口(微信端)
	 * 
	 */
	@RequestMapping(value = "/webwxRefundNotify", produces = "text/html;charset=UTF-8")
	@ApiOperation(value = "微信退款成功通知接口(微信端)", notes = "")
	public void webwxRefundNotify(HttpServletRequest request, HttpServletResponse response) {
		// 获取请求数据
		String xmlData = null;
		try {
			xmlData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));

			// 将XML转为MAP,确保所有字段都参与签名验证
			Map<String, String> mapData = XMLConverUtil.convertToMap(xmlData);
			// 转换数据对象
			MchPayRefundNotify payNotify = XMLConverUtil.convertToObject(MchPayRefundNotify.class, xmlData);
			String result = wxPayServiceImpl.webwxPayRrfundNotify(mapData, payNotify);
			
			if ("success".equals(result)) {
				String out_refund_no = mapData.get("out_refund_no");
				RefundApply refundApply = refundApplyService.selectByRefundOrderSn(out_refund_no);
				if(ToolUtil.isNotEmpty(refundApply)){
					refundApply.setStatus(8);
					refundApplyService.updateById(refundApply);
				}
				MchBaseResult baseResult = new MchBaseResult();
				baseResult.setReturn_code("SUCCESS");
				baseResult.setReturn_msg("OK");
				response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
			} else {
				MchBaseResult baseResult = new MchBaseResult();
				baseResult.setReturn_code("FAIL");
				baseResult.setReturn_msg("ERROR");
				response.getOutputStream().write(XMLConverUtil.convertToXML(baseResult).getBytes());
			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	
	@ApiOperation(value = "申请退款", notes = "申请退款")
	@RequestMapping(value = "/refundApply", method = RequestMethod.POST)
	public ResponseEntity<?> refundApply(@RequestBody RefundApplyRequest refundApplyRequest) {
		JSONObject jb = new JSONObject();
		Order order = orderServiceImpl.selectById(refundApplyRequest.getOrderId());
		Calendar cld = Calendar.getInstance();
		cld.setTime(order.getCreateTime());
		cld.add(Calendar.DATE, 30);
		Timestamp timestamp = DateUtil.format(cld.getTime());
		if (timestamp.before(new Date())) {
			jb.put("data", "订单过期,无法退款");
			return ResponseEntity.ok(jb);
		}
		boolean flag;
		if (ToolUtil.isNotEmpty(order)) {
			flag = refundApplyService.add(order, refundApplyRequest.getApplyWhy(), refundApplyRequest.getRefundType() ,refundApplyRequest.getOrderItemId());
		} else {
			jb.put("data", "查找不到订单");
			return ResponseEntity.ok(jb);
		}
		if (flag) {
			jb.put("data", "申请成功");
			return ResponseEntity.ok(jb);
		}
		jb.put("data", "请勿重复申请，请联系客服");
		return ResponseEntity.ok(jb);
	}
	
	@ApiOperation(value = "提交物流信息", notes = "提交物流信息")
	@RequestMapping(value = "/submitogisticsMsg", method = RequestMethod.POST)
	public ResponseEntity<?> submitogisticsMsg(@RequestBody RefundApplyRequest refundApplyRequest) {
		if(ToolUtil.isNotEmpty(refundApplyRequest.getRefundApplyId())) {
			RefundApply refundApply = refundApplyService.selectById(refundApplyRequest.getRefundApplyId());
			refundApply.setLogisticsMsg(refundApplyRequest.getLogisticsMsg());
			refundApplyService.updateById(refundApply);
			return ResponseEntity.ok(SUCCESS);
		}
		return ResponseEntity.ok(ERROR);
	}

}
