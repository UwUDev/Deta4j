package me.uwu.deta4j.base.utils;

import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import me.uwu.deta4j.base.DetaBase;
import me.uwu.deta4j.base.struct.QueryResponse;
import me.uwu.deta4j.base.struct.ResponseItem;

@RequiredArgsConstructor
public class DetaBaseScrapper {
    private final DetaBase base;

    public int countElements(){
        int count = 0;
        QueryResponse response = base.query();
        while (response != null){
            count += response.getPaging().getSize();
            response = response.next();
        }
        return count;
    }

    public JsonArray dumpAll(){
        JsonArray array = new JsonArray();
        QueryResponse response = base.query();
        while (response != null){
            for (ResponseItem item : response.getItems())
                array.add(item.getJson());
            response = response.next();
        }
        return array;
    }
}
