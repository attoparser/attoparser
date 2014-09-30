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
package org.attoparser.dom.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.attoparser.dom.IElement;
import org.attoparser.dom.INestableNode;
import org.attoparser.dom.INode;



/**
 * <p>
 *   Default implementation of the {@link IElement} interface.
 * </p>
 * 
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.1
 *
 */
public class Element 
        extends AbstractNestableNode
        implements IElement, Serializable {

    private static final long serialVersionUID = -8980986739486971174L;

    private String elementName;
    
    private Map<String,String> attributes = null;
    private int attributesLen = 0;
    



    public Element(final String name) {
        super();
        Validate.notNull(name, "Element name cannot be null");
        this.elementName = name;
    }

    
    
    
    public String getElementName() {
        return this.elementName;
    }
    
    public void setElementName(final String name) {
        Validate.notNull(name, "Element elementName cannot be null");
        this.elementName = name;
    }
    
    public boolean elementNameMatches(final String name) {
        return this.elementName.equals(name);
    }
    
    
    
    
    /*
     * **********************
     *  ATTRIBUTE MANAGEMENT
     * **********************
     */



    public int numAttributes() {
        return this.attributesLen;
    }
    

    public boolean hasAttributes() {
        return this.attributesLen != 0;
    }

    
    
    
    public boolean hasAttribute(final String attributeName) {
        if (this.attributesLen > 0) {
            return this.attributes.containsKey(attributeName);
        }
        return false;
    }

    
    public String getAttributeValue(final String attributeName) {
        if (this.attributesLen > 0) {
            return this.attributes.get(attributeName);
        }
        return null;
    }

    
    
    
    public Map<String,String> getAttributeMap() {
        if (this.attributesLen > 0) {
            return Collections.unmodifiableMap(this.attributes);
        }
        return Collections.emptyMap();
    }


    

    public void addAttribute(final String attributeName, final String attributeValue) {
        
        if (this.attributesLen == 0) {
            this.attributes = new LinkedHashMap<String, String>();
        }
        this.attributes.put(attributeName, attributeValue);
        this.attributesLen++;
        
    }


    public void addAttributes(final Map<String,String> newAttributes) {
        
        if (newAttributes != null) {
            if (this.attributesLen == 0) {
                this.attributes = new LinkedHashMap<String, String>();
            }
            this.attributes.putAll(newAttributes);
            this.attributesLen += newAttributes.size();
        }

    }

    
    
    
    public void removeAttribute(final String attributeName) {
        
        if (this.attributesLen > 0) {
            
            if (this.attributes.containsKey(attributeName)) {
                this.attributes.remove(attributeName);
                this.attributesLen--;
                if (this.attributesLen == 0) {
                    this.attributes = null;
                }
            }
            
        }
        
    }

    
    
    
    public void clearAttributes() {
        this.attributes = null;
        this.attributesLen = 0;
    }
    

    

    
    /*
     * *********
     *  CLONING
     * *********
     */


    public Element cloneNode(final INestableNode parent) {
        final Element element = new Element(this.elementName);
        element.addAttributes(this.attributes);
        for (final INode child : getChildren()) {
            final INode clonedChild = child.cloneNode(element);
            element.addChild(clonedChild);
        }
        element.setLine(getLine());
        element.setCol(getCol());
        element.setParent(parent);
        return element;
    }

    

}
