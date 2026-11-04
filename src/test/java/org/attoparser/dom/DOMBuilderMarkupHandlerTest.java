/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2026 Attoparser (https://www.attoparser.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       https://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.attoparser.dom;

import java.io.StringWriter;

import org.attoparser.IMarkupParser;
import org.attoparser.MarkupParser;
import org.attoparser.config.ParseConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/*
 * 
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public class DOMBuilderMarkupHandlerTest {


    @Test
    public void test() throws Exception {

        final ParseConfiguration xmlConfig = ParseConfiguration.xmlConfiguration();
        final ParseConfiguration xmlAutoCloseConfig = ParseConfiguration.xmlConfiguration();
        xmlAutoCloseConfig.setElementBalancing(ParseConfiguration.ElementBalancing.AUTO_CLOSE);
        final ParseConfiguration htmlConfig = ParseConfiguration.htmlConfiguration();


        check(xmlConfig, "<div>hello</div>", "<div>hello</div>");
        check(xmlConfig, "<div>\n<div>hello</div>\n</div>", "<div>\n<div>hello</div>\n</div>");

        check(htmlConfig, "<div>hello</div>", "<div>hello</div>");
        check(htmlConfig, "<div>\n<div>hello</div>\n</div>", "<div>\n<div>hello</div>\n</div>");

        check(htmlConfig, "<ul><li>hello<li>goodbye</ul>", "<ul><li>hello</li><li>goodbye</li></ul>");
        check(htmlConfig, "<ul><li>hello<li>goodbye", "<ul><li>hello</li><li>goodbye</li></ul>");
        check(xmlAutoCloseConfig, "<ul><li>hello<li>goodbye</ul>", "<ul><li>hello<li>goodbye</li></li></ul>");
        check(xmlAutoCloseConfig, "<ul><li>hello<li>goodbye", "<ul><li>hello<li>goodbye</li></li></ul>");

        check(xmlConfig, "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>");
        check(htmlConfig, "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>");
        check(htmlConfig, "<!doctype html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>");

        check(xmlConfig, "one<!-- hello! -->two", "one<!-- hello! -->two");
        check(htmlConfig, "one<!-- hello! -->two", "one<!-- hello! -->two");

    }


    /*
     * Broader coverage of the DOM build + serialize round-trip over attributes, namespaces, CDATA,
     * processing instructions, comments, element siblings and mixed content (XML), and over HTML
     * normalizations: void elements rendered self-closed, boolean attributes expanded to name="",
     * single quotes normalized to double, and optional/void tags auto-closed.
     */
    @Test
    public void testVariety() throws Exception {

        final ParseConfiguration xmlConfig = ParseConfiguration.xmlConfiguration();
        final ParseConfiguration htmlConfig = ParseConfiguration.htmlConfiguration();

        // XML: these all reconstruct exactly...
        check(xmlConfig, "<a b=\"1\" c=\"2\">x</a>",            "<a b=\"1\" c=\"2\">x</a>");
        check(xmlConfig, "<a b=\"1\"/>",                        "<a b=\"1\"/>");
        check(xmlConfig, "<ns:a xmlns:ns=\"http://x\">y</ns:a>", "<ns:a xmlns:ns=\"http://x\">y</ns:a>");
        check(xmlConfig, "<a>&amp;&lt;</a>",                    "<a>&amp;&lt;</a>");
        check(xmlConfig, "<root><?pi data?></root>",           "<root><?pi data?></root>");
        check(xmlConfig, "<a><!-- c --></a>",                  "<a><!-- c --></a>");
        check(xmlConfig, "<a><b/><c/></a>",                    "<a><b/><c/></a>");
        check(xmlConfig, "<a>t1<b>t2</b>t3</a>",               "<a>t1<b>t2</b>t3</a>");
        // CDATA sections keep their delimiters, so markup-like content inside stays literal text.
        check(xmlConfig, "<a><![CDATA[ <b> ]]></a>",           "<a><![CDATA[ <b> ]]></a>");
        check(xmlConfig, "<a>x<![CDATA[1 < 2 && 3]]>y</a>",    "<a>x<![CDATA[1 < 2 && 3]]>y</a>");
        check(xmlConfig, "<a><![CDATA[]]></a>",                "<a><![CDATA[]]></a>");
        check(htmlConfig, "<div><![CDATA[ <b> ]]></div>",      "<div><![CDATA[ <b> ]]></div>");

        // HTML normalizations applied on serialization.
        check(htmlConfig, "<div class=\"a\">x</div>",          "<div class=\"a\">x</div>");
        check(htmlConfig, "<img src=\"a\">",                   "<img src=\"a\"/>");              // void -> self-closed
        check(htmlConfig, "<input type=\"text\" disabled>",    "<input type=\"text\" disabled=\"\"/>"); // boolean attr
        check(htmlConfig, "<p>a<br>b</p>",                     "<p>a<br/>b</p>");
        check(htmlConfig, "<a href='single'>y</a>",            "<a href=\"single\">y</a>");      // quotes normalized
        check(htmlConfig, "<SELECT><OPTION>a</OPTION></SELECT>", "<SELECT><OPTION>a</OPTION></SELECT>");
        check(htmlConfig, "<table><tr><td>x</table>",          "<table><tr><td>x</td></tr></table>"); // auto-close

    }


    private static void check(final ParseConfiguration configuration, final String input, final String expectedOutput) throws Exception {

        final IMarkupParser parser = new MarkupParser(configuration);
        final DOMBuilderMarkupHandler handler = new DOMBuilderMarkupHandler("test");

        parser.parse(input, handler);

        final Document doc = handler.getDocument();

        final StringWriter writer = new StringWriter();

        DOMWriter.write(doc, writer);

        final String output = writer.toString();

        Assertions.assertEquals(expectedOutput, output);

    }



}
