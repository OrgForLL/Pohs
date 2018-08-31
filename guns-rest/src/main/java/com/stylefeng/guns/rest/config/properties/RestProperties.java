package com.stylefeng.guns.rest.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目相关配置
 *
 * @author fengshuonan
 * @date 2017年10月23日16:44:15
 */
@Configuration
@ConfigurationProperties(prefix = RestProperties.REST_PREFIX)
public class RestProperties {

    public static final String REST_PREFIX = "rest";

    private boolean authOpen = true;

    private boolean signOpen = true;
    
    private String messagePath;  //外部消息接口
    
    private String message2Path;  //外部短信接口
    
    private String notifyUrl;  //微信支付异步回调地址接口
    
    public boolean isAuthOpen() {
        return authOpen;
    }

    public void setAuthOpen(boolean authOpen) {
        this.authOpen = authOpen;
    }

    public boolean isSignOpen() {
        return signOpen;
    }

    public void setSignOpen(boolean signOpen) {
        this.signOpen = signOpen;
    }

	public String getMessagePath() {
		return messagePath;
	}

	public void setMessagePath(String messagePath) {
		this.messagePath = messagePath;
	}


	public String getMessage2Path() {
		return message2Path;
	}

	public void setMessage2Path(String message2Path) {
		this.message2Path = message2Path;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}
}
