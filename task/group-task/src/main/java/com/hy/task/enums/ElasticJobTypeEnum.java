package com.hy.task.enums;
/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/16 13:27
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/16 13:27
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public enum ElasticJobTypeEnum {

	SIMPLE("SimpleJob", "简单类型job"),
	DATAFLOW("DataflowJob", "流式类型job"),
	SCRIPT("ScriptJob", "脚本类型job");
	
	private String type;
	
	private String desc;
	
	private ElasticJobTypeEnum(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
