package com.pi4j.boardinfoservice.service;

import com.pi4j.boardinfo.model.BoardInfo;
import com.pi4j.boardinfo.model.BoardReading;
import com.pi4j.boardinfo.model.JvmMemory;
import com.pi4j.boardinfo.util.BoardInfoHelper;
import org.springframework.stereotype.Service;

@Service
public class SystemInfoService {

    private final BoardInfo boardInfo;

    public SystemInfoService() {
        boardInfo = BoardInfoHelper.current();
    }

    public BoardInfo getDetectedBoard() {
        return boardInfo;
    }

    public JvmMemory getJvmMemory() {
        return BoardInfoHelper.getJvmMemory();
    }

    public BoardReading getBoardReading() {
        return BoardInfoHelper.getBoardReading();
    }
}
