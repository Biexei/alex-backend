package org.alex.platform;

import org.alex.platform.exception.BusinessException;
import org.alex.platform.exception.ParseException;
import org.alex.platform.util.ParseUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ParseTest {
    @Test
    public void testParseXml() throws ParseException {
        String xml = "<?xml version=\"1.0\" encoding=\"utf8\"?>\n" +
                " \n" +
                "<bookstore> \n" +
                "  <book> \n" +
                "    <title lang=\"USA\">Harry Potter1</title>  \n" +
                "    <author>J K. Rowling1</author>  \n" +
                "    <year name=\"1\">20051</year>  \n" +
                "    <price>1000</price> \n" +
                "  </book>  \n" +
                "  <book> \n" +
                "    <title lang=\"cn\">Harry Potter2</title>  \n" +
                "    <author>J K. Rowling2</author>  \n" +
                "    <year name=\"1\">20053</year>  \n" +
                "    <price>29.992</price> \n" +
                "  </book>  \n" +
                "  <book> \n" +
                "    <title lang=\"zn\">Harry Potter3</title>  \n" +
                "    <author>J K. Rowling3</author>  \n" +
                "    <year>20053</year>  \n" +
                "    <price>29.993</price> \n" +
                "  </book> \n" +
                "</bookstore>";
        String xpath = "//title[@lang='zn']";
        System.out.println(ParseUtil.parseXml(xml, xpath));
    }
}
