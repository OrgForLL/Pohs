package com.stylefeng.guns.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.md.goods.service.IGoodsService;
import com.md.member.service.IMemberService;
import com.md.order.service.IOrderItemService;
import com.md.order.service.IOrderService;
import com.stylefeng.guns.core.base.controller.BaseController;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/testOrder")
public class TestOrderController extends BaseController{

	@Resource
	IOrderService orderService;
	
	@Resource
	IMemberService memberService;
	
	@Resource
	IOrderItemService orderItemService;
	
	@Resource
	IGoodsService goodsService;
	
	public static final String user = "1851588926@qq.com";
	public static final String pwd="463111";
	public static final String account= "900003";
	public static final String appKey = "2588c2dc-d622-4e8f-9852-69d3a56fa3cd";
	public static final String appSecret="asq2q2";
	
	
	@ApiOperation(value = "测试", notes = "测试")
	@RequestMapping(value = "/saleDelivery", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject saleDelivery(){
		JSONObject jb = new JSONObject();
		String pathUrl= "http://192.168.35.96:8900/base-msgservice/Create";
		Map<String, String> mapParam = new HashMap<String, String>();
		String data = "{\"MsgTypeID\":3101,\"CreateID\":3100,\"MsgJson\":{\"id\":201801},\"RequestID\":\"\"}";
		mapParam.put("data", data);
		String result = sendPost(pathUrl, mapParam);
		System.out.println(result);
		jb.put("data", result);
		return jb;
	}
	
	public static String sendPost(String url, Map<String, String> paramMap) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// conn.setRequestProperty("Charset", "UTF-8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
 
			// 设置请求属性
			String param = "";
			if (paramMap != null && paramMap.size() > 0) {
				Iterator<String> ite = paramMap.keySet().iterator();
				while (ite.hasNext()) {
					String key = ite.next();// key
					String value = paramMap.get(key);
					param += key + "=" + value + "&";
				}
				param = param.substring(0, param.length() - 1);
			}
 
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.err.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}



}
