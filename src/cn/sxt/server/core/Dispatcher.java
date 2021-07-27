package cn.sxt.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Dispatcher implements Runnable {
    private Socket client;
    private Request request;
    private Response response;
    private Servlet servlet;

    public Dispatcher(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //获取请求协议
            request = new Request( client );
            //获取响应协议
            response = new Response( client );
            String url = request.getUrl();
            if (null == url || "".equals( url )) {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream( "index.html" );
                if (null != is) {
                    response.print( new String( is.readAllBytes() ) );
                    response.pushToBrowser( 200 );
                    is.close();
                }
                return;
            }
            servlet = WebApp.getServletFromUrl( url );
            if (null != servlet) {
                servlet.service( request, response );
                response.pushToBrowser( 200 );
            } else {
                InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream( "error.html" );
                if (null != is) {
                    response.print( new String( is.readAllBytes() ) );
                    response.pushToBrowser( 404 );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                response.println( "服务器正在维护中..." );
                response.pushToBrowser( 505 );
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        //短连接
        release();
    }

    public void release() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
