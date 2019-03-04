package org.springboot.example.swagger.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springboot.example.swagger.properties.SwaggerProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Slf4j
@Configuration
@Import({Swagger2Configuration.class})
public class SwaggerAutoConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    /**
     * swagger-ui-配置 (被废除了绝大部分，可以不要，但是可以自定义)
     *
     * @param swaggerProperties swagger配置属性文件
     * @return
     */
    @Bean
    public UiConfiguration uiConfiguration(SwaggerProperties swaggerProperties) {
        return UiConfigurationBuilder.builder()
                .deepLinking(swaggerProperties.getUiConfig().getDeepLinking())
                .defaultModelExpandDepth(swaggerProperties.getUiConfig().getDefaultModelExpandDepth())
                .defaultModelRendering(swaggerProperties.getUiConfig().getDefaultModelRendering())
                .defaultModelsExpandDepth(swaggerProperties.getUiConfig().getDefaultModelsExpandDepth())
                .displayOperationId(swaggerProperties.getUiConfig().getDisplayOperationId())
                .displayRequestDuration(swaggerProperties.getUiConfig().getDisplayRequestDuration())
                .docExpansion(swaggerProperties.getUiConfig().getDocExpansion())
                .maxDisplayedTags(swaggerProperties.getUiConfig().getMaxDisplayedTags())
                .operationsSorter(swaggerProperties.getUiConfig().getOperationsSorter())
                .showExtensions(swaggerProperties.getUiConfig().getShowExtensions())
                .tagsSorter(swaggerProperties.getUiConfig().getTagsSorter())
                .validatorUrl(swaggerProperties.getUiConfig().getValidatorUrl())
                .build();
    }

    /**
     * 配置基于 ApiKey 的鉴权对象
     *
     * @return
     */
    private ApiKey apiKey() {
        return new ApiKey(
                swaggerProperties().getAuthorization().getName(),
                swaggerProperties().getAuthorization().getKeyName(),
                ApiKeyVehicle.HEADER.getValue());
    }

    /**
     * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
     * 其中 securityReferences 为配置启用的鉴权策略
     *
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                // securityReferences 为配置启用的鉴权策略
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(swaggerProperties().getAuthorization().getAuthRegex()))
                .build();
    }

    /**
     * 配置默认的全局鉴权策略；其中返回的 SecurityReference 中，reference 即为ApiKey对象里面的name，保持一致才能开启全局鉴权
     *
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(
                SecurityReference.builder()
                .reference(swaggerProperties().getAuthorization().getName())
                .scopes(authorizationScopes).build());
    }

    /**
     * Swagger-创建RestFul-API
     *
     * @param swaggerProperties swagger配置属性文件
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    // @ConditionalOnMissingBean 仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean
    @ConditionalOnBean(value = {UiConfiguration.class})                         // @ConditionalOnBean （仅仅在当前上下文中存在某个对象时，才会实例化一个Bean）
    @ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)     // @ConditionalOnProperty表示如果在application.properties中配置了spring.enabled属性，则该配置会生效
    public List<Docket> createRestApi(SwaggerProperties swaggerProperties) {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();

        // 配置Bean工厂
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> docketList = new LinkedList<>();

        // 没有分组
        if (swaggerProperties.getDocket().size() == 0) {
            /*********************************** 设置基本信息 ***************************************/
            ApiInfo apiInfo = new ApiInfoBuilder()
                    // 标题
                    .title(swaggerProperties.getTitle())
                    // 描述
                    .description(swaggerProperties.getDescription())
                    // 版本
                    .version(swaggerProperties.getVersion())
                    // 许可证
                    .license(swaggerProperties.getLicense())
                    // 许可证url
                    .licenseUrl(swaggerProperties.getLicenseUrl())
                    // 维护人
                    .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                    // 服务条款URL
                    .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                    .build();

            /*********************************** 匹配路径规则 *****************************************/
            // base-path处理
            if (swaggerProperties.getBasePath().isEmpty()) {
                // 当没有，配置任何path的时候，解析/**
                swaggerProperties.getBasePath().add("/**");
            }
            // 有，添加path规格
            List<Predicate<String>> basePath = new ArrayList();
            for (String path : swaggerProperties.getBasePath()) {
                basePath.add(PathSelectors.ant(path));
            }

            // exclude-path处理
            List<Predicate<String>> excludePath = new ArrayList<>();
            for (String path : swaggerProperties.getExcludePath()) {
                excludePath.add(PathSelectors.ant(path));
            }

            /***********************************构建 文档 *******************************************/
            Docket docketForBuilder = new Docket(DocumentationType.SWAGGER_2)
                    .host(swaggerProperties.getHost()) //获取主机地址
                    // 基本信息
                    .apiInfo(apiInfo)
                    // 安全方案
                    .securitySchemes(Collections.singletonList(apiKey())) // ApiKey 的鉴权对象
                    // securityReferences 为配置启用的鉴权策略
                    .securityContexts(Collections.singletonList(securityContext()))
                    // 全局参数配置
                    .globalOperationParameters(buildGlobalOperationParametersFromSwaggerProperties(swaggerProperties.getGlobalOperationParameters()));

            // 设置全局响应消息
            if (!swaggerProperties.getApplyDefaultResponseMessages()) {
                buildGlobalResponseMessage(swaggerProperties, docketForBuilder);
            }

            Docket docket = docketForBuilder.select()
                    .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                    .paths(
                            Predicates.and(
                                    Predicates.not(Predicates.or(excludePath)),
                                    Predicates.or(basePath)
                            )
                    ).build();

            /* 设置忽略参数类型 **/
            Class<?>[] array = new Class[swaggerProperties.getIgnoredParameterTypes().size()];
            Class<?>[] ignoredParameterTypes = swaggerProperties.getIgnoredParameterTypes().toArray(array);
            docket.ignoredParameterTypes(ignoredParameterTypes);

            // 注册，只被实例化一次
            configurableBeanFactory.registerSingleton("defaultDocket", docket);
            docketList.add(docket);
            return docketList;
        }

        // 分组创建
        for (String groupName : swaggerProperties.getDocket().keySet()) {
            SwaggerProperties.DocketInfo docketInfo = swaggerProperties.getDocket().get(groupName);

            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title(docketInfo.getTitle().isEmpty() ? swaggerProperties.getTitle() : docketInfo.getTitle())
                    .description(docketInfo.getDescription().isEmpty() ? swaggerProperties.getDescription() : docketInfo.getDescription())
                    .version(docketInfo.getVersion().isEmpty() ? swaggerProperties.getVersion() : docketInfo.getVersion())
                    .license(docketInfo.getLicense().isEmpty() ? swaggerProperties.getLicense() : docketInfo.getLicense())
                    .licenseUrl(docketInfo.getLicenseUrl().isEmpty() ? swaggerProperties.getLicenseUrl() : docketInfo.getLicenseUrl())
                    .contact(
                            new Contact(
                                    docketInfo.getContact().getName().isEmpty() ? swaggerProperties.getContact().getName() : docketInfo.getContact().getName(),
                                    docketInfo.getContact().getUrl().isEmpty() ? swaggerProperties.getContact().getUrl() : docketInfo.getContact().getUrl(),
                                    docketInfo.getContact().getEmail().isEmpty() ? swaggerProperties.getContact().getEmail() : docketInfo.getContact().getEmail()
                            )
                    )
                    .termsOfServiceUrl(docketInfo.getTermsOfServiceUrl().isEmpty() ? swaggerProperties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                    .build();

            // base-path处理
            // 当没有配置任何path的时候，解析/**
            if (docketInfo.getBasePath().isEmpty()) {
                docketInfo.getBasePath().add("/**");
            }
            List<Predicate<String>> basePath = new ArrayList();
            for (String path : docketInfo.getBasePath()) {
                basePath.add(PathSelectors.ant(path));
            }

            // exclude-path处理
            List<Predicate<String>> excludePath = new ArrayList();
            for (String path : docketInfo.getExcludePath()) {
                excludePath.add(PathSelectors.ant(path));
            }

            Docket docketForBuilder = new Docket(DocumentationType.SWAGGER_2)
                    .host(swaggerProperties.getHost())
                    .apiInfo(apiInfo)
                    .securitySchemes(Collections.singletonList(apiKey()))
                    .securityContexts(Collections.singletonList(securityContext()))
                    .globalOperationParameters(assemblyGlobalOperationParameters(swaggerProperties.getGlobalOperationParameters(),
                            docketInfo.getGlobalOperationParameters()));


            // 全局响应消息
            if (!swaggerProperties.getApplyDefaultResponseMessages()) {
                buildGlobalResponseMessage(swaggerProperties, docketForBuilder);
            }

            Docket docket = docketForBuilder.groupName(groupName)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()))
                    .paths(
                            Predicates.and(
                                    Predicates.not(Predicates.or(excludePath)),
                                    Predicates.or(basePath)
                            )
                    )
                    .build();

            /* ignoredParameterTypes **/
            Class<?>[] array = new Class[docketInfo.getIgnoredParameterTypes().size()];
            Class<?>[] ignoredParameterTypes = docketInfo.getIgnoredParameterTypes().toArray(array);
            docket.ignoredParameterTypes(ignoredParameterTypes);

            configurableBeanFactory.registerSingleton(groupName, docket);
            docketList.add(docket);
        }
        return docketList;
    }





    /**
     * 构建全局参数配置
     *
     * @param globalOperationParameters
     * @return
     */
    private List<Parameter> buildGlobalOperationParametersFromSwaggerProperties(
            List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters) {
        List<Parameter> parameters = newArrayList();

        if (Objects.isNull(globalOperationParameters)) {
            return parameters;
        }
        for (SwaggerProperties.GlobalOperationParameter globalOperationParameter : globalOperationParameters) {
            parameters.add(new ParameterBuilder()
                    .name(globalOperationParameter.getName())
                    .description(globalOperationParameter.getDescription())
                    .modelRef(new ModelRef(globalOperationParameter.getModelRef()))
                    .parameterType(globalOperationParameter.getParameterType())
                    .required(Boolean.parseBoolean(globalOperationParameter.getRequired()))
                    .build());
        }
        return parameters;
    }

    /**
     * 局部参数按照name覆盖局部参数
     *
     * @param globalOperationParameters
     * @param docketOperationParameters
     * @return
     */
    private List<Parameter> assemblyGlobalOperationParameters(
            List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters,
            List<SwaggerProperties.GlobalOperationParameter> docketOperationParameters) {

        if (Objects.isNull(docketOperationParameters) || docketOperationParameters.isEmpty()) {
            return buildGlobalOperationParametersFromSwaggerProperties(globalOperationParameters);
        }

        Set<String> docketNames = docketOperationParameters.stream()
                .map(SwaggerProperties.GlobalOperationParameter::getName)
                .collect(Collectors.toSet());

        List<SwaggerProperties.GlobalOperationParameter> resultOperationParameters = newArrayList();

        if (Objects.nonNull(globalOperationParameters)) {
            for (SwaggerProperties.GlobalOperationParameter parameter : globalOperationParameters) {
                if (!docketNames.contains(parameter.getName())) {
                    resultOperationParameters.add(parameter);
                }
            }
        }

        resultOperationParameters.addAll(docketOperationParameters);
        return buildGlobalOperationParametersFromSwaggerProperties(resultOperationParameters);
    }

    /**
     * 设置全局响应消息
     *
     * @param swaggerProperties swaggerProperties 支持 POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE
     * @param docketForBuilder  swagger docket builder
     */
    private void buildGlobalResponseMessage(SwaggerProperties swaggerProperties, Docket docketForBuilder) {

        SwaggerProperties.GlobalResponseMessage globalResponseMessages = swaggerProperties.getGlobalResponseMessage();

        /* POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE 响应消息体 **/
        List<ResponseMessage> postResponseMessages = getResponseMessageList(globalResponseMessages.getPost());
        List<ResponseMessage> getResponseMessages = getResponseMessageList(globalResponseMessages.getGet());
        List<ResponseMessage> putResponseMessages = getResponseMessageList(globalResponseMessages.getPut());
        List<ResponseMessage> patchResponseMessages = getResponseMessageList(globalResponseMessages.getPatch());
        List<ResponseMessage> deleteResponseMessages = getResponseMessageList(globalResponseMessages.getDelete());
        List<ResponseMessage> headResponseMessages = getResponseMessageList(globalResponseMessages.getHead());
        List<ResponseMessage> optionsResponseMessages = getResponseMessageList(globalResponseMessages.getOptions());
        List<ResponseMessage> trackResponseMessages = getResponseMessageList(globalResponseMessages.getTrace());

        docketForBuilder.useDefaultResponseMessages(swaggerProperties.getApplyDefaultResponseMessages())
                .globalResponseMessage(RequestMethod.POST, postResponseMessages)
                .globalResponseMessage(RequestMethod.GET, getResponseMessages)
                .globalResponseMessage(RequestMethod.PUT, putResponseMessages)
                .globalResponseMessage(RequestMethod.PATCH, patchResponseMessages)
                .globalResponseMessage(RequestMethod.DELETE, deleteResponseMessages)
                .globalResponseMessage(RequestMethod.HEAD, headResponseMessages)
                .globalResponseMessage(RequestMethod.OPTIONS, optionsResponseMessages)
                .globalResponseMessage(RequestMethod.TRACE, trackResponseMessages);
    }

    /**
     * 获取返回消息体列表
     *
     * @param globalResponseMessageBodyList 全局Code消息返回集合
     * @return
     */
    private List<ResponseMessage> getResponseMessageList
    (List<SwaggerProperties.GlobalResponseMessageBody> globalResponseMessageBodyList) {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        for (SwaggerProperties.GlobalResponseMessageBody globalResponseMessageBody : globalResponseMessageBodyList) {
            ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder();
            responseMessageBuilder.code(globalResponseMessageBody.getCode()).message(globalResponseMessageBody.getMessage());

            if (!StringUtils.isEmpty(globalResponseMessageBody.getModelRef())) {
                responseMessageBuilder.responseModel(new ModelRef(globalResponseMessageBody.getModelRef()));
            }
            responseMessages.add(responseMessageBuilder.build());
        }

        return responseMessages;
    }


}