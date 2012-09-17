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

import java.io.CharArrayReader;
import java.io.StringWriter;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

import org.attoparser.exception.AttoParseException;
import org.attoparser.markup.MarkupAttoParser;
import org.attoparser.markup.TracingMarkupAttoHandler;



/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public class AttoParserTest extends TestCase {

    private static int totalTestExecutions = 0;
    

    public void test() throws Exception {

        testDoc( 
            "Hello, <p>lala</p>",
            "[T(o, ){1,1}T(<p){1,4}]",
            4, 5);
        testDoc( 
            "Hello, World!",
            "[T(Hello, World!){1,1}]");
        testDoc( 
            "",
            "[]");
        testDoc( 
            "<p>Hello</p>",
            "[ES(p){1,1}T(Hello){1,4}EE(p){1,9}]");
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
            "Hello, <br/>",
            "[T(Hello, ){1,1}E(br){1,8}]");
        testDoc( 
            "Hello, <br th:text=\"ll\"/>",
            "[T(Hello, ){1,1}E(br){1,8}A(th:text=\"ll\"){1,12}]");
        testDoc( 
            "Hello, <br  th:text=\"ll\"/>",
            "[T(Hello, ){1,1}E(br){1,8}A(th:text=\"ll\"){1,13}]");
        testDoc( 
            "Hello, <br \nth:text=\"ll\"/>",
            "[T(Hello, ){1,1}E(br){1,8}A(th:text=\"ll\"){2,1}]");
        testDoc( 
            "Hello, World! <br/>\n<div\n l\n     a=\"12 3\" zas    o=\"\"  b=\"lelo\n  = s\">lala</div> <p th=\"lala\" >liool</p>",
            "[T(Hello, World! ){1,1}E(br){1,15}T(\n){1,20}ES(div){2,1}A(l=\"\"){3,2}A(a=\"12 3\"){4,6}A(zas=\"\"){4,15}A(o=\"\"){4,22}A(b=\"lelo\n  = s\"){4,28}T(lala){5,8}EE(div){5,12}T( ){5,18}ES(p){5,19}A(th=\"lala\"){5,22}T(liool){5,33}EE(p){5,38}]");
        testDoc( 
            "Hello, World! <br/>\n<div\n l\n \n\n    a=\"12 3\" zas    o=\"\"  b=\"lelo\n  = s\">lala</div> <p th=\"lala\" >liool</p>",
            "[T(Hello, World! ){1,1}E(br){1,15}T(\n){1,20}ES(div){2,1}A(l=\"\"){3,2}A(a=\"12 3\"){6,5}A(zas=\"\"){6,14}A(o=\"\"){6,21}A(b=\"lelo\n  = s\"){6,27}T(lala){7,8}EE(div){7,12}T( ){7,18}ES(p){7,19}A(th=\"lala\"){7,22}T(liool){7,33}EE(p){7,38}]");
        testDoc( 
            "Hello, World! <br/>\n<div\n l\n \n\n    a=\"12 3\" zas    o=\"\"\nb=\"lelo\n  = s\">lala</div> <p th=\"lala\" >liool</p>",
            "[T(Hello, World! ){1,1}E(br){1,15}T(\n){1,20}ES(div){2,1}A(l=\"\"){3,2}A(a=\"12 3\"){6,5}A(zas=\"\"){6,14}A(o=\"\"){6,21}A(b=\"lelo\n  = s\"){7,1}T(lala){8,8}EE(div){8,12}T( ){8,18}ES(p){8,19}A(th=\"lala\"){8,22}T(liool){8,33}EE(p){8,38}]");

        testDoc( 
            "Hello<!--hi!-->, <br/>",
            "[T(Hello){1,1}C(hi!){1,6}T(, ){1,16}E(br){1,18}]");

        testDoc( 
            "Hello<!-- 4 > 3 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3 ){1,6}T(, ){1,20}E(br){1,22}]");
        testDoc( 
            "Hello<!-- 4 > 3 > 10 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3 > 10 ){1,6}T(, ){1,25}E(br){1,27}]");
        testDoc( 
            "Hello<!-- 4 > 3\n > 10 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3\n > 10 ){1,6}T(, ){2,10}E(br){2,12}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n > 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n > 10 ){1,6}T(, ){2,10}E(br){2,12}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3 > 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3 > 10 ){1,6}T(, ){1,30}E(br){1,32}]");
        testDoc( 
                "Hello<!-- 4 > 3 > 10 -->, <br/>",
                "[T(Hello){1,1}C( 4 > 3 > 10 ){1,6}T(, ){1,25}E(br){1,27}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n\n\n\n > 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n\n\n\n > 10 ){1,6}T(, ){5,10}E(br){5,12}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n > 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n > 10 ){1,6}T(, ){5,10}E(br){5,12}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n   \t> 10 ){1,6}T(, ){5,13}E(br){5,15}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n   \t> 10 ){1,6}T(, ){5,13}E(br){5,15}" +
                "T(\nHello){5,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){6,6}T(, ){10,13}E(br){10,15}" +
                "T(\nHello){10,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){11,6}T(, ){15,13}E(br){15,15}" +
                "T(\nHello){15,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){16,6}T(, ){20,13}E(br){20,15}" +
                "T(\nHello){20,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){21,6}T(, ){25,13}E(br){25,15}]");
        testDoc( 
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
                "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>",
                "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n   \t> 10 ){1,6}T(, ){5,13}E(br){5,15}" +
                "T(\nHello){5,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){6,6}T(, ){10,13}E(br){10,15}" +
                "T(\nHello){10,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){11,6}T(, ){15,13}E(br){15,15}" +
                "T(\nHello){15,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){16,6}T(, ){20,13}E(br){20,15}" +
                "T(\nHello){20,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){21,6}T(, ){25,13}E(br){25,15}" +
                "T(\nHello){25,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){26,6}T(, ){30,13}E(br){30,15}" +
                "T(\nHello){30,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){31,6}T(, ){35,13}E(br){35,15}" +
                "T(\nHello){35,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){36,6}T(, ){40,13}E(br){40,15}" +
                "T(\nHello){40,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){41,6}T(, ){45,13}E(br){45,15}" +
                "T(\nHello){45,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){46,6}T(, ){50,13}E(br){50,15}" +
                "T(\nHello){50,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){51,6}T(, ){55,13}E(br){55,15}]");
        testDoc( 
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh",
                "[T(kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh){1,1}]");
        testDoc( 
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh",
                "[T(" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "){1,1}]");
        testDoc( 
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp;<p> aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\"</p> khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh",
                "[T(" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp;){1,1}ES(p){11,12}T( aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\"){11,15}EE(p){22,41}T( khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
                "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
                "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
                "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
                "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
                "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
                "){22,45}]");


        System.out.println("TOTAL Test executions: " + totalTestExecutions);
        
    }
    
    
    
    static void testDoc(final String input, final String output) throws AttoParseException {
        testDoc(input.toCharArray(), output, 0, input.length());
    }
    
    static void testDoc(String input, final String output, final int offset, final int len) throws AttoParseException {
        testDoc(input.toCharArray(), output, offset, len);
    }
    
    static void testDoc(final String input, final String output, final int bufferSize) throws AttoParseException {
        testDoc(input.toCharArray(), output, 0, input.length(), bufferSize);
    }
    
    static void testDoc(String input, final String output, final int offset, final int len, final int bufferSize) throws AttoParseException {
        testDoc(input.toCharArray(), output, offset, len, bufferSize);
    }
    
    static void testDoc(final char[] input, final String output, final int offset, final int len) throws AttoParseException {

        final int maxBufferSize = 16384;
        for (int bufferSize = 1; bufferSize < maxBufferSize; bufferSize++) {
            testDoc(input, output, offset, len, bufferSize);
        }
        
    }

    
    static void testDoc(final char[] input, final String output, final int offset, final int len, final int bufferSize) 
            throws AttoParseException {

        try {
            
            final StringWriter sw = new StringWriter(); 
            final IAttoHandler handler = new TracingMarkupAttoHandler(sw);
            final IAttoParser parser = new MarkupAttoParser();
            if (offset == 0 && len == input.length) {
                ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input), handler, bufferSize);
            } else { 
                ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input, offset, len), handler, bufferSize);
            }
            final String result = sw.toString();
            assertEquals(output, result);
            
            totalTestExecutions++;
            
        } catch (final ComparisonFailure cf) {
            System.err.println("Error parsing text \"" + new String(input, offset, len) + "\" with buffer size: " + bufferSize);
            throw cf;
        } catch (final Exception e) {
            throw new AttoParseException("Error parsing text \"" + new String(input, offset, len) + "\" with buffer size: " + bufferSize, e);
        }
        
    }
    
    
    
}
