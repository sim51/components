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
public class Test01SourceOrSinkIT {

    @Test
    public void test1() {
        Test01Definition definition = new Test01Definition();
        Test01Properties properties = new Test01Properties();
        Test01SourceOrSink sourceOrSink = new Test01SourceOrSink();
        Test01Reader reader = new Test01Reader();

        definition.method1();
        definition.method2();
        definition.method3();

        properties.method1();
        properties.method2();
        properties.method3();

        sourceOrSink.method1();
        sourceOrSink.method2();
        reader.method1();
    }

    @Test
    public void test2() {
        Test01Definition definition = new Test01Definition();
        Test01Properties properties = new Test01Properties();
        Test01SourceOrSink sourceOrSink = new Test01SourceOrSink();
        Test01Reader reader = new Test01Reader();

        definition.method1();
        definition.method2();
        definition.method3();

        properties.method1();
        properties.method2();
        properties.method3();

        sourceOrSink.method2();
        sourceOrSink.method3();
        reader.method1();
    }
}
