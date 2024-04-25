package com.hws.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hws.feign.cart.CartFeignClient;
import com.hws.feign.product.ProductFeignClient;
import com.hws.feign.user.UserFeignClient;
import com.hws.model.dto.h5.OrderInfoDto;
import com.hws.model.entity.h5.CartInfo;
import com.hws.model.entity.order.OrderInfo;
import com.hws.model.entity.order.OrderItem;
import com.hws.model.entity.order.OrderLog;
import com.hws.model.entity.product.ProductSku;
import com.hws.model.entity.user.UserAddress;
import com.hws.model.entity.user.UserInfo;
import com.hws.model.vo.common.ResultCodeEnum;
import com.hws.model.vo.h5.TradeVo;
import com.hws.mq.constant.MqConst;
import com.hws.mq.service.RabbitService;
import com.hws.order.mapper.OrderInfoMapper;
import com.hws.order.mapper.OrderItemMapper;
import com.hws.order.mapper.OrderLogMapper;
import com.hws.order.service.OrderInfoService;
import com.hws.service.exception.MyException;
import com.hws.utils.AuthContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private CartFeignClient cartFeignClient;
    //结算，需要远程调用cart
    @Override
    public TradeVo getTrade() {
        //远程调用获取购物车选中商品列表
        List<CartInfo> cartInfoList = cartFeignClient.getAllChecked();
        //创建list集合封装订单项
        List<OrderItem> orderItemList = new ArrayList<>();
        for(CartInfo cartInfo : cartInfoList){
            OrderItem orderItem = new OrderItem();
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setThumbImg(cartInfo.getImgUrl());
            orderItemList.add(orderItem);
        }
        //获取订单总金额
        BigDecimal totalPrice = new BigDecimal(0);
        for(OrderItem orderItem : orderItemList){
            totalPrice =totalPrice.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        TradeVo tradeVo = new TradeVo();
        //封装数据
        tradeVo.setOrderItemList(orderItemList);
        tradeVo.setTotalAmount(totalPrice);
        return tradeVo;
    }

    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderLogMapper orderLogMapper;
    @Autowired
    private UserFeignClient userFeignClient;

    @Override
    public Long submitOrder(OrderInfoDto orderInfoDto) {
        //从dto获取所有订单项list
        List<OrderItem> orderItemList = orderInfoDto.getOrderItemList();
        //list为空，抛出异常
        if(orderItemList.isEmpty()){
            throw new MyException(ResultCodeEnum.DATA_ERROR);
        }
        //检验商品库存是否充足
        //遍历list<OrderItem>得到每个item
        for(OrderItem orderItem : orderItemList){
            //校验每个item库存量是否充足--远程调用获取商品sku信息（库存量）
            ProductSku productSku = productFeignClient.getBySkuId(orderItem.getSkuId());
            if(productSku == null){
                throw new MyException(ResultCodeEnum.DATA_ERROR);
            }
            if(orderItem.getSkuNum().intValue() >productSku.getStockNum().intValue()){
                throw new MyException(ResultCodeEnum.STOCK_LESS);
            }
        }
        //添加数据到orderinfo表----封装数据到orderinfo对象
        UserInfo userInfo = AuthContextUtil.getUserInfo();
        OrderInfo orderInfo = new OrderInfo();
        //订单编号
        orderInfo.setOrderNo(String.valueOf(System.currentTimeMillis()));
        //用户id
        orderInfo.setUserId(userInfo.getId());
        //用户昵称
        orderInfo.setNickName(userInfo.getNickName());
        //远程调用获取收货地址信息
        UserAddress userAddress = userFeignClient.getUserAddress(orderInfoDto.getUserAddressId());
        orderInfo.setReceiverName(userAddress.getName());
        orderInfo.setReceiverPhone(userAddress.getPhone());
        orderInfo.setReceiverTagName(userAddress.getTagName());
        orderInfo.setReceiverProvince(userAddress.getProvinceCode());
        orderInfo.setReceiverCity(userAddress.getCityCode());
        orderInfo.setReceiverDistrict(userAddress.getDistrictCode());
        orderInfo.setReceiverAddress(userAddress.getFullAddress());
        //添加数据到orderitem表---把list<OrderItem>得到每个item添加进去即可
        //添加数据到orderlog表
        //订单金额
        BigDecimal totalAmount = new BigDecimal(0);
        for (OrderItem orderItem : orderItemList) {
            totalAmount = totalAmount.add(orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum())));
        }
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setCouponAmount(new BigDecimal(0));
        orderInfo.setOriginalTotalAmount(totalAmount);
        orderInfo.setFeightFee(orderInfoDto.getFeightFee());
        orderInfo.setPayType(2);
        orderInfo.setOrderStatus(0);
        orderInfoMapper.save(orderInfo);
        //保存订单明细
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(orderInfo.getId());
            orderItemMapper.save(orderItem);
        }
        //记录日志
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderInfo.getId());
        orderLog.setProcessStatus(0);
        orderLog.setNote("提交订单");
        orderLogMapper.save(orderLog);
        // TODO 远程调用service-cart微服务接口清空购物车数据
        cartFeignClient.deleteChecked();
        //返回订单id
        return orderInfo.getId();
    }
    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        return orderInfoMapper.getById(orderId);
    }

    @Override
    public TradeVo buy(Long skuId) {
        // 查询商品
        ProductSku productSku = productFeignClient.getBySkuId(skuId);
        List<OrderItem> orderItemList = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setSkuId(skuId);
        orderItem.setSkuName(productSku.getSkuName());
        orderItem.setSkuNum(1);
        orderItem.setSkuPrice(productSku.getSalePrice());
        orderItem.setThumbImg(productSku.getThumbImg());
        orderItemList.add(orderItem);
        // 计算总金额
        BigDecimal totalAmount = productSku.getSalePrice();
        TradeVo tradeVo = new TradeVo();
        tradeVo.setTotalAmount(totalAmount);
        tradeVo.setOrderItemList(orderItemList);
        // 返回
        return tradeVo;
    }

    @Override
    public PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus) {
        PageHelper.startPage(page, limit);
        Long userId = AuthContextUtil.getUserInfo().getId();
        List<OrderInfo> orderInfoList = orderInfoMapper.findUserPage(userId, orderStatus);

        orderInfoList.forEach(orderInfo -> {
            List<OrderItem> orderItem = orderItemMapper.findByOrderId(orderInfo.getId());
            orderInfo.setOrderItemList(orderItem);
        });

        return new PageInfo<>(orderInfoList);
    }

    @Override
    public OrderInfo getByOrderNo(String orderNo) {
        OrderInfo orderInfo = orderInfoMapper.getByOrderNo(orderNo);
        List<OrderItem> orderItem = orderItemMapper.findByOrderId(orderInfo.getId());
        orderInfo.setOrderItemList(orderItem);
        return orderInfo;
    }

}
