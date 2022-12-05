package me.uwu.deta4j.base.query.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.uwu.deta4j.base.query.QueryOperator;

@RequiredArgsConstructor
public class OrQuery implements QueryOperator {
    private final QueryOperator[] queries;

    @Override
    public JsonArray buildQuery() {
        JsonArray array = new JsonArray();
        for (QueryOperator query : queries) {
            array.add(query.buildQuery());
        }
        return array;
    }
}
