package com.pi4j.raspberrypiinfoservice.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemInfoService {

    private final Map<String, Object> javaVersion;
    private final Map<String, Object> osVersion;

    public SystemInfoService() {
        javaVersion = new HashMap<>();
        javaVersion.put("version", System.getProperty("java.version"));
        javaVersion.put("runtime", System.getProperty("java.runtime.version"));

        osVersion = new HashMap<>();
        osVersion.put("name", System.getProperty("os.name"));
        osVersion.put("version", System.getProperty("os.version"));
        osVersion.put("architecture", System.getProperty("os.arch"));
    }

    public Map<String, Object> getJavaVersion() {
        return javaVersion;
    }

    public Map<String, Object> getOsVersion() {
        return osVersion;
    }
}
