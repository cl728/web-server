package cn.sxt.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

/**
 * 封装请求信息
 */
public class Request {
    private String requestInfo;
    private InputStream is;
    private String method;
    private String url;
    private String parameter;
    private final String CRLF = "\r\n";
    private Map<String, List<String>> parameterMap = new HashMap<>();

    public Request() {
    }

    public Request(Socket client) {
        try {
            is = client.getInputStream();
            byte[] datas = new byte[1024 * 1024];
            int len = is.read( datas );
            requestInfo = new String( datas, 0, len );
            parseRequestInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseRequestInfo() {
        System.out.println( "--------- requestInfo解析开始 -----------" );
        method = requestInfo.substring( 0, requestInfo.indexOf( "/" ) ).trim();
        System.out.println( "请求方式为:" );
        System.out.println( method );

        int startIdx = requestInfo.indexOf( "/" ) + 1;
        int endIdx = requestInfo.indexOf( "HTTP/" );
        url = requestInfo.substring( startIdx, endIdx ).trim();
        int qStr = url.indexOf( "?" );
        if (qStr >= 0) {
            String[] urlArr = url.split( "\\?" );
            url = urlArr[0];
            parameter = urlArr[1];
        }
        if ("POST".equals( method )) {
            String str = requestInfo.substring( requestInfo.lastIndexOf( CRLF ) ).trim();
            if (null == parameter || parameter.length() == 0) {
                parameter = str;
            } else {
                parameter += "&" + str;
            }
        }
        if (parameter == null) {
            parameter = "";
        }
        System.out.println( "请求路径为:" );
        System.out.println( url );
        System.out.println( "请求参数为:" );
        System.out.println( decode( parameter, "UTF-8" ) );

        //将requestInfo转成Map格式 fav=1&fav=2&uname=vae&age=32&others=
        convertMap();
    }

    /**
     * 将请求参数以Map的格式存储到了parameterMap中
     */
    private void convertMap() {
        //分割字符串 &
        String[] keyValues = parameter.split( "&" );
        for (String keyValue : keyValues) {
            String[] kv = keyValue.split( "=" );
            kv = Arrays.copyOf( kv, 2 );
            String key = kv[0];
            String value = kv[1] == null ? "" : decode( kv[1], "UTF-8" );
            if (!parameterMap.containsKey( key )) {
                parameterMap.put( key, new ArrayList<>() );
            }
            parameterMap.get( key ).add( value );
        }
    }

    /**
     * 通过一个Key找到对应的多个value
     *
     * @param key
     * @return
     */
    public String[] getParameterValues(String key) {
        return parameterMap.get( key ).toArray( new String[0] );
    }

    /**
     * 通过一个key找到对应的一个value
     *
     * @param key
     * @return
     */
    public String getParameterValue(String key) {
        return getParameterValues( key )[0];
    }

    /**
     * 解决中文乱码问题
     *
     * @param str
     * @param enc
     * @return
     */
    private String decode(String str, String enc) {
        try {
            return URLDecoder.decode( str, enc );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getUrl() {
        return url;
    }
}
