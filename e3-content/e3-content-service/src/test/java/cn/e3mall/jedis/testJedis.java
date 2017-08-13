package cn.e3mall.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class testJedis {
	
	@Test
	public void JedisTest() throws Exception{
		// 1、把jedis相关的jar包添加到工程中。
		// 2、创建一个Jedis对象，构造方法，两个参数host、port
		Jedis jedis = new Jedis("192.168.169.130",6379);
		// 3、直接使用Jedis对象操作redis，对应每个redis命令都有一个同名方法。
		jedis.set("mytest", "1234");
		String string = jedis.get("mytest");
		System.out.println(string);
		// 4、关闭连接
		jedis.close();
	}
	
	@Test
	public void JedisPool() throws Exception{
		// 1、创建一个JedisPool对象，构造方法，两个参数host、port
		JedisPool jedisPool = new JedisPool("192.168.169.130", 6379);
		// 2、从链接池获得一个链接Jedis对象
		Jedis resource = jedisPool.getResource();
		// 3、使用Jedis对象
		resource.set("test", "给我100000元我什么都干");
		// 4、打印结果
		String string = resource.get("test");
		System.out.println(string);
		// 5、关闭连接
		resource.close();
		// 6、关闭连接池
		jedisPool.close();
	}
	
	@Test
	public void testJedisCluster() throws Exception{
		//1.创建一个JedisClusterdui对象，构造方法参数只有一个nodes是一个set，set中有多个HostAndPort对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.169.130", 7001));
		nodes.add(new HostAndPort("192.168.169.130", 7002));
		nodes.add(new HostAndPort("192.168.169.130", 7003));
		nodes.add(new HostAndPort("192.168.169.130", 7004));
		nodes.add(new HostAndPort("192.168.169.130", 7005));
		nodes.add(new HostAndPort("192.168.169.130", 7006));
		JedisCluster jedisCluster = new JedisCluster(nodes);
		//2.直接使用JedisCluster对象管理Jedis
		jedisCluster.set("mytest2", "1000");
		String string = jedisCluster.get("mytest");
		//3.打印结果
		System.out.println(string);
		//4.关闭JedisCluster(在系统中可以单例)
		jedisCluster.close();
	}
}
