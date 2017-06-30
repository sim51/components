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

import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;

public class DummyRecord implements IndexedRecord {

    List<Object> data;

    /**
     * @param o
     */
    public DummyRecord(Object o) {
        data = new ArrayList<>();
        data.add(o);
    }

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
    public void put(int i, Object v) {
        data.add(i, v);

    }

    @Override
    public Object get(int i) {
        return data.get(i);
    }

}
