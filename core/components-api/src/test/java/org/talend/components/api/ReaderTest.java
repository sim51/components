// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.components.api;

import org.talend.components.api.ReaderUseCases.SourceNotEmpty;
import org.talend.components.api.component.runtime.Reader;

/**
 * This interface define the main use cases to test for a {@link Reader}
 * Every Reader should implement and validate those tests use cases
 * 
 * see {@link ReaderUseCases} to find out some use cases helper to setup and run<br/>
 * 
 * <b>For example :</b>
 * To implement a test for {@link ReaderTest#testStartWithSourceNotEmpty()} you can go with something like :
 * 
 * <pre>
 * 
 * &#64;Test
 * public void testStartWhenSourceNotEmpty() {
 * 
 *     SourceNotEmpty<IndexedRecord> readerNotEmptyUseCase = new SourceNotEmpty<IndexedRecord>(runtimeContiner, dataSource) {
 * 
 *         &#64;Override
 *         public ComponentProperties initProperties() {
 *             // initialize the component properties for the test...
 *             return properties;
 *         }
 * 
 *         &#64;Override
 *         public Reader<IndexedRecord> mockReaderService() {
 *             reader = (ComponenetTypeReader) getReader();
 *             // mock the reader if necessary...
 *             return reader;
 *         }
 *     };
 * 
 *     readerNotEmptyUseCase.runTest(); // run the use case test
 * }
 * </pre>
 *
 */
public interface ReaderTest {

    /**
     * Implement this test using {@link SourceNotEmpty} class.
     * You should initialize properties using {@link SourceNotEmpty#initProperties()} and mock the reader if necessary using
     * {@link SourceNotEmpty#mockReaderService()} to make {@link SourceNotEmpty#runTest()} work as junit test
     */
    public void testStartWithSourceNotEmpty();

    public void testStartWithSourceEmpty();

    public void testAdvanceWithSourceNotEmpty();

    public void testAdvanceWithSourceHasOnly1Element();

    public void testGetCurrentWhenNonStartable();

    public void testGetCurrentWhenNonAdvancable();

    public void testGetCurrent();

}
