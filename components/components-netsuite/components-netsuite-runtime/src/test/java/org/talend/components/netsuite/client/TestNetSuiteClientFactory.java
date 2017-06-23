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

package org.talend.components.netsuite.client;

import org.talend.components.netsuite.NetSuiteVersion;

import com.netsuite.webservices.test.platform.NetSuitePortType;

/**
 *
 */
public class TestNetSuiteClientFactory implements NetSuiteClientFactory<NetSuitePortType> {

    public static final TestNetSuiteClientFactory INSTANCE = new TestNetSuiteClientFactory();

    @Override
    public NetSuiteClientService<NetSuitePortType> createClient() throws NetSuiteException {
        return new TestNetSuiteClientService();
    }

    @Override
    public NetSuiteVersion getApiVersion() {
        return new NetSuiteVersion(2016, 2);
    }
}
