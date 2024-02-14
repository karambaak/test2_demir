package com.example.firsthttpserver.utills;

import com.example.firsthttpserver.entities.Information;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
@Slf4j
public class FileUtil {

    @Value("${file.files-path}")
    private String mainPath;

    private final Gson gson = new Gson().newBuilder().setPrettyPrinting().create();

    public void writeToFile(Information information) {
        String fileName = generateFileName(information.getType(), information.getDate());
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(gson.toJson(information));
            writer.flush();
            log.info("File {} has wrote", fileName);
        } catch (IOException e) {
            log.error("Error writing file {}", fileName);
        }
    }

    public Optional<Information> readFile(String name, String date) {
        File directory = new File(mainPath);
        if (!directory.mkdirs()) {
            log.info("directory created");
        }
        File file = new File(generateFileName(name, date));
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                Information information = gson.fromJson(content.toString(), Information.class);
                return Optional.ofNullable(information);
            } catch (JsonSyntaxException | FileNotFoundException e) {
                return Optional.empty();
            } catch (IOException e) {
                log.error("Error reading file");
            }
        }
        return Optional.empty();


    }

    private String generateFileName(String infoType, String date) {
        return mainPath + infoType + "-" + date + ".log";
    }

}
