package com.ikeguang.monitor.mysql.util;

import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * @author BG388892
 * @date 2020/5/1
 */
@Data
public class Items<T> {

    private Long count = 0L;

    private Object append;

    private List<T> items = Collections.emptyList();

    private Items() {
    }


    public static <T> Items<T> of(List<T> resultList, Long count) {
        Items items = new Items();
        items.setItems(resultList);
        items.setCount(count);
        return items;
    }

    public static <T> Items<T> of(List<T> resultList) {
        Items items = new Items();
        items.setItems(resultList);
        return items;
    }
}
