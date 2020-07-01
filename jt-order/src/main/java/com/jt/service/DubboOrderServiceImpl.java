package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class DubboOrderServiceImpl implements DubboOrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	/**1.一个业务逻辑需要操作三张表
	 * 通过order对象,封装了三个对象     order orderShipping orderItem
	 * 1.动态生成orderId
	 * 2.分别实现入库
	 * 3.注意事物控制
	 * 
	 */
	@Transactional
	@Override
	public String insertOrder(Order order) {
		//1.动态生成orderId  登录用户id+当前时间戳
		String orderId = ""+order.getUserId()+System.currentTimeMillis();  //陨石坑
		Date date = new Date();
		//2.完成订单入库操作
		order.setOrderId(orderId).setStatus(1)
		.setCreated(date).setUpdated(date);
		orderMapper.insert(order);
		System.out.println("订单入库成功!!!!");
		//3.订单物流入库
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId)
		.setCreated(date).setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单物流入库成功!!!");
		//4.订单商品信息入库  1.代码入库  2.mybatis 动态sql完成入库
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId)
			.setCreated(date)
			.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("恭喜订单入库成功!!!!");

		return orderId;
	}
	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping orderShipping = orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id", id);
		List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
		order.setOrderShipping(orderShipping)
		.setOrderItems(orderItems);
		return order;
	}


}
