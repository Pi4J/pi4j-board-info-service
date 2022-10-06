package com.pi4j.raspberrypiinfoservice.service;

import com.pi4j.raspberrypiinfoservice.util.ExecUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemInfoService {

    private final Map<String, Object> javaVersion;
    private final Map<String, Object> osVersion;
    Logger logger = LogManager.getLogger(SystemInfoService.class);

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

    public Map<String, Object> getJvmMemory() {
        Map<String, Object> memory = new HashMap<>();

        int mb = 1024 * 1024;
        Runtime instance = Runtime.getRuntime();
        memory.put("total_mb", (instance.totalMemory() / mb));
        memory.put("free_mb", (instance.freeMemory() / mb));
        memory.put("used_mb", ((instance.totalMemory() - instance.freeMemory()) / mb));
        memory.put("max_mb", (instance.maxMemory() / mb));

        return memory;
    }

    public Map<String, Object> getBoardVersion() {
        Map<String, Object> boardVersion = new HashMap<>();
        boardVersion.put("board", getCommandOutput("cat /proc/device-tree/model"));
        // https://raspberry-projects.com/pi/command-line/detect-rpi-hardware-version
        boardVersion.put("boardVersionCode", getCommandOutput("cat /proc/cpuinfo | grep 'Revision' | awk '{print $3}'"));
        // https://linuxhint.com/commands-for-hardware-information-raspberry-pi/
        boardVersion.put("temp", getCommandOutput("vcgencmd measure_temp"));
        boardVersion.put("uptimeInfo", getCommandOutput("uptime"));
        // https://linuxhint.com/find-hardware-information-raspberry-pi/
        boardVersion.put("volt", getCommandOutput("vcgencmd measure_volts"));
        // https://www.baeldung.com/linux/total-physical-memory
        boardVersion.put("memory", getCommandOutput("cat /proc/meminfo | head -n 1"));
        return boardVersion;
    }

    private String getCommandOutput(String command) {
        ExecUtil execUtil = new ExecUtil(command);
        if (!execUtil.isFinished() || !execUtil.getErrorMessage().isEmpty()) {
            logger.error("Could not execute '{}': {}", command, execUtil.getErrorMessage());
            return "";
        }
        return execUtil.getOutputMessage();
    }
}
