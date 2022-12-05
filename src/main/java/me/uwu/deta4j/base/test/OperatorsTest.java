package me.uwu.deta4j.base.test;

import me.uwu.deta4j.base.query.QueryOperator;
import me.uwu.deta4j.base.query.impl.*;

public class OperatorsTest {
    public static void main(String[] args) {
        QueryOperator query = new LessThanOrEqualQuery("test", 1);
        QueryOperator query2 = new ContainsQuery("name", "Ja");
        QueryOperator query3 = new StartWithQuery("id", "123");
        QueryOperator[] queries = new QueryOperator[]{query, query2, query3};
        QueryOperator query4 = new AndQuery(queries);
        QueryOperator query5 = new OrQuery(queries);
        System.out.println(query4.buildQuery());
        System.out.println(query5.buildQuery());
    }
}
