/*
 * =============================================================================
 *
 *   Copyright (c) 2012-2025 Attoparser (https://www.attoparser.org)
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
package org.attoparser.select;

/**
 * <p>
 *   Interface modeling <em>reference resolvers</em>, the objects that can be used for tuning the selector matching
 *   operations done by {@link org.attoparser.select.BlockSelectorMarkupHandler} and
 *   {@link org.attoparser.select.NodeSelectorMarkupHandler}.
 * </p>
 * <p>
 *   This <em>tuning</em> is performed by means of <em>selector references</em>, which look like
 *   <kbd>%ref</kbd> and which are passed to the reference resolvers in order to convert them to more specific selectors.
 * </p>
 * <p>
 *   For example, a reference resolver might convert <kbd>%someref</kbd> to <kbd>//p[data-additional="someref"]</kbd> so
 *   that <kbd>%someref</kbd> simply becomes an easier way to specify <em>"a paragraph element that has an attribute
 *   called 'data-additional' with value '<kbd>someref</kbd>'".</em>
 * </p>
 * <p>
 *   As another example, this <kbd>%fragment</kbd> (or simply <kbd>fragment</kbd>) syntax is heavily used by
 *   <a href="http://www.thymeleaf.org">Thymeleaf</a>
 *   in order to search for elements with a <kbd>th:fragment</kbd> or <kbd>data-th-fragment</kbd> attribute with a
 *   specific <kbd>"fragment"</kbd> value.
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
     *   For example given a selector reference like <kbd>%someref</kbd>, this method will be called with reference
     *   value <kbd>"someref"</kbd>, and the result could be something like <kbd>//p[data-additional="someref"]</kbd> in
     *   order to make <kbd>%ref</kbd> a synonym to <em>"a paragraph element that has an attribute
     *   called 'data-additional' with value '<kbd>someref</kbd>'".</em>
     * </p>
     *
     * @param reference the reference value (without the <kbd>%</kbd>).
     * @return the equivalent, complete markup selector.
     */
    public String resolveSelectorFromReference(final String reference);

}
