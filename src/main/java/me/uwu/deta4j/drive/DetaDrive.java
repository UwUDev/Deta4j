package me.uwu.deta4j.drive;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.SneakyThrows;
import me.uwu.deta4j.base.struct.Paging;
import me.uwu.deta4j.drive.struct.FileResponse;
import me.uwu.deta4j.drive.struct.PackedFilesResponse;
import me.uwu.deta4j.drive.utils.ChunkUploader;
import okhttp3.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.uwu.deta4j.Constants.*;

@SuppressWarnings("ConstantConditions")
public class DetaDrive {
    @Getter
    private final String baseUrl, name;
    private final OkHttpClient client;
    public DetaDrive(String apiKey, String name, OkHttpClient client) {
        this.client = client;
        this.name = name;
        this.baseUrl = "https://drive.deta.sh/v1/" + apiKey.split("_")[0] + "/" + name + "/";
    }

    @SneakyThrows // la flemme ™️
    public Optional<FileResponse> upload(File file, String filePath) {
        byte [] data = Files.readAllBytes(file.toPath());
        if (data.length > 10000000) {
            return new ChunkUploader(data, client, filePath, this).upload();
        } else {
            return smallUpload(data, filePath);
        }
    }

    public Optional<FileResponse> upload(File file) {
        return upload(file, file.getName());
    }

    private Optional<FileResponse> smallUpload(byte[] data, String fullPath) {
        MediaType mediaType = OCTET_STREAM_MEDIA_TYPE;
        if (fullPath.endsWith(".png"))
            mediaType = PNG_MEDIA_TYPE;
        else if (fullPath.endsWith(".jpg") || fullPath.endsWith(".jpeg"))
            mediaType = JPEG_MEDIA_TYPE;

        //noinspection deprecation
        RequestBody body = RequestBody.create(mediaType, data);
        Request request = new Request.Builder()
                .url("https://drive.deta.sh/v1/a04cwru2/test/files?name=" + fullPath)
                .post(body)
                .addHeader("Content-Type", "application/octet-stream")
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            if (response.isSuccessful()) {
                JsonObject jsonObject = GSON.fromJson(responseBody, JsonObject.class);
                String name = jsonObject.get("name").getAsString();
                String projectId = jsonObject.get("project_id").getAsString();
                String driveName = jsonObject.get("drive_name").getAsString();
                response.close();
                return Optional.of(new FileResponse(name, projectId, driveName, this));
            } else {
                response.close();
                return Optional.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public String[] deleteFile(String... filePaths) {
        JsonArray array = new JsonArray();
        for (String filePath : filePaths)
            array.add(filePath);
        JsonObject payload = new JsonObject();
        payload.add("names", array);

        Request request = new Request.Builder()
                .url(baseUrl + "files")
                .delete(RequestBody.create(payload.toString(), MediaType.get("application/json")))
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                JsonArray deleted = GSON.fromJson(response.body().string(), JsonObject.class).get("deleted").getAsJsonArray();
                String[] deletedFiles = new String[deleted.size()];
                for (int i = 0; i < deletedFiles.length; i++)
                    deletedFiles[i] = deleted.get(i).getAsString();
                response.close();
                return deletedFiles;
            } else {
                response.close();
                return new String[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    @SneakyThrows // la flemme ™️
    public byte[] download(String filePath) {
        Request request = new Request.Builder()
                .url(baseUrl + "files/download?name=" + filePath)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        byte[] bytes = response.body().bytes();
        response.close();
        return bytes;
    }

    public PackedFilesResponse search(int limit, String prefix, String last) {
        StringBuilder params = new StringBuilder();
        if (limit > 0)
            params.append("&limit=").append(limit);
        if (prefix != null)
            params.append("&prefix=").append(prefix);
        if (last != null)
            params.append("&last=").append(last);

        Request request = new Request.Builder()
                .url(baseUrl + "files" + params.toString().replaceFirst("&", "?"))
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                JsonObject json = GSON.fromJson(response.body().string(), JsonObject.class);
                JsonArray fileNames = json.get("names").getAsJsonArray();
                Paging paging = GSON.fromJson(json.get("paging").getAsJsonObject(), Paging.class);
                List<FileResponse> files = new ArrayList<>();
                for (int i = 0; i < fileNames.size(); i++)
                    files.add(new FileResponse(fileNames.get(i).getAsString(), name, null, this));
                response.close();
                return new PackedFilesResponse(files, paging, this);
            } else {
                response.close();
                return new PackedFilesResponse(new ArrayList<>(), new Paging(0, null), this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PackedFilesResponse(new ArrayList<>(), new Paging(0, null), this);
    }

    public PackedFilesResponse search() {
        return search(0, null, null);
    }

    public PackedFilesResponse search(int limit) {
        return search(limit, null, null);
    }

    public PackedFilesResponse search(int limit, String last) {
        return search(limit, null, last);
    }

    public PackedFilesResponse search(String last) {
        return search(0, null, last);
    }

    public PackedFilesResponse search(String prefix, String last) {
        return search(0, prefix, last);
    }

    @SneakyThrows // la flemme ™️
    public File export(String filePath, File outputFile) {
        byte[] bytes = download(filePath);
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(bytes);
        fos.close();
        return outputFile;
    }
}
