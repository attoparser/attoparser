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
package org.attoparser.markup.dom;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



/**
 * <p>
 *   Base abstract class for all nodes in a attoDOM tree which have
 *   children.
 * </p>
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 *
 */
public final class Element extends Node {

    private static final long serialVersionUID = -8980986739486971174L;

    
    private final String name;
    private final boolean standalone;
    
    private List<Node> children = null;
    private int childrenLen = 0;
    
    private Map<String,String> attributes = null;
    private int attributesLen = 0;
    



    Element(final String name, final boolean standalone, final int line, final int col) {
        super(line, col);
        this.name = name;
        this.standalone = standalone;
    }

    
    
    
    public String getName() {
        return this.name;
    }
    
    public boolean isStandalone() {
        return this.standalone;
    }
    
    

    
    
    /*
     * ************************
     * ************************
     *        CHILDREN
     * ************************
     * ************************
     */

    
    
    
    /**
     * <p>
     *   Returns whether this node has any children.
     * </p>
     * 
     * @return true if the node as any children, false if not.
     */
    public final boolean hasChildren() {
        return this.childrenLen != 0;
    }
    

    /**
     * <p>
     *   Returns the number of children in this node.
     * </p>
     * 
     * @return the number of children.
     */
    public final int numChildren() {
        return this.childrenLen;
    }
    

    /**
     * <p>
     *   Returns the children of this node. The returned list is immutable.
     * </p>
     * 
     * @return the list of children.
     */
    public final List<Node> getChildren() {
        
        if (this.childrenLen == 0) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.children);
    }


    /**
     * <p>
     *   Returns only the {@link Element} children
     *   of this node, discarding children of any other types.
     *   The returned list is immutable.
     * </p>
     * 
     * @return the list of Element children.
     */
    public final List<Element> getElementChildren() {
        if (this.childrenLen == 0) {
            return Collections.emptyList();
        }
        final List<Element> elementChildren = new ArrayList<Element>();
        for (final Node child : this.children) {
            if (child instanceof Element) {
                elementChildren.add((Element)child);
            }
        }
        return Collections.unmodifiableList(elementChildren);
    }
    

    /**
     * <p>
     *   Returns the first child of this node.
     * </p>
     * 
     * @return the first child, or null if there are no children.
     */
    public final Node getFirstChild() {
        if (this.childrenLen == 0) {
            return null;
        }
        return this.children.get(0);
    }


    /**
     * <p>
     *   Returns the first child of type {@link Element}.
     * </p>
     * 
     * @return the first Element child, or null if there are no children
     *         or there are but none is an Element.
     */
    public final Element getFirstElementChild() {
        if (this.childrenLen == 0) {
            return null;
        }
        for (final Node child : this.children) {
            if (child instanceof Element) {
                return (Element)child;
            }
        }
        return null;
    }
    
    

    
    void addChild(final Node newChild) {
        
        if (newChild != null) {
            if (this.childrenLen == 0) {
                this.children = new ArrayList<Node>();
            }
            this.children.add(newChild);
            this.childrenLen++;
            
            newChild.parent = this;
            
        }
        
    }
    
    

    
    
    /*
     * ************************
     * ************************
     *        ATTRIBUTES
     * ************************
     * ************************
     */

    
    
    
    /**
     * <p>
     *   Returns whether this element has any attributes or not.
     * </p>
     * 
     * @return true if this element has attributes, false if not.
     */
    public boolean hasAttributes() {
        return this.attributesLen != 0;
    }
    

    /**
     * <p>
     *   Returns the number of attributes contained in this element.
     * </p>
     * 
     * @return the number of attributes.
     */
    public int numAttributes() {
        return this.attributesLen;
    }


    /**
     * <p>
     *   Returns whether an attribute exists in the element or not.
     * </p>
     * 
     * @param attributeName the name of the attribute to be checked.
     * @return true if the attribute exists, false if not.
     */
    public boolean hasAttribute(final String attributeName) {
        if (this.attributesLen > 0) {
            return this.attributes.containsKey(attributeName);
        }
        return false;
    }


    /**
     * <p>
     *   Returns whether an attribute exists in the element or not,
     *   ignoring case differences.
     * </p>
     * 
     * @param attributeName the name of the attribute to be checked.
     * @return true if the attribute exists, false if not.
     */
    public boolean hasAttributeIgnoreCase(final String attributeName) {
        if (this.attributesLen > 0) {
            for (final Map.Entry<String,String> attributeEntry : this.attributes.entrySet()) {
                final String entryName = attributeEntry.getKey();
                if (entryName == null) {
                    if (attributeName == null) {
                        return true;
                    }
                } else if (entryName.equalsIgnoreCase(attributeName)) {
                    return true;
                }
            }
        }
        return false;
    }
    

    /**
     * <p>
     *   Returns the value of an attribute from its attribute name.
     * </p>
     * 
     * @param attributeName the attribute name.
     * @return the value of the attribute.
     */
    public String getAttributeValue(final String attributeName) {
        if (this.attributesLen > 0) {
            return this.attributes.get(attributeName);
        }
        return null;
    }
    

    /**
     * <p>
     *   Returns the value of an attribute from its attribute name, ignoring
     *   case in attribute name.
     * </p>
     * 
     * @param attributeName the attribute name.
     * @return the value of the attribute.
     */
    public String getAttributeValueIgnoreCase(final String attributeName) {
        if (this.attributesLen > 0) {
            for (final Map.Entry<String,String> attributeEntry : this.attributes.entrySet()) {
                final String entryName = attributeEntry.getKey();
                if (entryName == null) {
                    if (attributeName == null) {
                        return attributeEntry.getValue();
                    }
                } else if (entryName.equalsIgnoreCase(attributeName)) {
                    return attributeEntry.getValue();
                }
            }
        }
        return null;
    }
    

    /**
     * <p>
     *   Returns a map with all the names and values of the element attributes.
     * </p>
     * <p>
     *   The map object returned by this method is immutable.
     * </p>
     * 
     * @return the map of all current attributes in the element.
     */
    public Map<String,String> getAttributeMap() {
        if (this.attributesLen > 0) {
            return Collections.unmodifiableMap(this.attributes);
        }
        return Collections.emptyMap();
    }
    
    


    void addAttribute(final String attributeName, final String attributeValue) {
        
        if (this.attributesLen == 0) {
            this.attributes = new LinkedHashMap<String, String>();
        }
        this.attributes.put(attributeName, attributeValue);
        this.attributesLen++;
        
    }



    void addAttributes(final Map<String,String> newAttributes) {
        
        if (newAttributes != null) {
            if (this.attributesLen == 0) {
                this.attributes = new LinkedHashMap<String, String>();
            }
            this.attributes.putAll(newAttributes);
            this.attributesLen += newAttributes.size();
        }

    }
    
    

    
    
    /*
     * ************************
     * ************************
     *        VISITOR
     * ************************
     * ************************
     */

    
    
    
    @Override
    public final void visit(final AttoDOMVisitor visitor)
            throws AttoDOMVisitorException {
        
        if (this.standalone) {
            visitor.visitStandaloneElement(this);
        } else {
            visitor.visitOpenElement(this);
            if (this.childrenLen > 0) {
                for (final Node child : this.children) {
                    child.visit(visitor);
                }
            }
            visitor.visitCloseElement(this);
        }
        
    }
    

}
