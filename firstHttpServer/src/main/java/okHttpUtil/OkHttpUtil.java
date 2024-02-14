package okHttpUtil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OkHttpUtil {
    private static final MediaType JSON = MediaType.get("application/json");
    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static void sendJson(String json) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url("http://127.0.0.1:9092/")
                .post(body)
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()){
                log.info("Json successfully sent.");
            }
        } catch (IOException e) {
            log.error("Error sending json");
        }
    }
}
