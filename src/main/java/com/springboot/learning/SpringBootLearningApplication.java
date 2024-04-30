package com.springboot.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @author forever_hgz
 */
@EnableCaching
@SpringBootApplication
public class SpringBootLearningApplication {

	public static void main(String[] args) {
		// http://localhost:8080/springboot-learning/swagger-ui/index.html
		// http://127.0.0.1:8080/springboot-learning/swagger-ui/index.html
		// http://192.168.31.163:8080/springboot-learning/swagger-ui/index.html
		// http://192.168.95.1:8080/springboot-learning/swagger-ui/index.html
		// http://192.168.253.1:8080/springboot-learning/swagger-ui/index.html
		SpringApplication.run(SpringBootLearningApplication.class, args);
	}

}
