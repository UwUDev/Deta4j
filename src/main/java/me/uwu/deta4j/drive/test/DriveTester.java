package me.uwu.deta4j.drive.test;

import me.uwu.deta4j.drive.DetaDrive;
import me.uwu.deta4j.drive.DetaDrives;
import me.uwu.deta4j.drive.struct.FileResponse;
import me.uwu.deta4j.drive.struct.PackedFilesResponse;

import java.io.File;
import java.util.Arrays;
import java.util.Optional;

public class DriveTester {
    public static void main(String[] args) {
        DetaDrives drives = new DetaDrives("nope");
        DetaDrive drive = drives.get("test");
        System.out.println(drive.getBaseUrl());
        File reallyCoolFile = new File("uploadMe.txt");
        Optional<FileResponse> fileResponse = drive.upload(reallyCoolFile);
        if (fileResponse.isPresent()) {
            System.out.println(fileResponse.get());
            byte[] downloadedFile = fileResponse.get().download();
            System.out.println(new String(downloadedFile));
            String[] deleted = fileResponse.get().delete();
            System.out.println("Deleted " + deleted.length + " files  " + Arrays.toString(deleted));
        } else {
            System.out.println("File not uploaded");
        }
        PackedFilesResponse packedFilesResponse = drive.search();
        for (FileResponse file : packedFilesResponse.getFiles()) {
            System.out.println("Found file: " + file.getName());
        }
        packedFilesResponse.nuke();

        File largeFile = new File("11MB.jpg");
        Optional<FileResponse> largeFileResponse = drive.upload(largeFile);
        if (largeFileResponse.isPresent()) {
            System.out.println(largeFileResponse.get());
            drive.export("11MB.jpg", new File("11MB_2.jpg"));
            System.out.println();
            largeFileResponse.get().delete();

        } else {
            System.out.println("File not uploaded");
        }

    }
}
