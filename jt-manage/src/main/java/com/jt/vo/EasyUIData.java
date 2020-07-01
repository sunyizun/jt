package com.jt.vo;

import java.util.List;

import com.jt.pojo.Item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
@Data
@Accessors
@NoArgsConstructor
@AllArgsConstructor
public class EasyUIData {

	private Integer total;//记录总数
	private List<Item> rows;//展示数据集合
}
