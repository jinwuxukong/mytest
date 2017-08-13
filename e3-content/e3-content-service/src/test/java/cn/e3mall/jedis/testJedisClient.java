package cn.e3mall.jedis;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.jedis.JedisClient;
import redis.clients.jedis.JedisCluster;

public class testJedisClient {
	
	@Test
	public void JedisClientTest(){
		//初始化spring容器
		ApplicationContext applicationContext 
					= new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		//从容器中获得JedisClient对象
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		//使用JedisClient查询数据
		String string = jedisClient.get("mytest");
		//打印结果
		System.out.println(string);
	}

}
