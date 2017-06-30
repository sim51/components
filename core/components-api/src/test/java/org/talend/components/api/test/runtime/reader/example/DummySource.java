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
import java.util.List;

import org.apache.avro.Schema;
import org.talend.components.api.component.runtime.BoundedReader;
import org.talend.components.api.component.runtime.BoundedSource;
import org.talend.components.api.container.RuntimeContainer;
import org.talend.components.api.properties.ComponentProperties;
import org.talend.daikon.NamedThing;
import org.talend.daikon.properties.ValidationResult;

/**
 * An example of a data source
 */
public class DummySource implements BoundedSource {

    private static final long serialVersionUID = -2859353629597048397L;

    private DummyComponentProperties properties;

    @Override
    public List<NamedThing> getSchemaNames(RuntimeContainer container) throws IOException {
        return null;
    }

    @Override
    public Schema getEndpointSchema(RuntimeContainer container, String schemaName) throws IOException {

        return null;
    }

    @Override
    public ValidationResult validate(RuntimeContainer container) {

        return ValidationResult.OK;
    }

    @Override
    public ValidationResult initialize(RuntimeContainer container, ComponentProperties properties) {
        this.properties = (DummyComponentProperties) properties;
        return ValidationResult.OK;
    }

    @Override
    public List<? extends BoundedSource> splitIntoBundles(long desiredBundleSizeBytes, RuntimeContainer adaptor)
            throws Exception {

        return null;
    }

    @Override
    public long getEstimatedSizeBytes(RuntimeContainer adaptor) {

        return 0;
    }

    @Override
    public boolean producesSortedKeys(RuntimeContainer adaptor) {

        return false;
    }

    @Override
    public BoundedReader createReader(RuntimeContainer adaptor) {
        return new DummyReader(properties, this);
    }

}
