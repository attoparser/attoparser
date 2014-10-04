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
package org.attoparser.config;

import java.io.Serializable;






/**
 * <p>
 *   Models a series of markup parsing parameterizations that can be applied during document parsing
 *   by {@link org.attoparser.MarkupEventProcessor} (and its subclasses).
 * </p>
 * <p>
 *   For example, a this parameterizations can be used for checking the well-formedness
 *   (from an XML/XHTML standpoint) of a document.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class ParseConfiguration implements Serializable, Cloneable {
    
    public static enum ElementBalancing { 
        REQUIRE_BALANCED, AUTO_CLOSE, AUTO_CLOSE_REQUIRE_NO_UNMATCHED_CLOSE, 
        REQUIRE_NO_UNMATCHED_CLOSE, NO_BALANCING } 

    private static final long serialVersionUID = 5191449744126332911L;

    // Cannot make public because they are mutable
    private static final ParseConfiguration DEFAULT_HTML_PARSE_CONFIGURATION;
    private static final ParseConfiguration DEFAULT_XML_PARSE_CONFIGURATION;


    private ParsingMode mode = ParsingMode.XML;
    private boolean caseSensitive = true;
    
    private ElementBalancing elementBalancing = ElementBalancing.NO_BALANCING;
    
    private boolean requireXmlWellFormedAttributeValues = false;
    private boolean requireUniqueAttributesInElement = false;
 
    private PrologParseConfiguration prologParseConfiguration = new PrologParseConfiguration();
    private UniqueRootElementPresence uniqueRootElementPresence = UniqueRootElementPresence.DEPENDS_ON_PROLOG_DOCTYPE;






    static {

        DEFAULT_HTML_PARSE_CONFIGURATION = new ParseConfiguration();
        DEFAULT_HTML_PARSE_CONFIGURATION.setMode(ParsingMode.HTML);
        DEFAULT_HTML_PARSE_CONFIGURATION.setCaseSensitive(false);
        DEFAULT_HTML_PARSE_CONFIGURATION.setElementBalancing(ElementBalancing.AUTO_CLOSE);
        DEFAULT_HTML_PARSE_CONFIGURATION.setRequireUniqueAttributesInElement(true);
        DEFAULT_HTML_PARSE_CONFIGURATION.setRequireXmlWellFormedAttributeValues(false);
        DEFAULT_HTML_PARSE_CONFIGURATION.setUniqueRootElementPresence(UniqueRootElementPresence.DEPENDS_ON_PROLOG_DOCTYPE);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setValidateProlog(true);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setPrologPresence(PrologPresence.ALLOWED);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setXmlDeclarationPresence(PrologPresence.ALLOWED);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setDoctypePresence(PrologPresence.ALLOWED);
        DEFAULT_HTML_PARSE_CONFIGURATION.getPrologParseConfiguration().setRequireDoctypeKeywordsUpperCase(false);


        DEFAULT_XML_PARSE_CONFIGURATION = new ParseConfiguration();
        DEFAULT_XML_PARSE_CONFIGURATION.setMode(ParsingMode.XML);
        DEFAULT_XML_PARSE_CONFIGURATION.setCaseSensitive(true);
        DEFAULT_XML_PARSE_CONFIGURATION.setElementBalancing(ElementBalancing.REQUIRE_BALANCED);
        DEFAULT_XML_PARSE_CONFIGURATION.setRequireUniqueAttributesInElement(true);
        DEFAULT_XML_PARSE_CONFIGURATION.setRequireXmlWellFormedAttributeValues(true);
        DEFAULT_XML_PARSE_CONFIGURATION.setUniqueRootElementPresence(UniqueRootElementPresence.DEPENDS_ON_PROLOG_DOCTYPE);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setValidateProlog(true);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setPrologPresence(PrologPresence.ALLOWED);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setXmlDeclarationPresence(PrologPresence.ALLOWED);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setDoctypePresence(PrologPresence.ALLOWED);
        DEFAULT_XML_PARSE_CONFIGURATION.getPrologParseConfiguration().setRequireDoctypeKeywordsUpperCase(true);

    }





    public static ParseConfiguration defaultHtmlConfiguration() {
        try {
            return DEFAULT_HTML_PARSE_CONFIGURATION.clone();
        } catch (final CloneNotSupportedException e) {
            // Will never be thrown
            throw new IllegalStateException(e);
        }
    }



    public static ParseConfiguration defaultXmlConfiguration() {
        try {
            return DEFAULT_XML_PARSE_CONFIGURATION.clone();
        } catch (final CloneNotSupportedException e) {
            // Will never be thrown
            throw new IllegalStateException(e);
        }
    }



    /**
     * <p>
     *   Creates a {@link ParseConfiguration} instance enforcing no restrictions at all.
     * </p>
     * <p>
     *   This is the setup:
     * </p>
     * <ul>
     *   <li><tt>{@link #isCaseSensitive()} = true</tt></li>
     *   <li><tt>{@link #getElementBalancing()} = {@link ElementBalancing#NO_BALANCING}</tt></li>
     *   <li><tt>{@link #getRequireXmlWellFormedAttributeValues()} = false</tt></li>
     *   <li><tt>{@link #getRequireUniqueAttributesInElement()} = false</tt></li>
     *   <li><tt>{@link #getPrologParseConfiguration()} = new {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration}()</tt></li>
     *   <li><tt>{@link #getUniqueRootElementPresence()} = {@link UniqueRootElementPresence#DEPENDS_ON_PROLOG_DOCTYPE}</tt></li>
     * </ul>
     */
    public static ParseConfiguration noRestrictions() {
        return new ParseConfiguration();
    }
    
    

    
    
    /**
     * <p>
     *   Creates a {@link ParseConfiguration} instance with
     *   a default configuration.
     * </p>
     * <p>
     *   Default values are the same as created by {@link #noRestrictions()}:
     * </p>
     * <ul>
     *   <li><tt>{@link #isCaseSensitive()} = true</tt></li>
     *   <li><tt>{@link #getElementBalancing()} = {@link ElementBalancing#NO_BALANCING}</tt></li>
     *   <li><tt>{@link #getRequireXmlWellFormedAttributeValues()} = false</tt></li>
     *   <li><tt>{@link #getRequireUniqueAttributesInElement()} = false</tt></li>
     *   <li><tt>{@link #getPrologParseConfiguration()} = new {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration}()</tt></li>
     *   <li><tt>{@link #getUniqueRootElementPresence()} = {@link UniqueRootElementPresence#DEPENDS_ON_PROLOG_DOCTYPE}</tt></li>
     * </ul>
     */
    public ParseConfiguration() {
        super();
    }





    public ParsingMode getMode() {
        return mode;
    }

    public void setMode(final ParsingMode mode) {
        this.mode = mode;
    }




    /**
     * <p>
     *   Determines whether validations performed on the parsed document should be
     *   case sensitive or not (e.g. attribute names, document root element name, element
     *   open vs close elements, etc.)
     * </p>
     * <p>
     *   Default is <b>true</b>.
     * </p>
     * 
     * @return whether validations should be case sensitive or not. 
     */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    
    public void setCaseSensitive(final boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }





    /**
     * <p>
     *   Determines the level of element balancing required at the document being parsed,
     *   enabling auto-closing of elements if needed.
     * </p>
     * <p>
     *   Possible values are:
     * </p>
     * <ul>
     *   <li>{@link ElementBalancing#NO_BALANCING} (default): means no corrections and/or
     *       validations of any kind will be performed. Artifacts will be reported by the
     *       corresponding events without further interpretation.</li>
     *   <li>{@link ElementBalancing#REQUIRE_BALANCED}: will require that all elements
     *       are perfectly balanced, raising an exception if this does not happen.</li>
     *   <li>{@link ElementBalancing#AUTO_CLOSE}: Parser will emit <i>autoclose</i> events
     *       for those elements that are left unclosed, in order to provide a complete XML-style
     *       tag balance.</li>
     *   <li>{@link ElementBalancing#AUTO_CLOSE_REQUIRE_NO_UNMATCHED_CLOSE}: Same as 
     *       {@link ElementBalancing#AUTO_CLOSE} but validating that there are no unmatched
     *       <i>close element</i> artifacts (without a corresponding <i>open element</i> artifact),
     *       raising an exception if any are found.</li>
     *   <li>{@link ElementBalancing#REQUIRE_NO_UNMATCHED_CLOSE}: Will not require complete
     *       balancing nor perform <i>autoclose</i>, but will require that there are no unmatched
     *       <i>close element</i> artifacts (without a corresponding <i>open element</i> artifact),
     *       raising an exception if any are found.</li>
     * </ul>
     * 
     * @return the level of element balancing.
     */
    public ElementBalancing getElementBalancing() {
        return this.elementBalancing;
    }


    public void setElementBalancing(final ElementBalancing elementBalancing) {
        this.elementBalancing = elementBalancing;
    }


    /**
     * <p>
     *   A {@link org.attoparser.config.ParseConfiguration.PrologParseConfiguration} object determining the way
     *   in which prolog (XML Declaration, DOCTYPE) will be dealt with during parsing.
     * </p>
     * 
     * @return the configuration object.
     */
    public PrologParseConfiguration getPrologParseConfiguration() {
        return this.prologParseConfiguration;
    }


    /**
     * <p>
     *   Determine whether element attributes will be required to be well-formed from the XML
     *   standpoint. This means:
     * </p>
     * <ul>
     *   <li>Attributes should always have a value.</li>
     *   <li>Attribute values should be surrounded by double-quotes.</li>
     * </ul>
     * 
     * @return whether attributes should be XML-well-formed or not.
     */
    public boolean getRequireXmlWellFormedAttributeValues() {
        return this.requireXmlWellFormedAttributeValues;
    }


    public void setRequireXmlWellFormedAttributeValues(
            final boolean requireXmlWellFormedAttributeValues) {
        this.requireXmlWellFormedAttributeValues = requireXmlWellFormedAttributeValues;
    }


    /**
     * <p>
     *   Determines whether attributes should never appear duplicated in elements.
     * </p>
     * 
     * @return whether attributes should never appear duplicated in elements.
     */
    public boolean getRequireUniqueAttributesInElement() {
        return this.requireUniqueAttributesInElement;
    }


    public void setRequireUniqueAttributesInElement(final boolean requireUniqueAttributesInElement) {
        this.requireUniqueAttributesInElement = requireUniqueAttributesInElement;
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
        conf.requireUniqueAttributesInElement = this.requireUniqueAttributesInElement;
        conf.requireXmlWellFormedAttributeValues = this.requireXmlWellFormedAttributeValues;
        conf.uniqueRootElementPresence = this.uniqueRootElementPresence;
        conf.prologParseConfiguration = this.prologParseConfiguration.clone();
        return conf;
    }







    public static enum ParsingMode {
        HTML, XML
    }




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
     *   Not all combinations of values of the tt>{@link #getPrologPresence()}</tt>, 
     *   <tt>{@link #getXmlDeclarationPresence()}</tt> and <tt>{@link #getDoctypePresence()}</tt> 
     *   are considered valid. See {@link #validateConfiguration()} for details.
     * </p>
     * 
     * @author Daniel Fern&aacute;ndez
     *  
     * @since 1.1
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