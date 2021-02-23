package com.flipper.api;

import java.io.File;

import com.flipper.helpers.Log;
import com.flipper.helpers.Persistor;
import com.flipper.responses.FlipResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UploadApi {
    public static FlipResponse uploadFlips() {
        try {
            File flipsJson = new File(Persistor.directory, Persistor.FLIPS_JSON_FILE);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    "flips",
                    RequestBody.create(MediaType.parse("text/json"), flipsJson))
                .addFormDataPart("some-field", "some-value")
                .build();
            Request request = new Request.Builder()
                .url(Api.createUrl("/uploads/flipper"))
                .post(requestBody)
                .build();
            Response response = Api.client.newCall(request).execute();
            String json = response.body().string();
            FlipResponse flipResponse = Api.gson.fromJson(json, FlipResponse.class);
            return flipResponse;
        } catch (Exception error) {
            Log.info("Failed to load flips json");
            return null;
        }
    }
}
