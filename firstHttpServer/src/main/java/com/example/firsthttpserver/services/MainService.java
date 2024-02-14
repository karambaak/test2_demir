package com.example.firsthttpserver.services;

import com.example.firsthttpserver.entities.Information;
import com.example.firsthttpserver.entities.InformationToSend;
import com.example.firsthttpserver.utills.FileUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okHttpUtil.OkHttpUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MainService {

    private final JsonService jsonService;

    private final FileUtil fileUtil;

    private final Gson gson = new Gson();

    public void doManipulations(String xml) {
        JsonNode jsonNode = jsonService.parseToJson(xml);
        String type = jsonService.getType(jsonNode);
        String date = jsonService.getDate(jsonNode);
        Optional<Information> maybeFile = fileUtil.readFile(type, date);
        if (maybeFile.isPresent()) {
            Information infFromFile = maybeFile.get();
            infFromFile.setCounter(infFromFile.getCounter() + 1);
            List<String> jsonsFromExistFile = new ArrayList<>(infFromFile.getJson());
            jsonsFromExistFile.add(jsonNode.toString());
            infFromFile.setJson(jsonsFromExistFile);
            infFromFile.setDate(date);
            infFromFile.setType(type);
            fileUtil.writeToFile(infFromFile);
            OkHttpUtil.sendJson(makeInformation(jsonNode, type, date));
        } else {
            List<String> jsons = new ArrayList<>();
            jsons.add(jsonNode.toString());
            fileUtil.writeToFile(Information.builder()
                    .counter(1L)
                    .json(jsons)
                    .type(type)
                    .date(date)
                    .build());
            OkHttpUtil.sendJson(makeInformation(jsonNode, type, date));
        }

    }

    private String makeInformation(JsonNode jsonNode, String type, String date){
        return gson.toJson(InformationToSend.builder()
                .type(type)
                .date(date)
                .json(jsonNode.toString())
                .build());
    }

}
