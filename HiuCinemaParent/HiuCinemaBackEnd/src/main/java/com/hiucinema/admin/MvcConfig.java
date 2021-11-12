package com.hiucinema.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
    //Khai báo đường dẫn tĩnh , vì trong spring boot mặc định có /template , /static ,... đây mình
    //khai báo thư mục thoe ý mình , để chỉ ra chỗ chứa ảnh của user
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String dirName = "user-photo";
       Path userPhotoDir = Paths.get(dirName);

       String userPhotoPath = userPhotoDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/"+dirName+"/**")
                .addResourceLocations("file:/" + userPhotoPath + "/");
    }
}
