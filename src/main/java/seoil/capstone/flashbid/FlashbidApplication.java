package seoil.capstone.flashbid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FlashbidApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlashbidApplication.class, args);
	}

}
