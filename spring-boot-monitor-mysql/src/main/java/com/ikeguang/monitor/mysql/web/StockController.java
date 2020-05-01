package com.ikeguang.monitor.mysql.web;

import com.ikeguang.monitor.mysql.model.StockLogPO;
import com.ikeguang.monitor.mysql.repository.StockRepository;
import com.ikeguang.monitor.mysql.service.MonitorTableService;
import com.ikeguang.monitor.mysql.util.Items;
import com.ikeguang.monitor.mysql.util.NativeQuery;
import com.ikeguang.monitor.mysql.util.PageUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Optional;

/**
 * @ Author: keguang
 * @ Date: 2019/7/25 11:36
 * @ version: v1.0.0
 * @ description:
 */
@Controller
public class StockController {

    private static final Logger logger = LoggerFactory.getLogger(StockController.class);

    @Resource
    MonitorTableService monitorTableService;

    @Resource
    StockRepository stockRepository;

    @RequestMapping("/")
    public String index() {
        return "redirect:/stock/list";
    }

    @Autowired
    EntityManager entityManager;

    @Autowired
    NativeQuery nativeQuery;

    /**
     * 对应url
     */
    @RequestMapping("/stock/list")
    @PostMapping
    public String list(Model model, @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
                       @RequestParam(value = "pageSize", defaultValue = "2")
                               int pageSize, StockLogPO stockLogPO) {
        pageNum = stockLogPO.getPageNum();


        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Page<StockLogPO> all = stockRepository.findAll(page);


        if (stockLogPO.getStockName() != "" && stockLogPO.getLogType() != "") {
            all = stockRepository.findByStockNameAndLogType(stockLogPO.getStockName(), stockLogPO.getLogType(), page);
        }
        //
        NativeQuery query = nativeQuery.select("")
                .from("qzw_stock_log_2")
                .where();

        String logType = stockLogPO.getLogType();
        String stockName = stockLogPO.getStockName();
        if (StringUtils.isNotBlank(stockName)) {
            query.eq("stockName", stockName);
        }
        if (StringUtils.isNotBlank(logType)) {
            query.eq("logType", logType);
        }
        query.count(true)
                .orderby(" id desc")
                .offset(pageNum*pageSize).limit(pageSize);
        Items<StockLogPO> items = query.executeQuery(StockLogPO.class);
        model.addAttribute("stockTables", PageUtil.transfer(items,pageNum,pageSize));
        // 对你应到模板的目录,这个不用改
        return "monitorTable/listStock";
    }

    @RequestMapping("/stock/add")
    public String add() {
        return "monitorTable/addStock";
    }

    @RequestMapping("/stock/addSubmit")
    public String addSubmit(StockLogPO stockLogPO) {

        stockLogPO.setStockCode("");
        stockLogPO.setDescription("");
        stockLogPO.setCreateTime(new Date());
        stockLogPO.setUpdateTime(new Date());
        stockRepository.save(stockLogPO);
        return "redirect:/stock/list";

    }

    @RequestMapping("/stock/edit")
    public String edit(Model model, long id) {
        StockLogPO stockLogPO = stockRepository.findById(id).orElse(null);
        model.addAttribute("stockTable", stockLogPO);
        return "monitorTable/editStock";
    }

    @RequestMapping("/stock/editSubmit")
    public String editSubmit(StockLogPO stockLogPO) {

        Optional<StockLogPO> item = stockRepository.findById(stockLogPO.getId());
        StockLogPO po = item.orElse(null);
        if (po != null) {
            po.setStockName(stockLogPO.getStockName());
            po.setLogType(stockLogPO.getLogType());
            po.setPrice(stockLogPO.getPrice());
            po.setAmount(stockLogPO.getAmount());
            po.setDescription(stockLogPO.getDescription() == null ? "" : stockLogPO.getDescription());
        }
        stockRepository.save(po);
        return "redirect:/stock/list";
    }

    @RequestMapping("/stock/delete")
    public String delete(long id) {
        stockRepository.deleteById(id);
        return "redirect:/stock/list";
    }
}
