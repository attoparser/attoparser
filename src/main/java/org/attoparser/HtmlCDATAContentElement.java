/*
 * =============================================================================
 * 
 *   Copyright (c) 2012-2022, The ATTOPARSER team (https://www.attoparser.org)
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

    // We will consider a script tag CDATA only if it has no "type" attribute, or a "type" attribute with
    // any of these values
    private static final char[] ATTRIBUTE_TYPE_JAVASCRIPT_VALUE = "javascript".toCharArray();
    private static final char[] ATTRIBUTE_TYPE_ECMASCRIPT_VALUE = "ecmascript".toCharArray();
    private static final char[] ATTRIBUTE_TYPE_TEXT_JAVASCRIPT_VALUE = "text/javascript".toCharArray();
    private static final char[] ATTRIBUTE_TYPE_TEXT_ECMASCRIPT_VALUE = "text/ecmascript".toCharArray();
    private static final char[] ATTRIBUTE_TYPE_APPLICATION_JAVASCRIPT_VALUE = "application/javascript".toCharArray();
    private static final char[] ATTRIBUTE_TYPE_APPLICATION_ECMASCRIPT_VALUE = "application/ecmascript".toCharArray();
    private static final char[] ATTRIBUTE_TYPE_MODULE_VALUE = "module".toCharArray();

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

        if (TextUtil.equals(false, buffer, nameOffset, nameLen, ATTRIBUTE_TYPE_NAME, 0, ATTRIBUTE_TYPE_NAME.length)) {
            // We are processing a 'type' attribute...
            if (TextUtil.equals(true, this.nameLower, 0, this.nameLower.length, ELEMENT_SCRIPT_NAME, 0, ELEMENT_SCRIPT_NAME.length)) {
                // ...and this is a <script> tag... so unless the "type" value we find here is one of the types
                // that disable parsing (javascript/ecmascript ones), we should consider it enabled again

                status.shouldDisableParsing = false;

                if (TextUtil.endsWith(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_JAVASCRIPT_VALUE, 0, ATTRIBUTE_TYPE_JAVASCRIPT_VALUE.length) ||
                    TextUtil.endsWith(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_ECMASCRIPT_VALUE, 0, ATTRIBUTE_TYPE_ECMASCRIPT_VALUE.length) ||
                    TextUtil.endsWith(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_MODULE_VALUE, 0, ATTRIBUTE_TYPE_MODULE_VALUE.length)) {
                    // The script type might be one of the types that disable parsing for script tags

                    if (TextUtil.equals(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_JAVASCRIPT_VALUE, 0, ATTRIBUTE_TYPE_JAVASCRIPT_VALUE.length) ||
                        TextUtil.equals(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_ECMASCRIPT_VALUE, 0, ATTRIBUTE_TYPE_ECMASCRIPT_VALUE.length)) {

                        status.shouldDisableParsing = true;

                    } else if (TextUtil.equals(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_TEXT_JAVASCRIPT_VALUE, 0, ATTRIBUTE_TYPE_TEXT_JAVASCRIPT_VALUE.length) ||
                               TextUtil.equals(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_TEXT_ECMASCRIPT_VALUE, 0, ATTRIBUTE_TYPE_TEXT_ECMASCRIPT_VALUE.length)) {

                        status.shouldDisableParsing = true;

                    } else if (TextUtil.equals(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_APPLICATION_JAVASCRIPT_VALUE, 0, ATTRIBUTE_TYPE_APPLICATION_JAVASCRIPT_VALUE.length) ||
                               TextUtil.equals(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_APPLICATION_ECMASCRIPT_VALUE, 0, ATTRIBUTE_TYPE_APPLICATION_ECMASCRIPT_VALUE.length)) {

                        status.shouldDisableParsing = true;

                    } else if (TextUtil.equals(false, buffer, valueContentOffset, valueContentLen, ATTRIBUTE_TYPE_MODULE_VALUE, 0, ATTRIBUTE_TYPE_MODULE_VALUE.length)) {

                        status.shouldDisableParsing = true;

                    }

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