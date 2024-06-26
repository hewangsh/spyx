package com.hws.manager.service.impl;

import com.hws.manager.mapper.ProductUnitMapper;
import com.hws.manager.service.ProductUnitService;
import com.hws.model.entity.base.ProductUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {
    @Autowired
    private ProductUnitMapper productUnitMapper ;

    @Override
    public List<ProductUnit> findAll() {
        return productUnitMapper.findAll() ;
    }
}
