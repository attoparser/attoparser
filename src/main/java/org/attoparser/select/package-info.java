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
 *   For example, all the following equivalent selectors will select every <tt>&lt;div&gt;</tt> with class
 *   <tt>content</tt>, in any position in markup:
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
 *     <dt><tt>x</tt><br><tt>//x</tt></dt>
 *     <dd>
 *         Both are equivalent, and mean <em>children of the current node with name <tt>x</tt>, at any depth in
 *         markup</em>. If a <em>reference resolver</em> is being used, they will also be equivalent to
 *         <tt>%x</tt> (see below).
 *     </dd>
 *
 *     <dt><tt>/x</tt></dt>
 *     <dd>
 *         Means <em>direct children of the current node with name <tt>x</tt></em>.
 *     </dd>
 *
 *     <dt><tt>x/y</tt></dt>
 *     <dd>
 *         Means <em>direct children with name <tt>y</tt> of elements with name <tt>x</tt>, being the parent
 *         <tt>x</tt> elements at any level in markup</em>.
 *     </dd>
 *
 *     <dt><tt>x//y</tt></dt>
 *     <dd>
 *         Means <em>children (at any level) with name <tt>y</tt> of elements with name <tt>x</tt>, being the parent
 *         <tt>x</tt> elements also at any level in markup</em>.
 *     </dd>
 *
 *     <dt><tt>text()</tt><br><tt>comment()</tt><br><tt>cdata()</tt><br><tt>doctype()</tt><br><tt>xmldecl()</tt><br><tt>procinstr()</tt></dt>
 *     <dd>
 *         These can be used like <tt>x</tt> (in the same places) but instead of selecting elements (i.e. tags)
 *         will select, respectively: text nodes, comments, CDATA sections, DOCTYPE clauses, XML Declarations and
 *         Processing Instructions.
 *     </dd>
 * </dl>
 *
 * <br>
 * <h3>Attribute matching</h3>
 *
 * <dl>
 *
 *     <dt><tt>x[z='v']</tt><br><tt>x[z="v"]</tt><br><tt>x[@z='v']</tt><br><tt>x[@z="v"]</tt></dt>
 *     <dd>
 *         All four equivalent, mean <em>elements with name <tt>x</tt> and an attribute called <tt>z</tt> with value
 *         <tt>v</tt></em>. Note attribute values can be surrounded by single or double quotes, and attribute names
 *         can be specified with a leading <tt>@</tt> (as in XPath) or without it (more similar to jQuery). For
 *         the sake of simplicity, only the single-quoted, no-<tt>@</tt> syntax will be used for the rest of
 *         the examples below.
 *     </dd>
 *
 *     <dt><tt>[z='v']</tt><br><tt>//[z='v']</tt></dt>
 *     <dd>
 *         Means <em>any elements with an attribute called <tt>z</tt> with value <tt>v</tt></em>.
 *     </dd>
 *
 *     <dt><tt>x[z]</tt></dt>
 *     <dd>
 *         Means <em>elements with name <tt>x</tt> and an attribute called <tt>z</tt>, with any value</em>.
 *     </dd>
 *
 *     <dt><tt>x[!z]</tt></dt>
 *     <dd>
 *         Means <em>elements with name <tt>x</tt> and no attribute called <tt>z</tt></em>.
 *     </dd>
 *
 *     <dt><tt>x[z1='v1' and z2='v2']</tt></dt>
 *     <dd>
 *         Means <em>elements with name <tt>x</tt> and attributes <tt>z1</tt> and <tt>z2</tt> with values
 *         <tt>v1</tt> and <tt>v2</tt>, respectively</em>.
 *     </dd>
 *
 *     <dt><tt>x[z1='v1' or z2='v2']</tt></dt>
 *     <dd>
 *         Means <em>elements with name <tt>x</tt> and, either an attribute <tt>z1</tt> with value
 *         <tt>v1</tt>, or an attribute <tt>z2</tt> with value <tt>v2</tt></em>.
 *     </dd>
 *
 *     <dt><tt>x[z1='v1' and (z2='v2' or z3='v3')]</tt></dt>
 *     <dd>
 *         Selects according to the specified attribute complex expression. As can be seen, these expressions
 *         can be parenthesized to introduce a certain evaluation order.
 *     </dd>
 *
 *     <dt><tt>x[z!='v']</tt><br><tt>x[z^='v']</tt><br><tt>x[z$='v']</tt><br><tt>x[z*='v']</tt></dt>
 *     <dd>
 *         Similar to <tt>x[z='v']</tt> but applying different operators to attribute matching instead of
 *         <em>equality</em> (<tt>=</tt>). Respectively: <em>not equal</em> (<tt>!=</tt>),
 *         <em>starts with</em> (<tt>^=</tt>), <em>ends with</em> (<tt>$=</tt>) and
 *         <em>contains</em> (<tt>*=</tt>).
 *     </dd>
 *
 *     <dt><tt>x.z</tt><br><tt>x[class='z']</tt></dt>
 *     <dd>
 *         When parsing in HTML mode (and only then), these two selectors will be completely equivalent. Besides,
 *         in this case the selector will look for an <tt>x</tt> element which has the <tt>z</tt> class, knowing that
 *         HTML's <tt>class</tt> attribute allows the specification of several classes separated by white space. So
 *         something like <tt>&lt;x class="z y w"&gt;</tt> will be matched by this selector.
 *     </dd>
 *
 *     <dt><tt>x#z</tt><br><tt>x[id='z']</tt></dt>
 *     <dd>
 *         When parsing in HTML mode (and only then), these two selectors will be completely equivalent, matching those
 *         <tt>x</tt> elements that have an ID with value <tt>z</tt>.
 *     </dd>
 *
 * </dl>
 *
 * <br>
 * <h3>Index-based matching</h3>
 *
 * <dl>
 *     <dt><tt>x[i]</tt></dt>
 *     <dd>
 *         Means <em>elements with name <tt>x</tt> positioned in index <tt>i</tt> among its siblings</em>.
 *         <em>Sibling</em> here means <em>node child of the same parent element, matching the same
 *         conditions</em> (in this case <em>"having <tt>x</tt> as name"</em>). Note indexes start with
 *         <tt>0</tt>.
 *     </dd>
 *
 *     <dt><tt>x[z='v'][i]</tt></dt>
 *     <dd>
 *         Means <em>elements with name <tt>x</tt>, attribute <tt>z</tt> with value <tt>v</tt> and positioned in
 *         number i among its siblings (same name, same attribute with that value)</em>.
 *     </dd>
 *
 *     <dt><tt>x[even()]</tt><br><tt>x[odd()]</tt></dt>
 *     <dd>
 *         Means <em>elements with name <tt>x</tt> positioned in an even (or odd) index among its siblings</em>.
 *         Note <em>even</em> includes the index number <tt>0</tt>.
 *     </dd>
 *
 *     <dt><tt>x[&gt;i]</tt><br><tt>x[&lt;i]</tt></dt>
 *     <dd>
 *         Mean <em>elements with name <tt>x</tt> positioned in an index greater (or lesser) than <tt>i</tt>
 *         among its siblings</em>.
 *     </dd>
 *
 *     <dt><tt>text()[i]</tt><br><tt>comment()[&gt;i]</tt></dt>
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
 *     <dt><tt>x%ref</tt></dt>
 *     <dd>
 *         Means <em>elements with name <tt>x</tt> and matching <strong>markup selector reference</strong>
 *         with value <tt>ref</tt></em>. These <em>markup selector references</em> usually have a user-defined
 *         meaning and are resolved to a markup selector without references by means of an instance of the
 *         {@link org.attoparser.select.IMarkupSelectorReferenceResolver} interface passed to the selecting
 *         markup handlers ({@link org.attoparser.select.BlockSelectorMarkupHandler} and
 *         {@link org.attoparser.select.NodeSelectorMarkupHandler}) during construction.
 *         For example, a reference resolver could be
 *         configured that converts (resolves) <tt>%someref</tt> into
 *         <tt>div[class='someref' or id='someref']</tt>. Also, the
 *         <a href="http://www.thymeleaf.org">Thymeleaf</a> template engine uses this mechanism
 *         for resolving <tt>%fragmentName</tt> (or simply <tt>fragmentName</tt>, as explained below) into
 *         <tt>//[th:fragment='fragmentName' or data-th-fragment='fragmentName']</tt>.
 *     </dd>
 *
 *     <dt><tt>%ref</tt></dt>
 *     <dd>
 *         Means <em>any elements (whichever the name) matching reference with value <tt>ref</tt></em>.
 *     </dd>
 *
 *     <dt><tt>ref</tt></dt>
 *     <dd>
 *         Equivalent to <tt>%ref</tt>. When a <em>markup selector reference resolver</em> has been configured,
 *         <tt>ref</tt> can bean both <em>"element with name <tt>x</tt>"</em> and
 *         <em>"element matching reference <tt>x</tt>"</em> (both will match).
 *     </dd>
 *
 * </dl>
 * <br>
 * <br>
 *
 */
package org.attoparser.select;