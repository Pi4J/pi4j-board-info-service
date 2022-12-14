package com.pi4j.boardinfoservice.controller;

import com.pi4j.boardinfoservice.service.Pi4JInfoService;
import com.pi4j.raspberrypiinfo.definition.BoardModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/raspberrypi")
public class RaspberryPiInfoController {

    private final Pi4JInfoService pi4JInfoService;

    public RaspberryPiInfoController(Pi4JInfoService pi4JInfoService) {
        this.pi4JInfoService = pi4JInfoService;
    }

    @GetMapping("/board")
    public List<BoardModel> getBoards() {
        return pi4JInfoService.getRaspberryPiBoards();
    }

    @GetMapping("/board/{name}")
    public ResponseEntity<BoardModel> getBoardByName(@PathVariable String name) {
        var board = pi4JInfoService.getRaspberryPiBoardByName(name);
        if (board.isPresent()) {
            return ResponseEntity.ok().body(board.get());
        }
        return ResponseEntity.notFound().build();
    }

    /*
    // Not very useful, this returns a set of Vaadin HTML components that don't render as a table
    @GetMapping("/header/{name}/html")
    public ResponseEntity<String> getHeaderView(@PathVariable String name) {
        var header = raspberryPiInfoService.getRaspberryPiHeaderByName(name);
        if (header.isPresent()) {
            var view = new HeaderPinView(header.get());
            return ResponseEntity.ok().body(view.getElement().getOuterHTML());
        }
        return ResponseEntity.notFound().build();
    }
    */
}
