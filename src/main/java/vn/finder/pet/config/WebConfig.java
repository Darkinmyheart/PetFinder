package vn.finder.pet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/imgAnimal/**")
                .addResourceLocations("file:./imgAnimal/");
        registry.addResourceHandler("/imgProfile/**")
                .addResourceLocations("file:./imgProfile/");
        registry.addResourceHandler("/imgShelter/**")
                .addResourceLocations("file:./imgShelter/");

    }


}