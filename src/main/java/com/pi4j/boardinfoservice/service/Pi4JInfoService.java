package com.pi4j.boardinfoservice.service;

import com.pi4j.Pi4J;
import com.pi4j.boardinfo.definition.BoardModel;
import com.pi4j.boardinfo.definition.HeaderPins;
import com.pi4j.boardinfo.util.BoardInfoHelper;
import com.pi4j.context.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class Pi4JInfoService {

    private static final Logger logger = LogManager.getLogger(Pi4JInfoService.class);

    private final Context pi4j;

    public Pi4JInfoService() {
        this.pi4j = Pi4J.newAutoContext();

        logger.info("Board model: {}", pi4j.boardInfo().getBoardModel().getLabel());
        logger.info("Operating system: {}", pi4j.boardInfo().getOperatingSystem());
        logger.info("Java versions: {}", pi4j.boardInfo().getJavaInfo());

        logger.info("Board model: {}", BoardInfoHelper.current().getBoardModel().getLabel());
        logger.info("Raspberry Pi model with RP1 chip (Raspberry Pi 5): {}", BoardInfoHelper.usesRP1());
        logger.info("OS is 64-bit: {}", BoardInfoHelper.is64bit());
        logger.info("JVM memory used (MB): {}", BoardInfoHelper.getJvmMemory().getUsedInMb());
        logger.info("Board temperature (°C): {}", BoardInfoHelper.getBoardReading().getTemperatureInCelsius());
    }

    public Context getPi4JContext() {
        return pi4j;
    }

    public List<BoardModel> getRaspberryPiBoards() {
        return Arrays.stream(BoardModel.values()).toList();
    }

    public Optional<BoardModel> getRaspberryPiBoardByName(String name) {
        return Arrays.stream(BoardModel.values())
                .filter(b -> b.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public Optional<HeaderPins> getRaspberryPiHeaderByName(String name) {
        return Arrays.stream(HeaderPins.values())
                .filter(b -> b.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
