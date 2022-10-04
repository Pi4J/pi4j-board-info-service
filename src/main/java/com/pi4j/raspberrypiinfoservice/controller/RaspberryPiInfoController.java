package com.pi4j.raspberrypiinfoservice.controller;

import com.pi4j.raspberrypiinfo.definition.BoardModel;
import com.pi4j.raspberrypiinfoservice.service.RaspberryPiInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/raspberrypi")
public class RaspberryPiInfoController {

    private final RaspberryPiInfoService raspberryPiInfoService;

    public RaspberryPiInfoController(RaspberryPiInfoService raspberryPiInfoService) {
        this.raspberryPiInfoService = raspberryPiInfoService;
    }

    @GetMapping("/board")
    public List<BoardModel> getBoards() {
        return raspberryPiInfoService.getRaspberryPiBoards();
    }

    @GetMapping("/board/{name}")
    public ResponseEntity<BoardModel> getBoardByName(@PathVariable String name) {
        var board = raspberryPiInfoService.getRaspberryPiBoardByName(name);
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
