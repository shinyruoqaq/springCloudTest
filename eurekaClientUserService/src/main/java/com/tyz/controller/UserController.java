package com.tyz.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shinyruo on 2020/7/2 14:47.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
}
