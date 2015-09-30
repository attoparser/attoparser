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
package org.attoparser;


import org.attoparser.util.TextUtil;

/*
 * Specialization of HtmlElement for HTML elements which body should not
 * be considered 'parseable', and therefore should be treated as CDATA
 * (instead of PCDATA). For example, <script> or <style> elements.
 *
 * These elements will disable parsing at the ParseStatus objects, until
 * their closing correspondent is found.
 * 
 * @author Daniel Fernandez
 * @since 2.0.0
 */
class HtmlCDATAContentElement extends HtmlElement {

    private static final char[] ELEMENT_SCRIPT_NAME = "script".toCharArray();
    private static final char[] ATTRIBUTE_TYPE_NAME = "type".toCharArray();
    private static final char[] ATTRIBUTE_TYPE_TEMPLATE_VALUE = "text/template".toCharArray();

    private final char[] nameLower;
    private final char[] nameUpper;
    private final char[] limitSequenceLower;
    private final char[] limitSequenceUpper;


    public HtmlCDATAContentElement(final String name) {

        super(name);

        // This result will mean parsing will be disabled until we find the closing tag for this element.
        final String nameLower = name.toLowerCase();
        final String nameUppoer = name.toUpperCase();

        this.nameLower = nameLower.toCharArray();
        this.nameUpper = nameUppoer.toCharArray();

        this.limitSequenceLower = ("</" + nameLower + ">").toCharArray();
        this.limitSequenceUpper = ("</" + nameUppoer + ">").toCharArray();

    }




    @Override
    public void handleOpenElementStart(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status,
            final boolean autoOpenEnabled, final boolean autoCloseEnabled)
            throws ParseException {

        status.shouldDisableParsing = true;

        handler.handleOpenElementStart(buffer, nameOffset, nameLen, line, col);

    }



    @Override
    public void handleOpenElementEnd(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int line, final int col,
            final IMarkupHandler handler,
            final ParseStatus status,
            final boolean autoOpenEnabled, final boolean autoCloseEnabled)
            throws ParseException {


        handler.handleOpenElementEnd(buffer, nameOffset, nameLen, line, col);

        if (status.shouldDisableParsing) {
            // This is an element with CDATA body, so we should disable parsing until we find the corresponding closing tag
            status.setParsingDisabled(computeLimitSequence(buffer, nameOffset, nameLen));
            // Clean the flag
            status.shouldDisableParsing = false;
        }

    }



    @Override
    public void handleAttribute(
            final char[] buffer,
            final int nameOffset, final int nameLen,
            final int nameLine, final int nameCol,
            final int operatorOffset, final int operatorLen,
            final int operatorLine, final int operatorCol,
            final int valueContentOffset, final int valueContentLen,
            final int valueOuterOffset, final int valueOuterLen,
            final int valueLine, final int valueCol,
            final IMarkupHandler handler,
            final ParseStatus status,
            final boolean autoOpenEnabled, final boolean autoCloseEnabled)
            throws ParseException {

        if (TextUtil.equals(false, ATTRIBUTE_TYPE_NAME, 0, ATTRIBUTE_TYPE_NAME.length, buffer, nameOffset, nameLen)) {
            // We are processing a 'type' attribute...
            if (TextUtil.equals(true, ELEMENT_SCRIPT_NAME, 0, ELEMENT_SCRIPT_NAME.length, this.nameLower, 0, this.nameLower.length)) {
                // ...and this is a <script> tag...
                if (TextUtil.equals(false, ATTRIBUTE_TYPE_TEMPLATE_VALUE, 0, ATTRIBUTE_TYPE_TEMPLATE_VALUE.length, buffer, valueContentOffset, valueContentLen)) {
                    // ...and value is 'text/template', so we don't have to disable parsing!
                    status.shouldDisableParsing = false;
                }
            }
        }

        handler.handleAttribute(
                buffer,
                nameOffset, nameLen, nameLine, nameCol,
                operatorOffset, operatorLen, operatorLine, operatorCol,
                valueContentOffset, valueContentLen, valueOuterOffset, valueOuterLen, valueLine, valueCol);

    }



    private char[] computeLimitSequence(final char[] buffer, final int nameOffset, final int nameLen) {

        if (TextUtil.equals(true, this.nameLower, 0, this.nameLower.length, buffer, nameOffset, nameLen)) {
            return this.limitSequenceLower;
        }

        if (TextUtil.equals(true, this.nameUpper, 0, this.nameUpper.length, buffer, nameOffset, nameLen)) {
            return this.limitSequenceUpper;
        }

        final char[] limitSeq = new char[nameLen + 3];
        limitSeq[0] = '<';
        limitSeq[1] = '/';
        System.arraycopy(buffer,nameOffset,limitSeq,2,nameLen);
        limitSeq[nameLen + 2] = '>';
        return limitSeq;

    }


    
}