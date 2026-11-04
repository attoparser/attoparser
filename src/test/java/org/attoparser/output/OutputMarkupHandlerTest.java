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
package org.attoparser.output;

import java.io.StringWriter;
import java.io.Writer;

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
public class OutputMarkupHandlerTest {


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

        check(htmlConfig, "<ul><li>hello<li>goodbye</ul>", "<ul><li>hello<li>goodbye</ul>");
        check(htmlConfig, "<ul><li>hello<li>goodbye", "<ul><li>hello<li>goodbye");
        check(xmlAutoCloseConfig, "<ul><li>hello<li>goodbye</ul>", "<ul><li>hello<li>goodbye</ul>");
        check(xmlAutoCloseConfig, "<ul><li>hello<li>goodbye", "<ul><li>hello<li>goodbye");

        check(xmlConfig, "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>");
        check(htmlConfig, "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>");
        check(htmlConfig, "<!doctype html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "<!doctype html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>");

        check(xmlConfig, "one<!-- hello! -->two", "one<!-- hello! -->two");
        check(htmlConfig, "one<!-- hello! -->two", "one<!-- hello! -->two");

    }


    /*
     * OutputMarkupHandler must reconstruct the original markup character-for-character. This exercises
     * that round-trip over a wide variety of constructs: attributes (double/single/unquoted, boolean,
     * entity-bearing), namespaced names, CDATA sections, processing instructions, XML declarations,
     * DOCTYPEs, comments (incl. multi-line), void/standalone elements, raw-text elements (script/style),
     * and assorted whitespace. For every case the expected output is the input itself.
     */
    @Test
    public void testRoundTripVariety() throws Exception {

        final ParseConfiguration xmlConfig = ParseConfiguration.xmlConfiguration();
        final ParseConfiguration htmlConfig = ParseConfiguration.htmlConfiguration();

        // XML-mode round-trips.
        checkRoundTrip(xmlConfig, "<a b=\"1\" c=\"2\">x</a>");
        checkRoundTrip(xmlConfig, "<a b=\"1\"/>");
        checkRoundTrip(xmlConfig, "<ns:a xmlns:ns=\"http://x\">y</ns:a>");
        checkRoundTrip(xmlConfig, "<a>&amp;&lt;&gt;</a>");              // entities are not unescaped
        checkRoundTrip(xmlConfig, "<a><![CDATA[ <b> & ]]></a>");       // CDATA kept verbatim
        checkRoundTrip(xmlConfig, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a>x</a>");
        checkRoundTrip(xmlConfig, "<?target instruction?>");           // processing instruction
        checkRoundTrip(xmlConfig, "<!-- a comment -->");
        checkRoundTrip(xmlConfig, "<a><!-- inside --></a>");
        checkRoundTrip(xmlConfig, "<a\n  b=\"1\"\n  c=\"2\"\n>x</a>"); // whitespace within a tag
        checkRoundTrip(xmlConfig, "<a   >x</a>");
        checkRoundTrip(xmlConfig, "<a b=\"x&amp;y\">z</a>");
        checkRoundTrip(xmlConfig, "<!DOCTYPE html SYSTEM \"foo.dtd\">\n<html/>");
        checkRoundTrip(xmlConfig, "<!-- multi\nline\ncomment -->");
        checkRoundTrip(xmlConfig, "<a></a >");                         // whitespace in a close tag
        checkRoundTrip(xmlConfig, "<a/><b/><c/>");

        // HTML-mode round-trips.
        checkRoundTrip(htmlConfig, "<div class=\"a\" id=\"b\">x</div>");
        checkRoundTrip(htmlConfig, "<input type=\"text\" disabled>");  // void element, boolean attribute
        checkRoundTrip(htmlConfig, "<img src=\"a.png\" alt=\"x\">");
        checkRoundTrip(htmlConfig, "<a href='single'>y</a>");          // single-quoted attribute
        checkRoundTrip(htmlConfig, "<a data-x=unquoted>y</a>");        // unquoted attribute
        checkRoundTrip(htmlConfig, "<SELECT><OPTION>a</OPTION></SELECT>");
        checkRoundTrip(htmlConfig, "<p>&nbsp;&copy;</p>");
        checkRoundTrip(htmlConfig, "<script>var x = 1 < 2;</script>"); // raw-text: '<' inside is not a tag
        checkRoundTrip(htmlConfig, "<style>a{color:red}</style>");
        checkRoundTrip(htmlConfig, "<p>a<br>b</p>");

    }


    private static void checkRoundTrip(final ParseConfiguration configuration, final String input) throws Exception {
        check(configuration, input, input);
    }


    private static void check(final ParseConfiguration configuration, final String input, final String expectedOutput) throws Exception {

        final Writer writer = new StringWriter();

        final IMarkupParser parser = new MarkupParser(configuration);
        final OutputMarkupHandler handler = new OutputMarkupHandler(writer);

        parser.parse(input, handler);

        final String output = writer.toString();

        Assertions.assertEquals(expectedOutput, output);

    }



}
