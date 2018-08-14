package com.md.pay.service.impl;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.md.order.dao.OrderMapper;
import com.md.order.dao.RefundApplyMapper;
import com.md.order.model.Order;
import com.md.pay.dao.PaymentOrderMapper;
import com.md.pay.dao.RefundOrderMapper;
import com.md.pay.dao.WeiXinMapper;
import com.md.pay.service.IALiPayService;
import com.alipay.api.response.AlipayTradeWapPayResponse;

@Service
public class ALiPayServiceImpl implements IALiPayService {

	@Resource
	OrderMapper orderMapper;
	@Resource
	PaymentOrderMapper paymentOrderMapper;
	@Resource
	RefundOrderMapper refundOrderMapper;
	@Resource
	WeiXinMapper weiXinMapper;
	@Resource
	RefundApplyMapper refundApplyMapper;

	// 商户appid
	public static String APPID = "2017062807585193";
	// 私钥 pkcs8格式的
	public static String RSA_PRIVATE_KEY = "MIICeQIBADANBgkqhkiG9w0BAQEFAASCAmMwggJfAgEAAoGBALk/rJbZcAvna/5Njy9Ha5eVqPsS58VzkLDe4rwyegN1Er+z/CVZLMwT7RhZosomgSFAJwLjDu0eOExWFGDjMwnjs0+V1OceSEeBD/NKIYl09djjWuGLiqOr8eBiZb79MBpQIePRw6d9s9wgAIOW+d0Xqd6ehie9Rh01lVSTfSj/AgMBAAECgYEAidw9ELYYkINU9xe5KzI3e8PA0bisC2GJmFcG3Y+jBdemcEv9pb3LfMSHecubPl2RU7FuVtOrVANdWlHlMIVkgPy/txQ831jHKvt0Z0INp3Qw4TVVMMa4w6jjhjnQgu6JZv7fUAYke0BEY4lkE2GJOYRuaK0uu/nuSFYeKsZ5koECQQD31NBe3A2SWiVRD1vGIk2p4vz++kEnq2ZEzBFOpvqyZPw07TMFseRP1maqd4NbcrgwD1zhRK0/wTZNH7jcs/QvAkEAv1rKx0Ej13A5T5qfawdEWYxlO+qu0ImWCbNIQcuXWHBYaOYL89/ekxZGKuXMMmRBHToRZMh+1TolQ1YhjtRUMQJBAOpgZgs/+snM3PuzFazkpiQjWfZdf57fluE7SKbkfbSWknoPVTBukyf58LAIRL8IWC6DaOhVxa7Er/DyLibzPNsCQQCLAvAxFHdvOQ3JcLDDVgInc8DPaEwbHviqj128vqRyERuJCSZBwq60+Ad9fnd7XC2Zh+e+6ZCKYrVTV60FT+bhAkEAvVk28OqXv9SrF8imypA2DbjfHkcfdCGHyA8XfcaurhPNzcV4/Vn/vDZH/vi8nAcUnAcaQ4IotE8HHFeaO9ASBw==";
	// 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "";
	// 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	// 商户可以自定义同步跳转地址
	public static String return_url = "";
	// 请求网关地址
	public static String URL = "https://openapi.alipay.com/gateway.do";
	// 编码
	public static String CHARSET = "UTF-8";
	// 返回格式
	public static String FORMAT = "json";
	// 支付宝公钥
	public static String ALIPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC5P6yW2XAL52v+TY8vR2uXlaj7EufFc5Cw3uK8MnoDdRK/s/wlWSzME+0YWaLKJoEhQCcC4w7tHjhMVhRg4zMJ47NPldTnHkhHgQ/zSiGJdPXY41rhi4qjq/HgYmW+/TAaUCHj0cOnfbPcIACDlvndF6nenoYnvUYdNZVUk30o/wIDAQAB";
	// 日志记录目录
	public static String log_path = "/log";
	// RSA2
	public static String SIGNTYPE = "RSA";

	@Override
	public String aLiPay(BigDecimal amount, String orderSn, String subject, String openid) {
		// TODO 自动生成的方法存根
		AlipayClient client = new DefaultAlipayClient(URL, APPID, RSA_PRIVATE_KEY, FORMAT, CHARSET, ALIPAY_PUBLIC_KEY,
				SIGNTYPE);
		AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();
		

		// 封装请求支付信息
		AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
		model.setOutTradeNo(orderSn);
		model.setSubject("商城测试商品");
		model.setTotalAmount(String.valueOf(amount));
		model.setTimeoutExpress("15m");
		model.setProductCode("QUICK_WAP_WAY");
		alipay_request.setBizModel(model);
		// 设置异步通知地址
		alipay_request.setNotifyUrl(notify_url);
		// 设置同步地址
		alipay_request.setReturnUrl(return_url);

		// form表单生产
		String form = "";
		try {
			// 调用SDK生成表单
			form = client.pageExecute(alipay_request).getBody();
		} catch (AlipayApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return form;
	}

}
