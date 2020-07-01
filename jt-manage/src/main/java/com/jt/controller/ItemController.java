package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUIData;
import com.jt.vo.SysResult;

@RestController
@RequestMapping("/item")
public class ItemController {

	@Autowired
	private ItemService itemService;
	@RequestMapping("/query")
	public EasyUIData findItemByPage(Integer page, Integer rows) {

		return itemService.findItemByPage(page,rows);

	}
	//保存商品
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
		try {
			itemService.saveItem(item,itemDesc);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.fail();
		}
	}
	//修改商品
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		try {
			itemService.updateItem(item,itemDesc);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.fail("修改商品失败");
		}
	}

	//删除商品
	@RequestMapping("/delete")
	public SysResult deleteItem(Long[] ids) {
		try {
			itemService.deleteItem(ids);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.fail("删除商品失败");
		}
	}
	//下架商品
	@RequestMapping("/instock")
	public SysResult instockItem(Long[] ids) {
		try {
			int status=2;
			itemService.updateStstus(ids,status);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.fail("下架商品失败");
		}
	}

	//上架商品
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		try {
			int ststus=1;
			itemService.updateStstus(ids,ststus);
			return SysResult.ok();
		} catch (Exception e) {
			return SysResult.fail("上架商品失败");
		}
	}
	
	
	//根据ItemId查询商品详情信息
		@RequestMapping("/query/item/desc/{itemId}")
		public SysResult findItemDescById(@PathVariable Long itemId) {
			try {
				ItemDesc itemDesc=itemService.findItemDescById(itemId);
				return SysResult.ok(itemDesc);
			} catch (Exception e) {
				return SysResult.fail("查询商品详情失败");
			}
		}
}
