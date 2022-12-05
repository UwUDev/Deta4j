package me.uwu.deta4j.base;

import lombok.Data;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public @Data class DetaBases {
    private final OkHttpClient client;
    private final String apiKey, projectId;

    public DetaBases(String apiKey) {
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

    public DetaBases(String apiKey, OkHttpClient.Builder customClientBuilder) {
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

    public DetaBase get(String name) {
        return new DetaBase(apiKey, name, client);
    }
}
