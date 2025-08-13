package com.furkan.project.common.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/__probe")
public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);

    @GetMapping("/log")
    public Map<String, Object> log(
            @RequestParam(defaultValue = "INFO") String level,
            @RequestParam(defaultValue = "hello from probe") String msg) {

        switch (level.toUpperCase()) {
            case "ERROR": log.error(msg); break;
            case "WARN":  log.warn(msg);  break;
            case "DEBUG": log.debug(msg); break;
            case "TRACE": log.trace(msg); break;
            default:      log.info(msg);
        }

        Map<String,Object> res = new HashMap<>();
        res.put("ok", true);
        res.put("level", level.toUpperCase());
        res.put("message", msg);
        return res;
    }
}