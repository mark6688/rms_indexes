package com.yunshi.index.util;

import com.fasterxml.jackson.core.JsonParseException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;

/**
 * StringUtil工具类
 * @version： v1.0
 * @author huangaigang
 * @date 2015-11-29 13:28:27
 */
public class StringUtil {

	private static final Logger logger = Logger.getLogger(StringUtil.class);

	/**
	 * 生成32位UUID
	 * 
	 * @return
	 */
	public static String randomUUID() {
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.toUpperCase();
		uuid = uuid.replaceAll("-", "");
		return uuid;
	}

	/**
	 * 字节数组转为字符串
	 *
	 * 该方法默认以ISO-8859-1转码
	 * 若想自己指定字符集,可以使用<code>getString(byte[] data, String charset)</code>方法
	 */
	public static String getString(byte[] data) {
		return getString(data, "ISO-8859-1");
	}

	/**
	 * 字节数组转为字符串
	 *
	 * 如果系统不支持所传入的<code>charset</code>字符集,则按照系统默认字符集进行转换
	 */
	public static String getString(byte[] data, String charset) {
		if (isEmpty(data)) {
			return "";
		}
		if (isEmpty(charset)) {
			return new String(data);
		}
		try {
			return new String(data, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error("将byte数组[" + data + "]转为String时发生异常:系统不支持该字符集[" + charset + "]");
			return new String(data);
		}
	}

	/**
	 * 获取一个字符串的简明效果
	 *
	 * @return String 返回的字符串格式类似于"abcd***hijk"
	 */
	public static String getStringSimple(String data) {
		return data.substring(0, 4) + "***" + data.substring(data.length() - 4);
	}

	/**
	 * 字符串转为字节数组
	 *
	 * 该方法默认以ISO-8859-1转码
	 * 若想自己指定字符集,可以使用<code>getBytes(String str, String charset)</code>方法
	 */
	public static byte[] getBytes(String data) {
		return getBytes(data, "ISO-8859-1");
	}

	/**
	 * 字符串转为字节数组
	 *
	 * 如果系统不支持所传入的<code>charset</code>字符集,则按照系统默认字符集进行转换
	 */
	public static byte[] getBytes(String data, String charset) {
		data = (data == null ? "" : data);
		if (isEmpty(charset)) {
			return data.getBytes();
		}
		try {
			return data.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			logger.error("将字符串[" + data + "]转为byte[]时发生异常:系统不支持该字符集[" + charset + "]" + e.getMessage());
			return data.getBytes();
		}
	}

	/**
	 * 字符编码
	 *
	 * 该方法默认会以UTF-8编码字符串
	 * 若想自己指定字符集,可以使用<code>encode(String chinese, String charset)</code>方法
	 */
	public static String encode(String chinese) {
		return encode(chinese, "UTF-8");
	}

	/**
	 * 字符编码
	 *
	 * 该方法通常用于对中文进行编码
	 * 若系统不支持指定的编码字符集,则直接将<code>chinese</code>原样返回
	 */
	public static String encode(String chinese, String charset) {
		chinese = (chinese == null ? "" : chinese);
		try {
			return URLEncoder.encode(chinese, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error("编码字符串[" + chinese + "]时发生异常:系统不支持该字符集[" + charset + "]" + e.getMessage());
			return chinese;
		}
	}

	/**
	 * 字符解码
	 *
	 * 该方法默认会以UTF-8解码字符串
	 * 若想自己指定字符集,可以使用<code>decode(String chinese, String charset)</code>方法
	 */
	public static String decode(String chinese) {
		return decode(chinese, "UTF-8");
	}

	/**
	 * 字符解码
	 *
	 * 该方法通常用于对中文进行解码
	 * 若系统不支持指定的解码字符集,则直接将<code>chinese</code>原样返回
	 */
	public static String decode(String chinese, String charset) {
		chinese = (chinese == null ? "" : chinese);
		try {
			return URLDecoder.decode(chinese, charset);
		} catch (UnsupportedEncodingException e) {
			logger.error("解码字符串[" + chinese + "]时发生异常:系统不支持该字符集[" + charset + "]" + e.getMessage());
			return chinese;
		}
	}

	/**
	 * 字符串右补空格
	 *
	 * 该方法默认采用空格(其ASCII码为32)来右补字符
	 * 若想自己指定所补字符,可以使用
	 *      <code>rightPadForByte(String str, int size, int padStrByASCII)</code>
	 *      方法
	 */
	public static String rightPadForByte(String str, int size) {
		return rightPadForByte(str, size, 32);
	}

	/**
	 * 字符串右补字符
	 *
	 * 若str对应的byte[]长度不小于size,则按照size截取str对应的byte[],而非原样返回str
	 * 所以size参数很关键..事实上之所以这么处理,是由于支付处理系统接口文档规定了字段的最大长度
	 * 若对普通字符串进行右补字符,建议org.apache.commons.lang.StringUtils.rightPad(...)
	 * @param size
	 *            该参数指的不是字符串长度,而是字符串所对应的byte[]长度
	 * @param padStrByASCII
	 *            该值为所补字符的ASCII码,如32表示空格,48表示0,64表示@等
	 */
	public static String rightPadForByte(String str, int size, int padStrByASCII) {
		byte[] srcByte = str.getBytes();
		byte[] destByte = null;
		if (srcByte.length >= size) {
			// 由于size不大于原数组长度,故该方法此时会按照size自动截取,它会在数组右侧填充'(byte)0'以使其具有指定的长度
			destByte = Arrays.copyOf(srcByte, size);
		} else {
			destByte = Arrays.copyOf(srcByte, size);
			Arrays.fill(destByte, srcByte.length, size, (byte) padStrByASCII);
		}
		return new String(destByte);
	}

	/**
	 * 字符串左补空格
	 *
	 * 该方法默认采用空格(其ASCII码为32)来左补字符
	 * 若想自己指定所补字符,可以使用
	 *      <code>leftPadForByte(String str, int size, int padStrByASCII)</code>
	 *      方法
	 */
	public static String leftPadForByte(String str, int size) {
		return leftPadForByte(str, size, 32);
	}

	/**
	 * 字符串左补字符
	 *
	 * 若str对应的byte[]长度不小于size,则按照size截取str对应的byte[],而非原样返回str
	 * 所以size参数很关键..事实上之所以这么处理,是由于支付处理系统接口文档规定了字段的最大长度
	 * @param padStrByASCII
	 *            该值为所补字符的ASCII码,如32表示空格,48表示0,64表示@等
	 */
	public static String leftPadForByte(String str, int size, int padStrByASCII) {
		byte[] srcByte = str.getBytes();
		byte[] destByte = new byte[size];
		Arrays.fill(destByte, (byte) padStrByASCII);
		if (srcByte.length >= size) {
			System.arraycopy(srcByte, 0, destByte, 0, size);
		} else {
			System.arraycopy(srcByte, 0, destByte, size - srcByte.length, srcByte.length);
		}
		return new String(destByte);
	}

	/**
	 * HTML字符转义
	 *
	 * 对输入参数中的敏感字符进行过滤替换,防止用户利用JavaScript等方式输入恶意代码
	 * String input = <img src='http://t1.baidu.com/it/fm=0&gp=0.jpg'/>
	 * HtmlUtils.htmlEscape(input); //from spring.jar
	 * StringEscapeUtils.escapeHtml(input); //from commons-lang.jar
	 * 尽管Spring和Apache都提供了字符转义的方法,但Apache的StringEscapeUtils功能要更强大一些
	 * StringEscapeUtils提供了对HTML,Java,JavaScript,SQL,XML等字符的转义和反转义
	 * 但二者在转义HTML字符时,都不会对单引号和空格进行转义,而本方法则提供了对它们的转义
	 * @return String 过滤后的字符串
	 */
	public static String htmlEscape(String input) {
		if (isEmpty(input)) {
			return input;
		}
		input = input.replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll(" ", "&nbsp;");
		input = input.replaceAll("'", "&#39;"); // IE暂不支持单引号的实体名称,而支持单引号的实体编号,故单引号转义成实体编号,其它字符转义成实体名称
		input = input.replaceAll("\"", "&quot;"); // 双引号也需要转义，所以加一个斜线对其进行转义
		input = input.replaceAll("\n", "<br/>"); // 不能把\n的过滤放在前面，因为还要对<和>过滤，这样就会导致<br/>失效了
		return input;
	}

	/**
	 * 判断输入的对象是否为空
	 *
	 * @return boolean 空则返回true,非空则flase
	 */
	public static boolean isEmpty(Object obj) {
		return null == obj;
	}

	/**
	 * 判断输入的字节数组是否为空
	 *
	 * @return boolean 空则返回true,非空则flase
	 */
	public static boolean isEmpty(byte[] bytes) {
		return null == bytes || 0 == bytes.length;
	}

	/**
	 * 判断输入的字符串参数是否为空
	 *
	 * @return boolean 空则返回true,非空则flase
	 */
	public static boolean isEmpty(String input) {
		return null == input || 0 == input.length() || 0 == input.replaceAll("\\s", "").length() || "null".equals(input);
	}

	/**
	 * 判断字符串参数是否为空
	 *
	 * @return boolean 空则返回true,非空则flase
	 */
	public static boolean verificationParameters(Object... strFlag) {
		boolean retult = false;
		for (Object string : strFlag) {
			if (isEmpty(string)) {
				return true;
			}
		}
		return retult;
	}

	/**
	 * 把集合中的元素合并成字符串，用分隔符分割
	 *
	 * @param c
	 * @param delimiter
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String mergeCollection(Collection c, String delimiter) {
		StringBuffer temp = new StringBuffer();
		Iterator it = c.iterator();
		while (it.hasNext()) {
			if (temp.length() == 0)
				temp.append(it.next());
			else
				temp.append(delimiter + it.next());
		}
		return temp.toString();
	}

	/**
	 * 根据指定字符串分解原字符串并去重复后保存在有序的set集合里
	 *
	 * @param str
	 * @param delimiter
	 * @return
	 */
	public static Set<String> splitString(String str, String delimiter) {
		Set<String> set = new LinkedHashSet<String>();
		if (str != null && !isEmpty(str) && delimiter != null) {
			String[] ss = str.split(delimiter);
			for (String s : ss)
				set.add(s);
		}
		return set;
	}

	/**
	 * 把数组中的元素合并成字符串，用分隔符分割
	 *
	 * @param s
	 * @param delimiter
	 * @return
	 */
	public static String mergeArray(Object[] s, String delimiter) {
		StringBuffer temp = new StringBuffer();
		if (s != null) {
			for (int i = 0; i < s.length; i++) {
				if (i == 0)
					temp.append(s[i]);
				else
					temp.append(delimiter + s[i]);
			}
		}
		return temp.toString();
	}

	/**
	 * object转string
	 *
	 * @param obj
	 * @return
	 */
	public static String obj2String(Object obj) {
		String str = "";
		if (null != obj && !"".equals(obj)) {
			str = obj.toString();
		}
		return str;
	}

	/**
	 * object转Integer
	 *
	 * @param obj
	 * @return
	 */
	public static Integer obj2Integer(Object obj) {
		Integer intObj = null;
		if (null != obj) {
			String strObj = String.valueOf(obj);
			if (!isEmpty(strObj)) {
				intObj = Integer.valueOf(strObj);
			}
		}
		return intObj;
	}

	/**
	 * string转long
	 *
	 * @param str
	 * @return
	 */
	public static Long str2Long(String str) {
		return Long.parseLong(str);
	}

	/**
	 * 把浮点转换为字符串数组
	 *
	 * @param obj
	 * @param start
	 * @param end
	 * @param precise
	 * @return
	 */
	public static String[] toStringArray(Object[] obj, int start, int end, int precise) {
		String[] rst = null;
		if (obj != null) {
			if (start < 0)
				start = 0;
			if (end < 0 || end > obj.length)
				end = obj.length;
			rst = new String[end - start];
			for (int i = start; i < end; i++) {
				if (obj[i] != null) {
					if (obj[i] instanceof Float) {
						rst[i - start] = String.format("%3." + precise + "f", ((Float) obj[i]).floatValue());
					} else {
						if (obj[i] instanceof Double)
							rst[i - start] = String.format("%3." + precise + "f", ((Double) obj[i]).doubleValue());
						else
							rst[i - start] = obj[i].toString();
					}
				}
			}
		}
		return rst;
	}

	/**
	 * 统计字符串出现的个数
	 *
	 * @param str
	 * @param subStr
	 * @return
	 */
	public static int countCharInString(String str, String subStr) {
		if (str != null && subStr != null) {
			int count = 0;
			int indexOf = str.indexOf(subStr);
			while (indexOf > -1) {
				count++;
				if (indexOf < str.length() - 1) {
					str = str.substring(indexOf + 1);
					indexOf = str.indexOf(subStr);
				} else
					break;
			}
			return count;
		}
		return 0;
	}

	/**
	 * 将以逗号分隔的字符串转换成list集合
	 *
	 * @param ids
	 * @return
	 */
	public static List<Long> convertIds2LongList(String ids) {
		List<Long> list = new ArrayList<Long>();
		if (!isEmpty(ids)) {
			String[] split = ids.split(",");
			for (String s : split) {
				try {
					list.add(Long.valueOf(s));
				} catch (NumberFormatException e) {
					logger.error("convert ids fail , this id = " + s);
				}
			}
		}
		return list;
	}

	/**
	 * 将以逗号分隔的字符串转换成list集合
	 *
	 * @param ids
	 * @return
	 */
	public static List<Integer> convertIds2IntList(String ids) {
		List<Integer> list = new ArrayList<Integer>();
		if (!isEmpty(ids)) {
			String[] split = ids.split(",");
			for (String s : split) {
				try {
					list.add(Integer.valueOf(s));
				} catch (NumberFormatException e) {
					logger.error("convert ids fail , this id = " + s);
				}
			}
		}
		return list;
	}

	/**
	 * 将以逗号分隔的字符串转换成list集合
	 *
	 * @param str
	 * @return
	 */
	public static List<String> convertStrs2List(String str) {
		List<String> list = new ArrayList<String>();
		if (!isEmpty(str)) {
			String[] split = str.split(",");
			for (String s : split) {
				list.add(s);
			}
		}
		return list;
	}

	/**
	 * 浮点数组转化为字符串
	 *
	 * @param values
	 * @param format
	 * @param delimiter
	 * @return
	 */
	public static String toString(float[] values, String format, String delimiter) {
		if (values != null) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < values.length; i++) {
				if (i > 0)
					sb.append(delimiter);
				sb.append(String.format(format, values[i]));
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * 字符串转化为浮动数组
	 *
	 * @param s
	 * @param delimiter
	 * @return
	 */
	public static float[] toFloat(String s, String delimiter) {
		float[] f = null;
		if (s != null) {
			String[] vs = s.split(delimiter);
			f = new float[vs.length];
			for (int i = 0; i < vs.length; i++) {
				try {
					f[i] = Float.parseFloat(vs[i]);
				} catch (Exception e) {
					logger.error(" 字符串转化为浮动数组失败:" + e.getMessage());
				}
			}
		}
		return f;
	}

	/**
	 * 将字符串转换为Unicode编码
	 *
	 * @param s
	 * @return
	 */
	public static String toUnicode(String s) {
		String ss = saveConvert(s, true);
		return ss;
	}

	private static String saveConvert(String theString, boolean escapeSpace) {
		int len = theString.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);
		for (int x = 0; x < len; x++) {
			char aChar = theString.charAt(x);
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case ' ':
				if (x == 0 || escapeSpace)
					outBuffer.append('\\');
				outBuffer.append(' ');
				break;
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			case '=': // Fall through
			case ':': // Fall through
			case '#': // Fall through
			case '!':
				outBuffer.append('\\');
				outBuffer.append(aChar);
				break;
			default:
				if ((aChar < 0x0020) || (aChar > 0x007e)) {
					outBuffer.append('\\');
					outBuffer.append('u');
					outBuffer.append(toHex((aChar >> 12) & 0xF));
					outBuffer.append(toHex((aChar >> 8) & 0xF));
					outBuffer.append(toHex((aChar >> 4) & 0xF));
					outBuffer.append(toHex(aChar & 0xF));
				} else {
					outBuffer.append(aChar);
				}
			}
		}
		return outBuffer.toString();
	}

	private static char toHex(int nibble) {
		return hexDigit[(nibble & 0xF)];
	}

	private static final char[] hexDigit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 过滤掉html标签
	 *
	 * @param inputString
	 * @return
	 */
	public static String HtmlText(String inputString) {
		if (inputString == null || inputString.length() == 0)
			return "";
		// 先替换回车
		inputString = inputString.replaceAll("<br>", "\r\n").replaceAll("<br/>", "\r\n").replaceAll("<BR/>", "\r\n").replaceAll("<BR>", "\r\n").replaceAll("<Br>", "\r\n")
				.replaceAll("<Br/>", "\r\n").replaceAll("<bR>", "\r\n").replaceAll("<bR/>", "\r\n");
		String htmlStr = inputString.trim(); // 含html标签的字符串
		Pattern p_script;
		java.util.regex.Matcher m_script;
		Pattern p_style;
		java.util.regex.Matcher m_style;
		Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>
																										// }
			String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
																									// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			/* 空格 —— */
			// p_html = Pattern.compile("\\ ", Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = htmlStr.replaceAll(" ", " ");

			htmlStr = htmlStr.replaceAll("&nbsp;", " ");

			htmlStr = htmlStr.replaceAll("\"", "");

			htmlStr = htmlStr.replaceAll("'", "");

		} catch (Exception e) {
			logger.error("过滤掉html标签失败：" + e.getMessage());
		}
		return htmlStr;
	}

	/**
	 * 判断字符串是否整型
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			logger.error("判断字符串是否整型失败:" + e.getMessage());
			return false;
		}
	}

	/**
	 * Encode a string using algorithm specified in web.xml and return the
	 * resulting encrypted password. If exception, the plain credentials string
	 * is returned
	 * 
	 * @Title: encodePassword
	 * @param: password
	 * @param: algorithm
	 * @return: String
	 * @throws
	 * @author huangaigang
	 * @Date 2014年5月13日9:09:42
	 */
	public static String encodePassword(String password, String algorithm) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;

		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			return password;
		}
		md.reset();
		md.update(unencodedPassword);
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return buf.toString();
	}

	/**
	 * html 代码转成 字符串
	 * 
	 * @param htmlcode
	 */
	public static String testhtmlEscape(String htmlcode) {
		String value = HtmlUtils.htmlEscape(htmlcode);
		return value;
	}

	/**
	 * 过滤特殊字符 %，+
	 * 
	 * @param str
	 * @return
	 */
	public static String url2HexString(String str) {
		return str.replace("%", "%25").replace("+", "%2B").replace("\r", "\\r").replace("\n", "\\n");
	}
	
	/**
	 * 移除字符串中的字符串
	 * @param str
	 * @param remove
	 * @author huangaigang
	 * @Date 2015-4-7 17:43:33
	 * @return
	 */
	public static String remove(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove))
			return str;
		else
			return replace(str, remove, "", -1);
	}
	
	public static String replace(String text, String searchString, String replacement, int max) {
		if (isEmpty(text) || isEmpty(searchString) || replacement == null || max == 0)
			return text;
		int start = 0;
		int end = text.indexOf(searchString, start);
		if (end == -1)
			return text;
		int replLength = searchString.length();
		int increase = replacement.length() - replLength;
		increase = increase >= 0 ? increase : 0;
		increase *= max >= 0 ? max <= 64 ? max : 64 : 16;
		StringBuilder buf = new StringBuilder(text.length() + increase);
		do {
			if (end == -1)
				break;
			buf.append(text.substring(start, end)).append(replacement);
			start = end + replLength;
			if (--max == 0)
				break;
			end = text.indexOf(searchString, start);
		} while (true);
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * 将数字格式化，一位数字前面补0
	 * @param i
	 * @return
	 */
	public static String formatNum(int i){
		String result = "";
		if(i<10){
			result+="0"+i;
		}else{
			result+=i;
		}
		return result;
	}

	/**
	 * 将json转换为map
	 * @param json
	 * @return
	 */
	public static Map<String,Object> readJSON2Map(String json){
		Map<String,Object> resultMap  = null;
		if(null != json && !"".equals(json)){
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				resultMap = objectMapper.readValue(json, Map.class);
			} catch (JsonParseException e) {
				e.getMessage();
			} catch (JsonMappingException e) {
				e.getMessage();
			} catch (IOException e) {
				e.getMessage();
			}
		}
		return resultMap;
	}


}
