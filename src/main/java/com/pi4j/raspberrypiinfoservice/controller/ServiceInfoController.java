package com.pi4j.raspberrypiinfoservice.controller;

import com.pi4j.raspberrypiinfoservice.service.SystemInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/service")
public class ServiceInfoController {

    private final SystemInfoService systemInfoService;

    public ServiceInfoController(SystemInfoService systemInfoService) {
        this.systemInfoService = systemInfoService;
    }

    @GetMapping("/java")
    public Map<String, Object> getJavaVersion() {
        return systemInfoService.getJavaVersion();
    }

    @GetMapping("/os")
    public Map<String, Object> getOsVersion() {
        return systemInfoService.getOsVersion();
    }

    @GetMapping("/memory")
    public Map<String, Object> getMemory() {
        return systemInfoService.getMemory();
    }
}
