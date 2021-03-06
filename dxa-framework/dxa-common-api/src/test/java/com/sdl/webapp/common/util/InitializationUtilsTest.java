package com.sdl.webapp.common.util;

import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.core.io.Resource;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Collection;
import java.util.EnumSet;
import java.util.EventListener;
import java.util.Properties;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InitializationUtilsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void shouldGetAllResources() throws Exception {
        //when
        Collection<Resource> resources = InitializationUtils.getAllResources();

        //then
        assertEquals(6, resources.size());
        assertThat(resources, contains(
                // contains() also check the order of items
                hasProperty("path", containsString("dxa.defaults.properties")),
                hasProperty("path", containsString("dxa.modules.cid.properties")),
                hasProperty("path", containsString("dxa.modules.xo.properties")),
                hasProperty("path", containsString("dxa.properties")),
                hasProperty("path", containsString("dxa.addons.staging.properties")),
                hasProperty("path", containsString("dxa.addons.utf8-bom"))));
    }

    @Test
    public void shouldOverridePropertyInTheConsequentFile() {
        //when
        Properties properties = InitializationUtils.loadDxaProperties();

        //then
        assertEquals("utf8-bom", properties.getProperty("dxa.override.utf8bom"));
        assertEquals("defaults", properties.getProperty("dxa.override.defaults"));
        assertEquals("modules", properties.getProperty("dxa.override.modules"));
        assertEquals("user", properties.getProperty("dxa.override.user"));
        assertEquals("addons", properties.getProperty("dxa.override.addons"));
    }

    @Test
    public void shouldNotLoadNotDxaProperties() {
        //when
        Properties properties = InitializationUtils.loadDxaProperties();

        //then
        assertNull(properties.getProperty("not.dxa.property"));
    }

    @Test
    public void shouldLoadResourcesOnlyOnce() {
        //when
        Collection<Resource> resources = InitializationUtils.getAllResources();
        Collection<Resource> resources2 = InitializationUtils.getAllResources();

        //then
        assertSame(resources, resources2);
    }

    @Test
    public void shouldLoadPropertiesOnlyOnce() {
        //when
        Properties properties = InitializationUtils.loadDxaProperties();
        Properties properties2 = InitializationUtils.loadDxaProperties();

        //then
        assertSame(properties, properties2);
    }

    @Test
    public void shouldRegisterListener() {
        //given
        ServletContext context = mock(ServletContext.class);
        EventListener listener = mock(EventListener.class);

        //when
        InitializationUtils.registerListener(context, listener);

        //then
        verify(context).addListener(same(listener));
    }

    @Test
    public void shouldRegisterListenerByClass() {
        //given
        ServletContext context = mock(ServletContext.class);
        Class<EventListener> listenerClass = EventListener.class;

        //when
        InitializationUtils.registerListener(context, listenerClass);

        //then
        verify(context).addListener(same(listenerClass));
    }

    @Test
    public void shouldRegisterListenerByNameIfPresent() {
        //given
        ServletContext context = mock(ServletContext.class);
        String className = "java.util.EventListener";

        //when
        InitializationUtils.registerListener(context, className);

        //then
        verify(context).addListener(same(className));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRegisterFilter() {
        //given
        ServletContext context = mock(ServletContext.class);
        Filter filter = mock(Filter.class);
        String mapping = "/mapping";
        FilterRegistration.Dynamic registration = mock(FilterRegistration.Dynamic.class);
        when(context.addFilter(eq(filter.getClass().getName()), same(filter))).thenReturn(registration);

        //when
        FilterRegistration.Dynamic dynamic = InitializationUtils.registerFilter(context, filter, mapping);

        //then
        verify(context).addFilter(eq(filter.getClass().getName()), same(filter));
        verify(registration).addMappingForUrlPatterns((EnumSet<DispatcherType>) Matchers.isNull(), eq(false), eq(mapping));
        assertSame(registration, dynamic);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRegisterFilterByClass() {
        //given
        ServletContext context = mock(ServletContext.class);
        String mapping = "/mapping";
        Class<Filter> filterClass = Filter.class;
        FilterRegistration.Dynamic registration = mock(FilterRegistration.Dynamic.class);
        when(context.addFilter(eq(filterClass.getName()), same(filterClass))).thenReturn(registration);

        //when
        FilterRegistration.Dynamic filterRegistration = InitializationUtils.registerFilter(context, filterClass, mapping);

        //then
        assertSame(registration, filterRegistration);
        verify(context).addFilter(eq(filterClass.getName()), same(filterClass));
        verify(registration).addMappingForUrlPatterns((EnumSet<DispatcherType>) Matchers.isNull(), eq(false), eq(mapping));
    }

    @Test
    public void shouldGiveClassForNameIfPresentOfNullOtherwise() {
        //when
        Class<?> present = InitializationUtils.classForNameIfPresent("java.lang.String");
        Class<?> notPresent = InitializationUtils.classForNameIfPresent("qwe.asd.zxc.NotPresentedClass");

        //then
        assertEquals(present, String.class);
        assertNull(notPresent);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRegisterFilterByNameIfPresent() {
        //given
        ServletContext context = mock(ServletContext.class);
        String className = "javax.servlet.Filter";
        String mapping = "/mapping";
        FilterRegistration.Dynamic registration = mock(FilterRegistration.Dynamic.class);
        when(context.addFilter(same(className), same(className))).thenReturn(registration);

        //when
        FilterRegistration.Dynamic dynamic = InitializationUtils.registerFilter(context, className, mapping);

        //then
        verify(context).addFilter(same(className), same(className));
        verify(registration).addMappingForUrlPatterns((EnumSet<DispatcherType>) Matchers.isNull(), eq(false), eq(mapping));
        assertSame(registration, dynamic);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRegisterServletByNameIfPresent() {
        //given
        ServletContext context = mock(ServletContext.class);
        String className = "javax.servlet.Servlet";
        String mapping = "/mapping";

        ServletRegistration.Dynamic servletRegistration = mock(ServletRegistration.Dynamic.class);
        when(context.addServlet(same(className), same(className))).thenReturn(servletRegistration);

        //when
        ServletRegistration.Dynamic servletRegistration1 = InitializationUtils.registerServlet(context, className, mapping);

        //then
        verify(context).addServlet(same(className), same(className));
        verify(servletRegistration).addMapping(eq(mapping));
        assertSame(servletRegistration, servletRegistration1);
    }

    @Test
    public void shouldRegisterServlet() {
        //given
        ServletContext context = mock(ServletContext.class);
        Servlet servlet = mock(Servlet.class);
        String mapping = "/mapping";

        ServletRegistration.Dynamic servletRegistration = mock(ServletRegistration.Dynamic.class);
        when(context.addServlet(eq(servlet.getClass().getName()), same(servlet))).thenReturn(servletRegistration);

        //when
        ServletRegistration.Dynamic servletRegistration1 = InitializationUtils.registerServlet(context, servlet, mapping);

        //then
        verify(context).addServlet(eq(servlet.getClass().getName()), same(servlet));
        verify(servletRegistration).addMapping(eq(mapping));
        assertSame(servletRegistration, servletRegistration1);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldNotRegisterAnythingByNameIfNotPresent() {
        //given
        ServletContext context = mock(ServletContext.class);
        String className = "com.example.asd.zxc.qwe.NotPresentedClass";
        String mapping = "/mapping";

        FilterRegistration.Dynamic filterRegistration = mock(FilterRegistration.Dynamic.class);
        when(context.addFilter(same(className), same(className))).thenReturn(filterRegistration);

        ServletRegistration.Dynamic servletRegistration = mock(ServletRegistration.Dynamic.class);
        when(context.addServlet(same(className), same(className))).thenReturn(servletRegistration);

        //when
        FilterRegistration.Dynamic filterRegistration1 = InitializationUtils.registerFilter(context, className, mapping);

        //then
        verify(context, never()).addFilter(same(className), same(className));
        verify(filterRegistration, never()).addMappingForUrlPatterns((EnumSet<DispatcherType>) Matchers.isNull(), eq(false), eq(mapping));
        assertNull(filterRegistration1);

        //when
        InitializationUtils.registerListener(context, className);

        //then
        verify(context, never()).addListener(anyString());

        //when
        ServletRegistration.Dynamic servletRegistration1 = InitializationUtils.registerServlet(context, className, mapping);

        //then
        verify(context, never()).addServlet(same(className), same(className));
        verify(filterRegistration, never()).addMappingForUrlPatterns((EnumSet<DispatcherType>) Matchers.isNull(), eq(false), eq(mapping));
        assertNull(servletRegistration1);
    }


}