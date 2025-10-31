package seoil.capstone.flashbid;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.TimeZone;

@EnableJpaAuditing
@SpringBootApplication
@EnableAsync
public class FlashbidApplication {
	@PostConstruct
	public void setKST() {
		// JVM의 기본 타임존을 'Asia/Seoul'로 강제 설정
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
	public static void main(String[] args) {
		SpringApplication.run(FlashbidApplication.class, args);
	}

}
