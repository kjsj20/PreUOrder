package com.jskim.preuorder.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping(value = "helloworld/string")
    @ResponseBody
    public String helloworldString() {
        return "helloworld";
    }

    @GetMapping(value = "helloworld/json")
    @ResponseBody
    public Hello HelloworldJson() {
        Hello hello = new Hello();
        hello.message = "helloworld";
        System.out.println("hello = " + hello);
        return hello;
    }

    @GetMapping(value = "helloworld/page")
    @ResponseBody
    public String helloworld() {
        return "helloworld123123";
    }

    @Setter
    @Getter
    public static class Hello {
        private String message;
    }
}
