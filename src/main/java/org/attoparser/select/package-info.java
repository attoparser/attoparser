/**
 * <p>
 *   Handlers for filtering a part or several parts of markup during parsing
 *   in a fast and efficient way.
 * </p>
 *
 * <br>
 * <h2>Handler Implementations</h2>
 *
 * <p>
 *   There are two main handlers (implementations of {@link org.attoparser.IMarkupHandler} for
 *   <em>markup selection</em> in this package:
 * </p>
 * <dl>
 *   <dt>{@link org.attoparser.select.BlockSelectorMarkupHandler}</dt>
 *   <dd>
 *     For selecting entire blocks of markup (i.e.
 *     elements and all the nodes in their subtrees). This can be used, for example, for <strong>extracting
 *     fragments of markup</strong> during the parsing of the document, in a way so that discarded markup does
 *     never reach higher layers of the document processing infrastructure.
 *   </dd>
 *   <dt>{@link org.attoparser.select.NodeSelectorMarkupHandler}</dt>
 *   <dd>
 *     For selecting only specific nodes in markup (i.e. not including their subtrees). This can be used
 *     for <strong>modifying certain specific tags in markup</strong> during parsing, for example by
 *     adding additional attributes to them that are not present in the original parsed markup.
 *   </dd>
 * </dl>
 *
 * <br>
 * <h2>Markup Selector Syntax</h2>
 *
 * <p>
 *   Markup selectors used by handlers in this package use a specific syntax with features borrowed from
 *   XPath, CSS and jQuery selectors, in order to provide ease-of-use for most users. Many times there are several
 *   ways to express the same selector, depending on the user's preferences.
 * </p>
 * <p>
 *   For example, all the following equivalent selectors will select every <kbd>&lt;div&gt;</kbd> with class
 *   <kbd>content</kbd>, in any position in markup:
 * </p>
 * <pre><code>
 *   //div[class='content']
 *   //div[@class='content']
 *   div[class='content']
 *   div[@class='content']
 *   //div.content
 *   div.content
 * </code></pre>
 * <p>
 *   These are the different operations this syntax allows:
 * </p>
 *
 * <br>
 * <h3>Basic selectors</h3>
 *
 * <dl>
 *
 *     <dt><kbd>x</kbd><br><kbd>//x</kbd></dt>
 *     <dd>
 *         Both are equivalent, and mean <em>children of the current node with name <kbd>x</kbd>, at any depth in
 *         markup</em>. If a <em>reference resolver</em> is being used, they will also be equivalent to
 *         <kbd>%x</kbd> (see below).
 *     </dd>
 *
 *     <dt><kbd>/x</kbd></dt>
 *     <dd>
 *         Means <em>direct children of the current node with name <kbd>x</kbd></em>.
 *     </dd>
 *
 *     <dt><kbd>x/y</kbd></dt>
 *     <dd>
 *         Means <em>direct children with name <kbd>y</kbd> of elements with name <kbd>x</kbd>, being the parent
 *         <kbd>x</kbd> elements at any level in markup</em>.
 *     </dd>
 *
 *     <dt><kbd>x//y</kbd></dt>
 *     <dd>
 *         Means <em>children (at any level) with name <kbd>y</kbd> of elements with name <kbd>x</kbd>, being the parent
 *         <kbd>x</kbd> elements also at any level in markup</em>.
 *     </dd>
 *
 *     <dt><kbd>text()</kbd><br><kbd>comment()</kbd><br><kbd>cdata()</kbd><br><kbd>doctype()</kbd><br><kbd>xmldecl()</kbd><br><kbd>procinstr()</kbd></dt>
 *     <dd>
 *         These can be used like <kbd>x</kbd> (in the same places) but instead of selecting elements (i.e. tags)
 *         will select, respectively: text nodes, comments, CDATA sections, DOCTYPE clauses, XML Declarations and
 *         Processing Instructions.
 *     </dd>
 *
 *     <dt><kbd>content()</kbd></dt>
 *     <dd>
 *         This selector can be used for selecting the entire contents of an element (i.e. all its body), including
 *         all texts, comments, elements, etc. inside it. But, note, not the container element itself.
 *     </dd>
 * </dl>
 *
 * <br>
 * <h3>Attribute matching</h3>
 *
 * <dl>
 *
 *     <dt><kbd>x[z='v']</kbd><br><kbd>x[z="v"]</kbd><br><kbd>x[@z='v']</kbd><br><kbd>x[@z="v"]</kbd></dt>
 *     <dd>
 *         All four equivalent, mean <em>elements with name <kbd>x</kbd> and an attribute called <kbd>z</kbd> with value
 *         <kbd>v</kbd></em>. Note attribute values can be surrounded by single or double quotes, and attribute names
 *         can be specified with a leading <kbd>@</kbd> (as in XPath) or without it (more similar to jQuery). For
 *         the sake of simplicity, only the single-quoted, no-<kbd>@</kbd> syntax will be used for the rest of
 *         the examples below.
 *     </dd>
 *
 *     <dt><kbd>[z='v']</kbd><br><kbd>//[z='v']</kbd></dt>
 *     <dd>
 *         Means <em>any elements with an attribute called <kbd>z</kbd> with value <kbd>v</kbd></em>.
 *     </dd>
 *
 *     <dt><kbd>x[z]</kbd></dt>
 *     <dd>
 *         Means <em>elements with name <kbd>x</kbd> and an attribute called <kbd>z</kbd>, with any value</em>.
 *     </dd>
 *
 *     <dt><kbd>x[!z]</kbd></dt>
 *     <dd>
 *         Means <em>elements with name <kbd>x</kbd> and no attribute called <kbd>z</kbd></em>.
 *     </dd>
 *
 *     <dt><kbd>x[z1='v1' and z2='v2']</kbd></dt>
 *     <dd>
 *         Means <em>elements with name <kbd>x</kbd> and attributes <kbd>z1</kbd> and <kbd>z2</kbd> with values
 *         <kbd>v1</kbd> and <kbd>v2</kbd>, respectively</em>.
 *     </dd>
 *
 *     <dt><kbd>x[z1='v1' or z2='v2']</kbd></dt>
 *     <dd>
 *         Means <em>elements with name <kbd>x</kbd> and, either an attribute <kbd>z1</kbd> with value
 *         <kbd>v1</kbd>, or an attribute <kbd>z2</kbd> with value <kbd>v2</kbd></em>.
 *     </dd>
 *
 *     <dt><kbd>x[z1='v1' and (z2='v2' or z3='v3')]</kbd></dt>
 *     <dd>
 *         Selects according to the specified attribute complex expression. As can be seen, these expressions
 *         can be parenthesized to introduce a certain evaluation order.
 *     </dd>
 *
 *     <dt><kbd>x[z!='v']</kbd><br><kbd>x[z^='v']</kbd><br><kbd>x[z$='v']</kbd><br><kbd>x[z*='v']</kbd></dt>
 *     <dd>
 *         Similar to <kbd>x[z='v']</kbd> but applying different operators to attribute matching instead of
 *         <em>equality</em> (<kbd>=</kbd>). Respectively: <em>not equal</em> (<kbd>!=</kbd>),
 *         <em>starts with</em> (<kbd>^=</kbd>), <em>ends with</em> (<kbd>$=</kbd>) and
 *         <em>contains</em> (<kbd>*=</kbd>).
 *     </dd>
 *
 *     <dt><kbd>x.z</kbd><br><kbd>x[class='z']</kbd></dt>
 *     <dd>
 *         When parsing in HTML mode (and only then), these two selectors will be completely equivalent. Besides,
 *         in this case the selector will look for an <kbd>x</kbd> element which has the <kbd>z</kbd> class, knowing that
 *         HTML's <kbd>class</kbd> attribute allows the specification of several classes separated by white space. So
 *         something like <kbd>&lt;x class="z y w"&gt;</kbd> will be matched by this selector.
 *     </dd>
 *
 *     <dt><kbd>x#z</kbd><br><kbd>x[id='z']</kbd></dt>
 *     <dd>
 *         When parsing in HTML mode (and only then), these two selectors will be completely equivalent, matching those
 *         <kbd>x</kbd> elements that have an ID with value <kbd>z</kbd>.
 *     </dd>
 *
 * </dl>
 *
 * <br>
 * <h3>Index-based matching</h3>
 *
 * <dl>
 *     <dt><kbd>x[i]</kbd></dt>
 *     <dd>
 *         Means <em>elements with name <kbd>x</kbd> positioned in index <kbd>i</kbd> among its siblings</em>.
 *         <em>Sibling</em> here means <em>node child of the same parent element, matching the same
 *         conditions</em> (in this case <em>"having <kbd>x</kbd> as name"</em>). Note indexes start with
 *         <kbd>0</kbd>.
 *     </dd>
 *
 *     <dt><kbd>x[z='v'][i]</kbd></dt>
 *     <dd>
 *         Means <em>elements with name <kbd>x</kbd>, attribute <kbd>z</kbd> with value <kbd>v</kbd> and positioned in
 *         number i among its siblings (same name, same attribute with that value)</em>.
 *     </dd>
 *
 *     <dt><kbd>x[even()]</kbd><br><kbd>x[odd()]</kbd></dt>
 *     <dd>
 *         Means <em>elements with name <kbd>x</kbd> positioned in an even (or odd) index among its siblings</em>.
 *         Note <em>even</em> includes the index number <kbd>0</kbd>.
 *     </dd>
 *
 *     <dt><kbd>x[&gt;i]</kbd><br><kbd>x[&lt;i]</kbd></dt>
 *     <dd>
 *         Mean <em>elements with name <kbd>x</kbd> positioned in an index greater (or lesser) than <kbd>i</kbd>
 *         among its siblings</em>.
 *     </dd>
 *
 *     <dt><kbd>text()[i]</kbd><br><kbd>comment()[&gt;i]</kbd></dt>
 *     <dd>
 *         Applies the specified index-based matching operations to nodes of types other than elements: texts,
 *         comments, CDATA sections, etc.
 *     </dd>
 *
 * </dl>
 *
 * <br>
 * <h3>Reference-based matching</h3>
 *
 * <dl>
 *
 *     <dt><kbd>x%ref</kbd></dt>
 *     <dd>
 *         Means <em>elements with name <kbd>x</kbd> and matching <strong>markup selector reference</strong>
 *         with value <kbd>ref</kbd></em>. These <em>markup selector references</em> usually have a user-defined
 *         meaning and are resolved to a markup selector without references by means of an instance of the
 *         {@link org.attoparser.select.IMarkupSelectorReferenceResolver} interface passed to the selecting
 *         markup handlers ({@link org.attoparser.select.BlockSelectorMarkupHandler} and
 *         {@link org.attoparser.select.NodeSelectorMarkupHandler}) during construction.
 *         For example, a reference resolver could be
 *         configured that converts (resolves) <kbd>%someref</kbd> into
 *         <kbd>div[class='someref' or id='someref']</kbd>. Also, the
 *         <a href="http://www.thymeleaf.org">Thymeleaf</a> template engine uses this mechanism
 *         for resolving <kbd>%fragmentName</kbd> (or simply <kbd>fragmentName</kbd>, as explained below) into
 *         <kbd>//[th:fragment='fragmentName' or data-th-fragment='fragmentName']</kbd>.
 *     </dd>
 *
 *     <dt><kbd>%ref</kbd></dt>
 *     <dd>
 *         Means <em>any elements (whichever the name) matching reference with value <kbd>ref</kbd></em>.
 *     </dd>
 *
 *     <dt><kbd>ref</kbd></dt>
 *     <dd>
 *         Equivalent to <kbd>%ref</kbd>. When a <em>markup selector reference resolver</em> has been configured,
 *         <kbd>ref</kbd> can bean both <em>"element with name <kbd>x</kbd>"</em> and
 *         <em>"element matching reference <kbd>x</kbd>"</em> (both will match).
 *     </dd>
 *
 * </dl>
 * <br>
 * <br>
 *
 */
package org.attoparser.select;