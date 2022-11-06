/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2022, The ATTOPARSER team (https://www.attoparser.org)
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
package org.attoparser.simple;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.attoparser.ParseException;
import org.attoparser.config.ParseConfiguration;


/*
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public class SimpleOutputMarkupHandlerTest extends TestCase {


    public void test() throws Exception {

        final ParseConfiguration xmlConfig = ParseConfiguration.xmlConfiguration();
        final ParseConfiguration xmlAutoCloseConfig = ParseConfiguration.xmlConfiguration();
        xmlAutoCloseConfig.setElementBalancing(ParseConfiguration.ElementBalancing.AUTO_CLOSE);
        final ParseConfiguration htmlConfig = ParseConfiguration.htmlConfiguration();


        check(xmlConfig, "<div>hello</div>", "<div>hello</div>");
        check(xmlConfig, "<div>\n<div>hello</div>\n</div>", "<div>\n<div>hello</div>\n</div>");

        check(htmlConfig, "<div>hello</div>", "<div>hello</div>");
        check(htmlConfig, "<div id=\"one\">\n<div id=\"two\">hello</div>\n</div>", "<div id=\"one\">\n<div id=\"two\">hello</div>\n</div>");
        check(htmlConfig, "<hr /><div>\n<div>hello</div>\n</div>", "<hr /><div>\n<div>hello</div>\n</div>");
        check(htmlConfig, "<div>\n<div>hello</div>\n<hr /><hr /></div>", "<div>\n<div>hello</div>\n<hr /><hr /></div>");

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


    private static void check(final ParseConfiguration configuration, final String input, final String expectedOutput) throws Exception {

        final ISimpleMarkupParser parser = new SimpleMarkupParser(configuration);
        final ISimpleMarkupHandler handler = new TestSimpleMarkupHandler();

        try {
            parser.parse(input, handler);
            assertTrue(true);
        } catch (ParseException e) {
            assertTrue(false);
        }

    }



    private static class TestSimpleMarkupHandler extends AbstractSimpleMarkupHandler {

        private final Map<String,String> names = new HashMap<String, String>();


        @Override
        public void handleStandaloneElement(final String elementName, final Map<String, String> attributes, final boolean minimized, final int line, final int col) throws ParseException {

            if (!this.names.containsKey(elementName)) {
                this.names.put(elementName, elementName);
            } else {
                final String mapElementName = this.names.get(elementName);
                if (mapElementName != elementName) {
                    throw new ParseException("Not same string: " + mapElementName);
                }
            }

            if (attributes != null) {
                for (final Map.Entry<String,String> entry : attributes.entrySet()) {
                    final String attributeName = entry.getKey();
                    if (!this.names.containsKey(attributeName)) {
                        this.names.put(attributeName, attributeName);
                    } else {
                        final String mapAttributeName = this.names.get(attributeName);
                        if (mapAttributeName != attributeName) {
                            throw new ParseException("Not same string: " + mapAttributeName);
                        }
                    }
                }
            }

        }

        @Override
        public void handleOpenElement(final String elementName, final Map<String, String> attributes, final int line, final int col) throws ParseException {

            if (!this.names.containsKey(elementName)) {
                this.names.put(elementName, elementName);
            } else {
                final String mapElementName = this.names.get(elementName);
                if (mapElementName != elementName) {
                    throw new ParseException("Not same string: " + mapElementName);
                }
            }

            if (attributes != null) {
                for (final Map.Entry<String,String> entry : attributes.entrySet()) {
                    final String attributeName = entry.getKey();
                    if (!this.names.containsKey(attributeName)) {
                        this.names.put(attributeName, attributeName);
                    } else {
                        final String mapAttributeName = this.names.get(attributeName);
                        if (mapAttributeName != attributeName) {
                            throw new ParseException("Not same string: " + mapAttributeName);
                        }
                    }
                }
            }

        }

        @Override
        public void handleCloseElement(final String elementName, final int line, final int col) throws ParseException {

            if (!this.names.containsKey(elementName)) {
                this.names.put(elementName, elementName);
            } else {
                final String mapElementName = this.names.get(elementName);
                if (mapElementName != elementName) {
                    throw new ParseException("Not same string: " + mapElementName);
                }
            }

        }

        @Override
        public void handleAutoCloseElement(final String elementName, final int line, final int col) throws ParseException {

            if (!this.names.containsKey(elementName)) {
                this.names.put(elementName, elementName);
            } else {
                final String mapElementName = this.names.get(elementName);
                if (mapElementName != elementName) {
                    throw new ParseException("Not same string: " + mapElementName);
                }
            }

        }

        @Override
        public void handleUnmatchedCloseElement(final String elementName, final int line, final int col) throws ParseException {
            super.handleUnmatchedCloseElement(elementName, line, col);
        }
    }



}
