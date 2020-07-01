package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service(timeout =3000)
public class DubboCartServiceImpl implements DubboCartService{
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		
		QueryWrapper<Cart> queryWrapper=new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}
	/**
	 * 更新商品数量信息
	 * 更新时间
	 */
	@Transactional
	@Override
	public void updateCartNum(Cart cart) {
		Cart tempCart = new Cart();
		tempCart.setNum(cart.getNum()).setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId());
		updateWrapper.eq("item_id", cart.getItemId());		
		cartMapper.update(tempCart, updateWrapper);
		
	}
	@Transactional
	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> wrapper = new QueryWrapper<Cart>(cart);
		
		cartMapper.delete(wrapper);
		
	}
	/**
	 * 新增购物车业务
	 * 第一次在新增可以直接入库
	 * 用户不是第一次入库,只做数量修改
	 */
	@Transactional
	@Override
	public void insertCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<Cart>();
		queryWrapper.eq("user_id", cart.getUserId()).eq("item_id", cart.getItemId());
		Cart cartDB=cartMapper.selectOne(queryWrapper);
		if(cartDB == null) {
			//第一次在新增可以直接入库
			cart.setCreated(new Date()).setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else {
			//用户不是第一次入库,只做数量修改
			int num= cart.getNum()+cartDB.getNum();
			cartDB.setNum(num).setUpdated(new Date());
		cartMapper.updateById(cartDB);
		}
	}
	

}
