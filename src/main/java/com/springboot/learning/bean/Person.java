package com.springboot.learning.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author forever_hgz
 */
@Getter
@Setter
@Component
// 使用POST http://127.0.0.1:8080/springboot-learning/actuator/refresh刷新可以立即生效
@ConfigurationProperties(prefix = "first.config.person")
public class Person {
    private String firstName;

    private String lastName;
}
