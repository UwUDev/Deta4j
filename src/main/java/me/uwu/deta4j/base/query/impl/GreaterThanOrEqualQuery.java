package me.uwu.deta4j.base.query.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.uwu.deta4j.base.query.QueryOperator;

@RequiredArgsConstructor
public class GreaterThanOrEqualQuery implements QueryOperator {
    private final String key;
    private final double value;

    @Override
    public JsonObject buildQuery() {
        JsonObject json = new JsonObject();
        json.addProperty(key + "?gte", value);
        return json;
    }
}
