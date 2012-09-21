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

import org.attoparser.markup.DuplicatingMarkupAttoHandler;
import org.attoparser.markup.DuplicatingMarkupBreakDownAttoHandler;
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

//        testDoc( 
//                "Hello, <p>lala</p><?xml version=\"1.0\"?>",
//                "[T(o, ){1,1}T(<p){1,4}]");
        testDoc( 
            "<h1>Hello</ h1>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello</ h1>){1,5}]");
        testDoc( 
            "Hello, World!",
            "[T(Hello, World!){1,1}]");
        testDoc( 
            "",
            "[]");
        testDoc( 
            "<p>Hello</p>",
            "[OES(<){1,1}OEN(p){1,2}OEE(>){1,3}T(Hello){1,4}CES(</){1,9}CEN(p){1,11}CEE(>){1,12}]");
        testDoc( 
            "<h1>Hello</h1>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}CEE(>){1,14}]");
        testDoc( 
            "<h1>Hello</h1 >",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}EW( ){1,14}CEE(>){1,15}]");
        testDoc( 
            "<h1>Hello</h1 \n\n>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}EW( \n\n){1,14}CEE(>){3,1}]");
        testDoc( 
            "<\np  >Hello</p>",
            "[T(<\np  >Hello){1,1}CES(</){2,10}CEN(p){2,12}CEE(>){2,13}]");
        testDoc( 
            "< h1  >Hello</h1>",
            "[T(< h1  >Hello){1,1}CES(</){1,13}CEN(h1){1,15}CEE(>){1,17}]");
        testDoc( 
            "<h1>Hello</ h1>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello</ h1>){1,5}]");
        testDoc( 
            "< h1>Hello</ h1>",
            "[T(< h1>Hello</ h1>){1,1}]");
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
            "[T(Hello, ){1,1}OES(<){1,8}OEN(p){1,9}OEE(>){1,10}T(lala){1,11}CES(</){1,15}CEN(p){1,17}CEE(>){1,18}]");
        testDoc( 
            "Hello, <p>lala</p>",
            "[T(o, ){1,1}OES(<){1,4}OEN(p){1,5}OEE(>){1,6}T(l){1,7}]",
            4, 7);
        testDoc( 
            "Hello, <br/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}SEE(/>){1,11}]");
        testDoc( 
            "Hello, <br th:text=\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}(=){1,19}(\"ll\"){1,20}SEE(/>){1,24}]");
        testDoc( 
            "Hello, <br th:text =\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( =){1,19}(\"ll\"){1,21}SEE(/>){1,25}]");
        testDoc( 
            "Hello, <br th:text =   \"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( =   ){1,19}(\"ll\"){1,24}SEE(/>){1,28}]");
        testDoc( 
            "Hello, <br th:text =   ll/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( =   ){1,19}(ll){1,24}SEE(/>){1,26}]");
        testDoc( 
            "Hello, <br th:text =   \"ll\"a=2/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( =   ){1,19}(\"ll\"){1,24}A(a){1,28}(=){1,29}(2){1,30}SEE(/>){1,31}]");
        testDoc( 
            "Hello, <br th:text =   \"ll\"a= \n 2/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( =   ){1,19}(\"ll\"){1,24}A(a){1,28}(= \n ){1,29}(2){2,2}SEE(/>){2,3}]");
        testDoc( 
            "Hello, <br th:text =   ll a= \n 2/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( =   ){1,19}(ll){1,24}EW( ){1,26}A(a){1,27}(= \n ){1,28}(2){2,2}SEE(/>){2,3}]");
        testDoc( 
            "Hello, <br th:text =   ll a/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( =   ){1,19}(ll){1,24}EW( ){1,26}A(a){1,27}(){1,28}(){1,28}SEE(/>){1,28}]");
        testDoc( 
            "Hello, <br th:text =   ll a=/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( =   ){1,19}(ll){1,24}EW( ){1,26}A(a){1,27}(=){1,28}(){1,29}SEE(/>){1,29}]");
        testDoc( 
            "Hello, <br th:text = a=/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( = ){1,19}(a=){1,22}SEE(/>){1,24}]");
        testDoc( 
            "Hello, <br th:text = a= b/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( = ){1,19}(a=){1,22}EW( ){1,24}A(b){1,25}(){1,26}(){1,26}SEE(/>){1,26}]");
        testDoc( 
            "Hello, <br th:text = a=b/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( = ){1,19}(a=b){1,22}SEE(/>){1,25}]");
        testDoc( 
            "Hello, <br th:text = \"a=b\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( = ){1,19}(\"a=b\"){1,22}SEE(/>){1,27}]");
        testDoc( 
            "Hello, <br th:text = \"a= b\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( = ){1,19}(\"a= b\"){1,22}SEE(/>){1,28}]");
        testDoc( 
            "Hello, <br th:text = \"a= b\"\n/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( = ){1,19}(\"a= b\"){1,22}EW(\n){1,28}SEE(/>){2,1}]");
        testDoc( 
            "Hello, <br  th:text=\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW(  ){1,11}A(th:text){1,13}(=){1,20}(\"ll\"){1,21}SEE(/>){1,25}]");
        testDoc( 
            "Hello, <br \nth:text=\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( \n){1,11}A(th:text){2,1}(=){2,8}(\"ll\"){2,9}SEE(/>){2,13}]");
        testDoc( 
            "Hello, World! <br/>\n<div\n l\n     a=\"12 3\" zas    o=\"\"  b=\"lelo\n  = s\">lala</div> <p th=\"lala\" >liool</p>",
            "[T(Hello, World! ){1,1}SES(<){1,15}SEN(br){1,16}SEE(/>){1,18}T(\n){1,20}" +
              "OES(<){2,1}OEN(div){2,2}EW(\n ){2,5}A(l){3,2}(){3,3}(){3,3}EW(\n     ){3,3}" +
              "A(a){4,6}(=){4,7}(\"12 3\"){4,8}EW( ){4,14}A(zas){4,15}(){4,18}(){4,18}EW(    ){4,18}" +
              "A(o){4,22}(=){4,23}(\"\"){4,24}EW(  ){4,26}A(b){4,28}(=){4,29}(\"lelo\n  = s\"){4,30}" +
              "OEE(>){5,7}T(lala){5,8}CES(</){5,12}CEN(div){5,14}CEE(>){5,17}T( ){5,18}" +
              "OES(<){5,19}OEN(p){5,20}EW( ){5,21}A(th){5,22}(=){5,24}(\"lala\"){5,25}EW( ){5,31}OEE(>){5,32}" +
              "T(liool){5,33}CES(</){5,38}CEN(p){5,40}CEE(>){5,41}]");

        testDoc( 
            "Hello<!--hi!-->, <br/>",
            "[T(Hello){1,1}C(hi!){1,6}T(, ){1,16}SES(<){1,18}SEN(br){1,19}SEE(/>){1,21}]");
        testDoc( 
            "Hello<!--hi\"!-->, <br/>",
            "[T(Hello){1,1}C(hi\"!){1,6}T(, ){1,17}SES(<){1,19}SEN(br){1,20}SEE(/>){1,22}]");

        testDoc( 
            "Hello<!-- 4 > 3 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3 ){1,6}T(, ){1,20}SES(<){1,22}SEN(br){1,23}SEE(/>){1,25}]");
        testDoc( 
            "Hello<!-- 4 > 3 > 10 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3 > 10 ){1,6}T(, ){1,25}SES(<){1,27}SEN(br){1,28}SEE(/>){1,30}]");
        testDoc( 
            "Hello<!-- 4 > 3\n > 10 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3\n > 10 ){1,6}T(, ){2,10}SES(<){2,12}SEN(br){2,13}SEE(/>){2,15}]");
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n > 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n > 10 ){1,6}T(, ){2,10}SES(<){2,12}SEN(br){2,13}SEE(/>){2,15}]");
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n \"> 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n \"> 10 ){1,6}T(, ){2,11}SES(<){2,13}SEN(br){2,14}SEE(/>){2,16}]");
        testDoc( 
            "Hello<![CDATA[ 4 > 3 > 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3 > 10 ){1,6}T(, ){1,30}SES(<){1,32}SEN(br){1,33}SEE(/>){1,35}]");
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n\n\n\n > 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n\n\n\n > 10 ){1,6}T(, ){5,10}SES(<){5,12}SEN(br){5,13}SEE(/>){5,15}]");
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n   \t> 10 ){1,6}T(, ){5,13}SES(<){5,15}SEN(br){5,16}SEE(/>){5,18}]");
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
            "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
            "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
            "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>\n" +
            "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n   \t> 10 ){1,6}T(, ){5,13}SES(<){5,15}SEN(br){5,16}SEE(/>){5,18}" +
            "T(\nHello){5,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){6,6}T(, ){10,13}SES(<){10,15}SEN(br){10,16}SEE(/>){10,18}" +
            "T(\nHello){10,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){11,6}T(, ){15,13}SES(<){15,15}SEN(br){15,16}SEE(/>){15,18}" +
            "T(\nHello){15,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){16,6}T(, ){20,13}SES(<){20,15}SEN(br){20,16}SEE(/>){20,18}" +
            "T(\nHello){20,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){21,6}T(, ){25,13}SES(<){25,15}SEN(br){25,16}SEE(/>){25,18}]");
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
            "la &aacute;\n lasd &amp;){1,1}OES(<){11,12}OEN(p){11,13}OEE(>){11,14}T( aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh" +
            "kl\njasdl kjaslkj asjqq9\nk fiuh 23kj hdfkjh assd\nflkjh lkjh fdfasdfkjlh dfs" +
            "llk\nd8u u hkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9\n))sad lkjsalkja aslk" +
            "la \n&aacute; lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hhjh" +
            "kljasdl kjaslkj asjqq9k fiuh 23kj hdfkjh assdflkjh lkjh fdfa\nsdfkjlh dfs" +
            "llkd8u u \nhkkj asyu 4lk vl jhksajhd889p3rk sl a, alkj a9))sad l\nkjsalkja aslk" +
            "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\"){11,15}CES(</){22,41}CEN(p){22,43}CEE(>){22,44}T( khj askjh 1 kh ak hh\njh" +
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

        testDocError( 
            "Hello, <p>lala</p>",
            "[T(o, ){1,1}OES(<){1,4}OEN(p){1,5}]",
            4, 5, 
            1, 4);
        
        testDocError( 
            "Hello, <!--lala-->",
            "[T(o, ){1,1}C(lala){1,4}]",
            4, 8,
            1, 4);
        
        testDoc( 
            "Hello, <![CDATA[lala]]>",
            "[T(o, <![CD){1,1}]",
            4, 8);

        testDocError( 
            "Hello, <![CDATA[lala]]>",
            "[T(o, <![CDATA[){1,1}]",
            4, 12, 
            1, 4);

        testDocError( 
            "Hello, <br th:text = \"a= b/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}EW( ){1,11}A(th:text){1,12}( = ){1,19}(\"a= b){1,22}SEE(/>){1,27}]",
            1, 8);
        
        testDoc( 
            "<div class = \"lala\">",
            "[OES(<){1,1}OEN(div){1,2}EW( ){1,5}A(class){1,6}( = ){1,11}(\"lala\"){1,14}OEE(>){1,20}]");
        testDoc( 
            "<div class \n\n= \nlala li=\nlla>",
            "[OES(<){1,1}OEN(div){1,2}EW( ){1,5}A(class){1,6}( \n\n= \n){1,11}(lala){4,1}EW( ){4,5}A(li){4,6}(=\n){4,8}(lla){5,1}OEE(>){5,4}]");
        testDoc( 
            "<div class \n\n= \n\"lala\"li=\nlla>",
            "[OES(<){1,1}OEN(div){1,2}EW( ){1,5}A(class){1,6}( \n\n= \n){1,11}(\"lala\"){4,1}A(li){4,7}(=\n){4,9}(lla){5,1}OEE(>){5,4}]");
        

        testDoc( 
            "<!DOCTYPE>",
            "[DT(DOCTYPE){1,3}(){1,10}(){1,10}(){1,10}(){1,10}]");
        testDoc( 
            "<!doctype>",
            "[DT(doctype){1,3}(){1,10}(){1,10}(){1,10}(){1,10}]");
        testDoc( 
            "<!DOCTYPE  >",
            "[DT(DOCTYPE){1,3}(){1,10}(){1,10}(){1,10}(){1,10}]");
        testDoc( 
            "<!DOCTYPE html>",
            "[DT(DOCTYPE){1,3}(html){1,11}(){1,15}(){1,15}(){1,15}]");
        testDoc( 
            "<!DOCTYPE  \nhtml>",
            "[DT(DOCTYPE){1,3}(html){2,1}(){2,5}(){2,5}(){2,5}]");
        testDoc( 
            "<!DOCTYPE html >",
            "[DT(DOCTYPE){1,3}(html){1,11}(){1,15}(){1,15}(){1,15}]");
        testDocError( 
            "<!DOCTYPE html \"lalero\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(lalero){1,16}(){1,24}(){1,24}]",
            1,1);
        testDocError( 
            "<!DOCTYPE html lalero>",
            "[DT(DOCTYPE){1,3}(html){1,11}(lalero){1,16}(){1,24}(){1,24}]",
            1,1);
        testDocError( 
            "<!DOCTYPE html lalero>",
            "[DT(DOCTYPE){1,3}(html){1,11}(lalero){1,16}(){1,24}(){1,24}]",
            1,1);
        testDocError( 
            "<!DOCTYPE html \"lalero\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(lalero){1,16}(){1,24}(){1,24}]",
            1,1);
        testDocError( 
            "<!DOCTYPE html \"lalero\"  >",
            "[DT(DOCTYPE){1,3}(html){1,11}(lalero){1,16}(){1,24}(){1,24}]",
            1,1);
        testDocError( 
            "<!DOCTYPE html \"lalero>",
            "[DT(DOCTYPE){1,3}(html){1,11}(lalero){1,16}(){1,23}(){1,23}]",
            1, 1);
        testDocError( 
            "<!DOCTYPE html PUBLIC \"lalero\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(PUBLIC){1,16}(lalero){1,23}(){1,23}]",
            1,1);
        testDoc( 
            "<!DOCTYPE html SYSTEM \"lalero\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(SYSTEM){1,16}(){1,23}(lalero){1,23}]");
        testDocError( 
            "<!DOCTYPE html PUBLIC lalero>",
            "[DT(DOCTYPE){1,3}(html){1,11}(PUBLIC){1,16}(lalero){1,24}(){1,24}]",
            1,1);
        testDocError( 
            "<!DOCTYPE html PUBLIC lalero   as>",
            "[DT(DOCTYPE){1,3}(html){1,11}(PUBLIC){1,16}(lalero){1,24}(){1,24}]",
            1,1);
        testDocError( 
            "<!DOCTYPE html PUBLIC \"lalero\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(PUBLIC){1,16}(lalero){1,24}(){1,24}]",
            1,1);
        testDoc( 
            "<!DOCTYPE html system \"lalero\"  >",
            "[DT(DOCTYPE){1,3}(html){1,11}(system){1,16}(){1,23}(lalero){1,23}]");
        testDoc( 
            "<!DOCTYPE html public \"lalero\"   \n\"hey\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(public){1,16}(lalero){1,23}(hey){2,1}]");
        testDoc( 
                "<!DOCTYPE html system \n\"lalero\"\"le\">",
                "[DT(DOCTYPE){1,3}(html){1,11}(system){1,16}(){2,1}(lalero\"\"le){2,1}]");
        
        
        
        
        System.out.println("TOTAL Test executions: " + totalTestExecutions);
        
    }
    
    
    
    static void testDocError(final String input, final String output, final int errorLine, final int errorCol) {
        try {
            testDoc(input, output);
            throw new ComparisonFailure(null, "exception", "no exception");
            
        } catch (final AttoParseException e) {
            assertEquals(Integer.valueOf(errorLine), e.getLine());
            assertEquals(Integer.valueOf(errorCol), e.getCol());
        }
    }

    
    static void testDocError(final String input, final String output, final int offset, final int len, final int errorLine, final int errorCol) {
        try {
            testDoc(input, output, offset, len);
            throw new ComparisonFailure(null, "exception", "no exception");
            
        } catch (final AttoParseException e) {
            assertEquals(Integer.valueOf(errorLine), e.getLine());
            assertEquals(Integer.valueOf(errorCol), e.getCol());
        }
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

            final IAttoParser parser = new MarkupAttoParser();

            // TEST WITH TRACING HANDLER
            {
                final StringWriter sw = new StringWriter(); 
                final IAttoHandler handler = new TracingMarkupAttoHandler(sw);
                if (offset == 0 && len == input.length) {
                    ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input), handler, bufferSize);
                } else { 
                    ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input, offset, len), handler, bufferSize);
                }
                final String result = sw.toString();
                assertEquals(output, result);
            }

            
            // TEST WITH DUPLICATING MARKUP HANDLER (few events)
            {
                final StringWriter sw = new StringWriter(); 
                final IAttoHandler handler = new DuplicatingMarkupAttoHandler(sw);
                if (offset == 0 && len == input.length) {
                    ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input), handler, bufferSize);
                } else { 
                    ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input, offset, len), handler, bufferSize);
                }
                final String desired =
                        (offset == 0 && len == input.length ? new String(input) : new String(input, offset, len));
                final String result = sw.toString();
                assertEquals(desired, result);
            }

            
            // TEST WITH DUPLICATING MARKUP BREAKDOWN HANDLER (many events)
            {
                final StringWriter sw = new StringWriter(); 
                final IAttoHandler handler = new DuplicatingMarkupBreakDownAttoHandler(sw);
                if (offset == 0 && len == input.length) {
                    ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input), handler, bufferSize);
                } else { 
                    ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input, offset, len), handler, bufferSize);
                }
                final String desired =
                        (offset == 0 && len == input.length ? new String(input) : new String(input, offset, len));
                final String result = sw.toString();
                assertEquals(desired, result);
            }

            
            totalTestExecutions++;
            
        } catch (final ComparisonFailure cf) {
            System.err.println("Error parsing text \"" + new String(input, offset, len) + "\" with buffer size: " + bufferSize);
            throw cf;
        } catch (final Exception e) {
            throw new AttoParseException("Error parsing text \"" + new String(input, offset, len) + "\" with buffer size: " + bufferSize, e);
        }
        
    }
    
    
    
}
