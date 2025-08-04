package com.example.July4.lombok;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/hello")
public class UserController {


    @GetMapping
    public String hello(){
        return "hello";
    }

    @GetMapping
    @RequestMapping("/hello2")
    public String hello2(@RequestParam String name, Model model){
        model.addAttribute("name", name);
        return "hello";
    }
    @GetMapping
    @RequestMapping("/hello3/{name}")
    public String hello3(@PathVariable String name, Model model){
        model.addAttribute("name", name);
        return "hello";
    }

}
