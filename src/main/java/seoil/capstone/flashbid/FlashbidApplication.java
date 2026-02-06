package seoil.capstone.flashbid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableJpaAuditing
@EnableFeignClients
@SpringBootApplication
@EnableAsync
@EnableRetry
public class FlashbidApplication {
	public static void main(String[] args) {
		SpringApplication.run(FlashbidApplication.class, args);
	}

}
