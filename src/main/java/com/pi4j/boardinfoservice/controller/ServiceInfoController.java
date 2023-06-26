package com.pi4j.boardinfoservice.controller;

import com.pi4j.boardinfo.model.DetectedBoard;
import com.pi4j.boardinfoservice.service.SystemInfoService;
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

    @GetMapping("/board")
    public DetectedBoard getDetectedBoard() {
        return systemInfoService.getDetectedBoard();
    }

    @GetMapping("/memory")
    public Map<String, Object> getMemory() {
        return systemInfoService.getJvmMemory();
    }

    @GetMapping("/actual")
    public Map<String, Object> getBoardReadings() {
        return systemInfoService.getBoardReadings();
    }
}
