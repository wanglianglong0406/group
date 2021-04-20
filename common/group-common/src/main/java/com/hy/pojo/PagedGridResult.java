package com.hy.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 
 * @Title: PagedGridResult.java
 * @Package com.hy.utils
 * @Description: 用来返回分页Grid的数据格式
 * Copyright: Copyright (c) 2019
 */
@Data
@Builder
@ApiModel(value = "分页结果", description = "分页返回的结果")
public class PagedGridResult {
	@ApiModelProperty(value = "当前页数", name = "pageNo", dataType = "int",  required = true)
	private int pageNo;			// 当前页数
	@ApiModelProperty(value = "总页数", name = "pages", dataType = "long", required = true)
	private long pages;			// 总页数
	@ApiModelProperty(value = "总记录数", name = "total", dataType = "long", required = true)
	private long total;		// 总记录数
	@ApiModelProperty(value = "每行显示的内容", name = "rows", dataType = "List",  required = true)
	private List<?> rows;		// 每行显示的内容

	private int code;
	private String msg;


}
