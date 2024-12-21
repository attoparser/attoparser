/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2025 Attoparser (https://www.attoparser.org)
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
package org.attoparser.minimize;

import java.io.StringWriter;
import java.io.Writer;

import org.attoparser.IMarkupParser;
import org.attoparser.MarkupParser;
import org.attoparser.config.ParseConfiguration;
import org.attoparser.minimize.MinimizeHtmlMarkupHandler.MinimizeMode;
import org.attoparser.output.OutputMarkupHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.attoparser.minimize.MinimizeHtmlMarkupHandler.MinimizeMode.COMPLETE;
import static org.attoparser.minimize.MinimizeHtmlMarkupHandler.MinimizeMode.ONLY_WHITE_SPACE;

/*
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public class MinimizeHtmlMarkupHandlerTest {


    @Test
    public void test() throws Exception {

        final ParseConfiguration htmlConfig = ParseConfiguration.htmlConfiguration();


        check(htmlConfig, ONLY_WHITE_SPACE, "<div >hello</div >", "<div>hello</div>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<hr /><div >hello</div >", "<hr/><div>hello</div>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class=\"one\"  \n   id=\"two\">hello</div>", "<div class=\"one\" id=\"two\">hello</div>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class=\"one\"  \n   id=\"two\"  >hello</div>", "<div class=\"one\" id=\"two\">hello</div>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class=\"one two\"  \n   id=\"two\"  >hello</div>", "<div class=\"one two\" id=\"two\">hello</div>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class='one two'  \n   id='two'  >hello</div>", "<div class='one two' id='two'>hello</div>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class =   \"one\"  \n   id =\"two\"  >hello</div>", "<div class=\"one\" id=\"two\">hello</div>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class =   \"one,two\"  \n   id =\"two\"  >hello</div>", "<div class=\"one,two\" id=\"two\">hello</div>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class =   \"\" >", "<div class=\"\">");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class =   >", "<div class=>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class=>", "<div class=>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<div   class =   something >", "<div class=something>");
        check(htmlConfig, ONLY_WHITE_SPACE, "<!-- something -->hey", "<!-- something -->hey");
        check(htmlConfig, ONLY_WHITE_SPACE, "<!-- something --> hey", "<!-- something --> hey");
        check(htmlConfig, ONLY_WHITE_SPACE, "<!-- something --> hey    ja", "<!-- something --> hey ja");
        check(htmlConfig, ONLY_WHITE_SPACE, "<!-- something --> hey  \n ja", "<!-- something --> hey ja");
        check(htmlConfig, ONLY_WHITE_SPACE, " <!-- something --> hey", " <!-- something --> hey");
        check(htmlConfig, ONLY_WHITE_SPACE, " <!-- something --> hey    ja", " <!-- something --> hey ja");
        check(htmlConfig, ONLY_WHITE_SPACE, "\n <!-- something --> hey  \n  ja", " <!-- something --> hey ja");
        check(htmlConfig, ONLY_WHITE_SPACE, "<td>                 <td>OK", "<td> <td>OK");

        check(htmlConfig, COMPLETE, "<div >hello</div >", "<div>hello</div>");
        check(htmlConfig, COMPLETE, "<hr /><div >hello</div >", "<hr><div>hello</div>");
        check(htmlConfig, COMPLETE, "<div   class=\"one\"  \n   id=\"two\">hello</div>", "<div class=one id=two>hello</div>");
        check(htmlConfig, COMPLETE, "<div   class=\"one\"  \n   id=\"two\"  >hello</div>", "<div class=one id=two>hello</div>");
        check(htmlConfig, COMPLETE, "<div   class=\"one two\"  \n   id=\"two\"  >hello</div>", "<div class=\"one two\" id=two>hello</div>");
        check(htmlConfig, COMPLETE, "<div   class='one two'  \n   id='two'  >hello</div>", "<div class='one two' id=two>hello</div>");
        check(htmlConfig, COMPLETE, "<div   class =   \"one\"  \n   id =\"two\"  >hello</div>", "<div class=one id=two>hello</div>");
        check(htmlConfig, COMPLETE, "<div   class =   \"one,two\"  \n   id =\"two\"  >hello</div>", "<div class=\"one,two\" id=two>hello</div>");
        check(htmlConfig, COMPLETE, "<div   class =   \"\" >", "<div class=\"\">");
        check(htmlConfig, COMPLETE, "<div   class =   >", "<div class=>");
        check(htmlConfig, COMPLETE, "<div   class=>", "<div class=>");
        check(htmlConfig, COMPLETE, "<div   class =   something >", "<div class=something>");
        check(htmlConfig, COMPLETE, "<!-- something -->hey", "hey");
        check(htmlConfig, COMPLETE, "<!-- something --> hey", " hey");
        check(htmlConfig, COMPLETE, "<!-- something --> hey    ja", " hey ja");
        check(htmlConfig, COMPLETE, "<!-- something --> hey  \n ja", " hey ja");
        check(htmlConfig, COMPLETE, " <!-- something --> hey", " hey");
        check(htmlConfig, COMPLETE, " <!-- something --> hey    ja", " hey ja");
        check(htmlConfig, COMPLETE, "\n <!-- something --> hey  \n  ja", " hey ja");
        check(htmlConfig, COMPLETE, "<!DOCTYPE html>\n\n<html>", "<!DOCTYPE html> <html>");
        check(htmlConfig, COMPLETE, "<head>\n  <title> TIT </title>\n    <meta ></meta>\n    <meta />\n    <link>\n  </head>", "<head><title> TIT </title><meta></meta><meta><link></head>");
        check(htmlConfig, COMPLETE, "<p> Hello </p> \n   <p>Goodbye <p>!</p>  </p>", "<p> Hello </p><p>Goodbye <p>!</p></p>");
        check(htmlConfig, COMPLETE, "<ul>  <li> One  <li>Two<li>Three  </ul>", "<ul><li> One <li>Two<li>Three </ul>");
        check(htmlConfig, COMPLETE, "<table>\n<caption>A  \n<colgroup><col><col> <col>  <thead> <tr>\n<td>a<th>b    <td>c   <tr><th>a<td> <td>\n</table>", "<table><caption>A <colgroup><col><col><col><thead><tr><td>a<th>b <td>c <tr><th>a<td> <td> </table>");
        check(htmlConfig, COMPLETE, "<html>\n<head>\n</head>\n<body>\n\n</body>\n</html>", "<html><head> </head><body> </body></html>");
        check(htmlConfig, COMPLETE, "<html>\n<head>\n</head>\n<body>\na\n</body>\n</html>", "<html><head> </head><body> a </body></html>");
        check(htmlConfig, COMPLETE, "<option name=\"one\" selected />", "<option name=one selected>");
        check(htmlConfig, COMPLETE, "<option name=\"one\" selected=\"selected\" />", "<option name=one selected>");
        check(htmlConfig, COMPLETE, "<option name=\"one\" selected=selected />", "<option name=one selected>");
        check(htmlConfig, COMPLETE, "<option name=\"one\" ant=ant />", "<option name=one ant=ant>");
        check(htmlConfig, COMPLETE, "<option name=\"one\" ant=ant required=required disabled=disabled/>", "<option name=one ant=ant required disabled>");
        check(htmlConfig, COMPLETE, "<pre>  \n\nlala\n\n </pre>", "<pre>  \n\nlala\n\n </pre>");
        check(htmlConfig, COMPLETE, "<textarea>  \n\nlala\n\n </textarea>", "<textarea>  \n\nlala\n\n </textarea>");

    }


    private static void check(final ParseConfiguration configuration, final MinimizeMode minimizeMode, final String input, final String expectedOutput) throws Exception {

        final Writer writer = new StringWriter();

        final IMarkupParser parser = new MarkupParser(configuration);
        final MinimizeHtmlMarkupHandler handler = new MinimizeHtmlMarkupHandler(minimizeMode, new OutputMarkupHandler(writer));

        parser.parse(input, handler);

        final String output = writer.toString();

        Assertions.assertEquals(expectedOutput, output);

    }



}
