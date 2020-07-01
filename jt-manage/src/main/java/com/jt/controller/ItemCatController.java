package com.jt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.service.ItemCatService;
import com.jt.vo.EasyUITree;


@RestController
@RequestMapping("/item/cat")
public class ItemCatController {
	@Autowired
	private ItemCatService itemCatService;
	@RequestMapping("/queryItemName")
	public String findItemName(Long itemCatId){
		return itemCatService.findItemName(itemCatId);
		
	}
	//查询全部数据的商品分类信息
	@RequestMapping("/list")
	@Cache_Find(key="ITEM_CAT",keyType=KEY_ENUM.AUTO)
	public List<EasyUITree> findItemCatByParentId(@RequestParam(value="id",defaultValue="0")Long parentId){
		return itemCatService.findItemCatByParentId(parentId);
		//return itemCatService.findItemCatByCache(parentId);
		
	}
	
}
