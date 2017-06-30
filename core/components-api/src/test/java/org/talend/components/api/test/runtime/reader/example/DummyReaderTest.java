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
package org.talend.components.api.test.runtime.reader.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.talend.components.api.component.runtime.Source;
import org.talend.components.api.container.DefaultComponentRuntimeContainerImpl;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.test.runtime.reader.ReaderAssert;
import org.talend.components.api.test.runtime.reader.SourceReaderTest;
import org.talend.components.api.test.runtime.reader.example.DummyReadService.ServiceException;
import org.talend.daikon.properties.ValidationResult;

public class DummyReaderTest implements SourceReaderTest {

    @Mock
    private DummyReadService readerServiceMock;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private RuntimeContainer container;

    private Source source;

    private DummyComponentProperties properties;

    @Before
    public void setup() {
        container = new DefaultComponentRuntimeContainerImpl();
        source = new DummySource();
        properties = new DummyComponentProperties("Dummy components properties name");
        properties.setupProperties();
    }

    @Test
    @Override
    public void testReadSourceEmpty() {

        try {

            // setup
            properties.dieOnError.setValue(false);
            assertEquals(ValidationResult.Result.OK, source.initialize(container, properties).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());
            DummyReader reader = (DummyReader) source.createReader(container);

            // mock
            List<Object> list = new ArrayList<>();
            when(readerServiceMock.connect()).thenReturn(true);
            when(readerServiceMock.read()).thenReturn(list);
            reader.readerService = readerServiceMock;

            // assert
            ReaderAssert.cannotStart(reader);
            ReaderAssert.cannotAdvance(reader);

        } catch (ServiceException e) {
            fail("should not throw exception" + e.getMessage());
        }

    }

    @Test
    @Override
    public void testReadSourceWithOnly1Element() {
        try {

            // setup
            properties.dieOnError.setValue(false);
            assertEquals(ValidationResult.Result.OK, source.initialize(container, properties).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());
            DummyReader reader = (DummyReader) source.createReader(container);

            // mock
            List<Object> list = new ArrayList<>();
            list.add(new Object());
            when(readerServiceMock.connect()).thenReturn(true);
            when(readerServiceMock.read()).thenReturn(list);
            reader.readerService = readerServiceMock;

            // assert
            ReaderAssert.canStart(reader);
            ReaderAssert.cannotAdvance(reader);

        } catch (ServiceException e) {
            fail("should not throw exception" + e.getMessage());
        }
    }

    @Test
    @Override
    public void testReadSourceWithMultipleElements() {
        try {

            // setup
            properties.dieOnError.setValue(false);
            assertEquals(ValidationResult.Result.OK, source.initialize(container, properties).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());
            DummyReader reader = (DummyReader) source.createReader(container);

            // mock
            List<Object> list = new ArrayList<>();
            list.add(new Object());
            list.add(new Object());
            list.add(new Object());
            when(readerServiceMock.connect()).thenReturn(true);
            when(readerServiceMock.read()).thenReturn(list);
            reader.readerService = readerServiceMock;

            // assert
            ReaderAssert.canStart(reader);
            ReaderAssert.canAdvance(reader);

        } catch (ServiceException e) {
            fail("should not throw exception" + e.getMessage());
        }

    }

    @Test
    @Override
    public void testReadSourceUnavailableDieOnError() {
        try {

            // setup
            properties.dieOnError.setValue(true);
            assertEquals(ValidationResult.Result.OK, source.initialize(container, properties).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());
            DummyReader reader = (DummyReader) source.createReader(container);

            // mock
            when(readerServiceMock.connect()).thenThrow(new ServiceException("500 Unavailable data source"));
            reader.readerService = readerServiceMock;

            // assert
            ReaderAssert.startAndDieOnError(reader);
            ReaderAssert.cannotAdvance(reader);

        } catch (ServiceException e) {
            fail("should not throw exception" + e.getMessage());
        }

    }

    @Test
    @Override
    public void testReadSourceUnavailableHandleError() {
        try {

            /// setup
            properties.dieOnError.setValue(false);
            assertEquals(ValidationResult.Result.OK, source.initialize(container, properties).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());
            DummyReader reader = (DummyReader) source.createReader(container);

            // mock
            when(readerServiceMock.connect()).thenThrow(new ServiceException("500 Unavailable data source"));
            reader.readerService = readerServiceMock;

            // assert
            ReaderAssert.cannotStart(reader);
            ReaderAssert.cannotAdvance(reader);

        } catch (ServiceException e) {
            fail("should not throw exception" + e.getMessage());
        }

    }

    @Test
    @Override
    public void testClose() {
        // TODO Auto-generated method stub

    }

}
