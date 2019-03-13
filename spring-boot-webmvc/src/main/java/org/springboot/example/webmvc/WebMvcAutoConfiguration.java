package org.springboot.example.webmvc;

import org.springboot.example.webmvc.i18n.CustomerLocaleResolver;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
@AutoConfigureBefore(org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.class)
public class WebMvcAutoConfiguration {

    /**
     * 自定义视图解析器
     *
     * @return
     */
//    @Bean
//    @ConditionalOnMissingBean(org.springboot.example.webmvc.resolver.ViewResolver.class)
//    public ViewResolver viewResolver() {
//        return new org.springboot.example.webmvc.resolver.ViewResolver();
//    }

//    /**
//     * 自定义格式转换器
//     *
//     * @return
//     */
//    @Bean
//    @ConditionalOnMissingBean(FormatConverter.class)
//    public FormatConverter formatConverter() {
//        return new FormatConverter();
//    }


    @Bean
    public LocaleResolver localeResolver(){
        return new CustomerLocaleResolver();
    }
}
