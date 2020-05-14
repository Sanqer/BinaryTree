package org.binarytree.filesave;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Type;

public class XmlSerializer implements Serializer
{
    private static Logger log = LoggerFactory.getLogger(XmlSerializer.class.getSimpleName());
    @Override
    public <T> String serialize(T elem) {
        if (elem == null) return "";
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String res = "";
        try {
            res = xmlMapper.writeValueAsString(elem);
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
        }
        return res;
    }

    @Override
    public <T> T deserialize(String str, Type elementType) {
        if (StringUtils.isBlank(str)) return null;
        XmlMapper xmlMapper = new XmlMapper();
        T res = null;
        try {
            res = xmlMapper.readValue(str, xmlMapper.constructType(elementType));
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
            System.out.println(ex.getMessage());
        }
        return res;
    }
}
