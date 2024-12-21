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
public class TextOutputMarkupHandlerTest {


    @Test
    public void test() throws Exception {

        final ParseConfiguration xmlConfig = ParseConfiguration.xmlConfiguration();
        final ParseConfiguration xmlAutoCloseConfig = ParseConfiguration.xmlConfiguration();
        xmlAutoCloseConfig.setElementBalancing(ParseConfiguration.ElementBalancing.AUTO_CLOSE);
        final ParseConfiguration htmlConfig = ParseConfiguration.htmlConfiguration();


        check(xmlConfig, "<div>hello</div>", "hello");
        check(xmlConfig, "<div>\n<div>hello</div>\n</div>", "\nhello\n");

        check(htmlConfig, "<div>hello</div>", "hello");
        check(htmlConfig, "<div>\n<div>hello</div>\n</div>", "\nhello\n");

        check(htmlConfig, "<ul><li>hello<li>goodbye</ul>", "hellogoodbye");
        check(htmlConfig, "<ul><li>hello<li>goodbye", "hellogoodbye");
        check(xmlAutoCloseConfig, "<ul><li>hello<li>goodbye</ul>", "hellogoodbye");
        check(xmlAutoCloseConfig, "<ul><li>hello<li>goodbye", "hellogoodbye");

        check(xmlConfig, "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "\n\nhello");
        check(htmlConfig, "<!DOCTYPE html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "\n\nhello");
        check(htmlConfig, "<!doctype html PUBLIC \"hello\" \"goodbye\">\n\n<html>hello</html>", "\n\nhello");

        check(xmlConfig, "one<!-- hello! -->two", "onetwo");
        check(htmlConfig, "one<!-- hello! -->two", "onetwo");

    }


    private static void check(final ParseConfiguration configuration, final String input, final String expectedOutput) throws Exception {

        final Writer writer = new StringWriter();

        final IMarkupParser parser = new MarkupParser(configuration);
        final TextOutputMarkupHandler handler = new TextOutputMarkupHandler(writer);

        parser.parse(input, handler);

        final String output = writer.toString();

        Assertions.assertEquals(expectedOutput, output);

    }



}
