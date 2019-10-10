package com.stamper.yx.common.sys.context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class SpringContextUtils implements ApplicationContextAware {
	private static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringContextUtils.context = context;
	}

	public static ApplicationContext getContext() {
		return context;
	}


	public static HttpServletRequest getRequest() {
		try {
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			return request;
		} catch (Exception e) {
			return null;
		}
	}

	public static String getRequestURI() {
		try {
			String requestURI = getRequest().getRequestURI();
			if (StringUtils.isNotBlank(requestURI) && requestURI.startsWith("//")) {
				requestURI = requestURI.substring(1);
			}
			return requestURI;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}