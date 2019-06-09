 package org.springboot.example.example.swagger.properties;

 import lombok.AllArgsConstructor;
 import lombok.Data;
 import lombok.NoArgsConstructor;
 import org.springframework.boot.context.properties.ConfigurationProperties;
 import springfox.documentation.swagger.web.DocExpansion;
 import springfox.documentation.swagger.web.ModelRendering;
 import springfox.documentation.swagger.web.OperationsSorter;
 import springfox.documentation.swagger.web.TagsSorter;

 import java.util.ArrayList;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;

 @ConfigurationProperties(prefix = "swagger")
 @Data
 public class SwaggerProperties {

   /** 是否开启swagger **/
   private Boolean enabled;

   /************************************************ swagger 基本信息描述 *****************************************/
   /**标题**/
   private String title = "";
   /**描述**/
   private String description = "";
   /**版本**/
   private String version = "";
   /**许可证**/
   private String license = "";
   /**许可证URL**/
   private String licenseUrl = "";
   /**服务条款URL**/
   private String termsOfServiceUrl = "";
   /**忽略的参数类型**/
   private List<Class<?>> ignoredParameterTypes = new ArrayList<>();
   /** 联系人**/
   private Contact contact = new Contact();


   /********************************************** swagger Path规则 *****************************************/
   /**swagger会解析的包路径**/
   private String basePackage = "";
   /**swagger会解析的url规则**/
   private List<String> basePath = new ArrayList<>();
   /**在basePath基础上需要排除的url规则**/
   private List<String> excludePath = new ArrayList<>();


   /********************************************** swagger 分组文档配置 **************************************/
   /**分组文档**/
   private Map<String, DocketInfo> docket = new LinkedHashMap<>();


   /********************************************** swagger 其他配置 *****************************************/
   /**host信息**/
   private String host = "";

   /********************************************* swagger 响应式配置*****************************************/
   /**全局响应消息**/
   private GlobalResponseMessage globalResponseMessage;

   /**是否使用默认预定义的响应消息 ，默认 true**/
   private Boolean applyDefaultResponseMessages = true;

   /*********************************************** swagger 全局参数配置 ************************************/
   /**全局参数配置**/
   private List<GlobalOperationParameter> globalOperationParameters;

   /*********************************************** swagger 页面功能配置 ************************************/
   /**页面功能配置**/
   private UiConfig uiConfig = new UiConfig();

   /*********************************************** swagger 统一鉴权配置 ***********************************/
   /**全局统一鉴权配置**/
   private Authorization authorization = new Authorization();

   /*********************************************************************************************************/

   /**全局参数配置**/
   @Data
   @NoArgsConstructor
   public class GlobalOperationParameter {
     /**参数名**/
     private String name;
     /**描述信息**/
     private String description;
     /**指定参数类型**/
     private String modelRef;
     /**参数放在哪个地方:header,query,path,body.form**/
     private String parameterType;
     /**参数是否必须传**/
     private String required;
   }

   /**文档配置**/
   @Data
   @NoArgsConstructor
   public class DocketInfo {
     /**标题**/
     private String title = "";
     /**描述**/
     private String description = "";
     /**版本**/
     private String version = "";
     /**许可证**/
     private String license = "";
     /**许可证URL**/
     private String licenseUrl = "";
     /**服务条款URL**/
     private String termsOfServiceUrl = "";
     /**联系人**/
     private Contact contact = new Contact();
     /**swagger会解析的包路径**/
     private String basePackage = "";
     /**swagger会解析的url规则**/
     private List<String> basePath = new ArrayList<>();
     /**在basePath基础上需要排除的url规则**/
     private List<String> excludePath = new ArrayList<>();
     /**全局配置参数**/
     private List<GlobalOperationParameter> globalOperationParameters;
     /**忽略的参数类型**/
     private List<Class<?>> ignoredParameterTypes = new ArrayList<>();
   }

   /**联系人**/
   @Data
   @NoArgsConstructor
   public static class Contact {
     /**联系人**/
     private String name = "";
     /**联系人url**/
     private String url = "";
     /**联系人email**/
     private String email = "";
   }

   /**响应消息配置**/
   @Data
   @NoArgsConstructor
   public static class GlobalResponseMessage {
     /**POST 响应消息体**/
     List<GlobalResponseMessageBody> post = new ArrayList<>();
     /**GET 响应消息体**/
     List<GlobalResponseMessageBody> get = new ArrayList<>();
     /**PUT 响应消息体**/
     List<GlobalResponseMessageBody> put = new ArrayList<>();
     /**PATCH 响应消息体**/
     List<GlobalResponseMessageBody> patch = new ArrayList<>();
     /**DELETE 响应消息体**/
     List<GlobalResponseMessageBody> delete = new ArrayList<>();
     /**HEAD 响应消息体**/
     List<GlobalResponseMessageBody> head = new ArrayList<>();
     /**OPTIONS 响应消息体**/
     List<GlobalResponseMessageBody> options = new ArrayList<>();
     /**TRACE 响应消息体**/
     List<GlobalResponseMessageBody> trace = new ArrayList<>();
   }

   /**响应消息体**/
   @Data
   @NoArgsConstructor
   public static class GlobalResponseMessageBody {
     /**响应码**/
     private int code;
     /**响应消息**/
     private String message;
     /**响应体**/
     private String modelRef;
   }

   /** 页面配置**/
   @Data
   @NoArgsConstructor
   public static class UiConfig {

     private String apiSorter = "alpha";
     /**是否启用json编辑器**/
     private Boolean jsonEditor = false;
     /**是否显示请求头信息**/
     private Boolean showRequestHeaders = true;
     /**支持页面提交的请求类型**/
     private String submitMethods = "get,post,put,delete,patch";
     /**请求超时时间**/
     private Long requestTimeout = 10000L;
     /**深层链接**/
     private Boolean deepLinking;
     /**显示操作Id**/
     private Boolean displayOperationId;
     /**默认所有模型拓展链接**/
     private Integer defaultModelsExpandDepth;
     /**默认模型拓展链接**/
     private Integer defaultModelExpandDepth;
     /**默认模型试图**/
     private ModelRendering defaultModelRendering;
     /**是否显示请求耗时，默认false**/
     private Boolean displayRequestDuration = true;
     /**可选 none | list**/
     private DocExpansion docExpansion;
     /**Boolean=false OR String**/
     private Object filter;
     private Integer maxDisplayedTags;
     private OperationsSorter operationsSorter;
     private Boolean showExtensions;
     private TagsSorter tagsSorter;
     /**Network**/
     private String validatorUrl;
   }

   /**securitySchemes 支持方式之一 ApiKey*/
   @Data
   @NoArgsConstructor
   @AllArgsConstructor
   public static class Authorization {
     /**鉴权策略ID，对应 SecurityReferences ID*/
     private String name = "Authorization";
     /**鉴权传递的Header参数*/
     private String keyName = "X-Auth-Token";
     /**需要开启鉴权URL的正则*/
     private String authRegex = "^.*$";
   }

 }