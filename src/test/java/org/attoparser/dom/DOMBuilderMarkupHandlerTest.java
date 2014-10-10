/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2014, The ATTOPARSER team (http://www.attoparser.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
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

import junit.framework.TestCase;
import org.attoparser.IMarkupParser;
import org.attoparser.MarkupParser;
import org.attoparser.config.ParseConfiguration;
import org.attoparser.dom.DOMBuilderMarkupHandler;
import org.attoparser.dom.DOMWriter;
import org.attoparser.dom.Document;


/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public class DOMBuilderMarkupHandlerTest extends TestCase {


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


    private static void check(final ParseConfiguration configuration, final String input, final String expectedOutput) throws Exception {

        final IMarkupParser parser = new MarkupParser(configuration);
        final DOMBuilderMarkupHandler handler = new DOMBuilderMarkupHandler("test");

        parser.parse(input, handler);

        final Document doc = handler.getDocument();

        final StringWriter writer = new StringWriter();

        DOMWriter.write(doc, writer);

        final String output = writer.toString();

        assertEquals(expectedOutput, output);

    }



}
