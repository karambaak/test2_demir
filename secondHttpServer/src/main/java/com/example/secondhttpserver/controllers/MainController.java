package com.example.secondhttpserver.controllers;

import com.example.secondhttpserver.entitites.AcquiredInformation;
import com.example.secondhttpserver.services.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class MainController {

    private final MainService mainService;

    @PostMapping
    public void getJsonFromFirstHttpServer(@RequestBody AcquiredInformation acquiredInformation) {
        mainService.writeFile(acquiredInformation);
    }
}
