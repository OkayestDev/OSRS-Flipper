package com.flipper.api;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;


import com.flipper.helpers.Log;
import com.flipper.helpers.Persistor;
import com.flipper.responses.FlipResponse;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Callback;

public class UploadApi {
    public static void uploadFlips(Consumer<FlipResponse> flipUploadCallback) {
        try {
            File flipsJson = new File(Persistor.directory, Persistor.FLIPS_JSON_FILE);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                    "json",
                    "json",
                    RequestBody.create(MediaType.parse("text/plain"), flipsJson))
                .build();
            Request request = new Request.Builder()
                .url(Api.createUrl("/uploads/flipper"))
                .header("Authorization", Api.jwt)
                .post(requestBody)
                .build();
            
            Call call = Api.client.newCall(request);
            call.enqueue(new Callback() {
                public void onResponse(Call call, Response response) throws IOException {
                    flipUploadCallback.accept(
                        Api.gson.fromJson(response.body().string(), FlipResponse.class)
                    );
                }
    
                public void onFailure(Call call, IOException error) {
                    Log.info("Flipper Server Error");
                }
            });
            
        } catch (Exception error) {
            Log.info("Failed to load flips json");
        }
    }
}
