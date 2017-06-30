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

import org.talend.components.api.properties.ComponentPropertiesImpl;
import org.talend.daikon.properties.property.Property;
import org.talend.daikon.properties.property.PropertyFactory;

public class DummyComponentProperties extends ComponentPropertiesImpl {

    private static final long serialVersionUID = -2325515560799749865L;

    public Property<Boolean> dieOnError = PropertyFactory.newBoolean("dieOnError");

    public DummyComponentProperties(String name) {
        super(name);
    }

}
