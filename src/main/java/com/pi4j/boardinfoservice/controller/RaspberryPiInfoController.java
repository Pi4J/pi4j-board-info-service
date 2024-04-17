package com.pi4j.boardinfoservice.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pi4j.boardinfo.definition.*;
import com.pi4j.boardinfo.model.HeaderPin;
import com.pi4j.boardinfoservice.service.Pi4JInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/raspberrypi")
public class RaspberryPiInfoController {

    private static final Logger logger = LoggerFactory.getLogger(RaspberryPiInfoController.class);

    private final Pi4JInfoService pi4JInfoService;
    private final ObjectMapper objectMapper;

    public RaspberryPiInfoController(Pi4JInfoService pi4JInfoService) {
        this.pi4JInfoService = pi4JInfoService;
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(BoardModel.class, new BoardModelSerializer());
        module.addSerializer(HeaderVersion.class, new HeaderVersionSerializer());
        module.addSerializer(PiModel.class, new PiModelSerializer());
        module.addSerializer(HeaderPins.class, new HeaderPinsSerializer());
        module.addSerializer(HeaderPin.class, new HeaderPinSerializer());
        module.addSerializer(Soc.class, new SocSerializer());
        module.addSerializer(Cpu.class, new CpuSerializer());
        module.addSerializer(PinFunction.class, new PinFunctionSerializer());
        module.addSerializer(InstructionSet.class, new InstructionSetSerializer());
        objectMapper.registerModule(module);
    }

    @GetMapping(path = "/board", produces = "application/json")
    public ResponseEntity<String> getBoards() {
        try {
            List<Map<String, String>> boards = new ArrayList<>();

            for (BoardModel board : pi4JInfoService.getRaspberryPiBoards().stream().filter(b -> b != BoardModel.UNKNOWN).toList()) {
                Map<String, String> boardJson = new HashMap<>();
                boardJson.put("name", board.name());
                boardJson.put("label", board.getLabel());
                boardJson.put("boardTypeName", board.getBoardType().name());
                boards.add(boardJson);
            }

            return ResponseEntity.ok().body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(boards));
        } catch (JsonProcessingException e) {
            logger.error("Error while parsing board model to JSON: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping(path = "/board/{name}", produces = "application/json")
    public ResponseEntity<?> getBoardByName(@PathVariable String name) {
        var board = pi4JInfoService.getRaspberryPiBoardByName(name);
        return board.map(boardModel -> {
            try {
                return ResponseEntity.ok().body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(boardModel));
            } catch (JsonProcessingException e) {
                logger.error("Error while parsing board model to JSON: {}", e.getMessage());
                return ResponseEntity.internalServerError().build();
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    static class BoardModelSerializer extends JsonSerializer<BoardModel> {
        @Override
        public void serialize(BoardModel value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.getName());
            gen.writeStringField("label", value.getLabel());
            gen.writeStringField("boardType", value.getBoardType().toString());
            gen.writeArrayFieldStart("boardCodes");
            for (String code : value.getBoardCodes()) {
                gen.writeString(code);
            }
            gen.writeEndArray();

            gen.writeObjectField("model", value.getModel());
            gen.writeObjectField("headerVersion", value.getHeaderVersion());
            gen.writeStringField("releaseDate", value.getReleaseDate().toString());
            gen.writeObjectField("soc", value.getSoc());
            gen.writeObjectField("cpu", value.getCpu());
            gen.writeNumberField("numberOfCpu", value.getNumberOfCpu());
            gen.writeArrayFieldStart("versionsProcessorSpeedInMhz");
            for (Integer speed : value.getVersionsProcessorSpeedInMhz()) {
                gen.writeNumber(speed);
            }
            gen.writeEndArray();
            gen.writeArrayFieldStart("versionsMemoryInKb");
            for (Integer memory : value.getVersionsMemoryInKb()) {
                gen.writeNumber(memory);
            }
            gen.writeEndArray();
            gen.writeArrayFieldStart("remarks");
            for (String remark : value.getRemarks()) {
                gen.writeString(remark);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    static class PiModelSerializer extends JsonSerializer<PiModel> {
        @Override
        public void serialize(PiModel value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.name());
            gen.writeStringField("label", value.getLabel());
            gen.writeStringField("description", value.getDescription());
            gen.writeEndObject();
        }
    }

    static class HeaderVersionSerializer extends JsonSerializer<HeaderVersion> {
        @Override
        public void serialize(HeaderVersion value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.name());
            gen.writeStringField("label", value.getLabel());
            gen.writeStringField("description", value.getDescription());
            gen.writeArrayFieldStart("headers");
            for (HeaderPins header : value.getHeaderPins()) {
                gen.writeObject(header);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    static class HeaderPinSerializer extends JsonSerializer<HeaderPin> {
        @Override
        public void serialize(HeaderPin value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeNumberField("pinNumber", value.getPinNumber());
            gen.writeStringField("pinType", value.getPinType().toString());
            gen.writeObjectField("pinFunction", value.getPinFunction());
            gen.writeStringField("name", value.getName());
            gen.writeStringField("bcmNumber", value.getBcmNumber() == null ? "" : value.getBcmNumber().toString());
            gen.writeStringField("wiringPinNumber", value.getWiringPiNumber() == null ? "" : value.getWiringPiNumber().toString());
            gen.writeStringField("remark", value.getRemark());
            gen.writeEndObject();
        }
    }

    static class HeaderPinsSerializer extends JsonSerializer<HeaderPins> {
        @Override
        public void serialize(HeaderPins value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.name());
            gen.writeStringField("label", value.getLabel());
            gen.writeArrayFieldStart("pins");
            for (HeaderPin pin : value.getPins()) {
                gen.writeObject(pin);
            }
            gen.writeEndArray();
            gen.writeEndObject();
        }
    }

    static class SocSerializer extends JsonSerializer<Soc> {
        @Override
        public void serialize(Soc value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.name());
            gen.writeObjectField("instructionSet", value.getInstructionSet());
            gen.writeEndObject();
        }
    }

    static class CpuSerializer extends JsonSerializer<Cpu> {
        @Override
        public void serialize(Cpu value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.name());
            gen.writeStringField("label", value.getLabel());
            gen.writeEndObject();
        }
    }

    static class PinFunctionSerializer extends JsonSerializer<PinFunction> {
        @Override
        public void serialize(PinFunction value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.name());
            gen.writeStringField("label", value.getLabel());
            gen.writeStringField("description", value.getDescription());
            gen.writeEndObject();
        }
    }

    static class InstructionSetSerializer extends JsonSerializer<InstructionSet> {
        @Override
        public void serialize(InstructionSet value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("name", value.name());
            gen.writeStringField("label", value.getLabel());
            gen.writeEndObject();
        }
    }
}
