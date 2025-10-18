package seoil.capstone.flashbid.global.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import seoil.capstone.flashbid.infrastructure.file.FileUploader;
import seoil.capstone.flashbid.infrastructure.file.LocalFileUploader;
import seoil.capstone.flashbid.infrastructure.file.S3FileUploader;

@Configuration
public class AppConfig {
    @Value("${MODE}")
    private String mode;

    @Bean
    public FileUploader fileUploader(){
        if(mode.equals("dev")){
            return new LocalFileUploader();
        }else{
            return new S3FileUploader();
        }
    }
}
