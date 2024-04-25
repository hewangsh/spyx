package com.hws.manager.service;

import com.hws.model.dto.order.OrderStatisticsDto;
import com.hws.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
