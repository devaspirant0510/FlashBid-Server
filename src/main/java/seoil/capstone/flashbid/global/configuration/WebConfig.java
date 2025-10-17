package seoil.capstone.flashbid.global.configuration;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.*;
import seoil.capstone.flashbid.global.core.interceptor.LoggingInterceptor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final LoggingInterceptor loggingInterceptor;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173","http://127.0.0.1:5173","http://localhost:5175","http://127.0.0.1:5175","http://172.27.226.250:5173","http://localhost:63342","http://172.27.183.188:5173","https://imaginative-sfogliatella-d7e149.netlify.app/","https://unknown-auction.shop")
                .allowedMethods("GET","POST","PUT","DELETE","PATCH")
                .allowedHeaders("*")
                .allowCredentials(true  )
                .exposedHeaders("Authorization","jwt-token")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadPath = System.getProperty("user.home") + "/seungho/uploads/";
        String uploadPath2 = System.getProperty("user.home")+
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loggingInterceptor);
    }
}
