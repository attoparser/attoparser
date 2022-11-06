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
package org.attoparser.config;

import java.io.Serializable;






/**
 * <p>
 *   Models a series of parsing configurations that can be applied during document parsing
 *   by {@link org.attoparser.MarkupParser} and its variants
 *   {@link org.attoparser.simple.SimpleMarkupParser} and {@link org.attoparser.dom.DOMMarkupParser}.
 * </p>
 * <p>
 *   Among others, the parameters that can be configured are:
 * </p>
 * <ul>
 *   <li>The parsing mode: <strong>XML</strong> or <strong>HTML</strong>.</li>
 *   <li>Whether to expect XML-well-formed code or not.</li>
 *   <li>Whether to perform automatic tag balancing or not.</li>
 *   <li>Whether we will allow parsing of markup fragments or just entire documents.</li>
 * </ul>
 * <p>
 *   The {@link #htmlConfiguration()} and {@link #xmlConfiguration()} static methods act as starting points
 *   for configuration. Once one of these pre-initialized configurations has been created, it can be
 *   fine-tuned for the user's needs.
 * </p>
 * <p>
 *   Note these configuration objects are <strong>mutable</strong>, so they should not be modified once they
 *   have been passed to a parser in order to initialize it.
 * </p>
 * <p>
 *   Instances of this class can be <strong>cloned</strong>, so creating a variant of an already-tuned configuration
 *   is easy.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 2.0.0
 *
 */
public final class ParseConfiguration implements Serializable, Cloneable {


    /**
     * <p>
     *   Enumeration representing the possible actions to be taken with regard to element balancing:
     * </p>
     * <ul>
     *   <li>{@link #NO_BALANCING}: Do not perform element balancing checks at all. Events will be
     *       reported as they appear. There is no guarantee that a DOM tree can be built from the
     *       fired events though.</li>
     *   <li>{@link #REQUIRE_BALANCED}: Require that elements are already correctly balanced in markup,
     *       throwing an exception if not. Note that when in HTML mode, this does not require the
     *       specification of optional tags such as <tt>&lt;tbody&gt;</tt>. Also note that this
     *       will automatically consider the
     *       {@link #setNoUnmatchedCloseElementsRequired(boolean)} flag to be set to <tt>true</tt>.</li>
     *   <li>{@link #AUTO_OPEN_CLOSE}: Auto open and close elements, which includes both those elements that,
     *       according to the HTML spec (when in HTML mode) have optional start or end tags (see
     *       <a href="http://www.w3.org/html/wg/drafts/html/master/syntax.html#optional-tags">http://www.w3.org/html/wg/drafts/html/master/syntax.html#optional-tags</a>)
     *       and those that simply are unclosed at the moment a parent element needs to be closed (so their closing
     *       is forced). As an example of optional tags, the HTML5 spec
     *       establishes that &lt;html&gt;, &lt;body&gt; and &lt;tbody&gt; are optional, and
     *       that an <tt>&lt;li&gt;</tt> will close any currently
     *       open <tt>&lt;li&gt;</tt> elements. This is not really
     *       <em>ill-formed code</em>, but something allowed by the spec. All of these will be
     *       reported as auto-* events by the parser.</li>
     *   <li>{@link #AUTO_CLOSE}: Equivalent to {@link #AUTO_OPEN_CLOSE} but not performing any auto-open
     *       operations, so that processing of HTML fragments is possible (no <tt>&lt;html&gt;</tt> or
     *       <tt>&lt;body&gt;</tt> elements are automatically added).</li>
     * </ul>
     * <p>
     *   This enumeration is used at the {@link org.attoparser.config.ParseConfiguration} class.
     * </p>
     */
    public static enum ElementBalancing {
        NO_BALANCING,
        REQUIRE_BALANCED,
        AUTO_OPEN_CLOSE,
        AUTO_CLOSE }



    private static final long serialVersionUID = 5191449744126332911L;

    // Cannot make public because they are mutable
    private static final ParseConfiguration DEFAULT_HTML_PARSE_CONFIGURATION;
    private static final ParseConfiguration DEFAULT_XML_PARSE_CONFIGURATION;


    private ParsingMode mode = ParsingMode.XML;
    private boolean caseSensitive = true;

    private boolean textSplittable = false;
    
    private ElementBalancing elementBalancing = ElementBalancing.NO_BALANCING;

    private boolean noUnmatchedCloseElementsRequired = false;
    private boolean xmlWellFormedAttributeValuesRequired = false;
    private boolean uniqueAttributesInElementRequired = false;
 
    private PrologParseConfiguration prologParseConfiguration = new PrologParseConfiguration();
    private UniqueRootElementPresence uniqueRootElementPresence = UniqueRootElementPresence.DEPENDS_ON_PROLOG_DOCTYPE;





    static {

        DEFAULT_HTML_PARSE_CONFIGURATION = new ParseConfiguration();
        DEFAULT_HTML_PARSE_CONFIGURATION.setMode(ParsingMode.HTML);
        DEFAULT_HTML_PARSE_CONFIGURATION.setTextSplittable(false);
        DEFAULT_HTML_PARSE_CONFIGURATION.setElementBalancing(ElementBalancing.AUTO_CLOSE);
        DEFAULT_HTML_PARSE_CONFIGURATION.setNoUnmatchedCloseElementsRequired(false);
        DEFAULT_HTML_PARSE_CONFIGURATION.setUniqueAttributesInElementRequired(false);
        DEFAULT_HTML_PARSE_CONFIGURATION.setXmlWellFormedAttributeValuesRequired(false);
        DEFAULT_HTML_PARSE_CONFIGURATION.setUniqueRootElementPresence(UniqueRootElementPresence.NOT_VALIDATED);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setValidateProlog(false);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setPrologPresence(PrologPresence.ALLOWED);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setXmlDeclarationPresence(PrologPresence.ALLOWED);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setDoctypePresence(PrologPresence.ALLOWED);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setRequireDoctypeKeywordsUpperCase(false);


        DEFAULT_XML_PARSE_CONFIGURATION = new ParseConfiguration();
        DEFAULT_XML_PARSE_CONFIGURATION.setMode(ParsingMode.XML);
        DEFAULT_XML_PARSE_CONFIGURATION.setTextSplittable(false);
        DEFAULT_XML_PARSE_CONFIGURATION.setElementBalancing(ElementBalancing.REQUIRE_BALANCED);
        DEFAULT_XML_PARSE_CONFIGURATION.setNoUnmatchedCloseElementsRequired(true);
        DEFAULT_XML_PARSE_CONFIGURATION.setUniqueAttributesInElementRequired(true);
        DEFAULT_XML_PARSE_CONFIGURATION.setXmlWellFormedAttributeValuesRequired(true);
        DEFAULT_XML_PARSE_CONFIGURATION.setUniqueRootElementPresence(UniqueRootElementPresence.DEPENDS_ON_PROLOG_DOCTYPE);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setValidateProlog(true);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setPrologPresence(PrologPresence.ALLOWED);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setXmlDeclarationPresence(PrologPresence.ALLOWED);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setDoctypePresence(PrologPresence.ALLOWED);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setRequireDoctypeKeywordsUpperCase(true);

    }


    /**
     * <p>
     *   Return an instance of {@link org.attoparser.config.ParseConfiguration} containing a valid configuration
     *   set for most HTML scenarios.
     * </p>
     * <ul>
     *     <li>Mode: {@link org.attoparser.config.ParseConfiguration.ParsingMode#HTML}</li>
     *     <li>Text splittable: false</li>
     *     <li>Element balancing: {@link org.attoparser.config.ParseConfiguration.ElementBalancing#AUTO_CLOSE}</li>
     *     <li>No unmatched close elements required: false</li>
     *     <li>Unique attributes in elements required: false</li>
     *     <li>Xml-well-formed attribute values required: false</li>
     *     <li>Unique root element presence: {@link org.attoparser.config.ParseConfiguration.UniqueRootElementPresence#NOT_VALIDATED}</li>
     *     <li>Validate Prolog: false</li>
     * </ul>
     *
     * @return a valid default configuration object for HTML parsing.
     */
    public static ParseConfiguration htmlConfiguration() {
        try {
            return DEFAULT_HTML_PARSE_CONFIGURATION.clone();
        } catch (final CloneNotSupportedException e) {
            // Will never be thrown
            throw new IllegalStateException(e);
        }
    }



    /**
     * <p>
     *   Return an instance of {@link org.attoparser.config.ParseConfiguration} containing a valid configuration
     *   set for most XML scenarios.
     * </p>
     * <ul>
     *     <li>Mode: {@link org.attoparser.config.ParseConfiguration.ParsingMode#XML}</li>
     *     <li>Text splittable: false</li>
     *     <li>Element balancing: {@link org.attoparser.config.ParseConfiguration.ElementBalancing#REQUIRE_BALANCED}</li>
     *     <li>No unmatched close elements required: true</li>
     *     <li>Unique attributes in elements required: true</li>
     *     <li>Xml-well-formed attribute values required: true</li>
     *     <li>Unique root element presence: {@link org.attoparser.config.ParseConfiguration.UniqueRootElementPresence#DEPENDS_ON_PROLOG_DOCTYPE}</li>
     *     <li>Validate Prolog: true</li>
     *     <li>Prolog presence: {@link org.attoparser.config.ParseConfiguration.PrologPresence#ALLOWED}</li>
     *     <li>XML Declaration presence: {@link org.attoparser.config.ParseConfiguration.PrologPresence#ALLOWED}</li>
     *     <li>DOCTYPE presence: {@link org.attoparser.config.ParseConfiguration.PrologPresence#ALLOWED}</li>
     *     <li>Require DOCTYPE keyword to be uppercase: true</li>
     * </ul>
     *
     * @return a valid default configuration object for XML parsing.
     */
    public static ParseConfiguration xmlConfiguration() {
        try {
            return DEFAULT_XML_PARSE_CONFIGURATION.clone();
        } catch (final CloneNotSupportedException e) {
            // Will never be thrown
            throw new IllegalStateException(e);
        }
    }





    /*
     * No need to make this public. Instances of ParseConfiguration should be created from the static
     * factory methods for XML and HTML config.
     */
    private ParseConfiguration() {
        super();
    }




    /**
     * <p>
     *   Return the parsing mode to be used. Can be <strong>XML</strong> or <strong>HTML</strong>.
     * </p>
     * <p>
     *   Depending on the selected mode parsers will behave differently, given HTML has some specific
     *   rules which are not XML-compatible (like void elements which might appear <em>unclosed</em> like
     *   <tt>&lt;meta&gt;</tt>.
     * </p>
     *
     * @return the parsing mode to be used.
     */
    public ParsingMode getMode() {
        return mode;
    }


    /**
     * <p>
     *   Specify the parsing mode to be used. Can be <strong>XML</strong> or <strong>HTML</strong>.
     * </p>
     * <p>
     *   Depending on the selected mode parsers will behave differently, given HTML has some specific
     *   rules which are not XML-compatible (like void elements which might appear <em>unclosed</em> like
     *   <tt>&lt;meta&gt;</tt>.
     * </p>
     *
     * @param mode the parsing mode to be used.
     */
    public void setMode(final ParsingMode mode) {
        this.mode = mode;
        if (ParsingMode.HTML.equals(this.mode)) {
            // We can never use HTML parsing in case-sensitive mode
            this.caseSensitive = false;
        }
    }




    /**
     * <p>
     *   Returns whether validations performed on the parsed document should be
     *   case sensitive or not (e.g. attribute names, document root element name, element
     *   open vs close elements, etc.)
     * </p>
     * <p>
     *   HTML requires this parameter to be <tt>false</tt>. Default for XML is <tt>true</tt>.
     * </p>
     * 
     * @return whether validations should be case sensitive or not. 
     */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }


    /**
     * <p>
     *   Specify whether validations performed on the parsed document should be
     *   case sensitive or not (e.g. attribute names, document root element name, element
     *   open vs close elements, etc.)
     * </p>
     * <p>
     *   HTML requires this parameter to be <tt>false</tt>. Default for XML is <tt>true</tt>.
     * </p>
     *
     * @param caseSensitive whether validations should be case sensitive or not.
     */
    public void setCaseSensitive(final boolean caseSensitive) {
        if (caseSensitive && ParsingMode.HTML.equals(this.mode)) {
            throw new IllegalArgumentException(
                    "Cannot set parser as case-sensitive for HTML mode. Use XML mode instead.");
        }
        this.caseSensitive = caseSensitive;
    }




    /**
     * <p>
     *   Returns whether text fragments in markup can be split in more than one text node, if it
     *   occupies more than an entire buffer in size.
     * </p>
     * <p>
     *   Default is <tt>false</tt>.
     * </p>
     *
     * @return whether text fragments can be split or not.
     */
    public boolean isTextSplittable() {
        return this.textSplittable;
    }


    /**
     * <p>
     *   Specify whether text fragments in markup can be split in more than one text node, if it
     *   occupies more than an entire buffer in size.
     * </p>
     * <p>
     *   Default is <tt>false</tt>.
     * </p>
     *
     * @param textSplittable whether text fragments can be split or not.
     */
    public void setTextSplittable(final boolean textSplittable) {
        this.textSplittable = textSplittable;
    }




    /**
     * <p>
     *   Returns the level of element balancing required at the document being parsed,
     *   enabling auto-closing of elements if needed.
     * </p>
     * <p>
     *   Possible values are:
     * </p>
     * <ul>
     *   <li>{@link ElementBalancing#NO_BALANCING}: Do not perform element balancing checks at all. Events will be
     *       reported as they appear. There is no guarantee that a DOM tree can be built from the
     *       fired events though.</li>
     *   <li>{@link ElementBalancing#REQUIRE_BALANCED}: Require that elements are already correctly balanced in markup,
     *       throwing an exception if not. Note that when in HTML mode, this does not require the
     *       specification of optional tags such as <tt>&lt;tbody&gt;</tt>. Also note that this
     *       will automatically consider the
     *       {@link #setNoUnmatchedCloseElementsRequired(boolean)} flag to be set to <tt>true</tt>.</li>
     *   <li>{@link ElementBalancing#AUTO_OPEN_CLOSE}: Auto open and close elements, which includes both those elements that,
     *       according to the HTML spec (when in HTML mode) have optional start or end tags (see
     *       <a href="http://www.w3.org/html/wg/drafts/html/master/syntax.html#optional-tags">http://www.w3.org/html/wg/drafts/html/master/syntax.html#optional-tags</a>)
     *       and those that simply are unclosed at the moment a parent element needs to be closed (so their closing
     *       is forced). As an example of optional tags, the HTML5 spec
     *       establishes that <tt>&lt;html&gt;</tt>, <tt>&lt;body&gt;</tt> and <tt>&lt;tbody&gt;</tt> are optional, and
     *       that an <tt>&lt;li&gt;</tt> will close any currently
     *       open <tt>&lt;li&gt;</tt> elements. This is not really
     *       <em>ill-formed code</em>, but something allowed by the spec. All of these will be
     *       reported as auto-* events by the parser.</li>
     *   <li>{@link ElementBalancing#AUTO_CLOSE}: Equivalent to {@link ElementBalancing#AUTO_OPEN_CLOSE} but not performing any auto-open
     *       operations, so that processing of HTML fragments is possible (no <tt>&lt;html&gt;</tt> or
     *       <tt>&lt;body&gt;</tt> elements are automatically added).</li>
     * </ul>
     * 
     * @return the level of element balancing.
     */
    public ElementBalancing getElementBalancing() {
        return this.elementBalancing;
    }


    /**
     * <p>
     *   Specify the level of element balancing required at the document being parsed,
     *   enabling auto-closing of elements if needed.
     * </p>
     * <p>
     *   Possible values are:
     * </p>
     * <ul>
     *   <li>{@link ElementBalancing#NO_BALANCING}: Do not perform element balancing checks at all. Events will be
     *       reported as they appear. There is no guarantee that a DOM tree can be built from the
     *       fired events though.</li>
     *   <li>{@link ElementBalancing#REQUIRE_BALANCED}: Require that elements are already correctly balanced in markup,
     *       throwing an exception if not. Note that when in HTML mode, this does not require the
     *       specification of optional tags such as <tt>&lt;tbody&gt;</tt>. Also note that this
     *       will automatically consider the
     *       {@link #setNoUnmatchedCloseElementsRequired(boolean)} flag to be set to <tt>true</tt>.</li>
     *   <li>{@link ElementBalancing#AUTO_OPEN_CLOSE}: Auto open and close elements, which includes both those elements that,
     *       according to the HTML spec (when in HTML mode) have optional start or end tags (see
     *       <a href="http://www.w3.org/html/wg/drafts/html/master/syntax.html#optional-tags">http://www.w3.org/html/wg/drafts/html/master/syntax.html#optional-tags</a>)
     *       and those that simply are unclosed at the moment a parent element needs to be closed (so their closing
     *       is forced). As an example of optional tags, the HTML5 spec
     *       establishes that &lt;html&gt;, &lt;body&gt; and &lt;tbody&gt; are optional, and
     *       that an <tt>&lt;li&gt;</tt> will close any currently
     *       open <tt>&lt;li&gt;</tt> elements. This is not really
     *       <em>ill-formed code</em>, but something allowed by the spec. All of these will be
     *       reported as auto-* events by the parser.</li>
     *   <li>{@link ElementBalancing#AUTO_CLOSE}: Equivalent to {@link ElementBalancing#AUTO_OPEN_CLOSE} but not performing any auto-open
     *       operations, so that processing of HTML fragments is possible (no <tt>&lt;html&gt;</tt> or
     *       <tt>&lt;body&gt;</tt> elements are automatically added).</li>
     * </ul>
     *
     * @param elementBalancing the level of element balancing.
     */
    public void setElementBalancing(final ElementBalancing elementBalancing) {
        this.elementBalancing = elementBalancing;
    }




    /**
     * <p>
     *   Returns the {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration} object determining the
     *   way in which prolog (XML Declaration, DOCTYPE) will be dealt with during parsing.
     * </p>
     * 
     * @return the configuration object.
     */
    public PrologParseConfiguration getPrologParseConfiguration() {
        return this.prologParseConfiguration;
    }




    /**
     * <p>
     *   Returns whether unmatched close elements (those not matching any equivalent open elements) are
     *   allowed or not.
     * </p>
     *
     * @return whether unmatched close elements will be allowed (<tt>false</tt>) or not (<tt>true</tt>).
     */
    public boolean isNoUnmatchedCloseElementsRequired() {
        return this.noUnmatchedCloseElementsRequired;
    }


    /**
     * <p>
     *   Specify whether unmatched close elements (those not matching any equivalent open elements) are
     *   allowed or not.
     * </p>
     *
     * @param noUnmatchedCloseElementsRequired whether unmatched close elements will be allowed
     *                                         (<tt>false</tt>) or not (<tt>true</tt>).
     */
    public void setNoUnmatchedCloseElementsRequired(
            final boolean noUnmatchedCloseElementsRequired) {
        this.noUnmatchedCloseElementsRequired = noUnmatchedCloseElementsRequired;
    }




    /**
     * <p>
     *   Returns whether element attributes will be required to be well-formed from the XML
     *   standpoint. This means:
     * </p>
     * <ul>
     *   <li>Attributes should always have a value.</li>
     *   <li>Attribute values should be surrounded by double-quotes.</li>
     * </ul>
     *
     * @return whether attributes should be XML-well-formed or not.
     */
    public boolean isXmlWellFormedAttributeValuesRequired() {
        return this.xmlWellFormedAttributeValuesRequired;
    }


    /**
     * <p>
     *   Specify whether element attributes will be required to be well-formed from the XML
     *   standpoint. This means:
     * </p>
     * <ul>
     *   <li>Attributes should always have a value.</li>
     *   <li>Attribute values should be surrounded by double-quotes.</li>
     * </ul>
     *
     * @param xmlWellFormedAttributeValuesRequired whether attributes should be XML-well-formed or not.
     */
    public void setXmlWellFormedAttributeValuesRequired(
            final boolean xmlWellFormedAttributeValuesRequired) {
        this.xmlWellFormedAttributeValuesRequired = xmlWellFormedAttributeValuesRequired;
    }




    /**
     * <p>
     *   Returns whether attributes should never appear duplicated in elements.
     * </p>
     *
     * @return whether attributes should never appear duplicated in elements.
     */
    public boolean isUniqueAttributesInElementRequired() {
        return this.uniqueAttributesInElementRequired;
    }


    /**
     * <p>
     *   Returns whether attributes should never appear duplicated in elements.
     * </p>
     *
     * @param uniqueAttributesInElementRequired whether attributes should never appear duplicated in elements.
     */
    public void setUniqueAttributesInElementRequired(final boolean uniqueAttributesInElementRequired) {
        this.uniqueAttributesInElementRequired = uniqueAttributesInElementRequired;
    }




    /**
     * <p>
     *   This value determines whether it will be required that the document has a unique
     *   root element.
     * </p>
     * <p>
     *   If set to {@link UniqueRootElementPresence#REQUIRED_ALWAYS}, then a document with
     *   more than one elements at the root level will never be considered valid. And if
     *   {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration#isValidateProlog()} is true and there is a DOCTYPE
     *   clause, it will be checked that the root name established at the DOCTYPE clause
     *   is the same as the document's element root.
     * </p>
     * <p>
     *   If set to {@link UniqueRootElementPresence#DEPENDS_ON_PROLOG_DOCTYPE}, then:
     * </p>
     * <ul>
     *   <li>If {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration#isValidateProlog()} is false, multiple
     *       document root elements will be allowed.</li>
     *   <li>If {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration#isValidateProlog()} is true:
     *       <ul>
     *         <li>If there is a DOCTYPE clause, a unique element root will be required,
     *             and its name will be checked against the name specified at the DOCTYPE
     *             clause.</li>
     *         <li>If there is no DOCTYPE clause (even if it is forbidden), multiple 
     *             document root elements will be allowed.</li>
     *       </ul>
     *   </li>
     * </ul>
     * <p>
     *   If set to {@link UniqueRootElementPresence#NOT_VALIDATED}, then nothing will be checked
     *   regarding the name of the root element/s.
     * </p>
     * <p>
     *   Default value is <b>{@link UniqueRootElementPresence#DEPENDS_ON_PROLOG_DOCTYPE}</b>.
     * </p>
     * 
     * @return the configuration value for validating the presence of a unique root element.
     */
    public UniqueRootElementPresence getUniqueRootElementPresence() {
        return this.uniqueRootElementPresence;
    }


    /**
     * <p>
     *   This value determines whether it will be required that the document has a unique
     *   root element.
     * </p>
     * <p>
     *   If set to {@link UniqueRootElementPresence#REQUIRED_ALWAYS}, then a document with
     *   more than one elements at the root level will never be considered valid. And if
     *   {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration#isValidateProlog()} is true and there is a DOCTYPE
     *   clause, it will be checked that the root name established at the DOCTYPE clause
     *   is the same as the document's element root.
     * </p>
     * <p>
     *   If set to {@link UniqueRootElementPresence#DEPENDS_ON_PROLOG_DOCTYPE}, then:
     * </p>
     * <ul>
     *   <li>If {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration#isValidateProlog()} is false, multiple
     *       document root elements will be allowed.</li>
     *   <li>If {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration#isValidateProlog()} is true:
     *       <ul>
     *         <li>If there is a DOCTYPE clause, a unique element root will be required,
     *             and its name will be checked against the name specified at the DOCTYPE
     *             clause.</li>
     *         <li>If there is no DOCTYPE clause (even if it is forbidden), multiple
     *             document root elements will be allowed.</li>
     *       </ul>
     *   </li>
     * </ul>
     * <p>
     *   If set to {@link UniqueRootElementPresence#NOT_VALIDATED}, then nothing will be checked
     *   regarding the name of the root element/s.
     * </p>
     * <p>
     *   Default value is <b>{@link UniqueRootElementPresence#DEPENDS_ON_PROLOG_DOCTYPE}</b>.
     * </p>
     *
     * @param uniqueRootElementPresence the configuration value for validating the presence of a unique root element.
     */
    public void setUniqueRootElementPresence(final UniqueRootElementPresence uniqueRootElementPresence) {
        validateNotNull(uniqueRootElementPresence, "The \"unique root element presence\" configuration value cannot be null");
        this.uniqueRootElementPresence = uniqueRootElementPresence;
    }




    
    @Override
    public ParseConfiguration clone() throws CloneNotSupportedException {
        final ParseConfiguration conf = (ParseConfiguration) super.clone();
        conf.mode = this.mode;
        conf.caseSensitive = this.caseSensitive;
        conf.elementBalancing = this.elementBalancing;
        conf.uniqueAttributesInElementRequired = this.uniqueAttributesInElementRequired;
        conf.xmlWellFormedAttributeValuesRequired = this.xmlWellFormedAttributeValuesRequired;
        conf.uniqueRootElementPresence = this.uniqueRootElementPresence;
        conf.prologParseConfiguration = this.prologParseConfiguration.clone();
        return conf;
    }


    /**
     * <p>
     *   Enumeration used for determining the parsing mode, which will affect the parser's behaviour.
     *   Values are <strong>XML</strong> and <strong>HTML</strong>.
     * </p>
     * <p>
     *   This enumeration is used at the {@link org.attoparser.config.ParseConfiguration} class.
     * </p>
     */
    public static enum ParsingMode {
        HTML, XML
    }


    /**
     * <p>
     *   Enumeration used for determining whether an element in the document prolog (DOCTYPE, XML Declaration) or
     *   the prolog itself should be allowed, required or even forbidden.
     * </p>
     * <p>
     *   This enumeration is used at the {@link org.attoparser.config.ParseConfiguration} class.
     * </p>
     */
    public static enum PrologPresence {
        
        REQUIRED(true, false, false), 
        ALLOWED(false, true, false), 
        FORBIDDEN(false, false, true); 
        
        private final boolean required;
        private final boolean allowed;
        private final boolean forbidden;
    
        private PrologPresence(
                final boolean required, final boolean allowed, final boolean forbidden) {
            this.required = required;
            this.allowed = allowed;
            this.forbidden = forbidden;
        }

        public boolean isRequired() {
            return this.required;
        }

        public boolean isAllowed() {
            return this.allowed;
        }

        public boolean isForbidden() {
            return this.forbidden;
        }
    
    }


    /**
     * <p>
     *   Enumeration used for determining the behaviour the parser should have with respect to the presence and
     *   number of root elements in the parsed document.
     * </p>
     * <p>
     *   Root elements are the elements that appear at the root of the document (e.g. <tt>&lt;html&gt;</tt> in
     *   complete HTML documents). This enumeration allows requiring that the root element is unique always,
     *   requiring it only if a document prolog (XML Declaration or DOCTYPE) is present, or not validating
     *   this at all.
     * </p>
     * <p>
     *   This enumeration is used at the {@link org.attoparser.config.ParseConfiguration} class.
     * </p>
     */
    public static enum UniqueRootElementPresence { 
        
        REQUIRED_ALWAYS(true, false), 
        DEPENDS_ON_PROLOG_DOCTYPE(false, true),
        NOT_VALIDATED(false, false);

        private final boolean requiredAlways;
        private final boolean dependsOnPrologDoctype;
    
        private UniqueRootElementPresence(
                final boolean requiredAlways, final boolean dependsOnPrologDoctype) {
            this.requiredAlways = requiredAlways;
            this.dependsOnPrologDoctype = dependsOnPrologDoctype;
        }

        public boolean isRequiredAlways() {
            return this.requiredAlways;
        }

        public boolean isDependsOnPrologDoctype() {
            return this.dependsOnPrologDoctype;
        }
    
    }
    
    
    
    
    

    /**
     * <p>
     *   Class encapsulating the configuration parameters used for parsing
     *   and validating the "prolog" section of a markup document. The prolog
     *   is the section of an XML/HTML document containing the XML declaration
     *   and the DOCTYPE clause (if these exist).
     * </p>
     * <p>
     *   If <tt>validateProlog</tt> is set to false, all other parameters
     *   should be ignored.
     * </p>
     * <p>
     *   If <tt>validateProlog</tt> is true, then the rest of the parameters
     *   will be considered.
     * </p>
     * <p>
     *   Not all combinations of values of the <tt>{@link #getPrologPresence()}</tt>, 
     *   <tt>{@link #getXmlDeclarationPresence()}</tt> and <tt>{@link #getDoctypePresence()}</tt> 
     *   are considered valid. See {@link #validateConfiguration()} for details.
     * </p>
     * 
     * @author Daniel Fern&aacute;ndez
     *  
     * @since 2.0.0
     */
    public static class PrologParseConfiguration implements Serializable, Cloneable {
        
        private static final long serialVersionUID = -4291053503740751549L;
        
        
        private boolean validateProlog = false;
        private PrologPresence prologPresence = PrologPresence.ALLOWED;
        private PrologPresence xmlDeclarationPresence = PrologPresence.ALLOWED;
        private PrologPresence doctypePresence = PrologPresence.ALLOWED;
        private boolean requireDoctypeKeywordsUpperCase = true;
        
        
        /**
         * <p>
         *   Creates a {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration} instance with
         *   a default configuration.
         * </p>
         * <p>
         *   Default values are:
         * </p>
         * <ul>
         *   <li><tt>{@link #isValidateProlog()} = false</tt></li>
         *   <li><tt>{@link #getPrologPresence()} = {@link PrologPresence#ALLOWED}</tt></li>
         *   <li><tt>{@link #getXmlDeclarationPresence()} = {@link PrologPresence#ALLOWED}</tt></li>
         *   <li><tt>{@link #getDoctypePresence()} = {@link PrologPresence#ALLOWED}</tt></li>
         *   <li><tt>{@link #isRequireDoctypeKeywordsUpperCase()} = true</tt></li>
         * </ul>
         * 
         */
        protected PrologParseConfiguration() {
            super();
        }

        /**
         * <p>
         *   This flag indicates whether the document's prolog should be validated
         *   at all or not. 
         * </p>
         * <p>
         *   If not validated, prolog-specific structures (XML Declaration
         *   and DOCTYPE) will be allowed to appear anywhere in the document.
         *   All other configuration paramters in this object will be ignored.
         * </p>
         * <p>
         *   If validated, prolog-specific structures will only be allowed to
         *   appear (under the conditions established in this object) at the
         *   beginning of the document, before the element root. Or if 
         *   {@link #getPrologPresence()} is set to {@link PrologPresence#FORBIDDEN},
         *   it will be validated that such structures do not appear at all.
         * </p>
         * <p>
         *   Also, if validated and a DOCTYPE is present, it will be checked
         *   that there is only one root element in the document and its name
         *   matches the root element name in the DOCTYPE clause.
         * </p>
         * <p>
         *   Default value is <b>false</b>.
         * </p>
         * 
         * @return whether prolog is to be validated or not.
         */
        public boolean isValidateProlog() {
            return this.validateProlog;
        }

        public void setValidateProlog(final boolean validateProlog) {
            this.validateProlog = validateProlog;
        }

        /**
         * <p>
         *  This flag indicates the level of presence desired for the prolog
         *  in the document, in case {@link #isValidateProlog()} has been set
         *  to true.
         * </p>
         * 
         * @return the level of presence desired for the prolog.
         */
        public PrologPresence getPrologPresence() {
            return this.prologPresence;
        }

        public void setPrologPresence(final PrologPresence prologPresence) {
            validateNotNull(this.prologPresence, "Prolog presence cannot be null");
            this.prologPresence = prologPresence;
        }

        /**
         * <p>
         *  This flag indicates the level of presence desired for the XML Declaration
         *  (a part of the prolog) in the document, in case {@link #isValidateProlog()} 
         *  has been set to true.
         * </p>
         * 
         * @return the level of presence desired for the XML Declaration.
         */
        public PrologPresence getXmlDeclarationPresence() {
            return this.xmlDeclarationPresence;
        }

        public void setXmlDeclarationPresence(final PrologPresence xmlDeclarationPresence) {
            validateNotNull(this.prologPresence, "XML Declaration presence cannot be null");
            this.xmlDeclarationPresence = xmlDeclarationPresence;
        }

        /**
         * <p>
         *  This flag indicates the level of presence desired for the DOCTYPE clause
         *  (a part of the prolog) in the document, in case {@link #isValidateProlog()} 
         *  has been set to true.
         * </p>
         * 
         * @return the level of presence desired for the DOCTYPE clause.
         */
        public PrologPresence getDoctypePresence() {
            return this.doctypePresence;
        }

        public void setDoctypePresence(final PrologPresence doctypePresence) {
            validateNotNull(this.prologPresence, "DOCTYPE presence cannot be null");
            this.doctypePresence = doctypePresence;
        }

        /**
         * <p>
         *   This configuration parameter allows to check that all keywords in
         *   a DOCTYPE clause ('DOCTYPE', 'SYSTEM', 'PUBLIC') are in upper-case as
         *   required by the XML specification (and not by the HTML5 one, for example).
         * </p>
         * <p>
         *   Default value is <b>true</b>, but it will apply only if
         *   {@link #isValidateProlog()} is true.
         * </p>
         * 
         * @return whether keywords in the DOCTYPE clause will be forced to be
         *         in upper-case.
         */
        public boolean isRequireDoctypeKeywordsUpperCase() {
            return this.requireDoctypeKeywordsUpperCase;
        }

        public void setRequireDoctypeKeywordsUpperCase(final boolean requireDoctypeKeywordsUpperCase) {
            this.requireDoctypeKeywordsUpperCase = requireDoctypeKeywordsUpperCase;
        }


        
        /**
         * <p>
         *   Checks that the combination of values in the <tt>{@link #getPrologPresence()}</tt>, 
         *   <tt>{@link #getXmlDeclarationPresence()}</tt> and <tt>{@link #getDoctypePresence()}</tt> 
         *   parameters makes sense.
         * </p>
         * <ol>
         *   <li>If {@link #getPrologPresence()} is {@link PrologPresence#FORBIDDEN}, then
         *       {@link #getXmlDeclarationPresence()} and {@link #getDoctypePresence()} must
         *       be {@link PrologPresence#FORBIDDEN} too.</li>
         *   <li>Else if at least one of {@link #getXmlDeclarationPresence()} or
         *       {@link #getDoctypePresence()} is {@link PrologPresence#REQUIRED}, the
         *       configuration is considered valid.</li>
         *   <li>Else if {@link #getPrologPresence()} is {@link PrologPresence#ALLOWED}, 
         *       the configuration is considered valid as long as not both
         *       {@link #getXmlDeclarationPresence()} and {@link #getDoctypePresence()}
         *       are {@link PrologPresence#FORBIDDEN}.</li>
         * </ol>
         * 
         * @throws IllegalArgumentException if the combination of values is not correct.
         */
        public void validateConfiguration() {

            /*
             * 
             *   1. PROLOG: REQUIRED, ALLOWED, FORBIDDEN
             *   2. XMLDECL : REQUIRED, ALLOWED, FORBIDDEN
             *   3. DOCTYPE : REQUIRED, ALLOWED, FORBIDDEN
             *   
             *   VALID: (RRR, RRA, RRF, RAR, RFR, ARR, ARA, ARF, AAR, AAA, AAF, AFR, AFA, FFF)
             *   NOT VALID: (RAA, RAF, RFA, RFF, AFF, FRR, FRA, FRF, FAR, FAA, FAF, FFR, FFA)
             *   
             *   FORMULA:
             *   [IF (* = F__) -> RET (* = _FF); IF (* = _R_ OR * = __R) -> RET TRUE; IF (* = A__) -> RET (* != _FF); RET FALSE] 
             * 
             */
            
            if (!this.validateProlog) {
                // There's nothing to check here!
                return;
            }
            
            if (PrologPresence.FORBIDDEN.equals(this.prologPresence)) {
                if (PrologPresence.FORBIDDEN.equals(this.xmlDeclarationPresence) && 
                        PrologPresence.FORBIDDEN.equals(this.doctypePresence)) {
                    return;
                }
            } else {
                if (PrologPresence.REQUIRED.equals(this.xmlDeclarationPresence) ||
                        PrologPresence.REQUIRED.equals(this.doctypePresence)) {
                    return;
                }
                if (PrologPresence.ALLOWED.equals(this.prologPresence)) {
                    if (!(PrologPresence.FORBIDDEN.equals(this.xmlDeclarationPresence) && 
                            PrologPresence.FORBIDDEN.equals(this.doctypePresence))) {
                        return;
                    }
                }
            }
            
            throw new IllegalArgumentException(
                    "Prolog parsing configuration is not valid: " +
                    "Prolog presence: " + this.prologPresence + ", " +
                    "XML Declaration presence: " + this.xmlDeclarationPresence + ", " +
                    "DOCTYPE presence: " + this.doctypePresence);
            
        }

        
        
        @Override
        public PrologParseConfiguration clone() throws CloneNotSupportedException {
            final PrologParseConfiguration conf = (PrologParseConfiguration) super.clone();
            conf.validateProlog = this.validateProlog;
            conf.prologPresence = this.prologPresence;
            conf.doctypePresence = this.doctypePresence;
            conf.xmlDeclarationPresence = this.xmlDeclarationPresence;
            conf.requireDoctypeKeywordsUpperCase = this.requireDoctypeKeywordsUpperCase;
            return conf;
        }
        
        
    }

    
    
    private static void validateNotNull(final Object obj, final String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
    
        
}