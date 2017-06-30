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

import java.util.List;

/**
 * This is an example service that wrap the interactions with the external system / API calls.
 */
public class DummyReadService {

    public boolean connect() throws ServiceException {
        return true;
    }

    public List<Object> read() throws ServiceException {
        return null;
    }

    public void disconnect() throws ServiceException {

    }

    public static class ServiceException extends Exception {

        private static final long serialVersionUID = 8839941634399312018L;

        public ServiceException(String msg) {
            super(msg);
        }

    }

}
