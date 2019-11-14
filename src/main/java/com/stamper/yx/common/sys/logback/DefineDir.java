package com.stamper.yx.common.sys.logback;

import ch.qos.logback.core.PropertyDefinerBase;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DefineDir extends PropertyDefinerBase {

	/**
	 * 动态定义logback日志输出文件路径
	 */
    @Override
	public String getPropertyValue() {
    	String path=System.getProperty("user.dir")+ File.separator+"logs";
        return path;
    }
}