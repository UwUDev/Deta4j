package me.uwu.deta4j.base.struct;

import lombok.Getter;
import lombok.ToString;
import me.uwu.deta4j.base.DetaBase;
import me.uwu.deta4j.base.query.QueryOperator;

@Getter
@ToString
public class QueryResponse {
    private final Paging paging;
    private final ResponseItem[] items;
    private final DetaBase base;
    private final QueryOperator query;

    public QueryResponse(Paging paging, ResponseItem[] items, DetaBase base, QueryOperator query) {
        this.paging = paging;
        this.items = items;
        this.base = base;
        this.query = query;
        for (ResponseItem item : items) item.setBase(base);
    }

    public boolean hasNext(){
        return paging.getLast() != null;
    }

    public QueryResponse next(){
        if (!hasNext()) return null;
        return base.query(query, paging.getLast());
    }

    public QueryResponse next(int limit){
        if (!hasNext()) return null;
        return base.query(query, paging.getLast(), limit);
    }
}
