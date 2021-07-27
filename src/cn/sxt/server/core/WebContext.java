package cn.sxt.server.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将web.xml的数据格式转成Map
 */
public class WebContext {
    private Map<String, String> entityMap = new HashMap<>();
    private Map<String, String> mappingMap = new HashMap<>();
    private List<Entity> entityList;
    private List<Mapping> mappingList;

    public WebContext(List<Entity> entityList, List<Mapping> mappingList) {
        this.entityList = entityList;
        this.mappingList = mappingList;

        for (Entity entity : entityList) {
            entityMap.put( entity.getName(), entity.getClz() );
        }

        for (Mapping mapping : mappingList) {
            for (String pattern : mapping.getPatterns()) {
                mappingMap.put( pattern, mapping.getName() );
            }
        }
    }

    public String getClz(String pattern) {
        String className = mappingMap.get( pattern );
        return entityMap.get( className );
    }
}
