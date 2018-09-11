package com.stylefeng.guns.rest.modular.pay;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.md.member.model.Member;
import com.md.member.service.IMemberService;
import com.md.order.model.Order;
import com.md.order.model.OrderItem;
import com.md.order.model.RefundApply;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.md.order.service.IRefundApplyService;
import com.md.pay.model.WeiXin;
import com.md.pay.service.IALiPayService;
import com.md.pay.service.IWeixinService;
import com.md.pay.service.IwxPayService;
import com.md.share.model.Configure;
import com.md.share.model.DefaultConfigure;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.exception.ApiException;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.config.properties.RestProperties;
import com.stylefeng.guns.rest.modular.pay.dto.PayRequest;
import com.stylefeng.guns.rest.modular.pay.dto.RefundApplyRequest;

import io.swagger.annotations.ApiOperation;
import weixin.popular.api.TicketAPI;
import weixin.popular.bean.paymch.MchBaseResult;
import weixin.popular.bean.paymch.MchPayNotify;
import weixin.popular.bean.paymch.MchPayRefundNotify;
import weixin.popular.bean.paymch.UnifiedorderResult;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.util.JsonUtil;
import weixin.popular.util.PayUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;

/**
 * 客户信息
 * 
 * @author 54476
 *
 */
@RestController
@RequestMapping("/pay")
public class ApiPayController extends BaseController {

	@Resource
	IOrderService orderServiceImpl;
	@Resource
	IMemberService MemberServiceImpl;
	@Resource
	IwxPayService wxPayServiceImpl;
	@Resource
	IWeixinService weixinService;
	@Resource
	IRefundApplyService refundApplyService;
	@Resource
	IALiPayService aLiPayService;
    @Resource 
    IOrderItemService orderItemService;
    @Autowired
	RestProperties restProperties;
    

	/**
	 * 微信支付
	 */
	@ApiOperation(value = "微信支付", notes = "微信支付")
	@RequestMapping(value = "/wxpay", method = { RequestMethod.POST })
	public ResponseEntity<?> wxpay(@RequestBody PayRequest payRequest, HttpServletRequest request) {
		JSONObject jb = new JSONObject();
		List<Order> orderList = new ArrayList<>();
		String orderSn =new Date().getTime()+ String.valueOf((int)((Math.random()* 9 + 1) * 100000));	
		List<Long> orderidList = payRequest.getOrderIdList();
		if (ToolUtil.isEmpty(orderidList)) {
			return ResponseEntity.ok(new ApiException(BizExceptionEnum.ORDER_NULL));
		}
		BigDecimal amount = new BigDecimal(0);
		//String orderSn = orderServiceImpl.selectById(orderidList.get(0)).getSn();
		for (Long orderid : orderidList) {
			Order order = orderServiceImpl.selectById(orderid);
			order.setSn(orderSn);
			orderServiceImpl.update(order);
			orderList.add(order);
			amount = amount.add(order.getActualPay());
		}
		Member member = MemberServiceImpl.selectById(payRequest.getMemberId());
		String memberIp = request.getRemoteAddr();
		UnifiedorderResult unifiedorderResult = wxPayServiceImpl.wxPayUnifiedorder(amount, orderSn, memberIp,
				member.getOpenId(),restProperties.getNotifyUrl(),payRequest.getTradeType(),payRequest.getConfigKey());
		if (("SUCCESS").equals(unifiedorderResult.getReturn_code())) {
			jb.put("code", unifiedorderResult.getReturn_code());
			jb.put("msg", unifiedorderResult.getReturn_msg());
			jb.put("data", PayUtil.generateMchPayJsRequestJson(unifiedorderResult.getPrepay_id(),
					unifiedorderResult.getAppid(), weixinService.getApiKey(payRequest.getConfigKey())));
		} else {
			jb.put("code", unifiedorderResult.getReturn_code());
			jb.put("msg", unifiedorderResult.getReturn_msg());
		}
		return ResponseEntity.ok(jb);
	}
	/**
	 * 支付宝支付
	 */
	@ApiOperation(value = "支付宝支付", notes = "支付宝支付")
	@RequestMapping(value = "/aLiPay", method = { RequestMethod.POST })
	public ResponseEntity<?> aLiPay(@RequestBody PayRequest payRequest, HttpServletRequest request ,HttpServletResponse response) {
		JSONObject jb = new JSONObject();
		String subject = "";
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
			for(OrderItem orderItem : orderItemService.findByOrderId(order.getId())) {
				subject += orderItem.getGoodsName() + ",";
    		}
		}
		Member member = MemberServiceImpl.selectById(payRequest.getMemberId());
		String form = aLiPayService.aLiPay(amount, orderSn, subject, member.getOpenId());
		response.setContentType("text/html;charset=UTF-8"); 
	    try {
			response.getWriter().write(form);
			//直接将完整的表单html输出到页面 
		    response.getWriter().flush(); 
		    response.getWriter().close();
	    } catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return ResponseEntity.ok(jb);
	}

	/**
	 * 微信支付成功通知接口(微信端)
	 * 
	 */
	@RequestMapping(value = "/webwxPayNotify", method = RequestMethod.GET)
	@ApiOperation(value = "微信支付成功通知接口(微信端)", notes = "")
	public void webwxPayNotify(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("微信支付回调成功");
		// 获取请求数据
		String xmlData = null;
		try {
			xmlData = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
			// 将XML转为MAP,确保所有字段都参与签名验证
			Map<String, String> mapData = XMLConverUtil.convertToMap(xmlData);
			// 转换数据对象
			MchPayNotify payNotify = XMLConverUtil.convertToObject(MchPayNotify.class, xmlData);
			payNotify.buildDynamicField(mapData);
			List<Order> orderList = orderServiceImpl.findOfSn(String.valueOf(mapData.get("out_trade_no")));
			String result = wxPayServiceImpl.webwxPayNotify(mapData, payNotify, orderList);
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
			e.printStackTrace();
		}

	}

	/**
	 * 微信退款成功通知接口(微信端)
	 * 
	 */
	@RequestMapping(value = "/webwxRefundNotify", method = RequestMethod.POST)
	@ApiOperation(value = "微信支付成功通知接口(微信端)", notes = "")
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

	// 获取签名
	@RequestMapping(value = "/getSignature", method = RequestMethod.POST)
	public ResponseEntity<?> getSignature(HttpServletRequest request, HttpServletResponse servletResponse,
			@RequestBody PayRequest payRequest) {
		String access_token = weixinService.getAccessToken(payRequest.getConfigKey());
		WeiXin weiXin = weixinService.getInfo(payRequest.getConfigKey());
		Ticket ticket = TicketAPI.ticketGetticket(access_token);
		String jsapi_ticket = ticket.getTicket();
		String noncestr = UUID.randomUUID().toString().replace("-", "");// 随机字符串
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);// 时间戳
		String url = payRequest.getHref();
		String str = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url="
				+ url;
		System.out.println("str = " + str);
		String signature = SHA1(str);
		System.out.println("signature = " + signature);
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("appId", weiXin.getAppid());
		map.put("nonceStr", noncestr);
		map.put("timeStamp", timestamp);
		map.put("signature", signature);
		return ResponseEntity.ok(JsonUtil.toJSONString(map));
	}

	public static String SHA1(String decript) {
		try {
			MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
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
			jb.put("data", "订单超过30天,无法退款");
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
