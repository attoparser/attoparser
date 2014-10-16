/*
 * =============================================================================
 *
 *   Copyright (c) 2012-2014, The ATTOPARSER team (http://www.attoparser.org)
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
package org.attoparser.select;

/**
 * <p>
 *   Interface modeling <em>reference resolvers</em>, the objects that can be used for tuning the selector matching
 *   operations done by {@link org.attoparser.select.BlockSelectorMarkupHandler} and
 *   {@link org.attoparser.select.NodeSelectorMarkupHandler}.
 * </p>
 * <p>
 *   This <em>tuning</em> is performed by means of <em>selector references</em>, which look like
 *   <tt>%ref</tt> and which are passed to the reference resolvers in order to convert them to more specific selectors.
 * </p>
 * <p>
 *   For example, a reference resolver might convert <tt>%someref</tt> to <tt>//p[data-additional="someref"]</tt> so
 *   that <tt>%someref</tt> simply becomes an easier way to specify <em>"a paragraph element that has an attribute
 *   called 'data-additional' with value '<tt>someref</tt>'".</em>
 * </p>
 * <p>
 *   As another example, this <tt>%fragment</tt> (or simply <tt>fragment</tt>) syntax is heavily used by Thymeleaf
 *   in order to search for elements with a <tt>th:fragment</tt> or <tt>data-th-fragment</tt> attribute with a
 *   specific <tt>"fragment"</tt> value.
 * </p>
 * <p>
 *   Note: avoid creating new instances of the same class implementing this interface: better always use just one
 *   instance, unless the implementation gives non-deterministic results. The object equality of this class
 *   (its equals()) will be used for caching parsed selectors.
 * </p>
 *
 * @author Daniel Fern&aacute;ndez
 *
 * @since 2.0.0
 *
 */
public interface IMarkupSelectorReferenceResolver {

    /**
     * <p>
     *   Convert the specified value, coming from a <em>selector reference</em>, into a complete <em>markup
     *   selector</em>.
     * </p>
     * <p>
     *   For example given a selector reference like <tt>%someref</tt>, this method will be called with reference
     *   value <tt>"someref"</tt>, and the result could be something like <tt>//p[data-additional="someref"]</tt> in
     *   order to make <tt>%ref</tt> a synonym to <em>"a paragraph element that has an attribute
     *   called 'data-additional' with value '<tt>someref</tt>'".</em>
     * </p>
     *
     * @param reference the reference value (without the <tt>%</tt>).
     * @return the equivalent, complete markup selector.
     */
    public String resolveSelectorFromReference(final String reference);

}
