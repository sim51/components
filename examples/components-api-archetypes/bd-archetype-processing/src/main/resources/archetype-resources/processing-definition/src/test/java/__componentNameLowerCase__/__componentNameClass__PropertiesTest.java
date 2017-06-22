// ============================================================================
//
// Copyright (C) 2006-2016 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package ${package}.${componentNameLowerCase};

import org.hamcrest.Matchers;
import org.junit.Test;
import org.talend.daikon.properties.presentation.Form;
import static org.hamcrest.MatcherAssert.assertThat;

public class ${componentNameClass}PropertiesTest {

    /**
     * Checks {@link ${componentNameClass}Properties} sets correctly initial schema property
     */
    @Test
    public void testDefaultProperties() {
        ${componentNameClass}Properties properties = new ${componentNameClass}Properties("test");
    }

    /**
     * Checks {@link ${componentNameClass}Properties} sets correctly initial layout properties
     */
    @Test
    public void testSetupLayout() {
        ${componentNameClass}Properties properties = new ${componentNameClass}Properties("test");

        properties.setupLayout();

        Form main = properties.getForm(Form.MAIN);
        assertThat(main, Matchers.notNullValue());
    }

    /**
     * Checks {@link ${componentNameClass}Properties#refreshLayout(Form)}
     */
    @Test
    public void testRefreshLayout() {
        ${componentNameClass}Properties properties = new ${componentNameClass}Properties("test");
        properties.init();
        properties.refreshLayout(properties.getForm(Form.MAIN));
    }
}
