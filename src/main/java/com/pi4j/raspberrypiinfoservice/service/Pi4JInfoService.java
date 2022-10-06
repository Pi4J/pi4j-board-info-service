package com.pi4j.raspberrypiinfoservice.service;

import com.pi4j.raspberrypiinfo.definition.BoardModel;
import com.pi4j.raspberrypiinfo.definition.HeaderPins;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class Pi4JInfoService {

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
