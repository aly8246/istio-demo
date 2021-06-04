package com.aly8246.order.service;

import com.aly8246.order.entity.Order;
import com.aly8246.order.repository.OrderMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper,Order> implements OrderService{

}
