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
package org.attoparser.markup.html;

import java.io.Serializable;

import org.attoparser.markup.MarkupParsingConfiguration.UniqueRootElementPresence;






/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public final class HtmlParsingConfiguration implements Serializable {
    

    private static final long serialVersionUID = 2971812279304039162L;
    
    
    private boolean caseSensitive = false;
    private boolean requireUniqueAttributesInElement = false;
    private boolean requireXmlWellFormedAttributeValues = false;
    private UniqueRootElementPresence uniqueRootElementPresence = UniqueRootElementPresence.DEPENDS_ON_PROLOG_DOCTYPE;
    

    
    
    /**
     * 
     * @return the new instance.
     */
    public HtmlParsingConfiguration() {
        super();
    }
    


    /**
     * <p>
     *   Determines whether validations performed on the parsed document should be
     *   case sensitive or not (e.g. attribute names, document root element name, element
     *   open vs close elements, etc.)
     * </p>
     * <p>
     *   Default is <b>false</b>.
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
     *   Determine whether element attributes will be required to be well-formed from the XML
     *   standpoint. This means:
     * </p>
     * <ul>
     *   <li>Attributes should always have a value.</li>
     *   <li>Attribute values should be surrounded by double-quotes.</li>
     * </ul>
     * <p>
     *   Default is <b>false</b>.
     * </p>
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
     * <p>
     *   Default is <b>false</b>.
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
     *   there is a DOCTYPE clause, it will be checked that the root name established 
     *   at the DOCTYPE clause is the same as the document's element root.
     * </p>
     * <p>
     *   If set to {@link UniqueRootElementPresence#DEPENDS_ON_PROLOG_DOCTYPE}, then:
     * </p>
     * <ul>
     *   <li>If there is a DOCTYPE clause, a unique element root will be required,
     *       and its name will be checked against the name specified at the DOCTYPE
     *       clause.</li>
     *   <li>If there is no DOCTYPE clause (even if it is forbidden), multiple 
     *       document root elements will be allowed.</li>
     * </ul>
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
    public HtmlParsingConfiguration clone() throws CloneNotSupportedException {
        final HtmlParsingConfiguration conf = new HtmlParsingConfiguration();
        conf.caseSensitive = this.caseSensitive;
        conf.requireUniqueAttributesInElement = this.requireUniqueAttributesInElement;
        conf.requireXmlWellFormedAttributeValues = this.requireXmlWellFormedAttributeValues;
        conf.uniqueRootElementPresence = this.uniqueRootElementPresence;
        return conf;
    }


    
    
    protected static void validateNotNull(final Object obj, final String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }
    
        
}