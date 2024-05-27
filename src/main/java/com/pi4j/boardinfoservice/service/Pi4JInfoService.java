package com.pi4j.boardinfoservice.service;

import com.pi4j.Pi4J;
import com.pi4j.boardinfo.definition.BoardModel;
import com.pi4j.boardinfo.definition.HeaderPins;
import com.pi4j.context.Context;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class Pi4JInfoService {

    private final Context pi4j;

    public Pi4JInfoService() {
        this.pi4j = Pi4J.newAutoContext();
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
