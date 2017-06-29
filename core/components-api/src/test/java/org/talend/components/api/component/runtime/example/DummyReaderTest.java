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
package org.talend.components.api.component.runtime.example;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.apache.avro.generic.IndexedRecord;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.talend.components.api.ReaderTest;
import org.talend.components.api.ReaderUseCases.SourceIsEmpty;
import org.talend.components.api.ReaderUseCases.SourceNotEmpty;
import org.talend.components.api.component.runtime.Reader;
import org.talend.components.api.component.runtime.Source;
import org.talend.components.api.component.runtime.example.DummyReadService.ServiceException;
import org.talend.components.api.container.DefaultComponentRuntimeContainerImpl;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;

public class DummyReaderTest implements ReaderTest {

    @Mock
    private DummyReadService readerServiceMock;

    @Mock
    private IndexedRecord recordMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    RuntimeContainer container;

    Source source;

    DummyComponentProperties properties;

    @Before
    public void setup() {
        container = new DefaultComponentRuntimeContainerImpl();
        source = new DummySource();
        properties = new DummyComponentProperties("Dummy components properties name");
        properties.setupProperties();
    }

    @Test
    @Override
    public void testStartWithSourceNotEmpty() {

        SourceNotEmpty<IndexedRecord> usecase = new SourceNotEmpty<IndexedRecord>(container, source) {

            @Override
            public ComponentProperties initProperties() {
                properties.dieOnError.setValue(false);
                return properties;
            }

            @Override
            public Reader<IndexedRecord> mockReaderService() {
                DummyReader reader = (DummyReader) getReader();
                reader.readerService = readerServiceMock;

                try {
                    when(readerServiceMock.start()).thenReturn(true);
                    when(readerServiceMock.read()).thenReturn(recordMock);
                } catch (ServiceException e) {
                    fail("should not throw exception" + e.getMessage());
                }
                return reader;
            }
        };

        usecase.assertTestRun();

    }

    @Test
    @Override
    public void testStartWithSourceEmpty() {
        SourceIsEmpty<IndexedRecord> usecase = new SourceIsEmpty<IndexedRecord>(container, source) {

            @Override
            public ComponentProperties initProperties() {
                properties.dieOnError.setValue(false);
                return properties;
            }

            @Override
            public Reader<IndexedRecord> mockReaderService() {
                DummyReader reader = (DummyReader) getReader();
                reader.readerService = readerServiceMock;

                try {
                    when(readerServiceMock.start()).thenReturn(false);
                } catch (ServiceException e) {
                    fail("should not throw exception" + e.getMessage());
                }
                return reader;
            }
        };

        usecase.assertTestRun();

    }

    @Override
    public void testAdvanceWithSourceNotEmpty() {
        // TODO Auto-generated method stub

    }

    @Override
    public void testAdvanceWithSourceHasOnly1Element() {
        // TODO Auto-generated method stub

    }

    @Override
    public void testGetCurrentWhenNonStartable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void testGetCurrentWhenNonAdvancable() {
        // TODO Auto-generated method stub

    }

    @Override
    public void testGetCurrent() {
        // TODO Auto-generated method stub

    }

}
