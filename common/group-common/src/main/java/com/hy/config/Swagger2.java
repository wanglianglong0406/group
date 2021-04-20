package com.hy.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: $- Swagger2 -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/11/17 0:23
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/11/17 0:23
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@Configuration
@EnableSwagger2
public class Swagger2 {

    //配置Swagger2核心配置 docket
    //http:127.0.0.1:8088/swagger-ui.html   原路径
    //http://localhost:8088/doc.html 原路径
    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)  //指定api 类型为 Swagger2
                .apiInfo(apiInfo())                              //用于定义api 文档信息总汇
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class)) //RestController 注解
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("在线交易娱乐平台接口API") // 文档的标题
                .contact(new Contact("wanglianglong", "https://www.xxx.com", "wanglianglong@outlook.com"))   //联系人信息
                .description("专门在线交易娱乐平台提供的API文档") //详细信息
                .version("1.0") //文档版本号
                .termsOfServiceUrl("https://www.xxx.com")//网站地址
                .build();
    }
}
