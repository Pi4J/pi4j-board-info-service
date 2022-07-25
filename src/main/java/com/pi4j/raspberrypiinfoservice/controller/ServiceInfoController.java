package com.pi4j.raspberrypiinfoservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/service")
public class ServiceInfoController {

    @GetMapping("/java")
    public Map<String, Object> getJavaVersion() {
        Map<String, Object> map = new HashMap<>();
        map.put("version", System.getProperty("java.version"));
        map.put("runtime", System.getProperty("java.runtime.version"));
        return map;
    }

    @GetMapping("/os")
    public Map<String, Object> getOsVersion() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", System.getProperty("os.name"));
        map.put("version", System.getProperty("os.version"));
        map.put("architecture", System.getProperty("os.arch"));
        return map;
    }
}
