package com.springboot.learning.controller;

import com.springboot.learning.bean.Person;
import com.springboot.learning.bean.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author forever_hgz
 */
@RestController
public class ConfigController {
//    @Value("${second.config.person.country}")
//    private String country;
//
//    @Value("${second.config.person.age}")
//    private int age;

    @Resource
    private User user;

    @Resource
    private Person person;

    @GetMapping("/getName1")
    public String getName1() {
        return "My name is:" + user.getFirstName() + user.getLastName();
    }

    @GetMapping("/getName2")
    public String getName2() {
        return "My name is:" + person.getFirstName() + person.getLastName();
    }

//    @GetMapping("/getName3")
//    public String getName3() {
//        return "My name is:" + country + age;
//    }
}
