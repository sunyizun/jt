package com.jt.aop;

import javax.management.RuntimeErrorException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

@Component//将对象交给spring容器管理
@Aspect//标识切面
public class RedisAspect {
	//容器初始化时不需要实例化该对象
	//只有用户使用时才初始化,一般工具类中添加该注解
	@Autowired(required = false)
	private JedisCluster jedis;
	//@Autowired(required = false)
	//private RedisService jedis;//引入哨兵工具类
	
	//private ShardedJedis jedis;
	//private Jedis jedis;
	@SuppressWarnings("unchecked")//压制警告
	@Around("@annotation(cache_find)")
	public Object around(ProceedingJoinPoint //连接点
    		jointPoint,Cache_Find cache_find) {
		//获取key的值
		String key=getKey(jointPoint,cache_find);
		//根据key查询缓存
		String result =jedis.get(key);
		Object data =null;
			try {
				if(StringUtils.isEmpty(result)){
					//如果为空,查询数据库
				data=jointPoint.proceed();
				String json = ObjectMapperUtil.toJSON(data);
				if(cache_find.secondes()==0)
					jedis.set(key,json );
				else
					jedis.setex(key, cache_find.secondes(), json);
				System.out.println("第一次查询");
				}else {
					Class targetClass = getClass(jointPoint);
					//表示有缓存
					data=ObjectMapperUtil.toObject(result, targetClass);
					System.out.println("业务查询缓存"); 
				}
			} catch (Throwable e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		
		return data;
		
	}
	private Class getClass(ProceedingJoinPoint jointPoint) {
		MethodSignature signature=(MethodSignature)jointPoint.getSignature();
		return signature.getReturnType();
	}
	/**
	 * 
	 * @param jointPoint
	 * @param cache_find
	 * @return
	 */
	
	private String getKey(ProceedingJoinPoint jointPoint, Cache_Find cache_find) {
		//1,获取key的类型
		KEY_ENUM key_ENUM =cache_find.keyType();
		//2判断key类型
		if(key_ENUM.equals(key_ENUM.EMPTY)) {
			//
			return cache_find.key();
		}
		String strArgs=String.valueOf(jointPoint.getArgs()[0]);
		String key =cache_find.key()+"_"+strArgs;

		return key;
	}

}
