package me.uwu.deta4j;

import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@SuppressWarnings("deprecation")
public class Constants {
    public static final Gson GSON = new Gson();
    public static final RequestBody NULL_BODY = RequestBody.create(null, new byte[0]);
    public static final MediaType OCTET_STREAM_MEDIA_TYPE = MediaType.parse("application/octet-stream");
    public static final MediaType JPEG_MEDIA_TYPE = MediaType.parse("image/jpeg");
    public static final MediaType PNG_MEDIA_TYPE = MediaType.parse("image/png");
}
