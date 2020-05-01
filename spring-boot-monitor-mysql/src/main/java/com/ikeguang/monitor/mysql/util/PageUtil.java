package com.ikeguang.monitor.mysql.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author BG388892
 * @date 2020/5/1
 */
public class PageUtil {

    public static <T> Page<T> transfer(Items<T> items, int pageNum, int pageSize) {
        // 数字没有意义
        Pageable pageRequest = new PageRequest(pageNum, pageSize);
        PageImpl<T> page = new PageImpl<T>(items.getItems(), pageRequest, items.getCount());
        return page;
    }
}
