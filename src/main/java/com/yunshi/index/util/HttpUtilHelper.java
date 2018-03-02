package com.yunshi.index.util;

import org.apache.commons.collections4.MapUtils;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtilHelper {
	/** 总连接数 */
	private static final int MAX_TOTAL = 100;
	/** 同路由的并发数 */
	private static final int MAX_PER_ROUTE = 100;
	/** 连接等待时长 */
	private static final int CONNECT_REQUEST_TIME_OUT = 1000;
	/** 连接超时 */
	private static final int CONNECT_TIME_OUT = 5000;
	/** 重试次数 */
	private static final int RETRY_COUNT = 5;

	private static final String HTTP = "http";
	private static final String HTTPS = "https";
	private static final String DEFAULT_CHARSET = "UTF-8";// 默认请求编码
	private static SSLContextBuilder builder = null;
	private static SSLConnectionSocketFactory sslSocketFactory = null;
	private static Registry<ConnectionSocketFactory> registry = null;
	private static PoolingHttpClientConnectionManager pollingConnectionManager = null;
	private static CloseableHttpClient httpClient = null;
	static {
		System.out.println("http连接池开始初始化...");
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// SSL配置开始
		try {
			builder = new SSLContextBuilder();
			// 全部信任 不做身份鉴定
			builder.loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			});
			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
			String[] arr = new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" };
			sslSocketFactory = new SSLConnectionSocketFactory(builder.build(), arr, null, hostnameVerifier);
			PlainConnectionSocketFactory plainConnectionSocketFactory = new PlainConnectionSocketFactory();
			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.<ConnectionSocketFactory> create();
			registry = registryBuilder.register(HTTP, plainConnectionSocketFactory).register(HTTPS, sslSocketFactory).build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// SSL配置结束
		// 创建池
		RequestConfig config = RequestConfig.custom().setConnectTimeout(CONNECT_TIME_OUT).setConnectionRequestTimeout(CONNECT_REQUEST_TIME_OUT).build();
		pollingConnectionManager = new PoolingHttpClientConnectionManager(registry);
		pollingConnectionManager.setMaxTotal(MAX_TOTAL); // 总连接数
		pollingConnectionManager.setDefaultMaxPerRoute(MAX_PER_ROUTE);// 同路由的并发数
		httpClientBuilder.setSSLSocketFactory(sslSocketFactory);
		httpClientBuilder.setConnectionManager(pollingConnectionManager);
		httpClientBuilder.setDefaultRequestConfig(config);
		httpClientBuilder.setConnectionManagerShared(true);
		httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(RETRY_COUNT, true));// 重试次数
		httpClient = httpClientBuilder.build();
		System.out.println("http连接池初始化成功！");
	}

	/**
	 * http,https的post请求方法
	 * 
	 * @param url
	 *            访问地址
	 * @param header
	 *            http头信息
	 * @param param
	 *            提交的参数
	 * @param entity
	 *            设置实体 优先级高
	 * @param config
	 *            设置连接超时时间和获取数据超时时间,setConnectTimeout：设置连接超时时间，单位毫秒
	 *            setConnectionRequestTimeout：设置从connect Manager获取Connection
	 *            超时时间，单位毫秒 setSocketTimeout：请求获取数据的超时时间，单位毫秒
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, String> header, Map<String, Object> param, HttpEntity entity, RequestConfig config) throws Exception {
		String result = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			// 设置头信息
			if (MapUtils.isNotEmpty(header)) {
				for (Entry<String, String> entry : header.entrySet()) {
					httpPost.addHeader(entry.getKey(), entry.getValue());
				}
			}
			// 设置请求参数
			if (MapUtils.isNotEmpty(param)) {
				List<NameValuePair> formparams = new ArrayList<NameValuePair>();
				for (Entry<String, Object> entry : param.entrySet()) {
					// 给参数赋值
					formparams.add(new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue())));
				}
				UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
				httpPost.setEntity(urlEncodedFormEntity);
			}
			// 设置实体 优先级高
			if (entity != null) {
				httpPost.setEntity(entity);
			}
			if (config != null) {
				httpPost.setConfig(config);
			}
			HttpResponse httpResponse = httpClient.execute(httpPost);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				HttpEntity resEntity = httpResponse.getEntity();
				result = EntityUtils.toString(resEntity);
			} else {
				result = readHttpResponse(httpResponse);
			}
		} catch (Exception e) {
			throw e;
		}
		return result;
	}

	/**
	 * http,https的post请求方法,带超时设置
	 *
	 * @param url
	 *            访问地址
	 * @param header
	 *            http头信息
	 * @param param
	 *            提交的参数
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, String> header, Map<String, Object> param) throws Exception {
		RequestConfig config = RequestConfig.custom().setSocketTimeout(CONNECT_TIME_OUT).build();
		return post(url, header, param, null, config);
	}

	/**
	 * http,https的post请求方法，带超时和header设置
	 *
	 * @param url
	 *            访问地址
	 * @param param
	 *            提交的参数
	 * @return
	 * @throws Exception
	 */
	public static String post(String url, Map<String, Object> param) throws Exception {
		RequestConfig config = RequestConfig.custom().setSocketTimeout(CONNECT_TIME_OUT).build();
		Map<String, String> header = new HashMap<String, String>();
		header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36");
		header.put("Accept-Language", "zh-cn,zh;q=0.5");
		header.put("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.7");
		header.put("Connection", "keep-alive");
		return post(url, header, param, null, config);
	}

	/**
	 * 执行HttpPost请求
	 *
	 * @param url
	 *            请求的远程地址
	 * @param paramsObj
	 *            提交的参数信息，目前支持Map,和String(JSON\xml)
	 * @param reffer
	 *            reffer信息，可传null
	 * @param cookie
	 *            cookies信息，可传null
	 * @param charset
	 *            请求编码，默认UTF8
	 * @return
	 * @throws IOException
	 * @throws ParseException
	 * @throws ClientProtocolException
	 */
	public static String executePost(String url, Object paramsObj, String reffer, String cookie, String charset) throws IOException, ParseException {
		CloseableHttpResponse httpResponse = null;
		try {
			HttpPost post = new HttpPost(url);
			if (cookie != null && !"".equals(cookie)) {
				post.setHeader("Cookie", cookie);
			}
			if (reffer != null && !"".equals(reffer)) {
				post.setHeader("Reffer", reffer);
			}
			charset = getCharset(charset);
			// 设置参数
			HttpEntity httpEntity = getEntity(paramsObj, charset);
			if (httpEntity != null) {
				post.setEntity(httpEntity);
			}
			httpResponse = httpClient.execute(post);
			return getResult(httpResponse, charset);
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	/**
	 * 执行HttpGet请求
	 *
	 * @param url
	 *            请求的远程地址
	 * @param reffer
	 *            reffer信息，可传null
	 * @param cookie
	 *            cookies信息，可传null
	 * @param charset
	 *            请求编码，默认UTF8
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static String executeGet(String url, String reffer, String cookie, String charset) throws IOException, ParseException {
		CloseableHttpResponse httpResponse = null;
		try {
			HttpGet get = new HttpGet(url);
			if (cookie != null && !"".equals(cookie)) {
				get.setHeader("Cookie", cookie);
			}
			if (reffer != null && !"".equals(reffer)) {
				get.setHeader("Reffer", reffer);
			}
			charset = getCharset(charset);
			httpResponse = httpClient.execute(get);
			return getResult(httpResponse, charset);
		} finally {
			if (httpResponse != null) {
				try {
					httpResponse.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取请求的
	 *
	 * @param paramsObj
	 * @param charset
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static HttpEntity getEntity(Object paramsObj, String charset) throws UnsupportedEncodingException {
		if (paramsObj == null) {
			System.out.println("当前未传入参数信息，无法生成HttpEntity");
			return null;
		}
		if (Map.class.isInstance(paramsObj)) {// 当前是map数据
			@SuppressWarnings("unchecked")
			Map<String, String> paramsMap = (Map<String, String>) paramsObj;
			List<NameValuePair> list = getNameValuePairs(paramsMap);
			UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(list, charset);
			httpEntity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
			return httpEntity;
		} else if (String.class.isInstance(paramsObj)) {// 当前是string对象，可能是
			String paramsStr = paramsObj.toString();
			StringEntity httpEntity = new StringEntity(paramsStr, charset);
			if (paramsStr.startsWith("{")) {
				httpEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
			} else if (paramsStr.startsWith("<")) {
				httpEntity.setContentType(ContentType.APPLICATION_XML.getMimeType());
			} else {
				httpEntity.setContentType(ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
			}
			return httpEntity;
		} else {
			System.out.println("当前传入参数不能识别类型，无法生成HttpEntity");
		}
		return null;
	}

	/**
	 * 将map类型参数转化为NameValuePair集合方式
	 *
	 * @param paramsMap
	 * @return
	 */
	private static List<NameValuePair> getNameValuePairs(Map<String, String> paramsMap) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (paramsMap == null || paramsMap.isEmpty()) {
			return list;
		}
		for (Entry<String, String> entry : paramsMap.entrySet()) {
			list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return list;
	}

	/**
	 * 转化请求编码
	 *
	 * @param charset
	 * @return
	 */
	private static String getCharset(String charset) {
		return charset == null ? DEFAULT_CHARSET : charset;
	}

	/**
	 * 从结果中获取出String数据
	 *
	 * @param httpResponse
	 * @param charset
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private static String getResult(CloseableHttpResponse httpResponse, String charset) throws ParseException, IOException {
		String result = null;
		if (httpResponse == null) {
			return result;
		}
		HttpEntity entity = httpResponse.getEntity();
		if (entity == null) {
			return result;
		}
		result = EntityUtils.toString(entity, charset);
		EntityUtils.consume(entity);// 关闭应该关闭的资源，适当的释放资源 ;也可以把底层的流给关闭了
		return result;
	}

	private static String readHttpResponse(HttpResponse httpResponse) throws ParseException, IOException {
		StringBuilder builder = new StringBuilder();
		// 获取响应消息实体
		HttpEntity entity = httpResponse.getEntity();
		// 响应状态
		builder.append("status:" + httpResponse.getStatusLine());
		builder.append("headers:");
		HeaderIterator iterator = httpResponse.headerIterator();
		while (iterator.hasNext()) {
			builder.append("\t" + iterator.next());
		}
		// 判断响应实体是否为空
		if (entity != null) {
			String responseString = EntityUtils.toString(entity);
			builder.append("response length:" + responseString.length());
			builder.append("response content:" + responseString.replace("\r\n", ""));
		}
		return builder.toString();
	}

	public static void main(String[] args) throws Exception {
		String strUrl = "https://mp.toutiao.com/open/new_article_post/";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("access_token", "7c3bf56343b0020f56a3f82e83c44e3d0011");
		map.put("client_key", "c99a2c18eda0bd4e");
		map.put("title", "最好的食材——绿皮冰糖橙 专属你的“青橙之恋”");
		map.put("content", "滴答滴答");
		map.put("abstract", "挑剔、极致、高要求、完美主义.....");
		map.put("save", "0");
		map.put("article_tag", "云视新闻");
		StringBuffer sb = new StringBuffer();
		sb.append("access_token=7c3bf56343b0020f56a3f82e83c44e3d0011");
		sb.append("&client_key=c99a2c18eda0bd4e");
		sb.append("&title=最好的食材——绿皮冰糖橙 专属你的“青橙之恋”");
		sb.append("&content=滴答滴答");
		sb.append("&abstract=挑剔、极致、高要求、完美主义.....");
		sb.append("&save=0");
		sb.append("&article_tag=云视新闻");
		System.out.println(executePost(strUrl, map, null, null, null));
		System.out.println(executePost(strUrl, sb.toString(), null, null, null));

	}

}
