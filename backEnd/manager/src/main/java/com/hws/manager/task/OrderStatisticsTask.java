package com.hws.manager.task;

import cn.hutool.core.date.DateUtil;
import com.hws.manager.mapper.OrderInfoMapper;
import com.hws.manager.mapper.OrderStatisticsMapper;
import com.hws.model.entity.order.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

//每天凌晨两点查询前一天的订单金额
@Component
public class OrderStatisticsTask {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;
    @Scheduled(cron = "0 0 2 * * ?")
    //@Scheduled(cron = "0/10 * * * * ?")
    public void orderTotalAmountStatistics() {
        //获取前一天的日期
        String createTime = DateUtil.offsetDay(new Date(), -1).toString(new SimpleDateFormat("yyyy-MM-dd"));
        //根据前一天的日期进行金额统计
        OrderStatistics orderStatistics = orderInfoMapper.selectOrderStatistics(createTime);
        //把统计后的结果添加到统计结果表中
        if(orderStatistics != null) {
            orderStatisticsMapper.insert(orderStatistics) ;
        }
    }
}
