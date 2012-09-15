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
package org.attoparser;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.attoparser.content.IAttoContentHandler;
import org.attoparser.content.TracingAttoContentHandler;
import org.attoparser.exception.AttoParseException;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class MarkupAttoParserTest extends TestCase {

    

    public void test() throws Exception {


        testDoc( 
            "<p>Hello</p>",
            "[ES(p){1,1}T(Hello){1,4}EE(p){1,9}]");
        testDoc( 
            "Hello, World!",
            "[T(Hello, World!){1,1}]");
        testDoc( 
            "Hello, World!",
            "[T(ello, Worl){1,1}]",
            1, 10);
        testDoc( 
            "Hello, World!",
            "[T(e){1,1}]",
            1, 1);
        testDoc( 
            "Hello, <p>lala</p>",
            "[T(Hello, ){1,1}ES(p){1,8}T(lala){1,11}EE(p){1,15}]");
        testDoc( 
            "Hello, <p>lala</p>",
            "[T(o, ){1,1}ES(p){1,4}T(l){1,7}]",
            4, 7);
        testDoc( 
            "Hello, <p>lala</p>",
            "[T(o, ){1,1}T(<p){1,4}]",
            4, 5);
        testDoc( 
            "Hello, <br/>",
            "[T(Hello, ){1,1}EM(br){1,8}]");
        testDoc( 
            "Hello, <br th:text=\"ll\"/>",
            "[T(Hello, ){1,1}EM(br){1,8}A(th:text=\"ll\"){1,12}]");
        testDoc( 
            "Hello, <br  th:text=\"ll\"/>",
            "[T(Hello, ){1,1}EM(br){1,8}A(th:text=\"ll\"){1,13}]");
        testDoc( 
            "Hello, <br \nth:text=\"ll\"/>",
            "[T(Hello, ){1,1}EM(br){1,8}A(th:text=\"ll\"){2,1}]");
        testDoc( 
            "Hello, World! <br/>\n<div\n l\n     a=\"12 3\" zas    o=\"\"  b=\"lelo\n  = s\">lala</div> <p th=\"lala\" >liool</p>",
            "[T(Hello, World! ){1,1}EM(br){1,15}T(\n){1,20}ES(div){2,1}A(l=\"\"){3,2}A(a=\"12 3\"){4,6}A(zas=\"\"){4,15}A(o=\"\"){4,22}A(b=\"lelo\n  = s\"){4,28}T(lala){5,8}EE(div){5,12}T( ){5,18}ES(p){5,19}A(th=\"lala\"){5,22}T(liool){5,33}EE(p){5,38}]");
        testDoc( 
            "Hello, World! <br/>\n<div\n l\n \n\n    a=\"12 3\" zas    o=\"\"  b=\"lelo\n  = s\">lala</div> <p th=\"lala\" >liool</p>",
            "[T(Hello, World! ){1,1}EM(br){1,15}T(\n){1,20}ES(div){2,1}A(l=\"\"){3,2}A(a=\"12 3\"){6,5}A(zas=\"\"){6,14}A(o=\"\"){6,21}A(b=\"lelo\n  = s\"){6,27}T(lala){7,8}EE(div){7,12}T( ){7,18}ES(p){7,19}A(th=\"lala\"){7,22}T(liool){7,33}EE(p){7,38}]");
        testDoc( 
            "Hello, World! <br/>\n<div\n l\n \n\n    a=\"12 3\" zas    o=\"\"\nb=\"lelo\n  = s\">lala</div> <p th=\"lala\" >liool</p>",
            "[T(Hello, World! ){1,1}EM(br){1,15}T(\n){1,20}ES(div){2,1}A(l=\"\"){3,2}A(a=\"12 3\"){6,5}A(zas=\"\"){6,14}A(o=\"\"){6,21}A(b=\"lelo\n  = s\"){7,1}T(lala){8,8}EE(div){8,12}T( ){8,18}ES(p){8,19}A(th=\"lala\"){8,22}T(liool){8,33}EE(p){8,38}]");

        testDoc( 
            "Hello<!--hi!-->, <br/>",
            "[T(Hello){1,1}C(hi!){1,6}T(, ){1,16}EM(br){1,18}]");

        testDoc( 
            "Hello<!-- 4 > 3 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3 ){1,6}T(, ){1,20}EM(br){1,22}]");
        testDoc( 
            "Hello<!-- 4 > 3 > 10 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3 > 10 ){1,6}T(, ){1,25}EM(br){1,27}]");
        testDoc( 
            "Hello<!-- 4 > 3\n > 10 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3\n > 10 ){1,6}T(, ){2,10}EM(br){2,12}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n > 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n > 10 ){1,6}T(, ){2,10}EM(br){2,12}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3 > 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3 > 10 ){1,6}T(, ){1,30}EM(br){1,32}]");
        testDoc( 
                "Hello<!-- 4 > 3 > 10 -->, <br/>",
                "[T(Hello){1,1}C( 4 > 3 > 10 ){1,6}T(, ){1,25}EM(br){1,27}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n\n\n\n > 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n\n\n\n > 10 ){1,6}T(, ){5,10}EM(br){5,12}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n > 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n > 10 ){1,6}T(, ){5,10}EM(br){5,12}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n   \t> 10 ){1,6}T(, ){5,13}EM(br){5,15}]");


        
        
    }
    
    
    
    private static void testDoc(final String input, final String output) throws AttoParseException {
        testDoc(input, output, 0, input.length());
    }
    
    private static void testDoc(final String input, final String output, final int offset, final int len) throws AttoParseException {
        final MarkupAttoParser parser = new MarkupAttoParser();
        final StringWriter sw = new StringWriter(); 
        final IAttoContentHandler handler = new TracingAttoContentHandler(sw);
        if (offset == 0 && len == input.length()) {
            parser.parse(input, handler);
        } else { 
            parser.parse(input, offset, len, handler);
        }
        final String result = sw.toString();
        assertEquals(output, result);
    }
    
    
    
}
