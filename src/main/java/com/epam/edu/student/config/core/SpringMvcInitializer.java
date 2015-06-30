package com.epam.edu.student.config.core;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.epam.edu.student.config.Config;

 
public class SpringMvcInitializer 
extends AbstractAnnotationConfigDispatcherServletInitializer {

@Override
protected Class<?>[] getRootConfigClasses() {
	return new Class[] { Config.class };
}

@Override
protected Class<?>[] getServletConfigClasses() {
	return null;
}

@Override
protected String[] getServletMappings() {
	return new String[] { "/" };
}

}
