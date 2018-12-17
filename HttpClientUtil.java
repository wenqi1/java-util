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
 * HttpClient�����࣬����get��post��put��delete����
 * @author wenqi
 *
 */
public class HttpClientUtils {
	// ����ɹرյ�HttpClient�ͻ��˶���
	private CloseableHttpClient httpClient = HttpClients.createDefault();
	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * ����get����
	 * @param url  �����ַ
	 * @param params  �������
	 * @return  ��Ӧ����
	 */
	public String sendGet(String url, Map<String, String> params) {
		//����ɹرյ���Ӧ���� 
		CloseableHttpResponse response = null;
		try {
			//����URI 
			URIBuilder builder = new URIBuilder(url);
			//�ж��Ƿ���Ҫ����������� 
			if (params != null && params.size() > 0) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					//�����������
					builder.addParameter(entry.getKey(), entry.getValue());
				}
			}
			//����HttpGet�������
			HttpGet httpGet = new HttpGet(builder.build());
			//ִ�����󣬵õ���Ӧ���� 
			response = httpClient.execute(httpGet);
			//��ȡ��Ӧ����
			String content = (response.getEntity() != null) ? 
					EntityUtils.toString(response.getEntity(), Consts.UTF_8): null;
			//������Ӧ����
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
	 * ����post����
	 * @param url  �����ַ
	 * @param params  �������
	 * @param json  ��������ύ�ĸ�ʽ��json/����
	 * @return  ��Ӧ����
	 */
	public String sendPost(String url, Map<String, String> params, boolean json){
		//����ɹرյ���Ӧ���� 
		CloseableHttpResponse response = null;
		try{
			//����URI
			URI uri = new URIBuilder(url).build();
			//����HttpPost�������
			HttpPost httpPost = new HttpPost(uri);
			//�ж��Ƿ���Ҫ�����������
			if (params != null && params.size() > 0){
				//��json��ʽ�ύ�������
				if (json){
					httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(params),
							ContentType.APPLICATION_JSON));
				//�ñ���ʽ�ύ�������
				}else{
					//����List���Ϸ�װ��������� 
					List<NameValuePair> nvpLists = new ArrayList<>();
					for (Map.Entry<String, String> entry : params.entrySet()){
						//����������� 
						nvpLists.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
					//����������� 
					httpPost.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			//ִ�����󣬵õ���Ӧ����
			response = httpClient.execute(httpPost);
			//��ȡ��Ӧ����
			String content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			//������Ӧ����
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
	 * ����put����
	 * @param url  �����ַ
	 * @param params  �������
	 * @param json  ��������ύ�ĸ�ʽ��json/����
	 * @return  ��Ӧ����
	 */
	public String sendPut(String url, Map<String, String> params, boolean json){
		//����ɹرյ���Ӧ���� */
		CloseableHttpResponse response = null;
		try{
			//����URI
			URI uri = new URIBuilder(url).build();
			//����HttpPut�������
			HttpPut httpPut = new HttpPut(uri);
			//�ж��Ƿ���Ҫ�����������
			if (params != null && params.size() > 0){
				//��json��ʽ�ύ�������
				if (json){
					httpPut.setEntity(new StringEntity(objectMapper.writeValueAsString(params),
							ContentType.APPLICATION_JSON));
				//�ñ���ʽ�ύ�������
				}else{
					// ����List���Ϸ�װ���������
					List<NameValuePair> nvpLists = new ArrayList<>();
					for (Map.Entry<String, String> entry : params.entrySet()){
						// �����������
						nvpLists.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
					}
					// ����������� 
					httpPut.setEntity(new UrlEncodedFormEntity(nvpLists, Consts.UTF_8));
				}
			}
			//ִ�����󣬵õ���Ӧ���� 
			response = httpClient.execute(httpPut);
			//��ȡ��Ӧ����
			String content = (response.getEntity() != null) 
					? EntityUtils.toString(response.getEntity(), Consts.UTF_8) : null;
			//������Ӧ����
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
	 * ����delete����
	 * @param url  �����ַ
	 * @param params  �������
	 * @return ��Ӧ����
	 */
	public String sendDelete(String url, Map<String, String> params){
		/** �ж��������Map���� */
		if (params == null){
			params = new HashMap<>();
		}
		params.put("_method", "delete");
		return sendPost(url, params, false);
	}

}
