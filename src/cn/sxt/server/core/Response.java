package cn.sxt.server.core;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 封装响应信息
 */
public class Response {
    private BufferedWriter bw;
    private StringBuilder responseInfo;
    private StringBuilder content;
    private int len;
    private final String BLANK = " ";
    private final String CRLF = "\r\n";

    public Response() {
        responseInfo = new StringBuilder();
        content = new StringBuilder();
        len = 0;
    }

    public Response(Socket client) throws IOException {
        this( client.getOutputStream() );
        try {
            bw = new BufferedWriter( new OutputStreamWriter( client.getOutputStream() ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Response(OutputStream os) {
        this();
        bw = new BufferedWriter( new OutputStreamWriter( os ) );
    }

    /**
     * 1.准备正文
     * 动态添加内容
     *
     * @param info
     * @return
     */
    public Response print(String info) {
        content.append( info );
        //2.获取字节数的长度
        len += info.getBytes().length;
        return this;
    }

    /**
     * 1.准备正文 + 回车
     * 动态添加内容
     *
     * @param info
     * @return
     */
    public Response println(String info) {
        content.append( info ).append( CRLF );
        //2.获取字节数的长度
        len += info.getBytes().length;
        return this;
    }

    /**
     * 3.构建头信息
     */
    private void creatHeadInfo(int code) {
        DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        responseInfo.append( "HTTP/1.1" ).append( BLANK ).append( code ).append( BLANK );
        switch (code) {
            case 200:
                responseInfo.append( "OK" ).append( CRLF );
                break;
            case 404:
                responseInfo.append( "NOT FOUND" ).append( CRLF );
                break;
            case 505:
                responseInfo.append( "SERVER ERROR" ).append( CRLF );
                break;
        }
        responseInfo.append( "Date:" ).append( formatter.format( new Date() ) ).append( CRLF );
        responseInfo.append( "Server:" ).append( "Colin-Chong Server/0.0.1;charset=GBK" ).append( CRLF );
        responseInfo.append( "Content-type:" ).append( "text/html" ).append( CRLF );
        responseInfo.append( "Content-length:" ).append( len ).append( CRLF );
        responseInfo.append( CRLF );
    }

    /**
     * 根据状态码，拼接响应信息，推送到客户端
     *
     * @param code
     * @throws IOException
     */
    public void pushToBrowser(int code) throws IOException {
        creatHeadInfo( code );
        responseInfo.append( content );
        bw.write( responseInfo.toString() );
        bw.flush();
    }
}
