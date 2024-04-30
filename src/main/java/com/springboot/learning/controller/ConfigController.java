package com.springboot.learning.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author forever_hgz
 */
@RestController
public class ConfigController {
    @Value("${firstName}")
    private String firstName;

    @Value("${lastName}")
    private String lastName;

    @GetMapping("/getName")
    public String getName() {
        return "My name is:" + firstName + lastName;
    }
}
