package me.uwu.deta4j.base.struct;

import lombok.Data;

public @Data class Paging {
    private final int size;
    private final String last;
}
