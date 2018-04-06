package com.tsystems.train.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan("com.tsystems.train.controllers")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //register view controllers that create a direct mapping between the URL and the view name
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/routes").setViewName("manager/routes");
        registry.addViewController("/stations").setViewName("manager/station");
        registry.addViewController("/trains").setViewName("manager/trains");
        registry.addViewController("/timetable").setViewName("timetable");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/tickets").setViewName("tickets");
        registry.addViewController("/passengers").setViewName("manager/passengers");
        registry.addViewController("/personal").setViewName("personal");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        // specify where our web pages are located
        resolver.setPrefix("/WEB-INF/views/");
        // format View which  will use
        resolver.setSuffix(".jsp");
        registry.viewResolver(resolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Connecting static resources
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/css/");
        registry.addResourceHandler("/js/**")
                .addResourceLocations("/js/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("/images/");
        registry.addResourceHandler("/img/**")
                .addResourceLocations("/img/");
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
                ObjectMapper objectMapper = jsonMessageConverter.getObjectMapper();
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                break;
            }
        }
    }

}
