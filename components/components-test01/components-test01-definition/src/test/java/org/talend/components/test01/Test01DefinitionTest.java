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

package org.talend.components.test01;

import org.junit.Test;

/**
 * Created by ihor.istomin on 7/3/2017.
 */
public class Test01DefinitionTest {

    @Test
    public void test1() {
        new Test01Definition().method1();
    }

    @Test
    public void test2() {
        new Test01Definition().method2();
    }
}
