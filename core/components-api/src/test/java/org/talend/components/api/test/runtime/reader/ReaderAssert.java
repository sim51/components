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
import org.talend.components.api.exception.ComponentException;

/**
 * A set of assert methods for {@link Reader}.
 */
public class ReaderAssert {

    /**
     * This method assert that :
     * <ul>
     * <li>{@link Reader#start()} return <code>true</code> and should not throws any exception</li>
     * <li>{@link Reader#getCurrent()} return the first read element.</li>
     * <li>{@link Reader#getReturnValues()} is not null</li>
     * <li>the value of {@link ComponentDefinition#RETURN_TOTAL_RECORD_COUNT} in the returned map {@link Reader#getReturnValues()}
     * is equal to 1
     * </i>
     * </ul>
     */
    public static <T> void canStart(Reader<T> reader) {
        try {
            assertNotNull(reader);
            assertTrue(reader.start());
            assertNotNull(reader.getCurrent());
            Map<String, Object> returnedValues = reader.getReturnValues();
            assertNotNull(returnedValues);
            assertTrue(returnedValues.containsKey(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
            assertEquals(1, returnedValues.get(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
        } catch (Throwable e) {
            fail("expect that the reader starts whit no error, instead an exception was thrown: "
                    + ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * This method assert that :
     * <ul>
     * <li>{@link Reader#start()} return <code>false</code> and should not throws any exception</li>
     * <li>{@link Reader#getCurrent()} throw NoSuchElementException.</li>
     * </ul>
     */
    public static <T> void cannotStart(Reader<T> reader) {
        try {
            assertNotNull(reader);
            assertFalse(reader.start());
            reader.getCurrent();
            fail("expect the reader#getCurrent() to throw a NoSuchElementException as the reader#start() returned false.");
        } catch (NoSuchElementException e) {
            // This is the expected behavior, so just ignore this exception here
            return;
        } catch (Throwable e) {
            fail("expect that the reader starts whit no error, instead an exception was thrown"
                    + ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * <b>Reader can/cannot start should be checked first</b><br/>
     * 
     * This method assert that :
     * <ul>
     * <li>{@link Reader#advance())} return <code>true</code> and should not throw any exception.</li>
     * <li>{@link Reader#getCurrent()} return read element as long as {@link Reader#advance())} return <code>true</code>.</li>
     * <li>the value of {@link ComponentDefinition#RETURN_TOTAL_RECORD_COUNT} in the returned map {@link Reader#getReturnValues()}
     * is equal to number of the records that was read
     * </i>
     * </ul>
     */
    public static <T> void canAdvance(Reader<T> reader) {
        try {
            assertNotNull(reader);
            int dataCount = 1;
            while (reader.advance()) {
                assertNotNull(reader.getCurrent());
                dataCount++;
            }
            assertTrue(dataCount > 1); // assert that the reader advanced at least once
            Map<String, Object> returnedValues = reader.getReturnValues();
            assertNotNull(returnedValues);
            assertTrue(returnedValues.containsKey(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
            assertEquals(dataCount, returnedValues.get(ComponentDefinition.RETURN_TOTAL_RECORD_COUNT));
        } catch (Throwable e) {
            fail("expect that the reader starts and advance whit no error, instead an exception was thrown: "
                    + ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * <b>Reader can/cannot start should be checked first</b><br/>
     * This method assert that :
     * <ul>
     * <li>The test of {@link ReaderAssert#canStart(Reader)} is ok</li>
     * <li>{@link Reader#advance()} return <code>false</code> and should not throws any exception</li>
     * <li>{@link Reader#getCurrent()} throw NoSuchElementException.</li>
     * </ul>
     */
    public static <T> void cannotAdvance(Reader<T> reader) {
        try {
            assertNotNull(reader);
            assertFalse(reader.advance());
            reader.getCurrent();
            fail("expect the reader#getCurrent() to throw a NoSuchElementException as the reader#advance() returned false.");
        } catch (NoSuchElementException e) {
            // This is the expected behavior, so just ignore this exception here
            return;
        } catch (Throwable e) {
            fail("expect that the reader starts whit no error, instead an exception was thrown"
                    + ExceptionUtils.getStackTrace(e));
        }
    }

    /**
     * this method assert that the {@link Reader} throw a {@link ComponentException} when {@link Reader#start()} is called
     */
    public static <T> void startAndDieOnError(Reader<T> reader) {
        try {
            assertNotNull(reader);
            reader.start();
            fail("Expect the reader to throw a ComponentException");
        } catch (ComponentException e) {
            // the reader should throw this exception
            return;
        } catch (Throwable e) {
            fail("Expect the reader to throw a ComponentException, instead of: " + ExceptionUtils.getStackTrace(e));
        }

    }

    /**
     * this method assert that the {@link Reader} throw a {@link ComponentException} when {@link Reader#advance()} is called
     */
    public static <T> void advanceAndDieOnError(Reader<T> reader) {
        try {
            assertNotNull(reader);
            reader.advance();
            fail("Expect the reader to throw a ComponentException");
        } catch (ComponentException e) {
            // the reader should throw this exception
            return;
        } catch (Throwable e) {
            fail("Expect the reader to throw a ComponentException, instead of: " + ExceptionUtils.getStackTrace(e));
        }
    }

}
