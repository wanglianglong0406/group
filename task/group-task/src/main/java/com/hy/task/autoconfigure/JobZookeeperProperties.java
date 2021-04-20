package com.hy.task.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * @Description: $- -$ #-->
 * @Author: 寒夜
 * @CreateDate: 2020/12/16 13:27
 * @UpdateUser: 寒夜
 * @UpdateDate: 2020/12/16 13:27
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
@ConfigurationProperties(prefix = "elastic.job.zk")
@Data
public class JobZookeeperProperties {

	private String namespace;
	
	private String serverLists;
	
	private int maxRetries = 3;

	private int connectionTimeoutMilliseconds = 15000;
	
	private int sessionTimeoutMilliseconds = 60000;
	
	private int baseSleepTimeMilliseconds = 1000;
	
	private int maxSleepTimeMilliseconds = 3000;
	
	private String digest = "";
	
}
