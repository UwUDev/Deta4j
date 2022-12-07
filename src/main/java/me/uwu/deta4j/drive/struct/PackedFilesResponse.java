package me.uwu.deta4j.drive.struct;

import lombok.Data;
import me.uwu.deta4j.base.struct.Paging;
import me.uwu.deta4j.drive.DetaDrive;

import java.util.List;

public @Data class PackedFilesResponse {
    private final List<FileResponse> files;
    private final Paging paging;
    private final DetaDrive drive;

    public void nuke() {
        for (FileResponse file : files) {
            file.delete();
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasNext(){
        return paging.getLast() != null;
    }

    public PackedFilesResponse next() {
        if (!hasNext()) return null;
        return drive.search(paging.getLast());
    }

    public PackedFilesResponse next(int limit) {
        if (!hasNext()) return null;
        return drive.search(limit, paging.getLast());
    }
}
