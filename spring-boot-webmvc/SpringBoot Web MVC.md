# 笔记二 SpringBoot Web MVC

[TOC]

## 1. SpringBoot Web MVC 简介

> 1. 什么是 Spring MVC ?
>
> ​    Spring MVC 是一种解耦的组合。
>
> ​    M 代表的是Model, 也就是控制器返回的**数据模型层**。
>
> ​    V  代表的是View, 也就是用户体验视图感官的**试图解析层。**
>
> ​    C  代表的是Controller, 也就是**控制层**，用于控制返回数据模型。

![1551975137922](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1551975137922.png)

## 2. Spring MVC 流程

> 1. 流程和组件是 Spring MVC 的核心
> 2. Spring MVC 的流程是围绕 DispatcherServlet 工作的。

![1551975636754](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1551975636754.png)

## 3. SpringBoot MVC 自动配置原理

### 3.1  默认自动配置 WebMvcAutoConfiguration.class

> 官方地址：https://docs.spring.io/spring-boot/docs/current/reference/html/

#### 3.1.1 ContentNegotiatingViewResolver 和 BeanNameViewResolver

- 译文：自动配置了**ViewResolver视图解析器**

- 源码分析：**ContentNegotiatingViewResolver.class 组合所有的视图解析器**

```java
@Bean
@ConditionalOnBean(ViewResolver.class)
@ConditionalOnMissingBean(name="viewResolver",value=ContentNegotiatingViewResolver.class)
public ContentNegotiatingViewResolver viewResolver(BeanFactory beanFactory) {
  ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
 
  resolver.setContentNegotiationManager(beanFactory.getBean(ContentNegotiationManager.class));

// contentatingviewresolver使用所有其他视图解析器来定位
//一个视图，因此它应该具有较高的优先级			
  resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
  return resolver;
}
```

- 编程案例：SpringBoot 提供了自定义视图解析器。

```java
package org.springboot.example.webmvc.resolver;

import org.springframework.web.servlet.View;

import java.util.Locale;

/**
 * 自定义实现视图解析器
 */
public class ViewResolver implements org.springframework.web.servlet.ViewResolver {

    /**
     * 解析视图名
     *
     * @param s
     * @param locale
     * @return
     * @throws Exception
     */
    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return null;
    }
}
```

放入到ioc容器，装载配置

```json
package org.springboot.example.webmvc;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

@Configuration
@AutoConfigureBefore(org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class)
public class WebMvcAutoConfiguration {

    /**
     * 将我自定义实现的视图解析器，放入到容器中。
     * @return
     */
    @Bean
    @ConditionalOnBean(org.springboot.example.webmvc.resolver.ViewResolver.class)
    public ViewResolver viewResolver(){
       return new org.springboot.example.webmvc.resolver.ViewResolver();
    }
}
```



#### 3.1.2 支持提供静态资源，包括对WebJars的支持（ [本文档稍后介绍](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-static-content)））

- 源码分析：ResourceProperties.class

  ```java
  @ConfigurationProperties(prefix = "spring.resources", ignoreUnknownFields = false)
  public class ResourceProperties {
  
  	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
  			"classpath:/META-INF/resources/", 
              "classpath:/resources/",
  			"classpath:/static/", 
              "classpath:/public/" };
  
  	// 默认静态资源文件路径
  	private String[] staticLocations = CLASSPATH_RESOURCE_LOCATIONS;
  
  	// 是否启用默认资源路径
  	private boolean addMappings = true;
  
  	private final Chain chain = new Chain();
  
  	private final Cache cache = new Cache();
  
      // 获取静态资源路径
  	public String[] getStaticLocations() {
  		return this.staticLocations;
  	}
     
      // 设置静态资源路径
  	public void setStaticLocations(String[] staticLocations) {
  		this.staticLocations = appendSlashIfNecessary(staticLocations);
  	}
  
  	private String[] appendSlashIfNecessary(String[] staticLocations) {
  		String[] normalized = new String[staticLocations.length];
  		for (int i = 0; i < staticLocations.length; i++) {
  			String location = staticLocations[i];
  			normalized[i] = location.endsWith("/") ? location : location + "/";
  		}
  		return normalized;
  	}
  
  ```

  WebMvcAutoConfiguration.class

  ```java
  @Override
  		public void addResourceHandlers(ResourceHandlerRegistry registry) {
  			if (!this.resourceProperties.isAddMappings()) {
  				logger.debug("Default resource handling disabled");
  				return;
  			}
  			Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
  			CacheControl cacheControl = this.resourceProperties.getCache()
  					.getCachecontrol().toHttpCacheControl();
  			if (!registry.hasMappingForPattern("/webjars/**")) {
  				customizeResourceHandlerRegistration(registry
  						.addResourceHandler("/webjars/**")
  						.addResourceLocations("classpath:/META-INF/resources/webjars/")
  						.setCachePeriod(getSeconds(cachePeriod))
  						.setCacheControl(cacheControl));
  			}
  			String staticPathPattern = this.mvcProperties.getStaticPathPattern();
  			if (!registry.hasMappingForPattern(staticPathPattern)) {
  				customizeResourceHandlerRegistration(
  						registry.addResourceHandler(staticPathPattern)
  								.addResourceLocations(getResourceLocations(
  										this.resourceProperties.getStaticLocations()))
  								.setCachePeriod(getSeconds(cachePeriod))
  								.setCacheControl(cacheControl));
  			}
  		}
  ```

  根据上面的源码，SpringBoot Web Mvc 自动配置将静态资源路径注册到容器中。

  我们可以修改配置文件application.properties 修改 **spring.resources** 静态资源配置。

  

#### 3.1.3 自动注册`Converter`，`GenericConverter`和`Formatter`beans

- **Converter**  转换器
- **Formatter** : 格式化器
- **GenericConverter**: 数组转换器

-  源码分析：spring boot 将所有的实现Converter ，Formatter,  GenericConverter 添加到格式注册器中。

```java
	@Override
    public void addFormatters(FormatterRegistry registry) {
		for (Converter<?, ?> converter : getBeansOfType(Converter.class)) {
				registry.addConverter(converter);
		}
		for (GenericConverter converter : getBeansOfType(GenericConverter.class)) {
			   registry.addConverter(converter);
		}
		for (Formatter<?> formatter : getBeansOfType(Formatter.class)) {
			  registry.addFormatter(formatter);
		}
    }
	
    private <T> Collection<T> getBeansOfType(Class<T> type) {
			return this.beanFactory.getBeansOfType(type).values();
    }
```

- 编程案例：自定义转化器，放入到容器中。

```java
  /**
     * 自定义格式转换器
     * 
     * @return
     */
    @Bean
    @ConditionalOnBean(FormatConverter.class)
    public FormatConverter formatConverter(){
        return new FormatConverter();
    }
```



#### 3.1.4 支持`HttpMessageConverters`（ [本文档后面部分](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-message-converters)）

- 作用:  用来转换Http请求和响应。

- 源码分析：将所有HttpMessageConverter 注册到容器中。

  ```json
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
  	this.messageConvertersProvider.ifAvailable((customConverters) -> converters
  					.addAll(customConverters.getConverters()));
  }
  ```

- 自动注册`MessageCodesResolver`（ [本文档后面部分](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-message-codes)）。

  - 作用：定义错误代码生成规则。



#### 3.1.5`index.html`支持。



#### 3.1.6`Favicon`支持（[本文档稍后介绍](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-favicon)）



#### 3.1.7`ConfigurableWebBindingInitializer`bean（ [本文档稍后介绍](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html#boot-features-spring-mvc-web-binding-initializer)）

- 作用：数据绑定器，验证数据。



> 拓展：
>
> - 如果您想保留Spring Boot MVC功能并且想要添加其他 [MVC配置](https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web.html#mvc)（拦截器，格式化程序，视图控制器和其他功能），您可以添加自己的`@Configuration`类类型`WebMvcConfigurer`但**不需要** `@EnableWebMvc`。
> - 如果您希望提供，或的 自定义实例`RequestMappingHandlerMapping`，则可以声明 实例以提供此类组件。`RequestMappingHandlerAdapter``ExceptionHandlerExceptionResolver``WebMvcRegistrationsAdapter`
>
> 如果您想完全控制Spring MVC，可以添加自己的`@Configuration` 注释`@EnableWebMvc`。
>
> 注意：如果使用了@EnableWebMvc , springBoot 自动配置自动失效。
>
> - 原因：@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)：容器中没有这个webMvcConfigurationSupport.class 才起作用。



## 4. SpringBoot MVC 拓展

> - Spring Mvc 的拓展配置主要是通过 WebMvcConfigurer.class
> - Spring Boot Mvc 拓展功能（拦截器，格式化程序，视图控制器和其他功能）

```java
package org.springboot.example.webmvc.config;

import org.springboot.example.webmvc.interceptor.CustomerHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Boot MVC 拓展功能（拦截器，格式化程序，视图控制器和其他功能）
 */
@Configuration
public class ExtractMvcConfig implements WebMvcConfigurer {

    /**
     * 添加视图控制器
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
         registry.addViewController("/index.html").setViewName("login");
         registry.addViewController("/").setViewName("login");
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry
                .addInterceptor(new CustomerHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/index.html",
                        "/",
                        "/user/login",
                        "/**/*.js",
                        "/**/*.css",
                        "/**/*.svg"
                );

    }


}

```



## 4. 国际化

> 国际化：各个国家语言文字的转换。

### 4.1 国际化消息源-配置项

1. 在application.properites 文件中编写国际化属性。

```properties
############################### il8n ####################################
# 设置国际化属性名（如果多个可以使用逗号分隔，默认为message）
spring.messages.basename= i18n.login
# 设置国际化消息是否采用格式化，默认为 false
spring.messages.always-use-message-format= false
# 设置国际化消息缓存超时秒数（默认为永远不过期，如果0表示每次都重新加载）
spring.messages.cache-duration=
# 国际化消息编码
spring.messages.encoding= utf-8
# 如果没有找到特定区域设置文件，则设置是否返回到系统区域设置
spring.messages.fallback-to-system-locale=true
# 是否使用消息编码作为默认的响应消息，而排抛出 NoSuchMessageException 异常， 只建议在开发阶段使用
spring.messages.use-code-as-default-message=false

```

### 4.2 国际化的消息设计图

### ![1552871613074](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1552871613074.png)4.3. 中文和英、美国国际化在resources 目录，创建一个il8n 文件夹

![1552871955857](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1552871955857.png)

默认的文件名message.properites.但是我在配置文件中设置的是il8n.login.所以设置的配置文件**login.properites**

```properties
login.btn=Sign In
login.password=password
login.remember=Remember Me
login.tip=Please sign in
login.username=username
```

然后，需要设置中文和英文的配置文件login_en_US.properties 和 login_zh_CN.properties

```properties
########################## login_en_US.properties ##############################
login.btn=Sign In
login.password=password
login.remember=Remember Me
login.tip=Please sign in
login.username=username
```

```properties
########################## login_zh_CN.properties ##############################
login.btn=登录
login.password=密码
login.remember=记住我
login.tip=请登录
login.username=用户名
```



### 4.4 国际化解析器

#### 4.4.1 国际化解析器的设计模式

![1552872720943](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1552872720943.png)

#### 4.4.2 国际化解析配置

```properties
# 指定国际化区域，可以覆盖"Accept-Language" 头消息
spring.mvc.locale=zh_CN
# 国际化解析器，可以选择fixed 或 accept-header (fixed 代表固定的国际化， accept-header 代表读取浏览器的"Accept-Language 头信息")
spring.mvc.locale-resolver=accept_header

```

1.  实现自定义国际化

   ```json
   package org.springboot.example.webmvc.i18n;
   
   
   import org.springframework.util.StringUtils;
   
   import javax.servlet.http.HttpServletRequest;
   import javax.servlet.http.HttpServletResponse;
   import java.util.Locale;
   
   /**
    * 自定义国际化
    */
   public class CustomerLocaleResolver implements org.springframework.web.servlet.LocaleResolver {
   
       @Override
       public Locale resolveLocale(HttpServletRequest httpServletRequest) {
           String l = httpServletRequest.getParameter("l");
           Locale locale = Locale.getDefault();
           if(!StringUtils.isEmpty(l)){
               String[] split = l.split("_");
               locale = new Locale(split[0],split[1]);
           }
           return locale;
       }
   
       @Override
       public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
   
       }
   }
   ```



## 5. 拦截器

### 5.1 拦截器执行流程

![1552897539518](C:\Users\Calvin\AppData\Roaming\Typora\typora-user-images\1552897539518.png)

### 5.2 自定义拦截器

```java
package org.springboot.example.webmvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomerHandlerInterceptor implements HandlerInterceptor {

    /**
     * 目标方法执行之前
     *
     * @param request 获取请求session
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute("loginUser");
        if(user == null){
            //未登陆，返回登陆页面
            request.setAttribute("msg","没有权限请先登陆");
            request.getRequestDispatcher("/login.html").forward(request,response);
            return false;
        }else{
            //已登陆，放行请求
            return true;
        }

    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

```

