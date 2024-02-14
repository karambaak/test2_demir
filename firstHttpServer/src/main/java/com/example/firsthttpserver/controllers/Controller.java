package com.example.firsthttpserver.controllers;

import com.example.firsthttpserver.services.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class Controller {

    private final MainService mainService;
    @PostMapping
    public HttpStatus acceptData(@RequestBody String xml){
        mainService.doManipulations(xml);
        return HttpStatus.OK;
    }
}
