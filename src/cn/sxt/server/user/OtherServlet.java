package cn.sxt.server.user;

import cn.sxt.server.core.Request;
import cn.sxt.server.core.Response;
import cn.sxt.server.core.Servlet;

public class OtherServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        response.print( "<html>" );
        response.print( "<head>" );
        response.print( "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">" );
        response.print( "<title>" );
        response.print( "这是测试页面" );
        response.print( "</title>" );
        response.print( "</head>" );
        response.print( "<body>" );
        response.print( "Hello, " + request.getParameterValue( "uname" ) + " ,Colin-Chong Server is at your service.." );
        response.print( "</body>" );
        response.print( "</html>" );
    }
}
