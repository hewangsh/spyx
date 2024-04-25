package com.hws.cart.service;

import com.hws.model.entity.h5.CartInfo;

import java.util.List;

//业务接口
public interface CartService {
    void addToCart(Long skuId, Integer skuNum);

    List<CartInfo> getCartList();

    void deleteCart(Long skuId);

    void checkCart(Long skuId, Integer isChecked);

    void allCheckCart(Integer isChecked);

    void clearCart();

    List<CartInfo> getAllChecked();

    void deleteChecked();
}