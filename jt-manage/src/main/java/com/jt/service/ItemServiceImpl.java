package com.jt.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.ItemDescMapper;
import com.jt.mapper.ItemMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.vo.EasyUIData;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private ItemDescMapper itemDescMapper;

	@Override
	public EasyUIData findItemByPage(Integer page, Integer rows) {

		Integer total=itemMapper.selectCount(null);
		int start =(page-1)*rows;
		List<Item> itemList = itemMapper.findItemByPage(start,rows);
		return new EasyUIData(total,itemList);
	}
	@Transactional
	@Override
	public void saveItem(Item item,ItemDesc itemDesc) {
		item.setStatus(1).setCreated(new Date()).setUpdated(item.getCreated());
		itemMapper.insert(item);
		//同时入库两张表
		itemDesc.setItemId(item.getId()).setCreated(item.getCreated()).setUpdated(item.getUpdated());
		itemDescMapper.insert(itemDesc);
		
	}
	//根据主键更新
	@Transactional
	@Override
	public void updateItem(Item item,ItemDesc itemDesc) {
		item.setUpdated(new Date());
		itemMapper.updateById(item);
		//同时修改两张表
		itemDesc.setItemId(item.getId()).setUpdated(item.getUpdated());
		itemDescMapper.updateById(itemDesc);
	}
	//根据主键删除
	@Transactional
	@Override
	public void deleteItem(Long[] ids) {
		//手动删除
		itemMapper.deleteItem(ids);
		//用plus提供方法
		List<Long> idList =Arrays.asList(ids);
		itemMapper.deleteBatchIds(idList);
		//同时删除两张表
		itemDescMapper.deleteBatchIds(idList);
	}
	//上下架商品
	@Transactional
	@Override
	public void updateStstus(Long[] ids, Integer status) {
		Item item = new Item();
		item.setStatus(status).setUpdated(new Date());
		UpdateWrapper<Item> updateWrapper = new UpdateWrapper<Item>();
		List<Long> idList =Arrays.asList(ids);
		updateWrapper.in("id", idList);
		itemMapper.update(item, updateWrapper);

	}
	@Transactional
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		ItemDesc itemDesc =itemDescMapper.selectById(itemId);
		return itemDesc;
	}
	@Override
	public Item findItemById(Long id) {
		
		return itemMapper.selectById(id);
	}








}
