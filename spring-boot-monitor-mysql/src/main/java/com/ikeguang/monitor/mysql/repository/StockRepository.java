package com.ikeguang.monitor.mysql.repository;

import com.ikeguang.monitor.mysql.model.MonitorTable;
import com.ikeguang.monitor.mysql.model.StockLogPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @ Author: keguang
 * @ Date: 2019/7/25 11:10
 * @ version: v1.0.0
 * @ description:
 */
public interface StockRepository extends JpaRepository<StockLogPO, Long> {



    /**
     * 根据是否启用状态查询
     * @param status 该监控是否启用
     * @return
     */
    Page<StockLogPO> findByStockName(String stockName, Pageable pageable);
    Page<StockLogPO> findByStockNameAndLogType(String stockName,String logType, Pageable pageable);


//    List<StockLogPO> findByStatusAndRealtime(String status, String realtime);
}
