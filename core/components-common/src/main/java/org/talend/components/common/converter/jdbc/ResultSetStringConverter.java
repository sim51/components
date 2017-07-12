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
import java.sql.SQLException;

import org.talend.components.api.exception.ComponentException;
import org.talend.daikon.avro.AvroUtils;
import org.talend.daikon.avro.converter.AbstractAvroConverter;

/**
 *
 */
public class ResultSetStringConverter extends AbstractAvroConverter<ResultSet, String> {
    
    /**
     * Column index to get from {@link ResultSet}
     */
    private final int columnIndex;

    /**
     * 
     * 
     * @param trim
     */
    public ResultSetStringConverter(boolean trim, int columnIndex) {
        super(ResultSet.class, AvroUtils._string());
        this.columnIndex = columnIndex;
    }
    
    /**
     * JDBC {@link ResultSet} is not used for writing
     * Thus, this method is not supported
     */
    @Override
    public ResultSet convertToDatum(String value) {
        throw new UnsupportedOperationException();
    }

    
    @Override
    public String convertToAvro(ResultSet resultSet) {
        try {
            return resultSet.getString(columnIndex);
        } catch (SQLException e) {
            throw new ComponentException(e);
        }
    }

}
