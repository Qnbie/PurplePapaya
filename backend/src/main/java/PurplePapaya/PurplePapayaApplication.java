package PurplePapaya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PurplePapayaApplication {

	public static void main(String[] args) {
		SpringApplication.run(PurplePapayaApplication.class, args);
	}

}
