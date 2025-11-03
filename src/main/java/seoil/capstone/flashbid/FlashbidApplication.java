package seoil.capstone.flashbid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
public class FlashbidApplication {
	public static void main(String[] args) {
		SpringApplication.run(FlashbidApplication.class, args);
	}

}
