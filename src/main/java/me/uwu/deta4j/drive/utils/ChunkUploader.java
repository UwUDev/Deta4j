package me.uwu.deta4j.drive.utils;

import com.google.gson.JsonObject;
import me.uwu.deta4j.drive.DetaDrive;
import me.uwu.deta4j.drive.struct.FileResponse;
import okhttp3.*;

import java.io.IOException;
import java.util.Optional;

import static me.uwu.deta4j.Constants.GSON;
import static me.uwu.deta4j.Constants.NULL_BODY;

@SuppressWarnings("ConstantConditions")
public class ChunkUploader {
    private final byte[][] chunks;
    private final OkHttpClient client;
    private final String filename;
    private String uploadId;
    private final DetaDrive drive;

    public ChunkUploader(byte[] bytes, OkHttpClient client, String filename, DetaDrive drive) {
        this.chunks = DriveUtils.chunkBytes(bytes);
        this.client = client;
        this.filename = filename;
        this.drive = drive;
    }

    public Optional<FileResponse> upload() {
        uploadId = requestUploadId();
        if (uploadId == null)
            return Optional.empty();

        for (int i = 0; i < chunks.length; i++) {
            if (!uploadChunk(i)) {
                cancelUpload();
                return Optional.empty();
            }
        }

        return endUpload();
    }

    private String requestUploadId() {
        Request request = new Request.Builder()
                .url("https://drive.deta.sh/v1/a04cwru2/test/uploads?name=" + filename)
                .post(NULL_BODY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String id = GSON.fromJson(response.body().string(), JsonObject.class).get("upload_id").getAsString();
            response.close();
            return id;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean uploadChunk(int chunkNumber) {
        Request request = new Request.Builder()
                .url("https://drive.deta.sh/v1/a04cwru2/test/uploads/" + uploadId + "/parts?name=" + filename + "&part=" + (chunkNumber + 1))
                .post(RequestBody.create(chunks[chunkNumber], MediaType.get("application/octet-stream")))
                .build();

        try {
            Response response = client.newCall(request).execute();
            boolean success = response.isSuccessful();
            response.close();
            return success;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Optional<FileResponse> endUpload() {
        Request request = new Request.Builder()
                .url("https://drive.deta.sh/v1/a04cwru2/test/uploads/" + uploadId + "?name=" + filename)
                .patch(NULL_BODY)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String fileNameResponse = GSON.fromJson(response.body().string(), JsonObject.class).get("name").getAsString();
                response.close();
                return Optional.of(new FileResponse(fileNameResponse, "a04cwru2", null, drive));
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private void cancelUpload() {
        Request request = new Request.Builder()
                .url("https://drive.deta.sh/v1/a04cwru2/test/uploads/" + uploadId + "?name=" + filename)
                .delete()
                .build();

        try {
            client.newCall(request).execute().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
