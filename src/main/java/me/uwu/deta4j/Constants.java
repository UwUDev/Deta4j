package me.uwu.deta4j;

import com.google.gson.Gson;
import okhttp3.RequestBody;

@SuppressWarnings("deprecation")
public class Constants {
    public static final Gson GSON = new Gson();
    public static final RequestBody NULL_BODY = RequestBody.create(null, new byte[0]);
}
