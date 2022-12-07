package me.uwu.deta4j.drive;

import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public @Data class DetaDrives {
    private final OkHttpClient client;
    private final String apiKey, projectId;

    public DetaDrives(String apiKey) {
        this.apiKey = apiKey;
        this.projectId = apiKey.split("_")[0];
        client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("X-API-Key", apiKey)
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    public DetaDrives(String apiKey, OkHttpClient.Builder customClientBuilder) {
        this.apiKey = apiKey;
        this.projectId = apiKey.split("_")[0];
        client = customClientBuilder.addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("X-API-Key", apiKey)
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }

    public DetaDrive get(String name) {
        return new DetaDrive(apiKey, name, client);
    }
}
