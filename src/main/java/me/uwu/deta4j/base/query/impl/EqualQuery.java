package me.uwu.deta4j.base.query.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.uwu.deta4j.base.query.QueryOperator;

import static me.uwu.deta4j.Constants.GSON;

@RequiredArgsConstructor
public class EqualQuery implements QueryOperator {
    private final String key;
    private final Object value;

    @Override
    public JsonObject buildQuery() {
        JsonObject json = new JsonObject();
        if (value instanceof String)
            json.addProperty(key, (String) value);
        else if (value instanceof Number)
            json.addProperty(key, (Number) value);
        else if (value instanceof Boolean)
            json.addProperty(key, (Boolean) value);
        else json.add(key, GSON.toJsonTree(value));
        return json;
    }
}
