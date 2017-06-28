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
package org.talend.components.api.component.runtime.example;

import org.apache.avro.generic.IndexedRecord;

/**
 * A service that encapsulate the interaction with external system / API calls
 */
public class DummyReadService {

    public boolean start() throws ServiceException {
        return true;
    }

    public IndexedRecord read() throws ServiceException {
        return null;
    }

    public void close() throws ServiceException {

    }

    public static class ServiceException extends Exception {

        private static final long serialVersionUID = 8839941634399312018L;

    }

}
