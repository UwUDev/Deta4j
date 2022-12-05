package me.uwu.deta4j.base.struct;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import me.uwu.deta4j.base.DetaBase;
import me.uwu.deta4j.base.utils.JsonUtils;

import static me.uwu.deta4j.Constants.GSON;

@AllArgsConstructor
public @Data class ResponseItem {
    private final JsonObject json;
    private DetaBase base;

    public String getKey() {
        return json.get("key").getAsString();
    }

    public <T> T serialize(Class<T> classOfT) {
        return GSON.fromJson(json.toString(), classOfT);
    }

    public boolean delete() {
        return base.deleteItemByKey(getKey());
    }

    public ResponseItem reinsert() {
        JsonObject clone = new JsonObject();
        json.entrySet().forEach(entry -> clone.add(entry.getKey(), entry.getValue()));
        clone.remove("key");
        return base.insert(GSON.fromJson(clone.toString(), Object.class));
    }

    public ResponseItem reinsert(String key) {
        JsonObject clone = new JsonObject();
        json.entrySet().forEach(entry -> clone.add(entry.getKey(), entry.getValue()));
        clone.remove("key");
        JsonUtils.setKeyAtFirst(clone, key);
        return base.insert(GSON.fromJson(clone.toString(), Object.class));
    }

    public ResponseItem update(Object o) {
        return base.update(o, getKey());
    }
}
