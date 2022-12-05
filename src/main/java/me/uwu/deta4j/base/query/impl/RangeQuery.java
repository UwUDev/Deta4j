package me.uwu.deta4j.base.query.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.uwu.deta4j.base.query.QueryOperator;

@RequiredArgsConstructor
public class RangeQuery implements QueryOperator {
    private final String key;
    private final double[] values;

    @Override
    public JsonObject buildQuery() {
        JsonObject json = new JsonObject();
        JsonArray array = new JsonArray();
        for (double value : values) {
            array.add(value);
        }
        json.add(key + "?r", array);
        return json;
    }
}
