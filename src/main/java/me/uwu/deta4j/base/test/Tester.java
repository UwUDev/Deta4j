package me.uwu.deta4j.base.test;

import me.uwu.deta4j.base.DetaBase;
import me.uwu.deta4j.base.DetaBases;
import me.uwu.deta4j.base.query.QueryOperator;
import me.uwu.deta4j.base.query.impl.EqualQuery;
import me.uwu.deta4j.base.struct.QueryResponse;
import me.uwu.deta4j.base.struct.ResponseItem;

import java.util.Optional;

public class Tester {
    public static void main(String[] args) {
        DetaBases bases = new DetaBases("deta_base_key");
        DetaBase base = bases.get("test");
        Optional<ResponseItem> item = base.getItemByKey("69");
        if (item.isPresent()) {
            System.out.println(item.get().getJson());
            TestItem testItem = item.get().serialize(TestItem.class);
            System.out.println("Test value: " + testItem.getTestValue());
        } else System.out.println("Not found");


        TestItem testItem = new TestItem("Super test value");

        ResponseItem responseItem = base.insert(testItem);
        System.out.println("Inserted item with id " + responseItem.getKey());
        if (responseItem.delete())
            System.out.println("Deleted item with id " + responseItem.getKey());
        else System.out.println("Failed to delete item with id " + responseItem.getKey());

        responseItem = base.insert(testItem, "InsaneID");
        System.out.println("Inserted item with id " + responseItem.getKey());
        if (responseItem.delete())
            System.out.println("Deleted item with id " + responseItem.getKey());
        else System.out.println("Failed to delete item with id " + responseItem.getKey());

        responseItem = base.insert(testItem);
        System.out.println("Inserted item with id " + responseItem.getKey());
        TestItem testItem2 = new TestItem("Super test value 2");
        responseItem = responseItem.update(testItem2);
        System.out.println("Updated item with id " + responseItem.getKey());
        if (responseItem.delete())
            System.out.println("Deleted item with id " + responseItem.getKey());
        else System.out.println("Failed to delete item with id " + responseItem.getKey());

        TestItem testItem3 = new TestItem("test");
        responseItem = base.insert(testItem3);
        System.out.println("Inserted item with id " + responseItem.getKey());
        for (int i = 0; i < 6; i++) {
            responseItem = responseItem.reinsert();
            System.out.println("Cloned item with id " + responseItem.getKey());
        }

        QueryOperator query = new EqualQuery("testValue", "test");

        QueryResponse queryResponse = base.query(query, 3);
        System.out.println("Query response: " + queryResponse);
        for (ResponseItem queryResponseItem : queryResponse.getItems()) {
            System.out.println(queryResponseItem.getJson());
            queryResponseItem.delete();
            System.out.println("Deleted item with id " + queryResponseItem.getKey());
        }

        while (queryResponse.hasNext()) {
            queryResponse = queryResponse.next(queryResponse.getPaging().getSize());
            for (ResponseItem queryResponseItem : queryResponse.getItems()) {
                System.out.println(queryResponseItem.getJson());
                queryResponseItem.delete();
                System.out.println("Deleted item with id " + queryResponseItem.getKey());
            }
        }

    }
}
