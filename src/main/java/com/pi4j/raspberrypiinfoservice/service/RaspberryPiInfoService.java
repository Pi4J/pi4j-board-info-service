package com.pi4j.raspberrypiinfoservice.service;

import com.pi4j.raspberrypiinfo.definition.BoardModel;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RaspberryPiInfoService {

    public List<BoardModel> getRaspberryPiBoards() {
        return Arrays.stream(BoardModel.values()).toList();
    }

    public Optional<BoardModel> getRaspberryPiBoardByName(String name) {
        return Arrays.stream(BoardModel.values())
                .filter(b -> b.name().equalsIgnoreCase(name))
                .findFirst();
    }
}
