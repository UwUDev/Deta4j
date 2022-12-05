package me.uwu.deta4j.base.query.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import me.uwu.deta4j.base.query.QueryOperator;

@RequiredArgsConstructor
public class AndQuery implements QueryOperator {
    private final QueryOperator[] queries;

    @Override
    public JsonObject buildQuery() {
        JsonObject json = new JsonObject();
        for (QueryOperator query : queries) {
            JsonObject queryJson = (JsonObject) query.buildQuery();
            json.add(queryJson.entrySet().iterator().next().getKey(), queryJson.entrySet().iterator().next().getValue());
        }
        return json;
    }
}
