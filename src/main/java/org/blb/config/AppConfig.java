package org.blb.config;


import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@org.springframework.context.annotation.Configuration
public class AppConfig {


    @Bean
    public Configuration freemakerConfiguration(){
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(new ClassTemplateLoader(AppConfig.class, "/mails/"));

        return configuration;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
