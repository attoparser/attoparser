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

import org.attoparser.markup.MarkupAttoParser;
import org.attoparser.markup.MarkupParsingConfiguration;
import org.attoparser.markup.MarkupParsingConfiguration.ElementBalancing;
import org.attoparser.markup.MarkupParsingConfiguration.PrologPresence;
import org.attoparser.markup.MarkupParsingConfiguration.UniqueRootElementPresence;
import org.attoparser.markup.duplicate.DuplicatingBasicMarkupAttoHandler;
import org.attoparser.markup.duplicate.DuplicatingDetailedMarkupAttoHandler;
import org.attoparser.markup.trace.TracingBasicMarkupAttoHandler;
import org.attoparser.markup.trace.TracingDetailedMarkupAttoHandler;
import org.attoparser.markup.trace.TracingStandardMarkupAttoHandler;



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
        
        
        final MarkupParsingConfiguration noRestrictions = MarkupParsingConfiguration.noRestrictions();
        
        final MarkupParsingConfiguration noRestrictionsUpperCaseDocType = noRestrictions.clone();
        noRestrictionsUpperCaseDocType.getPrologParsingConfiguration().setValidateProlog(true);
        noRestrictionsUpperCaseDocType.getPrologParsingConfiguration().setRequireDoctypeKeywordsUpperCase(true);
        
        final MarkupParsingConfiguration noRestrictionsAutoClose = MarkupParsingConfiguration.noRestrictions();
        noRestrictionsAutoClose.setElementBalancing(ElementBalancing.AUTO_CLOSE);
        
        final MarkupParsingConfiguration noUnbalacedClosed = MarkupParsingConfiguration.noRestrictions();
        noUnbalacedClosed.setElementBalancing(ElementBalancing.AUTO_CLOSE_REQUIRE_NO_UNMATCHED_CLOSE);
        
        final MarkupParsingConfiguration wellFormedXml = new MarkupParsingConfiguration();
        wellFormedXml.setElementBalancing(ElementBalancing.REQUIRE_BALANCED);
        wellFormedXml.setRequireUniqueAttributesInElement(true);
        wellFormedXml.setRequireXmlWellFormedAttributeValues(true);
        wellFormedXml.getPrologParsingConfiguration().setValidateProlog(true);
        wellFormedXml.getPrologParsingConfiguration().setPrologPresence(PrologPresence.ALLOWED);
        wellFormedXml.getPrologParsingConfiguration().setXmlDeclarationPresence(PrologPresence.ALLOWED);
        wellFormedXml.getPrologParsingConfiguration().setDoctypePresence(PrologPresence.ALLOWED);
        wellFormedXml.setUniqueRootElementPresence(UniqueRootElementPresence.DEPENDS_ON_PROLOG_DOCTYPE);
        
        final MarkupParsingConfiguration wellFormedXmlCaseInsensitive = wellFormedXml.clone();
        wellFormedXmlCaseInsensitive.setCaseSensitive(false);
        
        
        
        final String dt1 = "<!DOCTYPE>"; 
        final String dt2 = "<!DOCTYPE html>"; 
        final String dt3 = "<!DOCTYPE html public \"lala\">"; 
        final String dt4 = "<!DOCTYPE html public \"aaa\" [<!ELEMENT>]>"; 
        
        final IAttoParser p = new MarkupAttoParser();
        
        StringWriter sw1 = new StringWriter();
        IAttoHandler h = new TracingBasicMarkupAttoHandler(sw1);        
        p.parse(dt1, h);
        assertEquals("[DT(){1,1}]", sw1.toString());
        
        StringWriter sw2 = new StringWriter();
        h = new TracingBasicMarkupAttoHandler(sw2);        
        p.parse(dt2, h);
        assertEquals("[DT(html){1,1}]", sw2.toString());
        
        StringWriter sw3 = new StringWriter();
        h = new TracingBasicMarkupAttoHandler(sw3);        
        p.parse(dt3, h);
        assertEquals("[DT(html public \"lala\"){1,1}]", sw3.toString());
        
        StringWriter sw4 = new StringWriter();
        h = new TracingBasicMarkupAttoHandler(sw4);        
        p.parse(dt4, h);
        assertEquals("[DT(html public \"aaa\" [<!ELEMENT>]){1,1}]", sw4.toString());
        
        
        testDoc( 
            "<h1>Hello</ h1>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello</ h1>){1,5}ACES(</){1,16}ACEN(h1){1,16}ACEE(>){1,16}]",
            "[OE(h1){1,1}T(Hello</ h1>){1,5}ACE(h1){1,16}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<h1>Hello</ h1>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello</ h1>){1,5}]",
            "[OE(h1){1,1}T(Hello</ h1>){1,5}]", 
            noRestrictions);
        testDoc( 
            "<p><h1>Hello</ h1></p>",
            "[OES(<){1,1}OEN(p){1,2}OEE(>){1,3}OES(<){1,4}OEN(h1){1,5}OEE(>){1,7}T(Hello</ h1>){1,8}ACES(</){1,19}ACEN(h1){1,19}ACEE(>){1,19}CES(</){1,19}CEN(p){1,21}CEE(>){1,22}]",
            "[OE(p){1,1}OE(h1){1,4}T(Hello</ h1>){1,8}ACE(h1){1,19}CE(p){1,19}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<p><h1>Hello</ h1></p>",
            "[OES(<){1,1}OEN(p){1,2}OEE(>){1,3}OES(<){1,4}OEN(h1){1,5}OEE(>){1,7}T(Hello</ h1>){1,8}CES(</){1,19}CEN(p){1,21}CEE(>){1,22}]",
            "[OE(p){1,1}OE(h1){1,4}T(Hello</ h1>){1,8}CE(p){1,19}]", 
            noRestrictions);
        testDoc( 
            "Hello, World!",
            "[T(Hello, World!){1,1}]",
            "[T(Hello, World!){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "",
            "[]",
            "[]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<p>Hello</p>",
            "[OES(<){1,1}OEN(p){1,2}OEE(>){1,3}T(Hello){1,4}CES(</){1,9}CEN(p){1,11}CEE(>){1,12}]",
            "[OE(p){1,1}T(Hello){1,4}CE(p){1,9}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<h1>Hello</h1>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}CEE(>){1,14}]",
            "[OE(h1){1,1}T(Hello){1,5}CE(h1){1,10}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<h1>Hello</h1 >",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}AS( ){1,14}CEE(>){1,15}]",
            "[OE(h1){1,1}T(Hello){1,5}CE(h1){1,10}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<h1>Hello</h1 \n\n>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}AS( \n\n){1,14}CEE(>){3,1}]",
            "[OE(h1){1,1}T(Hello){1,5}CE(h1){1,10}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<\np  >Hello</p>",
            "[T(<\np  >Hello){1,1}UCES(</){2,10}UCEN(p){2,12}UCEE(>){2,13}]",
            "[T(<\np  >Hello){1,1}UCE(p){2,10}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "< h1  >Hello</h1>",
            "[T(< h1  >Hello){1,1}UCES(</){1,13}UCEN(h1){1,15}UCEE(>){1,17}]",
            "[T(< h1  >Hello){1,1}UCE(h1){1,13}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<h1>Hello</ h1>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello</ h1>){1,5}ACES(</){1,16}ACEN(h1){1,16}ACEE(>){1,16}]",
            "[OE(h1){1,1}T(Hello</ h1>){1,5}ACE(h1){1,16}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "< h1>Hello</ h1>",
            "[T(< h1>Hello</ h1>){1,1}]",
            "[T(< h1>Hello</ h1>){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, World!",
            "[T(ello, Worl){1,1}]",
            "[T(ello, Worl){1,1}]",
            1, 10, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, World!",
            "[T(e){1,1}]",
            "[T(e){1,1}]",
            1, 1, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <p>lala</p>",
            "[T(Hello, ){1,1}OES(<){1,8}OEN(p){1,9}OEE(>){1,10}T(lala){1,11}CES(</){1,15}CEN(p){1,17}CEE(>){1,18}]",
            "[T(Hello, ){1,1}OE(p){1,8}T(lala){1,11}CE(p){1,15}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <p>lal'a</p>",
            "[T(Hello, ){1,1}OES(<){1,8}OEN(p){1,9}OEE(>){1,10}T(lal'a){1,11}CES(</){1,16}CEN(p){1,18}CEE(>){1,19}]",
            "[T(Hello, ){1,1}OE(p){1,8}T(lal'a){1,11}CE(p){1,16}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <p>l'al'a</p>",
            "[T(Hello, ){1,1}OES(<){1,8}OEN(p){1,9}OEE(>){1,10}T(l'al'a){1,11}CES(</){1,17}CEN(p){1,19}CEE(>){1,20}]",
            "[T(Hello, ){1,1}OE(p){1,8}T(l'al'a){1,11}CE(p){1,17}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <p>lala</p>",
            "[T(o, ){1,1}OES(<){1,4}OEN(p){1,5}OEE(>){1,6}T(l){1,7}ACES(</){1,8}ACEN(p){1,8}ACEE(>){1,8}]",
            "[T(o, ){1,1}OE(p){1,4}T(l){1,7}ACE(p){1,8}]",
            4, 7, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}SEE(/>){1,11}]",
            "[T(Hello, ){1,1}SE(br){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text=\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}(=){1,19}(\"ll\"){1,20}SEE(/>){1,24}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text='ll'/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}(=){1,19}('ll'){1,20}SEE(/>){1,24}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =){1,19}(\"ll\"){1,21}SEE(/>){1,25}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =   \"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =   ){1,19}(\"ll\"){1,24}SEE(/>){1,28}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =   ll/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =   ){1,19}(ll){1,24}SEE(/>){1,26}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =   \"ll\"a=2/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =   ){1,19}(\"ll\"){1,24}A(a){1,28}(=){1,29}(2){1,30}SEE(/>){1,31}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll',a='2']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =   'll'a=2/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =   ){1,19}('ll'){1,24}A(a){1,28}(=){1,29}(2){1,30}SEE(/>){1,31}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll',a='2']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =   \"ll\"a= \n 2/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =   ){1,19}(\"ll\"){1,24}A(a){1,28}(= \n ){1,29}(2){2,2}SEE(/>){2,3}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll',a='2']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =   ll a= \n 2/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =   ){1,19}(ll){1,24}AS( ){1,26}A(a){1,27}(= \n ){1,28}(2){2,2}SEE(/>){2,3}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll',a='2']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =   ll a/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =   ){1,19}(ll){1,24}AS( ){1,26}A(a){1,27}(){1,28}(){1,28}SEE(/>){1,28}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll',a='']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text =   ll a=/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( =   ){1,19}(ll){1,24}AS( ){1,26}A(a){1,27}(=){1,28}(){1,29}SEE(/>){1,29}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll',a='']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text = a=/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( = ){1,19}(a=){1,22}SEE(/>){1,24}]",
            "[T(Hello, ){1,1}SE(br[th:text='a=']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text = a= b/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( = ){1,19}(a=){1,22}AS( ){1,24}A(b){1,25}(){1,26}(){1,26}SEE(/>){1,26}]",
            "[T(Hello, ){1,1}SE(br[th:text='a=',b='']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text = a=b/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( = ){1,19}(a=b){1,22}SEE(/>){1,25}]",
            "[T(Hello, ){1,1}SE(br[th:text='a=b']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text = \"a=b\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( = ){1,19}(\"a=b\"){1,22}SEE(/>){1,27}]",
            "[T(Hello, ){1,1}SE(br[th:text='a=b']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text = \"a= b\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( = ){1,19}(\"a= b\"){1,22}SEE(/>){1,28}]",
            "[T(Hello, ){1,1}SE(br[th:text='a= b']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text = 'a= b'/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( = ){1,19}('a= b'){1,22}SEE(/>){1,28}]",
            "[T(Hello, ){1,1}SE(br[th:text='a= b']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br th:text = \"a= b\"\n/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}( = ){1,19}(\"a= b\"){1,22}AS(\n){1,28}SEE(/>){2,1}]",
            "[T(Hello, ){1,1}SE(br[th:text='a= b']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br  th:text=\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS(  ){1,11}A(th:text){1,13}(=){1,20}(\"ll\"){1,21}SEE(/>){1,25}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll']){1,8}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, <br \nth:text=\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( \n){1,11}A(th:text){2,1}(=){2,8}(\"ll\"){2,9}SEE(/>){2,13}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello, World! <br/>\n<div\n l\n     a=\"12 3\" zas    o=\"\"  b=\"lelo\n  = s\">lala</div> <p th=\"lala\" >liool</p>",
            "[T(Hello, World! ){1,1}SES(<){1,15}SEN(br){1,16}SEE(/>){1,18}T(\n){1,20}" +
              "OES(<){2,1}OEN(div){2,2}AS(\n ){2,5}A(l){3,2}(){3,3}(){3,3}AS(\n     ){3,3}" +
              "A(a){4,6}(=){4,7}(\"12 3\"){4,8}AS( ){4,14}A(zas){4,15}(){4,18}(){4,18}AS(    ){4,18}" +
              "A(o){4,22}(=){4,23}(\"\"){4,24}AS(  ){4,26}A(b){4,28}(=){4,29}(\"lelo\n  = s\"){4,30}" +
              "OEE(>){5,7}T(lala){5,8}CES(</){5,12}CEN(div){5,14}CEE(>){5,17}T( ){5,18}" +
              "OES(<){5,19}OEN(p){5,20}AS( ){5,21}A(th){5,22}(=){5,24}(\"lala\"){5,25}AS( ){5,31}OEE(>){5,32}" +
              "T(liool){5,33}CES(</){5,38}CEN(p){5,40}CEE(>){5,41}]",
              null, 
              noRestrictionsAutoClose);

        testDoc( 
            "Hello<!--hi!-->, <br/>",
            "[T(Hello){1,1}C(hi!){1,6}T(, ){1,16}SES(<){1,18}SEN(br){1,19}SEE(/>){1,21}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<!--hi\"!-->, <br/>",
            "[T(Hello){1,1}C(hi\"!){1,6}T(, ){1,17}SES(<){1,19}SEN(br){1,20}SEE(/>){1,22}]",
            null, 
            noRestrictionsAutoClose);

        testDoc( 
            "Hello<!-- 4 > 3 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3 ){1,6}T(, ){1,20}SES(<){1,22}SEN(br){1,23}SEE(/>){1,25}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<!-- 4 > 3 > 10 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3 > 10 ){1,6}T(, ){1,25}SES(<){1,27}SEN(br){1,28}SEE(/>){1,30}]",
            "[T(Hello){1,1}C( 4 > 3 > 10 ){1,6}T(, ){1,25}SE(br){1,27}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<!-- 4 > 3\n > 10 -->, <br/>",
            "[T(Hello){1,1}C( 4 > 3\n > 10 ){1,6}T(, ){2,10}SES(<){2,12}SEN(br){2,13}SEE(/>){2,15}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n > 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n > 10 ){1,6}T(, ){2,10}SES(<){2,12}SEN(br){2,13}SEE(/>){2,15}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n \"> 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n \"> 10 ){1,6}T(, ){2,11}SES(<){2,13}SEN(br){2,14}SEE(/>){2,16}]",
            "[T(Hello){1,1}D( 4 > 3\n \"> 10 ){1,6}T(, ){2,11}SE(br){2,13}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n '> 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n '> 10 ){1,6}T(, ){2,11}SES(<){2,13}SEN(br){2,14}SEE(/>){2,16}]",
            "[T(Hello){1,1}D( 4 > 3\n '> 10 ){1,6}T(, ){2,11}SE(br){2,13}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<![CDATA[ 4 > 3 > 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3 > 10 ){1,6}T(, ){1,30}SES(<){1,32}SEN(br){1,33}SEE(/>){1,35}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n\n\n\n > 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n\n\n\n > 10 ){1,6}T(, ){5,10}SES(<){5,12}SEN(br){5,13}SEE(/>){5,15}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "Hello<![CDATA[ 4 > 3\n\n  \n   \n   \t> 10 ]]>, <br/>",
            "[T(Hello){1,1}D( 4 > 3\n\n  \n   \n   \t> 10 ){1,6}T(, ){5,13}SES(<){5,15}SEN(br){5,16}SEE(/>){5,18}]",
            null, 
            noRestrictionsAutoClose);
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
            "T(\nHello){20,20}D( 4 > 3\n\n  \n   \n   \t> 10 ){21,6}T(, ){25,13}SES(<){25,15}SEN(br){25,16}SEE(/>){25,18}]",
            null, 
            noRestrictionsAutoClose);
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
            "la &aacute;\n lasd &amp; aiass da & asdll . asi ua&$\" khj askjh 1 kh ak hh\njh){1,1}]",
            null, 
            noRestrictionsAutoClose);
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
            "){1,1}]",
            null, 
            noRestrictionsAutoClose);
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
            "){22,45}]",
            null, 
            noRestrictionsAutoClose);

        testDocError( 
            "Hello, <p>lala</p>",
            null, null,
            4, 5,
            1, 4, 
            noRestrictionsAutoClose);
        
        testDocError( 
            "Hello, <!--lala-->",
            null, null,
            4, 8,
            1, 4, 
            noRestrictionsAutoClose);
        
        testDoc( 
            "Hello, <![CDATA[lala]]>",
            "[T(o, <![CD){1,1}]",
            null,
            4, 8, 
            noRestrictionsAutoClose);

        testDocError( 
            "Hello, <![CDATA[lala]]>",
            null, null,
            4, 12, 
            1, 4, 
            noRestrictionsAutoClose);

        testDocError( 
            "Hello, <br th:text = \"a= b/>",
            null, null,
            1, 8, 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<div class = \"lala\">",
            "[OES(<){1,1}OEN(div){1,2}AS( ){1,5}A(class){1,6}( = ){1,11}(\"lala\"){1,14}OEE(>){1,20}ACES(</){1,21}ACEN(div){1,21}ACEE(>){1,21}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<div class \n\n= \nlala li=\nlla>",
            "[OES(<){1,1}OEN(div){1,2}AS( ){1,5}A(class){1,6}( \n\n= \n){1,11}(lala){4,1}AS( ){4,5}A(li){4,6}(=\n){4,8}(lla){5,1}OEE(>){5,4}ACES(</){5,5}ACEN(div){5,5}ACEE(>){5,5}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<div class \n\n= \n\"lala\"li=\nlla>",
            "[OES(<){1,1}OEN(div){1,2}AS( ){1,5}A(class){1,6}( \n\n= \n){1,11}(\"lala\"){4,1}A(li){4,7}(=\n){4,9}(lla){5,1}OEE(>){5,4}ACES(</){5,5}ACEN(div){5,5}ACEE(>){5,5}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<div class \n\n= \n'lala'li=\nlla>",
            "[OES(<){1,1}OEN(div){1,2}AS( ){1,5}A(class){1,6}( \n\n= \n){1,11}('lala'){4,1}A(li){4,7}(=\n){4,9}(lla){5,1}OEE(>){5,4}ACES(</){5,5}ACEN(div){5,5}ACEE(>){5,5}]",
            null, 
            noRestrictionsAutoClose);
        

        testDoc( 
            "<!DOCTYPE>",
            "[DT(DOCTYPE){1,3}(){1,10}(){1,10}(){1,10}(){1,10}(){1,10}]",
            "[DT()()()(){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<!doctype>",
            "[DT(doctype){1,3}(){1,10}(){1,10}(){1,10}(){1,10}(){1,10}]",
            "[DT()()()(){1,1}]", 
            noRestrictionsAutoClose);
        testDocError( 
            "<!doctype>",
            null, null, 1, 1,
            noRestrictionsUpperCaseDocType);
        testDocError( 
            "<!DOCTYPE html system \"lala\">",
            null, null, 1, 1,
            noRestrictionsUpperCaseDocType);
        testDoc( 
            "<!DOCTYPE  >",
            "[DT(DOCTYPE){1,3}(){1,10}(){1,10}(){1,10}(){1,10}(){1,10}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html>",
            "[DT(DOCTYPE){1,3}(html){1,11}(){1,15}(){1,15}(){1,15}(){1,15}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE  \nhtml>",
            "[DT(DOCTYPE){1,3}(html){2,1}(){2,5}(){2,5}(){2,5}(){2,5}]",
            "[DT(html)()()(){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html >",
            "[DT(DOCTYPE){1,3}(html){1,11}(){1,16}(){1,16}(){1,16}(){1,16}]",
            "[DT(html)()()(){1,1}]", 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html \"lalero\">",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html lalero>",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html lalero>",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html \"lalero\">",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html \"lalero\"  >",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html \"lalero>",
            null, null,
            1, 1, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html PUBLIC \"lalero\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(PUBLIC){1,16}(lalero){1,23}(){1,31}(){1,31}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html PUBLIC 'lalero'>",
            "[DT(DOCTYPE){1,3}(html){1,11}(PUBLIC){1,16}(lalero){1,23}(){1,31}(){1,31}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html SYSTEM \"lalero\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(SYSTEM){1,16}(){1,23}(lalero){1,23}(){1,31}]",
            "[DT(html)()(lalero)(){1,1}]", 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html PUBLIC lalero>",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html PUBLIC lalero   as>",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html PUBLIC \"lalero\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(PUBLIC){1,16}(lalero){1,23}(){1,31}(){1,31}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html system \"lalero\"  >",
            "[DT(DOCTYPE){1,3}(html){1,11}(system){1,16}(){1,23}(lalero){1,23}(){1,33}]",
            "[DT(html)()(lalero)(){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html public \"lalero\"   \n\"hey\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(public){1,16}(lalero){1,23}(hey){2,1}(){2,6}]",
            "[DT(html)(lalero)(hey)(){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html public 'lalero'   \n'hey'>",
            "[DT(DOCTYPE){1,3}(html){1,11}(public){1,16}(lalero){1,23}(hey){2,1}(){2,6}]",
            "[DT(html)(lalero)(hey)(){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html public \"lalero\n\"   \n\"hey\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(public){1,16}(lalero\n){1,23}(hey){3,1}(){3,6}]",
            "[DT(html)(lalero\n)(hey)(){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html system \n\"lalero\"\"le\">",
            "[DT(DOCTYPE){1,3}(html){1,11}(system){1,16}(){2,1}(lalero\"\"le){2,1}(){2,13}]",
            null, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html system \n\"lalero\" \"le\">",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html system \n\"lalero\" [somethinghere]>",
            "[DT(DOCTYPE){1,3}(html){1,11}(system){1,16}(){2,1}(lalero){2,1}(somethinghere){2,10}]",
            "[DT(html)()(lalero)(somethinghere){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html public \n\"lalero\" [somethinghere]>",
            "[DT(DOCTYPE){1,3}(html){1,11}(public){1,16}(lalero){2,1}(){2,10}(somethinghere){2,10}]",
            "[DT(html)(lalero)()(somethinghere){1,1}]", 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html public \n\"lalero\" asas [somethinghere]>",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html system \n\"lalero\" asas [somethinghere]>",
            null, null, 
            1,1, 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html system \n\"lalero\" \"asas\" [somethinghere]>",
            null, null, 
            1,1, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html public \n\"lalero\" \"asas\" [somethinghere]>",
            "[DT(DOCTYPE){1,3}(html){1,11}(public){1,16}(lalero){2,1}(asas){2,10}(somethinghere){2,17}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE html public \n\"lalero\" \"asas\" \n\n[somethinghere]\n  >",
            "[DT(DOCTYPE){1,3}(html){1,11}(public){1,16}(lalero){2,1}(asas){2,10}(somethinghere){4,1}]",
            "[DT(html)(lalero)(asas)(somethinghere){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE sgml public \"lele\" [\n <!ELEMENT sgml ANY>\n  <!ENTITY % std       \"standard SGML\">\n ]>",
            "[DT(DOCTYPE){1,3}(sgml){1,11}(public){1,16}(lele){1,23}(){1,30}(\n <!ELEMENT sgml ANY>\n  <!ENTITY % std       \"standard SGML\">\n ){1,30}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE sgml [\n <!ELEMENT sgml ANY>\n  <!ENTITY % std       \"standard SGML\">\n ]>",
            "[DT(DOCTYPE){1,3}(sgml){1,11}(){1,16}(){1,16}(){1,16}(\n <!ELEMENT sgml ANY>\n  <!ENTITY % std       \"standard SGML\">\n ){1,16}]",
            "[DT(sgml)()()(\n <!ELEMENT sgml ANY>\n  <!ENTITY % std       \"standard SGML\">\n ){1,1}]", 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE sgml public [\n <!ELEMENT sgml ANY>\n  <!ENTITY % std       \"standard SGML\">\n ]>",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE sgml public \"lele\" [\n <!ELEMENT sgml ANY>\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ]>",
            "[DT(DOCTYPE){1,3}(sgml){1,11}(public){1,16}(lele){1,23}(){1,30}(\n <!ELEMENT sgml ANY>\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ){1,30}]",
            null, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE sgml system \"lele\" [\n <!ELEMENT sgml ANY>\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ]>",
            "[DT(DOCTYPE){1,3}(sgml){1,11}(system){1,16}(){1,23}(lele){1,23}(\n <!ELEMENT sgml ANY>\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ){1,30}]",
            "[DT(sgml)()(lele)(\n <!ELEMENT sgml ANY>\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ){1,1}]", 
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE sgml public \"lele\" [\n <!ELEMENT sgml [ ANY>\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ]>",
            null, null,
            1,1, 
            noRestrictionsAutoClose);
        testDoc( 
            "<!DOCTYPE sgml public \"lele\" [\n <!ELEMENT sgml [ ANY>]\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ]>",
            "[DT(DOCTYPE){1,3}(sgml){1,11}(public){1,16}(lele){1,23}(){1,30}(\n <!ELEMENT sgml [ ANY>]\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ){1,30}]",
            "[DT(sgml)(lele)()(\n <!ELEMENT sgml [ ANY>]\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n ){1,1}]", 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<?xml version=\"1.0\"?>",
            "[X(1.0){1,15}(null){1,20}(null){1,20}]",
            "[X(1.0)(null)(null){1,1}]", 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<?xml version=\"1.0\" encoding=\"\"?>",
            "[X(1.0){1,15}(){1,30}(null){1,32}]",
            "[X(1.0)()(null){1,1}]", 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>",
            "[X(1.0){1,15}(UTF-8){1,30}(null){1,37}]",
            "[X(1.0)(UTF-8)(null){1,1}]", 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<?xml version=\"1.0\"   \nencoding=\"UTF-8\"   ?>",
            "[X(1.0){1,15}(UTF-8){2,10}(null){2,20}]",
            "[X(1.0)(UTF-8)(null){1,1}]", 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>",
            "[X(1.0){1,15}(UTF-8){1,30}(yes){1,49}]",
            "[X(1.0)(UTF-8)(yes){1,1}]", 
            noRestrictionsAutoClose);
        
        testDocError( 
            "<?xml version=\"1.0\" standalone=\"yes\" encoding=\"UTF-8\"?>",
            null, null, 1, 1, 
            noRestrictionsAutoClose);
        
        testDocError( 
            "<?xml standalone=\"yes\" version=\"1.0\" encoding=\"UTF-8\"?>",
            null, null, 1, 1, 
            noRestrictionsAutoClose);
        
        testDocError( 
            "<?xml versio=\"1.0\"?>",
            null, null, 1, 1, 
            noRestrictionsAutoClose);
        
        testDocError( 
            "<?xml?>",
            null, null, 1, 1, 
            noRestrictionsAutoClose);
        
        testDocError( 
            "<?xml  ?>",
            null, null, 1, 1, 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<?XML version=\"1.0\"?>",
            "[P(XML){1,3}(version=\"1.0\"){1,7}]",
            "[P(XML)(version=\"1.0\"){1,1}]", 
            noRestrictionsAutoClose);
        
        testDocError( 
            "<?xml Version=\"1.0\"?>",
            null, null, 1, 1, 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<?xml version=\"1.0\"  ?>",
            "[X(1.0){1,15}(null){1,22}(null){1,22}]",
            "[X(1.0)(null)(null){1,1}]", 
            noRestrictionsAutoClose);
            
        testDoc( 
            "<?xml version=\"1.0\"?><!DOCTYPE html>",
            "[X(1.0){1,15}(null){1,20}(null){1,20}DT(DOCTYPE){1,24}(html){1,32}(){1,36}(){1,36}(){1,36}(){1,36}]",
            "[X(1.0)(null)(null){1,1}DT(html)()()(){1,22}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<?xml version=\"1.0\"?>\n<!DOCTYPE html>",
            "[X(1.0){1,15}(null){1,20}(null){1,20}T(\n){1,22}DT(DOCTYPE){2,3}(html){2,11}(){2,15}(){2,15}(){2,15}(){2,15}]",
            "[X(1.0)(null)(null){1,1}T(\n){1,22}DT(html)()()(){2,1}]", 
            noRestrictionsAutoClose);
                
        testDoc( 
            "\n <!ELEMENT sgml ANY>",
            "[T(\n ){1,1}OES(<){2,2}OEN(!ELEMENT){2,3}AS( ){2,11}A(sgml){2,12}(){2,16}(){2,16}AS( ){2,16}A(ANY){2,17}(){2,20}(){2,20}OEE(>){2,20}ACES(</){2,21}ACEN(!ELEMENT){2,21}ACEE(>){2,21}]",
            "[T(\n ){1,1}OE(!ELEMENT[sgml='',ANY='']){2,2}ACE(!ELEMENT){2,21}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "\n <!ELEMENT sgml ANY>\n <!-- this is a comment inside --> <!ENTITY % std       \"standard SGML\">\n",
            "[T(\n ){1,1}OES(<){2,2}OEN(!ELEMENT){2,3}AS( ){2,11}A(sgml){2,12}(){2,16}(){2,16}AS( ){2,16}A(ANY){2,17}(){2,20}(){2,20}OEE(>){2,20}T(\n ){2,21}C( this is a comment inside ){3,2}T( ){3,35}OES(<){3,36}OEN(!ENTITY){3,37}AS( ){3,44}A(%){3,45}(){3,46}(){3,46}AS( ){3,46}A(std){3,47}(){3,50}(){3,50}AS(       ){3,50}A(\"standard){3,57}(){3,66}(){3,66}AS( ){3,66}A(SGML\"){3,67}(){3,72}(){3,72}OEE(>){3,72}T(\n){3,73}ACES(</){4,1}ACEN(!ENTITY){4,1}ACEE(>){4,1}ACES(</){4,1}ACEN(!ELEMENT){4,1}ACEE(>){4,1}]",
            "[T(\n ){1,1}OE(!ELEMENT[sgml='',ANY='']){2,2}T(\n ){2,21}C( this is a comment inside ){3,2}T( ){3,35}OE(!ENTITY[%='',std='',\"standard='',SGML\"='']){3,36}T(\n){3,73}ACE(!ENTITY){4,1}ACE(!ELEMENT){4,1}]", 
            noRestrictionsAutoClose);

        
        testDoc( 
            "<?xsl-stylesheet a=\"1\"?>",
            "[P(xsl-stylesheet){1,3}(a=\"1\"){1,18}]",
            "[P(xsl-stylesheet)(a=\"1\"){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<?xsl-stylesheet ?>",
            "[P(xsl-stylesheet){1,3}(null){1,18}]",
            "[P(xsl-stylesheet)(null){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<?xsl-stylesheet?>",
            "[P(xsl-stylesheet){1,3}(null){1,17}]",
            "[P(xsl-stylesheet)(null){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<?xsl-stylesheet a=\"1\" a b > uas23 ?>",
            "[P(xsl-stylesheet){1,3}(a=\"1\" a b > uas23 ){1,18}]",
            "[P(xsl-stylesheet)(a=\"1\" a b > uas23 ){1,1}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<p><!--a--></p>",
            "[OES(<){1,1}OEN(p){1,2}OEE(>){1,3}C(a){1,4}CES(</){1,12}CEN(p){1,14}CEE(>){1,15}]",
            "[OE(p){1,1}C(a){1,4}CE(p){1,12}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<p><!--a-->",
            "[OES(<){1,1}OEN(p){1,2}OEE(>){1,3}C(a){1,4}ACES(</){1,12}ACEN(p){1,12}ACEE(>){1,12}]",
            "[OE(p){1,1}C(a){1,4}ACE(p){1,12}]", 
            noRestrictionsAutoClose);
        testDoc( 
            "<p><?xsl-stylesheet a=\"1\" a b > uas23 ?>",
            "[OES(<){1,1}OEN(p){1,2}OEE(>){1,3}P(xsl-stylesheet){1,6}(a=\"1\" a b > uas23 ){1,21}ACES(</){1,41}ACEN(p){1,41}ACEE(>){1,41}]",
            "[OE(p){1,1}P(xsl-stylesheet)(a=\"1\" a b > uas23 ){1,4}ACE(p){1,41}]", 
            noRestrictionsAutoClose);
        
        testDoc( 
            "<p>Hello</p>",
            "[OES(<){1,1}OEN(p){1,2}OEE(>){1,3}T(Hello){1,4}CES(</){1,9}CEN(p){1,11}CEE(>){1,12}]",
            "[OE(p){1,1}T(Hello){1,4}CE(p){1,9}]", 
            wellFormedXml);
        testDoc( 
            "<h1>Hello</h1>",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}CEE(>){1,14}]",
            "[OE(h1){1,1}T(Hello){1,5}CE(h1){1,10}]", 
            wellFormedXml);
        testDocError( 
            "<p>Hello</h1>",
            null, null, 1, 9,
            wellFormedXml);
        testDocError( 
            "<p>Hello",
            null, null, -1, -1,
            wellFormedXml);
        testDocError( 
            "Hello</h1>",
            null, null, 1, 6, 
            wellFormedXml);
        testDoc( 
            "<h1>Hello</h1 >",
            "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}AS( ){1,14}CEE(>){1,15}]",
            "[OE(h1){1,1}T(Hello){1,5}CE(h1){1,10}]", 
            wellFormedXml);
        
        testDoc( 
            "<?xml version=\"1.0\"?>\n<!DOCTYPE html>\n<html></html>",
            "[X(1.0){1,15}(null){1,20}(null){1,20}T(\n){1,22}DT(DOCTYPE){2,3}(html){2,11}(){2,15}(){2,15}(){2,15}(){2,15}T(\n){2,16}OES(<){3,1}OEN(html){3,2}OEE(>){3,6}CES(</){3,7}CEN(html){3,9}CEE(>){3,13}]",
            "[X(1.0)(null)(null){1,1}T(\n){1,22}DT(html)()()(){2,1}T(\n){2,16}OE(html){3,1}CE(html){3,7}]", 
            wellFormedXml);
        testDoc( 
            "<?xml version=\"1.0\"?>\n<html></html>",
            "[X(1.0){1,15}(null){1,20}(null){1,20}T(\n){1,22}OES(<){2,1}OEN(html){2,2}OEE(>){2,6}CES(</){2,7}CEN(html){2,9}CEE(>){2,13}]",
            "[X(1.0)(null)(null){1,1}T(\n){1,22}OE(html){2,1}CE(html){2,7}]", 
            wellFormedXml);
        testDoc( 
            "<!DOCTYPE html>\n<html></html>",
            "[DT(DOCTYPE){1,3}(html){1,11}(){1,15}(){1,15}(){1,15}(){1,15}T(\n){1,16}OES(<){2,1}OEN(html){2,2}OEE(>){2,6}CES(</){2,7}CEN(html){2,9}CEE(>){2,13}]",
            "[DT(html)()()(){1,1}T(\n){1,16}OE(html){2,1}CE(html){2,7}]", 
            wellFormedXml);
        testDoc( 
            "\n<!DOCTYPE html>\n<html></html>",
            "[T(\n){1,1}DT(DOCTYPE){2,3}(html){2,11}(){2,15}(){2,15}(){2,15}(){2,15}T(\n){2,16}OES(<){3,1}OEN(html){3,2}OEE(>){3,6}CES(</){3,7}CEN(html){3,9}CEE(>){3,13}]",
            "[T(\n){1,1}DT(html)()()(){2,1}T(\n){2,16}OE(html){3,1}CE(html){3,7}]", 
            wellFormedXml);
        testDoc( 
            "\n<?xml version=\"1.0\"?>\n<!DOCTYPE html>\n<html></html>",
            "[T(\n){1,1}X(1.0){2,15}(null){2,20}(null){2,20}T(\n){2,22}DT(DOCTYPE){3,3}(html){3,11}(){3,15}(){3,15}(){3,15}(){3,15}T(\n){3,16}OES(<){4,1}OEN(html){4,2}OEE(>){4,6}CES(</){4,7}CEN(html){4,9}CEE(>){4,13}]",
            "[T(\n){1,1}X(1.0)(null)(null){2,1}T(\n){2,22}DT(html)()()(){3,1}T(\n){3,16}OE(html){4,1}CE(html){4,7}]", 
            wellFormedXml);
        testDoc( 
            "<?xml version=\"1.0\"?>\n<!-- a comment -->\n<!DOCTYPE html>\n<html></html>",
            "[X(1.0){1,15}(null){1,20}(null){1,20}T(\n){1,22}C( a comment ){2,1}T(\n){2,19}DT(DOCTYPE){3,3}(html){3,11}(){3,15}(){3,15}(){3,15}(){3,15}T(\n){3,16}OES(<){4,1}OEN(html){4,2}OEE(>){4,6}CES(</){4,7}CEN(html){4,9}CEE(>){4,13}]",
            "[X(1.0)(null)(null){1,1}T(\n){1,22}C( a comment ){2,1}T(\n){2,19}DT(html)()()(){3,1}T(\n){3,16}OE(html){4,1}CE(html){4,7}]", 
            wellFormedXml);
        testDoc( 
            "<!-- a comment -->\n<?xml version=\"1.0\"?>\n<!DOCTYPE html>\n<html></html>",
            "[C( a comment ){1,1}T(\n){1,19}X(1.0){2,15}(null){2,20}(null){2,20}T(\n){2,22}DT(DOCTYPE){3,3}(html){3,11}(){3,15}(){3,15}(){3,15}(){3,15}T(\n){3,16}OES(<){4,1}OEN(html){4,2}OEE(>){4,6}CES(</){4,7}CEN(html){4,9}CEE(>){4,13}]",
            "[C( a comment ){1,1}T(\n){1,19}X(1.0)(null)(null){2,1}T(\n){2,22}DT(html)()()(){3,1}T(\n){3,16}OE(html){4,1}CE(html){4,7}]", 
            wellFormedXml);
        testDoc( 
            "<!DOCTYPE html>\n<html><?xml version=\"1.0\"?>\n</html>",
            "[DT(DOCTYPE){1,3}(html){1,11}(){1,15}(){1,15}(){1,15}(){1,15}T(\n){1,16}OES(<){2,1}OEN(html){2,2}OEE(>){2,6}X(1.0){2,21}(null){2,26}(null){2,26}T(\n){2,28}CES(</){3,1}CEN(html){3,3}CEE(>){3,7}]", 
            "[DT(html)()()(){1,1}T(\n){1,16}OE(html){2,1}X(1.0)(null)(null){2,7}T(\n){2,28}CE(html){3,1}]",
            noRestrictionsAutoClose);
        testDocError( 
            "<!DOCTYPE html>\n<?xml version=\"1.0\"?>\n<html></html>",
            null, null, 2, 1,
            wellFormedXml);
        testDocError( 
            "<!DOCTYPE html>\n<html><?xml version=\"1.0\"?>\n</html>",
            null, null, 2, 7,
            wellFormedXml);
        testDocError( 
            "<html><?xml version=\"1.0\"?>\n</html>",
            null, null, 1, 7,
            wellFormedXml);
        testDocError( 
            "<html><!DOCTYPE html>\n</html>",
            null, null, 1, 7,
            wellFormedXml);
        testDocError( 
            "<!DOCTYPE html><!DOCTYPE html>",
            null, null, 1, 16,
            wellFormedXml);
        testDoc( 
            "<!DOCTYPE html><!DOCTYPE html>",
            "[DT(DOCTYPE){1,3}(html){1,11}(){1,15}(){1,15}(){1,15}(){1,15}DT(DOCTYPE){1,18}(html){1,26}(){1,30}(){1,30}(){1,30}(){1,30}]", 
            null,
            noRestrictionsAutoClose);
        
        testDoc( 
            "Hello, <br th:text=\"ll\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}(=){1,19}(\"ll\"){1,20}SEE(/>){1,24}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll']){1,8}]", 
            wellFormedXml);
        testDoc( 
            "Hello, <br th:text='ll'/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}(=){1,19}('ll'){1,20}SEE(/>){1,24}]",
            "[T(Hello, ){1,1}SE(br[th:text='ll']){1,8}]", 
            wellFormedXml);
        testDocError( 
            "Hello, <br th:text=ll/>",
            null, null, 1, 20,
            wellFormedXml);
        testDocError( 
            "Hello, <br th:text=/>",
            null, null, 1, 20,
            wellFormedXml);
        testDocError( 
            "Hello, <br th:text/>",
            null, null, 1, 19,
            wellFormedXml);
        testDoc( 
            "<html></html><html></html>",
            "[OES(<){1,1}OEN(html){1,2}OEE(>){1,6}CES(</){1,7}CEN(html){1,9}CEE(>){1,13}OES(<){1,14}OEN(html){1,15}OEE(>){1,19}CES(</){1,20}CEN(html){1,22}CEE(>){1,26}]", 
            "[OE(html){1,1}CE(html){1,7}OE(html){1,14}CE(html){1,20}]", 
            wellFormedXml);
        testDocError( 
            "<!DOCTYPE html><html></html><html></html>",
            null, null, 1, 29, 
            wellFormedXml);
        testDocError( 
            "<!DOCTYPE html><htmla></htmla>",
            null, null, 1, 16,
            wellFormedXml);
        testDocError( 
            "<!DOCTYPE html><htma></htma>",
            null, null, 1, 16,
            wellFormedXml);
        testDocError( 
            "<!DOCTYPE html><htma/>",
            null, null, 1, 16,
            wellFormedXml);
        testDoc( 
            "Hello, <br th:text=\"ll\" th:text=\"la\"/>",
            "[T(Hello, ){1,1}SES(<){1,8}SEN(br){1,9}AS( ){1,11}A(th:text){1,12}(=){1,19}(\"ll\"){1,20}AS( ){1,24}A(th:text){1,25}(=){1,32}(\"la\"){1,33}SEE(/>){1,37}]",
            "[T(Hello, ){1,1}SE(br[th:text='la']){1,8}]", 
            noRestrictionsAutoClose);
        testDocError( 
            "Hello, <br th:text=\"ll\" th:text=\"la\"/>",
            null, null, 1, 25,
            wellFormedXml);
        testDocError( 
            "<!DOCTYPE html>",
            null, null, -1, -1, 
            wellFormedXml);
        
        testDoc( 
                "<h1>Hello</h1>",
                "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}CES(</){1,10}CEN(h1){1,12}CEE(>){1,14}]",
                "[OE(h1){1,1}T(Hello){1,5}CE(h1){1,10}]", 
                noUnbalacedClosed);
        testDocError( 
                "Hello</h1>",
                null,
                null, 
                1, 6,
                noUnbalacedClosed);
        testDoc( 
                "<h1>Hello",
                "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}T(Hello){1,5}ACES(</){1,10}ACEN(h1){1,10}ACEE(>){1,10}]",
                "[OE(h1){1,1}T(Hello){1,5}ACE(h1){1,10}]", 
                noRestrictionsAutoClose);
        testDocError( 
                "<h2>Hello</h1>",
                null,
                null,
                1, 10,
                noUnbalacedClosed);
        testDoc( 
                "<h1><h2>Hello</h1>",
                "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}OES(<){1,5}OEN(h2){1,6}OEE(>){1,8}T(Hello){1,9}ACES(</){1,14}ACEN(h2){1,14}ACEE(>){1,14}CES(</){1,14}CEN(h1){1,16}CEE(>){1,18}]",
                "[OE(h1){1,1}OE(h2){1,5}T(Hello){1,9}ACE(h2){1,14}CE(h1){1,14}]", 
                noUnbalacedClosed);
        testDocError( 
                "Hello</h1>",
                null,
                null,
                1, 6, 
                noUnbalacedClosed);
        testDoc( 
                "<h1><h2>Hello</h2>",
                "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}OES(<){1,5}OEN(h2){1,6}OEE(>){1,8}T(Hello){1,9}CES(</){1,14}CEN(h2){1,16}CEE(>){1,18}ACES(</){1,19}ACEN(h1){1,19}ACEE(>){1,19}]",
                "[OE(h1){1,1}OE(h2){1,5}T(Hello){1,9}CE(h2){1,14}ACE(h1){1,19}]", 
                noUnbalacedClosed);
        testDoc( 
                "<h1><h2>Hello<!--a--></h1>",
                "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}OES(<){1,5}OEN(h2){1,6}OEE(>){1,8}T(Hello){1,9}C(a){1,14}ACES(</){1,22}ACEN(h2){1,22}ACEE(>){1,22}CES(</){1,22}CEN(h1){1,24}CEE(>){1,26}]",
                "[OE(h1){1,1}OE(h2){1,5}T(Hello){1,9}C(a){1,14}ACE(h2){1,22}CE(h1){1,22}]", 
                noUnbalacedClosed);
        testDoc( 
                "<h1></H1>",
                "[OES(<){1,1}OEN(h1){1,2}OEE(>){1,4}CES(</){1,5}CEN(H1){1,7}CEE(>){1,9}]",
                "[OE(h1){1,1}CE(H1){1,5}]", 
                wellFormedXmlCaseInsensitive);
        testDocError( 
                "<h1></H1>",
                null, null, 1, 5, 
                wellFormedXml);
        testDoc( 
                "<!DOCTYPE h1><H1></H1>",
                "[DT(DOCTYPE){1,3}(h1){1,11}(){1,13}(){1,13}(){1,13}(){1,13}OES(<){1,14}OEN(H1){1,15}OEE(>){1,17}CES(</){1,18}CEN(H1){1,20}CEE(>){1,22}]",
                "[DT(h1)()()(){1,1}OE(H1){1,14}CE(H1){1,18}]", 
                wellFormedXmlCaseInsensitive);
        testDocError( 
                "<!DOCTYPE h1><H1></H1>",
                null, null, 1, 14, 
                wellFormedXml);
        testDocError( 
                "<a b=\"2\" B=\"3\"/>",
                null, null, 1, 10, 
                wellFormedXmlCaseInsensitive);
        testDoc( 
                "<a b=\"2\" B=\"3\"/>",
                "[SES(<){1,1}SEN(a){1,2}AS( ){1,3}A(b){1,4}(=){1,5}(\"2\"){1,6}AS( ){1,9}A(B){1,10}(=){1,11}(\"3\"){1,12}SEE(/>){1,15}]", 
                "[SE(a[b='2',B='3']){1,1}]", 
                wellFormedXml);
        
        System.out.println("TOTAL Test executions: " + totalTestExecutions);
        
        
    }
    
    
    
    static void testDocError(final String input, final String outputBreakDown, final String outputSimple, final int errorLine, final int errorCol, final MarkupParsingConfiguration markupParsingConfiguration) {
        try {
            testDoc(input, outputBreakDown, outputSimple, markupParsingConfiguration);
            throw new ComparisonFailure(null, "exception", "no exception");
            
        } catch (final AttoParseException e) {
            if (errorLine != -1) {
                assertEquals(Integer.valueOf(errorLine), e.getLine());
            } else {
                assertNull(e.getLine());
            }
            if (errorCol != -1) {
                assertEquals(Integer.valueOf(errorCol), e.getCol());
            } else {
                assertNull(e.getCol());
            }
        }
    }

    
    static void testDocError(final String input, final String outputBreakDown, final String outputSimple, final int offset, final int len, final int errorLine, final int errorCol, final MarkupParsingConfiguration markupParsingConfiguration) {
        try {
            testDoc(input, outputBreakDown, outputSimple, offset, len, markupParsingConfiguration);
            throw new ComparisonFailure(null, "exception", "no exception");
            
        } catch (final AttoParseException e) {
            if (errorLine != -1 && errorCol != -1) {
                assertEquals(Integer.valueOf(errorLine), e.getLine());
                assertEquals(Integer.valueOf(errorCol), e.getCol());
            } else {
                assertNull(e.getLine());
                assertNull(e.getCol());
            }
        }
    }
    
    
    static void testDoc(final String input, final String outputBreakDown, final String outputSimple, final MarkupParsingConfiguration markupParsingConfiguration) throws AttoParseException {
        testDoc(input.toCharArray(), outputBreakDown, outputSimple, 0, input.length(), markupParsingConfiguration);
    }
    
    static void testDoc(String input, final String outputBreakDown, final String outputSimple, final int offset, final int len, final MarkupParsingConfiguration markupParsingConfiguration) throws AttoParseException {
        testDoc(input.toCharArray(), outputBreakDown, outputSimple, offset, len, markupParsingConfiguration);
    }
    
    static void testDoc(final String input, final String outputBreakDown, final String outputSimple, final int bufferSize, final MarkupParsingConfiguration markupParsingConfiguration) throws AttoParseException {
        testDoc(input.toCharArray(), outputBreakDown, outputSimple, 0, input.length(), bufferSize, markupParsingConfiguration);
    }
    
    static void testDoc(String input, final String outputBreakDown, final String outputSimple, final int offset, final int len, final int bufferSize, final MarkupParsingConfiguration markupParsingConfiguration) throws AttoParseException {
        testDoc(input.toCharArray(), outputBreakDown, outputSimple, offset, len, bufferSize, markupParsingConfiguration);
    }
    
    
    
    
    static void testDoc(final char[] input, final String outputBreakDown, final String outputSimple, 
            final int offset, final int len, final MarkupParsingConfiguration markupParsingConfiguration) throws AttoParseException {

        final int maxBufferSize = 16384;
        for (int bufferSize = 1; bufferSize <= maxBufferSize; bufferSize++) {
            testDoc(input, outputBreakDown, outputSimple, offset, len, bufferSize, markupParsingConfiguration);
        }
        
    }

    
    static void testDoc(
            final char[] input, 
            final String outputBreakDown, final String outputSimple,
            final int offset, final int len, final int bufferSize,
            final MarkupParsingConfiguration markupParsingConfiguration) 
            throws AttoParseException {

        try {

            final IAttoParser parser = new MarkupAttoParser();

            // TEST WITH TRACING HANDLER
            {
                final StringWriter sw = new StringWriter(); 
                final IAttoHandler handler = new TracingDetailedMarkupAttoHandler(sw, markupParsingConfiguration);
                if (offset == 0 && len == input.length) {
                    ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input), handler, bufferSize);
                } else { 
                    ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input, offset, len), handler, bufferSize);
                }
                final String result = sw.toString();
                if (outputBreakDown != null) {
                    assertEquals(outputBreakDown, result);
                }
            }

            
            // TEST WITH DUPLICATING MARKUP HANDLER (few events)
            {
                final StringWriter sw = new StringWriter(); 
                final IAttoHandler handler = new DuplicatingBasicMarkupAttoHandler(sw);
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
                final IAttoHandler handler = new DuplicatingDetailedMarkupAttoHandler(sw, markupParsingConfiguration);
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

            
            if (outputSimple != null) {
                // TEST WITH TRACING SIMPLE MARKUP HANDLER (String literals)
                {
                    final StringWriter sw = new StringWriter(); 
                    final IAttoHandler handler = new TracingStandardMarkupAttoHandler(sw, markupParsingConfiguration);
                    if (offset == 0 && len == input.length) {
                        ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input), handler, bufferSize);
                    } else { 
                        ((AbstractBufferedAttoParser)parser).parseDocument(new CharArrayReader(input, offset, len), handler, bufferSize);
                    }
                    final String result = sw.toString();
                    assertEquals(outputSimple, result);
                }
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
