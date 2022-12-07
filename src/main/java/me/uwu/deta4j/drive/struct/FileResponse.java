package me.uwu.deta4j.drive.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import me.uwu.deta4j.drive.DetaDrive;

public @Data class FileResponse {
    private final String name;
    @SerializedName("project_id")
    private final String projectId;
    @SerializedName("drive_name")
    private final String driveName;
    private final DetaDrive drive;

    public String[] delete() {
        return drive.deleteFile(name);
    }

    public byte[] download() {
        return drive.download(name);
    }
}
