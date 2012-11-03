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
package org.attoparser.markup;

import java.io.Serializable;






/**
 * <p>
 *   Models a series of restrictions that can be applied during document parsing
 *   by subclasses of {@link AbstractDetailedMarkupAttoHandler}.
 * </p>
 * <p>
 *   A fixed set of these restrictions can be used for checking the well-formedness
 *   (from an XML/XHTML standpoint) of a document:
 * </p>
 * <ul>
 *   <li><tt>requireBalancedElements</tt>: Elements are correctly nested, and no <i>open element</i> lacks its corresponding 
 *       <i>close element</i>.</li>
 *   <li><tt>requireUniqueRootElement</tt>: Require the existance of only one element at root level.</li>
 *   <li><tt>requireWellFormedProlog</tt>: <i>Document prolog</i> is well-formed: only one optional XML Declaration, followed by
 *       only one optional DOCTYPE clause, followed by only one required root element (with any number
 *       of comments among them).</li>
 *   <li><tt>requireWellFormedAttributeValues</tt>: All attribute values must have an equals sign (=) and must be surrounded 
 *       by commas (double or single).</li>
 *   <li><tt>requireUniqueAttributesInElement</tt>: No element can have repeated attributes.</li>
 * </ul>
 * <p>
 *   Besides these well-formedness related restrictions, there is also a <tt>requireNoProlog</tt>
 *   restriction that ensures no XML Declaration or DOCTYPE clause appears in the parsed document.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class DocumentRestrictions implements Serializable {

    
    private static final long serialVersionUID = 5191449744126332916L;
    
    
    private boolean requireBalancedElements = false;
    private boolean requireNoUnbalancedCloseElements = false;
    private boolean requireWellFormedProlog = false;
    private boolean requireUniqueRootElement = false;
    private boolean requireWellFormedAttributeValues = false;
    private boolean requireUniqueAttributesInElement = false;
    private boolean requireNoProlog = false;
    


    

    /**
     * <p>
     *   Creates a {@link DocumentRestrictions} instance enforcing no restrictions at all.
     * </p>
     * 
     * @return the new instance.
     */
    public static DocumentRestrictions none() {
        final DocumentRestrictions spec = new DocumentRestrictions();
        spec.requireBalancedElements = false;
        spec.requireNoUnbalancedCloseElements = false;
        spec.requireWellFormedProlog = false;
        spec.requireUniqueRootElement = false;
        spec.requireWellFormedAttributeValues = false;
        spec.requireUniqueAttributesInElement = false;
        spec.requireNoProlog = false;
        return spec;
    }


    /**
     * <p>
     *   Creates a {@link DocumentRestrictions} instance enforcing the set of restrictions
     *   that check the well-formedness (from an XML/XHTML standpoint): 
     *   <tt>requireBalancedElements</tt>, <tt>requireWellFormedProlog</tt>,
     *   <tt>requireUniqueRootElement</tt>, <tt>requireWellFormedAttributeValues</tt>
     *   and <tt>requireUniqueAttributesInElement</tt>. 
     * </p>
     * 
     * @return the new instance.
     */
    public static DocumentRestrictions wellFormed() {
        final DocumentRestrictions spec = new DocumentRestrictions();
        spec.requireBalancedElements = true;
        spec.requireNoUnbalancedCloseElements = true;
        spec.requireWellFormedProlog = true;
        spec.requireUniqueRootElement = true;
        spec.requireWellFormedAttributeValues = true;
        spec.requireUniqueAttributesInElement = true;
        spec.requireNoProlog = false;
        return spec;
    }
    
    

    
    
    private DocumentRestrictions() {
        super();
    }
    

    
    public boolean getRequireBalancedElements() {
        return this.requireBalancedElements;
    }


    public void setRequireBalancedElements(final boolean requireBalancedElements) {
        this.requireBalancedElements = requireBalancedElements;
    }


    /**
     * @since 1.1
     */
    public boolean getRequireNoUnbalancedCloseElements() {
        return this.requireNoUnbalancedCloseElements;
    }


    /**
     * @since 1.1
     */
    public void setRequireNoUnbalancedCloseElements(final boolean requireNoUnbalancedCloseElements) {
        this.requireNoUnbalancedCloseElements = requireNoUnbalancedCloseElements;
    }


    public boolean getRequireWellFormedProlog() {
        return this.requireWellFormedProlog;
    }


    public void setRequireWellFormedProlog(final boolean requireWellFormedProlog) {
        this.requireWellFormedProlog = requireWellFormedProlog;
    }


    public boolean getRequireWellFormedAttributeValues() {
        return this.requireWellFormedAttributeValues;
    }


    public void setRequireWellFormedAttributeValues(
            final boolean requireWellFormedAttributeValues) {
        this.requireWellFormedAttributeValues = requireWellFormedAttributeValues;
    }


    public boolean getRequireUniqueAttributesInElement() {
        return this.requireUniqueAttributesInElement;
    }


    public void setRequireUniqueAttributesInElement(final boolean requireUniqueAttributesInElement) {
        this.requireUniqueAttributesInElement = requireUniqueAttributesInElement;
    }


    public boolean getRequireUniqueRootElement() {
        return this.requireUniqueRootElement;
    }


    public void setRequireUniqueRootElement(final boolean requireUniqueRootElement) {
        this.requireUniqueRootElement = requireUniqueRootElement;
    }


    public boolean getRequireNoProlog() {
        return this.requireNoProlog;
    }


    public void setRequireNoProlog(final boolean requireNoProlog) {
        this.requireNoProlog = requireNoProlog;
    }
    
        
}