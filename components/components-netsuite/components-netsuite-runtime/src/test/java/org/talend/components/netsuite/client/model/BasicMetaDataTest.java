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

package org.talend.components.netsuite.client.model;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.talend.components.netsuite.client.NetSuiteException;
import org.talend.components.netsuite.client.model.customfield.CustomFieldRefType;

import com.netsuite.webservices.test.lists.accounting.Account;
import com.netsuite.webservices.test.platform.core.CustomFieldList;
import com.netsuite.webservices.test.platform.core.NullField;
import com.netsuite.webservices.test.platform.core.RecordRef;
import com.netsuite.webservices.test.platform.core.StringCustomFieldRef;
import com.netsuite.webservices.test.platform.core.types.SearchRecordType;
import com.netsuite.webservices.test.setup.customization.CustomRecord;

/**
 *
 */
public class BasicMetaDataTest {

    private TestBasicMetaDataImpl basicMetaData = TestBasicMetaDataImpl.getInstance();

    @Test
    public void testBasicRecordType() {
        Collection<String> typeNames = Arrays.asList(
                "crmCustomField",
                "entityCustomField",
                "itemCustomField",
                "itemNumberCustomField",
                "itemOptionCustomField",
                "otherCustomField",
                "transactionBodyCustomField",
                "transactionColumnCustomField",
                "customRecordCustomField",
                "transaction",
                "item",
                "customList",
                "customRecord",
                "customRecordType",
                "customTransaction",
                "customTransactionType"
        );

        for (String typeName : typeNames) {
            BasicRecordType value = BasicRecordType.getByType(typeName);
            assertNotNull(value);
            if (value.getSearchType() != null) {
                assertNotNull(SearchRecordType.fromValue(value.getSearchType()));
            }
        }
    }

    @Test
    public void testGetTypeClass() {
        assertEquals(Account.class, basicMetaData.getTypeClass(TestRecordTypeEnum.ACCOUNT.getTypeName()));
        assertEquals(CustomRecord.class, basicMetaData.getTypeClass(TestRecordTypeEnum.CUSTOM_RECORD.getTypeName()));
        assertEquals(RecordRef.class, basicMetaData.getTypeClass(RefType.RECORD_REF.getTypeName()));
        assertEquals(StringCustomFieldRef.class, basicMetaData.getTypeClass(CustomFieldRefType.STRING.getTypeName()));
        assertEquals(CustomFieldList.class, basicMetaData.getTypeClass("CustomFieldList"));
        assertEquals(NullField.class, basicMetaData.getTypeClass("NullField"));
        assertNull(basicMetaData.getTypeClass("Unknown"));
    }

    @Test
    public void testGetTypeInfo() {
        TypeDesc typeDesc1 = basicMetaData.getTypeInfo(basicMetaData.getTypeClass(TestRecordTypeEnum.ACCOUNT.getTypeName()));
        assertNotNull(typeDesc1);
        assertFalse(typeDesc1.getFields().isEmpty());
        assertNull(typeDesc1.getField("class"));

        assertNotNull(typeDesc1.getField("internalId"));
        assertNotNull(typeDesc1.getField("internalId").asSimple().getPropertyName());
        assertTrue(typeDesc1.getField("internalId").isKey());

        assertNotNull(typeDesc1.getField("externalId"));
        assertNotNull(typeDesc1.getField("externalId").asSimple().getPropertyName());
        assertTrue(typeDesc1.getField("externalId").isKey());

        TypeDesc typeDesc2 = basicMetaData.getTypeInfo(basicMetaData.getTypeClass(TestRecordTypeEnum.CUSTOM_RECORD.getTypeName()));

        assertNotNull(typeDesc2.getField("scriptId"));
        assertNotNull(typeDesc2.getField("scriptId").asSimple().getPropertyName());
        assertTrue(typeDesc2.getField("scriptId").isKey());
    }

    @Test
    public void testCreateInstance() {
        Object instance = basicMetaData.createInstance(TestRecordTypeEnum.ACCOUNT.getTypeName());
        assertNotNull(instance);
        assertThat(instance, instanceOf(Account.class));
    }

    @Test(expected = NetSuiteException.class)
    public void testCreateInstanceForUnknownType() {
        basicMetaData.createInstance("Unknown");
    }
}
