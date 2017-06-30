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

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.avro.generic.IndexedRecord;
import org.joda.time.Instant;
import org.talend.components.api.component.runtime.AbstractBoundedReader;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.component.runtime.Result;
import org.talend.components.api.exception.ComponentException;
import org.talend.components.api.test.runtime.reader.example.DummyReadService.ServiceException;

public class DummyReader extends AbstractBoundedReader<IndexedRecord> {

    // Reader service
    public DummyReadService readerService;

    // reader parameters
    private boolean dieOnError;

    private boolean started;

    private Boolean advanced;

    private IndexedRecord current;

    private Iterator<Object> recordsIterator;

    private Result result;

    /**
     * @param source
     */
    protected DummyReader(DummyComponentProperties properties, BoundedSource source) {
        super(source);
        this.dieOnError = properties.dieOnError.getValue();
        result = new Result();
        this.readerService = new DummyReadService();

    }

    @Override
    public boolean start() throws IOException {
        try {
            started = readerService.connect();
            if (started) {
                List<Object> records = readerService.read();
                recordsIterator = records.iterator();
                current = convertRecordToAvro(recordsIterator.next());
                result.totalCount++;
            }

        } catch (ServiceException e) {
            if (dieOnError) {
                throw new ComponentException(e);
            }
        }
        return started;
    }

    @Override
    public boolean advance() throws IOException {

        if (!started) {
            return false;
        }

        advanced = recordsIterator.hasNext();
        if (advanced) {
            current = convertRecordToAvro(recordsIterator.next());
            result.totalCount++;
        }

        return advanced;
    }

    private IndexedRecord convertRecordToAvro(Object o) {
        return new DummyRecord(o);
    }

    @Override
    public IndexedRecord getCurrent() throws NoSuchElementException {
        if (!started || (advanced != null && !advanced)) {
            throw new NoSuchElementException("No element in the reader, call start() first");
        }

        return current;
    }

    @Override
    public Instant getCurrentTimestamp() throws NoSuchElementException {

        return null;
    }

    @Override
    public void close() throws IOException {
        try {
            readerService.disconnect();

        } catch (ServiceException e) {
            if (dieOnError) {
                throw new ComponentException(e);
            }
        }
    }

    @Override
    public Map<String, Object> getReturnValues() {
        return result.toMap();
    }

}
