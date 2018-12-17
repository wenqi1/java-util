package com.newisest.balancetong.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newisest.balancetong.exception.XRException;

/**
 * HttpClient工具类，发送get，post，put，delete请求。
 * @author wenqi
 *
 */
public class HttpClientUtils {
	// 定义可关闭的HttpClient客户端对象
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 发送get请求
	 * @param url  请求地址
	 * @param params  请求参数
	 * @return  响应数据
	 */
	public String sendGet(String url, Map<String, String> params) {
		//定义可关闭的响应对象 
		CloseableHttpResponse response = null;
		try {
			//创建URI 
			URIBuilder builder = new URIBuilder(url);
			//判断是否需要设置请求参数 
			if (params != null && params.size() > 0) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					//设置请求参数
					builder.addParameter(entry.getKey(), entry.getValue());
				}
			}
			//创建HttpGet请求对象
			HttpGet httpGet = new HttpGet(builder.build());
			//执行请求，得到响应对象 
			response = httpClient.execute(httpGet);
			//获取响应数据
			String content = (response.getEntity() != null) ? 
					EntityUtils.toString(response.getEntity(), Consts.UTF_8): null;
			//返回响应数据
			return content;
		} catch (Exception ex) {
			throw new XRException("0004");
		} finally {
			try {
				if (response != null)
					response.close();
			} catch (Exception ex) {
			}
		}
	}
	
	/**
	 * 发送post请求
	 * @param url  请求地址
	 * @param params  请求参数
	 * @param json  请求参数提交的格式（json/表单）
	 * @return  响应数据
	 */
	public String sendPost(String url, Map<String, String> params, boolean json){
		//定义可关闭的响应对象 
		CloseableHttpResponse response = null;
		try{
			//创建URI
			URI uri = new URIBuilder(url).build();
			//创建HttpPost请求对象
			HttpPost httpPost = new HttpPost(uri);
			//判断是否需要设置请求参数
			if (params != null && params.size() > 0){
				//用json格式提交请求参数
				if (json){
					httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(params),
							ContentType.APPLICATION_JSON));
				//用表单格式提交请求参数
				}else{
					//定义List集合封装表单请求参数 
					List<NameValuePair> nvpLists = new ArrayList<>();
					for (Map.Entry<String, String> entry : params.entrySet()){
						//设置请求参数 
						nvpLists.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
					//设置请求参数 
					httpPost.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			//执行请求，得到响应对象
			response = httpClient.execute(httpPost);
			//获取响应数据
			String content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			//返回响应数据
			return content;
		}catch(Exception ex){
			throw new XRException("0004");
		}finally{
			try{
				if (response != null) response.close();
			}catch(Exception ex){}
		}
	}
	
	
	/**
	 * 发送put请求
	 * @param url  请求地址
	 * @param params  请求参数
	 * @param json  请求参数提交的格式（json/表单）
	 * @return  响应数据
	 */
	public String sendPut(String url, Map<String, String> params, boolean json){
		//定义可关闭的响应对象 */
		CloseableHttpResponse response = null;
		try{
			//创建URI
			URI uri = new URIBuilder(url).build();
			//创建HttpPut请求对象
			HttpPut httpPut = new HttpPut(uri);
			//判断是否需要设置请求参数
			if (params != null && params.size() > 0){
				//用json格式提交请求参数
				if (json){
					httpPut.setEntity(new StringEntity(objectMapper.writeValueAsString(params),
							ContentType.APPLICATION_JSON));
				//用表单格式提交请求参数
				}else{
					// 定义List集合封装表单请求参数
					List<NameValuePair> nvpLists = new ArrayList<>();
					for (Map.Entry<String, String> entry : params.entrySet()){
						// 设置请求参数
						nvpLists.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
					// 设置请求参数 
					httpPut.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			//执行请求，得到响应对象 
			response = httpClient.execute(httpPut);
			//获取响应数据
			String content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			//返回响应数据
			return content;
		}catch(Exception ex){
			throw new XRException("0004");
		}finally{
			try{
				if (response != null) response.close();
			}catch(Exception ex){}
		}
	}

	/**
	 * 发送delete请求
	 * @param url  请求地址
	 * @param params  请求参数
	 * @return 响应数据
	 */
	public String sendDelete(String url, Map<String, String> params){
		/** 判断请求参数Map集合 */
		if (params == null){
			params = new HashMap<>();
		}
		params.put("_method", "delete");
		return sendPost(url, params, false);
	}

}
