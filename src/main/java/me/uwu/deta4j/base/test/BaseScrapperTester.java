package me.uwu.deta4j.base.test;

import com.google.gson.JsonArray;
import me.uwu.deta4j.base.DetaBase;
import me.uwu.deta4j.base.DetaBases;
import me.uwu.deta4j.base.utils.DetaBaseScrapper;

public class BaseScrapperTester {
    public static void main(String[] args) {
        DetaBases bases = new DetaBases("a04cwru2_jAjDdjHWY9HyPVXuxRHb2KitvBJGVEA4");
        DetaBase base = bases.get("test");
        DetaBaseScrapper scrapper = new DetaBaseScrapper(base);
        System.out.println(scrapper.countElements());
        JsonArray array = scrapper.dumpAll();
        System.out.println("Dumped " + array.size() + " elements");
        System.out.println(array);
    }
}
