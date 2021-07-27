package cn.sxt.server.core;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析Web.xml的处理器
 */
public class WebHandler extends DefaultHandler {
    private Entity entity;
    private Mapping mapping;
    private List<Entity> entityList;
    private List<Mapping> mappingList;
    private boolean isMapping;
    private String tag;

    public List<Entity> getEntityList() {
        return entityList;
    }

    public List<Mapping> getMappingList() {
        return mappingList;
    }

    @Override
    public void startDocument() throws SAXException {
        entityList = new ArrayList<>();
        mappingList = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        tag = qName;
        if (null != qName) {
            if ("servlet".equals( qName )) {
                entity = new Entity();
                isMapping = false;
            } else if ("servlet-mapping".equals( qName )) {
                mapping = new Mapping();
                isMapping = true;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String content = new String( ch, start, length );
        if (null != tag) {
            if (!isMapping) {
                if ("servlet-name".equals( tag )) {
                    entity.setName( content );
                } else if ("servlet-class".equals( tag )) {
                    entity.setClz( content );
                }
            } else {
                if ("servlet-name".equals( tag )) {
                    mapping.setName( content );
                } else if ("url-pattern".equals( tag )) {
                    mapping.addPattern( content );
                }
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (null != qName) {
            if ("servlet".equals( qName )) {
                entityList.add( entity );
            } else if ("servlet-mapping".equals( qName )) {
                mappingList.add( mapping );
            }
        }
        tag = null;
    }

    @Override
    public void endDocument() throws SAXException {

    }
}
