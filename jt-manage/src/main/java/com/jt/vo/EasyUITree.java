package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain =true)
public class EasyUITree {

	private long id; //节点id
	private String text;//名称
	private String stste;//状态 open,closed
}
