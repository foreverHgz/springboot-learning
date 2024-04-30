package com.springboot.learning.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author forever_hgz
 */
@Getter
@Setter
@Component
public class User {
    // 使用POST http://127.0.0.1:8080/springboot-learning/actuator/refresh刷新不生效
    @Value("${first.config.person.firstName}")
    private String firstName;

    @Value("${first.config.person.lastName}")
    private String lastName;
}
