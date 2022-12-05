package me.uwu.deta4j.base.query.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.uwu.deta4j.base.query.QueryOperator;

@RequiredArgsConstructor
public class NotContainsQuery implements QueryOperator {
    private final String key;
    private final String value;

    @Override
    public JsonObject buildQuery() {
        JsonObject json = new JsonObject();
        json.addProperty(key + "?not_contains", value);
        return json;
    }
}
