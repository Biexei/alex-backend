package org.alex.platform.util;

import org.alex.platform.exception.ParseException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ParseUtil {
    /**
     * xpath解析
     * @param xmlContent
     * @param xpath
     * @return
     * @throws ParseException
     */
    public static List<String> parseXml(String xmlContent, String xpath) throws ParseException {
        List<String> result = new ArrayList<>();
        try {
            Document document = new SAXReader().read(new StringReader(xmlContent));
            List<Node> nodes = document.selectNodes(xpath);
            for (Node node : nodes) {
                result.add(node.getText());
            }
        } catch (DocumentException e) {
            throw new ParseException("xml格式错误");
        } catch (Exception e) {
            throw new ParseException("未找到该元素，请确保xpath无误");
        }
        return result;
    }
}
