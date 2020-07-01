package com.jt;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClient {
	/**
     * 1.实例化httpClient对象
     * 2.确定请求url地址
     * 3.定义请求方式类型  get post put delete
     * 4.利用api发起http请求,获取响应结果
     * 5.判断返回值是否正确   校验状态码.
     * 6.如果返回值状态码正确的(200),则动态获取返回值信息
     */
	@Autowired
	private CloseableHttpClient client;
	@Test
	public void test() throws ClientProtocolException, IOException {
		//定义httpClient实例
		//CloseableHttpClient httpClient =HttpClients.createDefault();
		//定义访问IP
		String url = "https://www.baidu.com";
		//设定请求 
		HttpGet httpget = new HttpGet(url);
		//获取response对象
		CloseableHttpResponse response =client.execute(httpget);
		if(response.getStatusLine().getStatusCode() == 200){
			System.out.println("获取请成功");
			//获取HTML
			String  html = EntityUtils.toString(response.getEntity());
			System.out.println(html);
		}else {
			throw new RuntimeException();
		}
		
	}
	@Autowired
	private HttpClientService httpClientService;
	@Test
	public void test01() {
		String url = "https://www.baidu.com";
		String result =httpClientService.doGet(url);
		System.out.println("结果:"+result);
	}
	
}
