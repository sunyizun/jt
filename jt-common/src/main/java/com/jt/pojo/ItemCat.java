package com.jt.pojo;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
@TableName("tb_item_cat")
public class ItemCat extends BasePojo {
	@TableId(type=IdType.AUTO)
	 private Integer id;               
	 private Integer parentId;  //父级ID       
	 private String name;       //名称       
	 private Integer status;  //'默认值为1，可选值：1正常，2删除',
	 private Integer sortOrder;   //排序号       
	 private Boolean isParent ;   //        
	        
	
	
}
