package cn.sxt.server.user;

import cn.sxt.server.core.Request;
import cn.sxt.server.core.Response;
import cn.sxt.server.core.Servlet;

public class RegisterServlet implements Servlet {
    @Override
    public void service(Request request, Response response) {
        String uname = request.getParameterValue( "uname" );
        String[] values = request.getParameterValues( "fav" );
        response.print( "<html>" );
        response.print( "<head>" );
        response.print( "<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">" );
        response.print( "<title>" );
        response.print( "欢迎注册" );
        response.print( "</title>" );
        response.print( "</head>" );
        response.print( "<body>" );
        response.println( "你注册的信息为：" + uname );
        response.println( "你喜欢的许嵩的歌曲为：" );
        for (String value : values) {
            if ("0".equals( value )) {
                response.println( "《最佳歌手》" );
            } else if ("1".equals( value )) {
                response.println( "《宿敌》" );
            } else if ("2".equals( value )) {
                response.println( "《摄影艺术》" );
            }
        }
        response.print( "</body>" );
        response.print( "</html>" );
    }
}
