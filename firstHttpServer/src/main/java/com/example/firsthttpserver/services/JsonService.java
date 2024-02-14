package com.example.firsthttpserver.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JsonService {

    private final XmlMapper xmlMapper;

    public JsonService() {
        this.xmlMapper = new XmlMapper();
    }

    @SneakyThrows
    public JsonNode parseToJson(String xml) {
        return xmlMapper.readTree(xml.getBytes());
    }

    public String getType(JsonNode jsonNode) {
        return jsonNode.path("Type").asText();
    }

    public String getDate(JsonNode jsonNode) {
        return jsonNode.path("Creation").path("Date").toString().split("T")[0].replace("\"", "");
    }
}
