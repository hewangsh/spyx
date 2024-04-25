package com.hws.pay.service;

import com.hws.model.entity.pay.PaymentInfo;

public interface PaymentInfoService {
    PaymentInfo savePaymentInfo(String orderNo);
}
