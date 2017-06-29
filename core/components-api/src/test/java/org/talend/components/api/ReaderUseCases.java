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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.talend.components.api.component.ComponentDefinition;
import org.talend.components.api.component.runtime.Reader;
import org.talend.components.api.component.runtime.Source;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.daikon.properties.ValidationResult;

public class ReaderUseCases {

    /**
     * <b>Scenario: The reader is not empty and can start reading from source</b>
     * <p>
     * This method should test the {@link Reader#start()} method when the reader can start.
     * </p>
     * This use case check that :
     * <ul>
     * <li>{@link Reader#start()} return <code>true</code> and should not throws any exception</li>
     * <li>{@link Reader#getCurrent()} return the first read element.</li>
     * <li>
     * Total record count equal to 1<br/>
     * <i>You can do this by calling {@link Reader#getReturnValues()} that return a {@link Map}.that map should contain the read
     * record count under the key {@link ComponentDefinition#RETURN_TOTAL_RECORD_COUNT}.</i>
     * </li>
     * </ul>
     */
    public static abstract class SourceNotEmpty<T> extends AbstractReaderUseCase<T> {

        public SourceNotEmpty(RuntimeContainer container, Source source) {
            super(container, source);
        }

        @Override
        public final void runTest() {
            assertEquals(ValidationResult.Result.OK, source.initialize(container, initProperties()).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());

            reader = source.createReader(container);
            reader = mockReaderService();
            assertNotNull(reader);

            try {
                assertTrue(reader.start());
                assertNotNull(reader.getCurrent());
                Map<String, Object> returnedValues = reader.getReturnValues();
                assertNotNull(returnedValues);
                assertTrue(returnedValues.containsKey(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
                assertEquals(1, returnedValues.get(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
            } catch (Exception e) {
                fail("expect that the reader starts whit no error in this scenario, instead an exception has been thrown"
                        + ExceptionUtils.getStackTrace(e));
            }
        }
    }

    /**
     * <b>Scenario: The reader is empty and can't start reading from source</b>
     * <p>
     * This method should test the {@link Reader#start()} method when the reader can't start.
     * </p>
     * This use case check that :
     * <ul>
     * <li>{@link Reader#start()} return <code>false</code> and should not throws any exception</li>
     * <li>{@link Reader#getCurrent()} throw NoSuchElementException.</li>
     * </ul>
     */
    public static abstract class SourceIsEmpty<T> extends AbstractReaderUseCase<T> {

        public SourceIsEmpty(RuntimeContainer container, Source source) {
            super(container, source);
        }

        @Override
        public final void runTest() {
            assertEquals(ValidationResult.Result.OK, source.initialize(container, initProperties()).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());

            reader = source.createReader(container);
            reader = mockReaderService();
            assertNotNull(reader);

            try {
                boolean canStart = reader.start();
                assertFalse(canStart);
                reader.getCurrent();
                fail("expect the reader to throw a NoSuchElementException as the source is empty in this scenario.");
            } catch (NoSuchElementException e) {
                // This is the expected behavior, so just ignore this exception here
            } catch (Exception e) {
                fail("expect that the reader starts whit no error in this scenario, instead an exception has been thrown"
                        + ExceptionUtils.getStackTrace(e));
            }
        }

    }

    /**
     * <b>Scenario [Can Advance] : The reader is not empty and can advance (more than one element in the reader)</b>
     * <p>
     * This method should test the {@link Reader#advance()} method when the reader can advance.
     * </p>
     * This use case check that :
     * <ul>
     * <li>{@link Reader#start()} return <code>true</code> and should not throws any exception</li>
     * <li>{@link Reader#getCurrent()} return the first read element.</li>
     * <li>{@link Reader#advance())} return <code>true</code> and should not throw any exception.</li>
     * <li>{@link Reader#getCurrent()} return read element as long as {@link Reader#advance())} return <code>true</code>.</li>
     * </ul>
     */
    public static abstract class CanAdvance<T> extends AbstractReaderUseCase<T> {

        public CanAdvance(RuntimeContainer container, Source source) {
            super(container, source);
        }

        @Override
        public final void runTest() {
            assertEquals(ValidationResult.Result.OK, source.initialize(container, initProperties()).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());

            reader = source.createReader(container);
            reader = mockReaderService();
            assertNotNull(reader);

            try {

                assertTrue(reader.start());
                assertNotNull(reader.getCurrent());
                assertTrue(reader.advance());
                assertNotNull(reader.getCurrent());
                int dataCount = 2;

                // read all the elements from the reader
                while (reader.advance()) {
                    assertNotNull(reader.getCurrent());
                    dataCount++;
                }

                Map<String, Object> returnedValues = reader.getReturnValues();
                assertNotNull(returnedValues);
                assertTrue(returnedValues.containsKey(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
                assertEquals(dataCount, returnedValues.get(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
            } catch (Exception e) {
                fail("expect that the reader starts whit no error in this scenario, instead an exception has been thrown"
                        + ExceptionUtils.getStackTrace(e));
            }
        }
    }

    /**
     * <b>Scenario [Can not Advance] : The reader can not advance (The reader contains only one element)</b>
     * <p>
     * This method should test the {@link Reader#advance()} method when the reader can not advance.
     * </p>
     * This use case check that :
     * <ul>
     * <li>{@link Reader#start()} return <code>true</code> and should not throws any exception</li>
     * <li>{@link Reader#getCurrent()} return the first read element.</li>
     * <li>{@link Reader#advance())} return <code>false</code> and should not throw any exception.</li>
     * <li>{@link Reader#getCurrent()} throw NoSuchElementException.</li>
     * </ul>
     */
    public static abstract class CannotAdvance<T> extends AbstractReaderUseCase<T> {

        public CannotAdvance(RuntimeContainer container, Source source) {
            super(container, source);
        }

        @Override
        public final void runTest() {
            assertEquals(ValidationResult.Result.OK, source.initialize(container, initProperties()).getStatus());
            assertEquals(ValidationResult.Result.OK, source.validate(container).getStatus());

            reader = source.createReader(container);
            reader = mockReaderService();
            assertNotNull(reader);

            try {

                assertTrue(reader.start());
                assertNotNull(reader.getCurrent());
                assertFalse(reader.advance());
                reader.getCurrent();
                fail("expect the reader to throw a NoSuchElementException as the source is not advancable in this scenario.");
            } catch (NoSuchElementException e) {
                // This is the expected behavior, so just ignore this exception here
            } catch (Exception e) {
                fail("expect that the reader starts whit no error in this scenario, instead an exception has been thrown"
                        + ExceptionUtils.getStackTrace(e));
            }
        }
    }

    protected static abstract class AbstractReaderUseCase<T> {

        protected final Source source;

        protected final RuntimeContainer container;

        protected Reader<T> reader;

        public AbstractReaderUseCase(RuntimeContainer container, Source source) {
            this.container = container;
            this.source = source;
        }

        /**
         * Initialize the use case properties
         */
        public abstract ComponentProperties initProperties();

        /**
         * Run the test use case
         */
        public abstract void runTest();

        /**
         * Personalize the created reader.<br/>
         * Use this method if you want to mock the reader before the use case test scenario
         * 
         * use {@link ReaderTestUseCase#getReader()} to get the created reader.<br/>
         * mock or personalize your reader for this test case<br/>
         * returned the personalized reader<br/>
         * 
         */
        public Reader<T> mockReaderService() {
            return getReader();
        }

        /**
         * @return the reader used in this test use case
         */
        public Reader<T> getReader() {
            return reader;
        }

    }

}
