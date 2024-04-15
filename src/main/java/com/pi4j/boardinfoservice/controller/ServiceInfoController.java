package com.pi4j.boardinfoservice.controller;

import com.pi4j.boardinfo.model.BoardInfo;
import com.pi4j.boardinfo.model.BoardReading;
import com.pi4j.boardinfo.model.JvmMemory;
import com.pi4j.boardinfoservice.service.SystemInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service")
public class ServiceInfoController {

    private final SystemInfoService systemInfoService;

    public ServiceInfoController(SystemInfoService systemInfoService) {
        this.systemInfoService = systemInfoService;
    }

    @GetMapping("/board")
    public BoardInfo getDetectedBoard() {
        return systemInfoService.getDetectedBoard();
    }

    @GetMapping("/memory")
    public JvmMemory getMemory() {
        return systemInfoService.getJvmMemory();
    }

    @GetMapping("/actual")
    public BoardReading getBoardReadings() {
        return systemInfoService.getBoardReading();
    }
}
