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
package org.talend.components.common.converter.jdbc;

import java.sql.ResultSet;

import org.apache.avro.Schema;
import org.apache.avro.generic.IndexedRecord;
import org.talend.daikon.avro.converter.AbstractAvroConverter;

/**
 * One way converter, which converts JDBC {@link ResultSet} (data source specific type) to {@link IndexedRecord} (Avro)
 * This converter to be used for whole record 
 * 
 * This converter can be used by any component which uses Java JDBC API
 * 
 * Such converter could be used in {@link Reader} to convert data storage
 * specific object to {@link IndexedRecord}
 */
public class ResultSetIndexedRecordConverter extends AbstractAvroConverter<ResultSet, IndexedRecord> {

    /**
     * Constructor sets runtime avro schema and {@link ResultSet} as datum class
     * 
     * @param schema Avro runtime schema
     */
    public ResultSetIndexedRecordConverter(Schema schema) {
       super(ResultSet.class, schema); 
    }
    
    /**
     * JDBC {@link ResultSet} is not used for writing
     * Thus, this method is not supported
     */
    @Override
    public ResultSet convertToDatum(IndexedRecord value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public IndexedRecord convertToAvro(ResultSet value) {
        // TODO Auto-generated method stub
        return null;
    }

}
