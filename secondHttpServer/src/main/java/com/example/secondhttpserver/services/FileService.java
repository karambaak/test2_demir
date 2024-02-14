package com.example.secondhttpserver.services;

import com.example.secondhttpserver.entitites.AcquiredInformation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

@RequiredArgsConstructor
@Service
@Slf4j
public class FileService {

    @Value("${file.files-path}")
    private String mainPath;

    private final Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

    public void workWithNamesAndWriteFile(AcquiredInformation acquiredInformation) {
        File directory = new File(mainPath);
        if (!directory.mkdirs()) {
            log.info("directory created");
        }
        var mayBeExistFile = searchFile(acquiredInformation);
        if (mayBeExistFile.isPresent()) {
            File file = new File(mayBeExistFile.get());
            List<String> content = reader(file);
            content.add(acquiredInformation.getJson());
            if (content.size() > 100) {
                String newFileName = makeNewFileNameFromExist(file);
                writeFile(new File(mainPath + newFileName), List.of(acquiredInformation.getJson()));
            } else {
                writeFile(file, content);
            }
        } else {
            createNewFile(acquiredInformation);
        }
    }


    private void createNewFile(AcquiredInformation acquiredInformation) {
        String fileName = generateFirstFileName(acquiredInformation);
        File newFile = new File(fileName);
        List<String> jsons = new ArrayList<>();
        jsons.add(acquiredInformation.getJson());
        writeFile(newFile, jsons);
    }

    public void writeFile(File file, List<String> jsons){
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(gson.toJson(jsons));
            writer.flush();
            log.info("File {} has wrote", file.getName());
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error writing file {}", file.getName());
        }
    }

    private String makeNewFileNameFromExist(File file){
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex == -1) {
            log.error("Error. File name dont have extension");
            return "";
        }
        String nameWithoutExtension = fileName.substring(0, dotIndex);
        String extension = fileName.substring(dotIndex);

        int numberIndex = nameWithoutExtension.lastIndexOf("-");
        if (numberIndex == -1) {
            log.error("Error with new file name");
            return "";
        }
        String numberStr = nameWithoutExtension.substring(numberIndex + 1);
        try {
            int number = Integer.parseInt(numberStr) + 1;
            return nameWithoutExtension.substring(0, numberIndex + 1) + String.format("%04d", number) + extension;
        } catch (NumberFormatException e) {
            log.error("Error with new file name");
            return "";
        }
    }

    private Optional<String> searchFile(AcquiredInformation acquiredInformation) {
        String type = acquiredInformation.getType();
        String date = acquiredInformation.getDate();
        File directory = new File(mainPath);
        if (!directory.exists()) {
            log.info("Directory {} is not exists", mainPath);
            return Optional.empty();
        }
        File[] files = directory.listFiles();
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName).reversed());
            for (File file : files) {
                if (file.getName().contains(type + "-" + date)) {
                    return Optional.of(file.getAbsolutePath());
                }
            }
        }
        return Optional.empty();
    }

    private List<String> reader(File file) {
        List<String> strings;
        Type itemsListType = new TypeToken<List<String>>() {
        }.getType();
        try (FileReader reader = new FileReader(file)) {
            strings = gson.fromJson(reader, itemsListType);
            return strings;
        } catch (FileNotFoundException e) {
            log.error("File not found");
        } catch (IOException e) {
            log.error("Error reading file");
        }
        return new ArrayList<>();
    }

    private String generateFirstFileName(AcquiredInformation acqInf) {
        return String.format("%s%s-%s-0001.log", mainPath, acqInf.getType(), acqInf.getDate());
    }
}
