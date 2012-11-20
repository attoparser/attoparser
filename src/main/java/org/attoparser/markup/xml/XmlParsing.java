/*
 * =============================================================================
 * 
 *   Copyright (c) 2012, The ATTOPARSER team (http://www.attoparser.org)
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
package org.attoparser.markup.xml;

import org.attoparser.markup.MarkupParsingConfiguration;
import org.attoparser.markup.MarkupParsingConfiguration.ElementBalancing;
import org.attoparser.markup.MarkupParsingConfiguration.PrologPresence;
import org.attoparser.markup.MarkupParsingConfiguration.UniqueRootElementPresence;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class XmlParsing {


    public static final MarkupParsingConfiguration XML_PARSING_CONFIGURATION;
    

    static {
        XML_PARSING_CONFIGURATION = new MarkupParsingConfiguration();
        XML_PARSING_CONFIGURATION.setElementBalancing(ElementBalancing.REQUIRE_BALANCED);
        XML_PARSING_CONFIGURATION.setRequireUniqueAttributesInElement(true);
        XML_PARSING_CONFIGURATION.setRequireXmlWellFormedAttributeValues(true);
        XML_PARSING_CONFIGURATION.getPrologParsingConfiguration().setValidateProlog(true);
        XML_PARSING_CONFIGURATION.getPrologParsingConfiguration().setPrologPresence(PrologPresence.ALLOWED);
        XML_PARSING_CONFIGURATION.getPrologParsingConfiguration().setXmlDeclarationPresence(PrologPresence.ALLOWED);
        XML_PARSING_CONFIGURATION.getPrologParsingConfiguration().setDoctypePresence(PrologPresence.ALLOWED);
        XML_PARSING_CONFIGURATION.setUniqueRootElementPresence(UniqueRootElementPresence.DEPENDS_ON_PROLOG_DOCTYPE);
    }


    
    private XmlParsing() {
        super();
    }
    
    
}