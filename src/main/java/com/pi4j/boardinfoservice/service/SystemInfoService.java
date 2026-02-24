package com.pi4j.boardinfoservice.service;

import com.pi4j.boardinfo.model.BoardInfo;
import com.pi4j.boardinfo.model.BoardReading;
import com.pi4j.boardinfo.model.JvmMemory;
import com.pi4j.boardinfo.util.BoardInfoHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemInfoService {
    private static final Logger logger = LogManager.getLogger(SystemInfoService.class);

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

    public Optional<BoardReading> getBoardReading() {
        try {
            return Optional.of(BoardInfoHelper.getBoardReading());
        } catch (Exception e) {
            logger.error("Error while getting board reading: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
