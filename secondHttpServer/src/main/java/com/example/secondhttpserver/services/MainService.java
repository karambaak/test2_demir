package com.example.secondhttpserver.services;

import com.example.secondhttpserver.entitites.AcquiredInformation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainService {

    private final FileService fileService;

    public void writeFile(AcquiredInformation acquiredInformation) {
        fileService.workWithNamesAndWriteFile(acquiredInformation);
    }

}
