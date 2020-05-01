package com.ikeguang.monitor.mysql.util;

import com.ikeguang.monitor.mysql.web.MonitorTableController;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author quanzongwei
 * @date 2020/5/1
 */

@Component
public class NativeQuery {

    private static final Logger logger = LoggerFactory.getLogger(MonitorTableController.class);

    /**
     * 注入无效
     */
    @PersistenceContext
    private EntityManager entityManager;

    public NativeQuery(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public NativeQuery() {
    }

    public StringBuilder str = new StringBuilder("");

    public List<Object> paramList = new ArrayList<>();

    private boolean count = false;

    public NativeQuery select(String select) {
        if (StringUtils.isBlank(select)) {
            str.append("select * ");
        }
        str.append(select).append(" ");
        return this;
    }

    public NativeQuery from(String tableName) {
        Assert.notNull(tableName);
        str.append("from `").append(tableName.trim()).append("`").append(" ");
        return this;
    }

    public NativeQuery where() {
        str.append("where 1=1 ");
        return this;
    }

    public NativeQuery eq(String fieldStr, Object value) {
        Assert.notNull(fieldStr);
        Assert.notNull(value);
        str.append("and `").append(fieldStr.trim()).append("`").append("=? ");
        paramList.add(value);
        return this;
    }

    public NativeQuery noteq(String fieldStr, Object value) {
        Assert.notNull(fieldStr);
        Assert.notNull(value);
        str.append("and `").append(fieldStr.trim()).append("`").append("<>? ");
        paramList.add(value);
        return this;
    }

    public NativeQuery gt(String fieldStr, Object value) {
        Assert.notNull(fieldStr);
        Assert.notNull(value);
        str.append("and `").append(fieldStr.trim()).append("`").append(">? ");
        paramList.add(value);
        return this;
    }

    public NativeQuery lt(String fieldStr, Object value) {
        Assert.notNull(fieldStr);
        Assert.notNull(value);
        str.append("and `").append(fieldStr.trim()).append("`").append("<? ");
        paramList.add(value);
        return this;
    }

    public NativeQuery gte(String fieldStr, Object value) {
        Assert.notNull(fieldStr);
        Assert.notNull(value);
        str.append("and `").append(fieldStr.trim()).append("`").append(">=? ");
        paramList.add(value);
        return this;
    }

    public NativeQuery lte(String fieldStr, Object value) {
        Assert.notNull(fieldStr);
        Assert.notNull(value);
        str.append("and `").append(fieldStr.trim()).append("`").append("<=? ");
        paramList.add(value);
        return this;
    }

    public NativeQuery like(String fieldStr, Object value) {
        Assert.notNull(fieldStr);
        Assert.notNull(value);
        str.append("and `").append(fieldStr.trim()).append("`").append("like '" + value + "' ");
        return this;
    }

    /**
     * value值必须正确,比如 1,2,3或者'1','2','3'
     */
    public NativeQuery in(String fieldStr, String value) {
        Assert.notNull(fieldStr);
        Assert.notNull(value);
        str.append("and `").append(fieldStr.trim()).append("`").append("in(").append(value).append(") ");
        return this;
    }

    public NativeQuery orderby(String orderby) {
        str.append("order by  ").append(orderby).append(" ");
        return this;
    }

    public NativeQuery offset(int offset) {
        str.append("limit ").append(offset).append(",");
        return this;
    }

    public NativeQuery limit(int offset) {
        str.append(offset).append("; ");
        return this;
    }

    public NativeQuery count(Boolean isCount) {
        if (isCount == null || isCount == false) {
            count = false;
        } else {
            count = true;
        }
        return this;
    }

    public <T> Items<T> executeQuery(Class<T> clazz) {
        List<T> resultList;
        try {
            String s = str.toString();
            logger.info(s);
            Query q = entityManager.createNativeQuery(str.toString(), clazz);
            int i = 1;
            for (Object one : paramList) {
                q.setParameter(i++, one);
            }
            resultList = new ArrayList<>();
            if (count == true) {
                int from = str.indexOf("from");
                String noSelect = str.substring(from, str.length()).toString();
                String sql = "select count(*) ".concat(noSelect);
                int limitIndex = sql.indexOf("limit");
                if (limitIndex != -1) {
                    sql = sql.substring(0, limitIndex);
                }
                Query q2 = entityManager.createNativeQuery(sql);
                int i2 = 1;
                for (Object one : paramList) {
                    q2.setParameter(i2++, one);
                }
                resultList = q.getResultList();
                Object singleResult;
                // 这个如果查不到数据是会报错的
                singleResult = q2.getSingleResult();
                return Items.of(resultList, Long.valueOf(String.valueOf(singleResult)));
            } else {
                resultList = q.getResultList();
            }
        } finally {
            doClear();
        }
        return Items.of(resultList);
    }

    private void doClear() {
        str = new StringBuilder();
        paramList = new ArrayList<>();
        count = false;
    }

}
