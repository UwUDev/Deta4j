package me.uwu.deta4j.base.utils;

import com.google.gson.JsonObject;

public class JsonUtils {
    public static JsonObject setKeyAtFirst(JsonObject json, String key) {
        JsonObject newJson = new JsonObject();
        newJson.addProperty("key", key);
        json.entrySet().forEach(entry -> newJson.add(entry.getKey(), entry.getValue()));
        return newJson;
    }
}
