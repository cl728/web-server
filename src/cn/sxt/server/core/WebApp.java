package cn.sxt.server.core;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class WebApp {
    private Request request;
    private Response response;
    private static WebContext webContext;

    public WebApp(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    static {
        try {
            //1. 获取解析工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 2. 从解析工厂获取解析器
            SAXParser parser = factory.newSAXParser();
            // 3. 编写处理器(最重要)
            // 4. 加载文档Document注册处理器
            WebHandler webHandler = new WebHandler();
            //5. 解析
            parser.parse( Thread.currentThread().getContextClassLoader().getResourceAsStream( "cn/sxt/server/core/web.xml" ), webHandler );
            //6. 获取数据
            webContext = new WebContext( webHandler.getEntityList(), webHandler.getMappingList() );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过url找到了对应的Servlet
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static Servlet getServletFromUrl(String url) {
        String className = webContext.getClz( "/" + url );
        Class clz;
        try {
            if (null == className) {
                return null;
            }
            clz = Class.forName( className );
            if (null != clz) {
                return (Servlet) clz.getConstructor().newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
