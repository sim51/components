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
package org.talend.components.api.test.runtime.reader;

import javax.xml.transform.Source;

import org.talend.components.api.component.runtime.Reader;

/**
 * This interface define the main use cases to test for a {@link Source} {@link Reader}.
 * <br/>
 * A reader read data from a data source and a data source can :<br/>
 * - contains 0 element<br/>
 * - only one element<br/>
 * - multiple elements<br/>
 * - be unavailable<br/>
 * 
 * <br/>
 * Every Reader should implement and validate those tests use cases at least
 * 
 */
public interface SourceReaderTest {

    /**
     * Test the reader behavior when the data source is empty
     */
    void testReadSourceEmpty();

    /**
     * Test the reader behavior when the data source contains only one element
     */
    void testReadSourceWithOnly1Element();

    /**
     * Test the reader behavior when the data source contains many elements
     */
    void testReadSourceWithManyElements();

    /**
     * Test the reader behavior when the data source is unavailable reader stop
     */
    void testReadSourceUnavailableDieOnError();

    /**
     * Test the reader behavior when the data source is unavailable reader handle error
     */
    void testReadSourceUnavailableHandleError();

    /**
     * Test reader close
     */
    void testClose();
}
