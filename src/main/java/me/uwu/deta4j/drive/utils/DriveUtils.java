package me.uwu.deta4j.drive.utils;

public class DriveUtils {
    public static int maxChunkSize = 9 * 1024 * 1024;
    public static boolean isValidPath(String path) {
        return path.matches("^[a-zA-Z0-9_\\-./]+$");
    }

    public static byte[][] chunkBytes(byte[] bytes) {
        int numOfChunks = (int) Math.ceil(bytes.length / (float) maxChunkSize);
        byte[][] output = new byte[numOfChunks][];

        for (int i = 0; i < numOfChunks; ++i) {
            int start = i * maxChunkSize;
            int length = Math.min(bytes.length - start, maxChunkSize);

            byte[] temp = new byte[length];
            System.arraycopy(bytes, start, temp, 0, length);
            output[i] = temp;
        }

        return output;
    }
}
