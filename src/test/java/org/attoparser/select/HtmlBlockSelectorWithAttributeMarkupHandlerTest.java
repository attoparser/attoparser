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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.attoparser.discard.DiscardMarkupHandler;
import org.attoparser.IMarkupHandler;
import org.attoparser.IMarkupParser;
import org.attoparser.MarkupParser;
import org.attoparser.config.ParseConfiguration;
import org.attoparser.output.OutputMarkupHandler;

/*
 *
 * @author Daniel Fernandez
 * @since 2.0.0
 */
public class HtmlBlockSelectorWithAttributeMarkupHandlerTest extends TestCase {

    private static final String RESOURCES_FOLDER = "htmlblockselectorwithattribute/";

    public HtmlBlockSelectorWithAttributeMarkupHandlerTest() {
        super();
    }
    
    
    public void test() throws Exception {

        final ParseConfiguration config = ParseConfiguration.htmlConfiguration();

        final IMarkupParser parser = new MarkupParser(config);

        final IMarkupSelectorReferenceResolver referenceResolver = new TestingFragmentReferenceResolver();

        final URL resourcesFolderURL = Thread.currentThread().getContextClassLoader().getResource(RESOURCES_FOLDER);
        assertNotNull(resourcesFolderURL);

        final File resourcesFolder = new File(resourcesFolderURL.toURI());
        assertTrue(resourcesFolder.isDirectory());

        final List<File> resourceFolderFiles = new ArrayList<File>(Arrays.asList(resourcesFolder.listFiles()));
        Collections.sort(resourceFolderFiles, new Comparator<File>() {
            public int compare(final File o1, final File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        final List<File> testFiles = new ArrayList<File>();
        final List<File> resultFiles = new ArrayList<File>();

        for (final File resourceFolderFile : resourceFolderFiles) {
            if (resourceFolderFile.getName().startsWith("test") && resourceFolderFile.getName().endsWith(".html")) {
                testFiles.add(resourceFolderFile);
            } else if (resourceFolderFile.getName().startsWith("result") && resourceFolderFile.getName().endsWith(".html")) {
                resultFiles.add(resourceFolderFile);
            }
        }

        for (int i = 0; i < testFiles.size(); i++) {

            final File testFile = testFiles.get(i);
            final File resultFile = resultFiles.get(i);

            final List<String> testFileLines =
                    IOUtils.readLines(new InputStreamReader(new FileInputStream(testFile), "UTF-8"));
            final String testFileContents = StringUtils.join(testFileLines,'\n');

            final List<String> resultFileLines =
                    IOUtils.readLines(new InputStreamReader(new FileInputStream(resultFile), "UTF-8"));
            final String blockSelector = resultFileLines.get(0);
            resultFileLines.remove(0);
            final String resultFileContents = StringUtils.join(resultFileLines,'\n');

            final String[] blockSelectors = StringUtils.split(blockSelector,",");

            check(parser, testFile.getName(), testFileContents, resultFileContents, blockSelectors, referenceResolver);

        }


    }
    




    private static void check(
            final IMarkupParser parser, final String templateName, final String input, final String output, final String[] blockSelectors,
            final IMarkupSelectorReferenceResolver referenceResolver) throws Exception{

        final StringReader reader = new StringReader(input);
        final StringWriter writer = new StringWriter();

        final IMarkupHandler directOutputHandler = new OutputMarkupHandler(writer);
        final IMarkupHandler attributeMarkingHandler = new OpenCloseAttributeSelectionMarkingMarkupHandler(directOutputHandler);

        final BlockSelectorMarkupHandler handler =
                new BlockSelectorMarkupHandler(attributeMarkingHandler, new DiscardMarkupHandler(), blockSelectors, referenceResolver);

        parser.parse(reader, handler);

        assertEquals("Test failed for file: " + templateName, output, writer.toString());

    }
    
    


    static final class TestingFragmentReferenceResolver implements IMarkupSelectorReferenceResolver {

        public String resolveSelectorFromReference(final String reference) {
            return "/[th:fragment='" + reference + "' or data-th-fragment='" + reference + "']";
        }
    }

}
