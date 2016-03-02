package org.hxn.shopxx.web;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

public class ResourcePathExposer implements ServletContextAware {
	private ServletContext servletContext;
	private String contextPath;
	private String resourceRoot;
	private String imagePath = "http://lxjin.com:81/SilverShop";

	public void init() {
		contextPath = getServletContext().getContextPath();
		String version = "1.0.2";
		resourceRoot = "/resources-" + version;
		getServletContext().setAttribute("contextPath", contextPath);
		getServletContext().setAttribute("resourceRoot", contextPath + resourceRoot);
		getServletContext().setAttribute("imagePath", imagePath);
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getResourceRoot() {
		return resourceRoot;
	}

	public String getImagePath() {
		return imagePath;
	}
}
