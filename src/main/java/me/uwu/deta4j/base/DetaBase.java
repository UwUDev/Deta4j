package me.uwu.deta4j.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import me.uwu.deta4j.base.query.QueryOperator;
import me.uwu.deta4j.base.struct.Paging;
import me.uwu.deta4j.base.struct.QueryResponse;
import me.uwu.deta4j.base.struct.ResponseItem;
import me.uwu.deta4j.base.utils.JsonUtils;
import me.uwu.deta4j.base.exception.SQLConflictException;
import me.uwu.deta4j.base.exception.SQLException;
import me.uwu.deta4j.base.exception.SQLNotFoundException;
import okhttp3.*;

import java.io.IOException;
import java.util.Optional;

import static me.uwu.deta4j.Constants.GSON;
import static me.uwu.deta4j.Constants.NULL_BODY;

@SuppressWarnings("ConstantConditions")
public class DetaBase {

    @Getter
    private final String baseUrl, name;
    private final OkHttpClient client;

    public DetaBase(String apiKey, String name, OkHttpClient client) {
        this.client = client;
        this.name = name;
        this.baseUrl = "https://database.deta.sh/v1/" + apiKey.split("_")[0] + "/" + name + "/";
    }

    public Optional<ResponseItem> getItemByKey(String key) {
        Request request = new Request.Builder()
                .url(baseUrl + "items/" + key)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            if (response.isSuccessful()) {
                response.close();
                return Optional.of(new ResponseItem(JsonParser.parseString(responseBody).getAsJsonObject(), this));
            } else {
                response.close();
                return Optional.empty();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public ResponseItem insert(JsonObject payload) {
        Request request = new Request.Builder()
                .url(baseUrl + "items")
                .post(RequestBody.create(payload.toString(), MediaType.get("application/json")))
                .build();

        try {
            Response response = client.newCall(request).execute();
            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (response.isSuccessful()) {
                response.close();
                return new ResponseItem(json, this);
            } else if(response.code() == 409) {
                response.close();
                throw SQLConflictException.of(json.get("errors").getAsJsonArray().get(0).getAsString());
            }else {
                response.close();
                System.out.println(json);
                try {
                    throw SQLException.of(json.get("errors").getAsJsonArray().get(0).getAsString());
                } catch (Exception e) {
                    throw SQLException.of(json.get("message").getAsJsonArray().get(0).getAsString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ResponseItem insert(Object o) {
        JsonObject payload = new JsonObject();
        JsonObject item = JsonParser.parseString(GSON.toJson(o)).getAsJsonObject();
        payload.add("item", item);

        return insert(payload);
    }

    public ResponseItem insert(Object o, String key) {
        JsonObject payload = new JsonObject();
        JsonObject item = GSON.toJsonTree(o).getAsJsonObject();
        item = JsonUtils.setKeyAtFirst(item, key);
        payload.add("item", item);

        return insert(payload);
    }

    public ResponseItem update(Object o, String key) {
        JsonObject payload = new JsonObject();
        JsonObject item = JsonParser.parseString(GSON.toJson(o)).getAsJsonObject();
        if (item.has("key")) item.remove("key");
        payload.add("set", item);

        return update(payload, key);
    }

    public ResponseItem update(JsonObject payload, String key) {
        Request request = new Request.Builder()
                .url(baseUrl + "items/" + key)
                .patch(RequestBody.create(payload.toString(), MediaType.get("application/json")))
                .build();

        try {
            Response response = client.newCall(request).execute();
            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (response.isSuccessful()) {
                response.close();
                return new ResponseItem(json, this);
            } else if(response.code() == 404) {
                response.close();
                throw SQLNotFoundException.of(json.get("errors").getAsJsonArray().get(0).getAsString());
            }else {
                response.close();
                try {
                    throw SQLException.of(json.get("errors").getAsJsonArray().get(0).getAsString());
                } catch (Exception e) {
                    throw SQLException.of(json.get("message").getAsJsonArray().get(0).getAsString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteItemByKey(String key) {
        Request request = new Request.Builder()
                .url(baseUrl + "items/" + key)
                .delete(NULL_BODY)
                .build();

        try {
            client.newCall(request).execute().close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public QueryResponse query(QueryOperator query, String last, int limit) {
        JsonObject payload = new JsonObject();
        JsonElement queryElement = query.buildQuery();
        if(queryElement.isJsonArray()) {
            payload.add("query", queryElement);
        }else {
            JsonArray array = new JsonArray();
            array.add(queryElement);
            payload.add("query", array);
        }
        if (last != null) payload.addProperty("last", last);
        if (limit > 0) payload.addProperty("limit", limit);

        return query(payload, query);
    }

    public QueryResponse query(QueryOperator query, String last) {
        return query(query, last, 0);
    }

    public QueryResponse query(QueryOperator query, int limit) {
        return query(query, null, limit);
    }

    public QueryResponse query(QueryOperator query) {
        return query(query, null, 0);
    }

    public QueryResponse query(JsonObject payload, QueryOperator query) {
        System.out.println(payload);
        Request request = new Request.Builder()
                .url(baseUrl + "query")
                .post(RequestBody.create(payload.toString(), MediaType.get("application/json")))
                .build();

        try {
            Response response = client.newCall(request).execute();
            JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
            if (response.isSuccessful()) {
                response.close();
                Paging paging = GSON.fromJson(json.get("paging"), Paging.class);
                JsonArray items = json.get("items").getAsJsonArray();
                ResponseItem[] responseItems = new ResponseItem[items.size()];
                for (int i = 0; i < items.size(); i++) {
                    responseItems[i] = new ResponseItem(items.get(i).getAsJsonObject(), this);
                }
                return new QueryResponse(paging, responseItems, this, query);
            } else if(response.code() == 409) {
                response.close();
                throw SQLConflictException.of(json.get("errors").getAsJsonArray().get(0).getAsString());
            }else {
                response.close();
                try {
                    throw SQLException.of(json.get("errors").getAsJsonArray().get(0).getAsString());
                } catch (Exception e) {
                    throw SQLException.of(json.get("message").getAsJsonArray().get(0).getAsString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
