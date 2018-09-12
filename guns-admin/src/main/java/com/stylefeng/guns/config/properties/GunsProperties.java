package com.stylefeng.guns.config.properties;

import static com.stylefeng.guns.core.util.ToolUtil.getTempPath;
import static com.stylefeng.guns.core.util.ToolUtil.isEmpty;

import java.io.File;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * guns项目配置
 *
 * @author stylefeng
 * @Date 2017/5/23 22:31
 */
@Component
@ConfigurationProperties(prefix = GunsProperties.PREFIX)
public class GunsProperties {

    public static final String PREFIX = "guns";

    private Boolean kaptchaOpen = false;

    private Boolean swaggerOpen = false;

    private String fileUploadPath;
    
    private String messagePath;  //外部消息接口
    
    private String message2Path;  //外部短息接口
    
    private Boolean messageOpen = false;
    
    private String norifyUrl;  //微信支付异步回调地址

    private Boolean haveCreatePath = false;

    private Boolean springSessionOpen = false;
    
    private String projectWebPath;

    private Integer sessionInvalidateTime = 30 * 60;  //session 失效时间（默认为30分钟 单位：秒）

    private Integer sessionValidationInterval = 15 * 60;  //session 验证失效时间（默认为15分钟 单位：秒）

    public String getFileUploadPath() {
        //如果没有写文件上传路径,保存到临时目录
        if (isEmpty(fileUploadPath)) {
            return getTempPath();
        } else {
            //判断有没有结尾符,没有得加上File.separator
            if (!fileUploadPath.endsWith(File.separator)) {
                fileUploadPath = fileUploadPath + File.separator;
            }
            //判断目录存不存在,不存在得加上
            if (haveCreatePath == false) {
                File file = new File(fileUploadPath);
                file.mkdirs();
                haveCreatePath = true;
            }
            return fileUploadPath;
        }
    }

    public String getProjectWebPath() {
		return projectWebPath;
	}

	public void setProjectWebPath(String projectWebPath) {
		this.projectWebPath = projectWebPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
        this.fileUploadPath = fileUploadPath;
    }

    public Boolean getKaptchaOpen() {
        return kaptchaOpen;
    }

    public void setKaptchaOpen(Boolean kaptchaOpen) {
        this.kaptchaOpen = kaptchaOpen;
    }

    public Boolean getSwaggerOpen() {
        return swaggerOpen;
    }

    public void setSwaggerOpen(Boolean swaggerOpen) {
        this.swaggerOpen = swaggerOpen;
    }

    public Boolean getSpringSessionOpen() {
        return springSessionOpen;
    }

    public void setSpringSessionOpen(Boolean springSessionOpen) {
        this.springSessionOpen = springSessionOpen;
    }

    public Integer getSessionInvalidateTime() {
        return sessionInvalidateTime;
    }

    public void setSessionInvalidateTime(Integer sessionInvalidateTime) {
        this.sessionInvalidateTime = sessionInvalidateTime;
    }

    public Integer getSessionValidationInterval() {
        return sessionValidationInterval;
    }

    public void setSessionValidationInterval(Integer sessionValidationInterval) {
        this.sessionValidationInterval = sessionValidationInterval;
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

	public String getNorifyUrl() {
		return norifyUrl;
	}

	public void setNorifyUrl(String norifyUrl) {
		this.norifyUrl = norifyUrl;
	}

	public Boolean getMessageOpen() {
		return messageOpen;
	}

	public void setMessageOpen(Boolean messageOpen) {
		this.messageOpen = messageOpen;
	}
}
