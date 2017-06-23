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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.talend.components.netsuite.client.model.BasicMetaData;
import org.talend.components.netsuite.client.model.TestBasicMetaDataImpl;

import com.netsuite.webservices.test.platform.NetSuitePortType;
import com.netsuite.webservices.test.platform.core.BaseRef;
import com.netsuite.webservices.test.platform.core.Record;
import com.netsuite.webservices.test.platform.core.SearchResult;
import com.netsuite.webservices.test.platform.core.Status;
import com.netsuite.webservices.test.platform.core.StatusDetail;
import com.netsuite.webservices.test.platform.messages.ReadResponse;
import com.netsuite.webservices.test.platform.messages.ReadResponseList;
import com.netsuite.webservices.test.platform.messages.WriteResponse;
import com.netsuite.webservices.test.platform.messages.WriteResponseList;

/**
 *
 */
public class TestNetSuiteClientService extends NetSuiteClientService<NetSuitePortType> {

    public static final String NS_URI_PLATFORM_MESSAGES =
            "urn:messages_2016_2.platform.webservices.netsuite.com";

    public TestNetSuiteClientService() {
        super();

        metaDataSource = createDefaultMetaDataSource();
    }

    @Override
    public <RecT, SearchT> NsSearchResult<RecT> search(SearchT searchRecord) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT> NsSearchResult<RecT> searchMore(int pageIndex) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT> NsSearchResult<RecT> searchMoreWithId(String searchId, int pageIndex) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT> NsSearchResult<RecT> searchNext() throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT, RefT> NsReadResponse<RecT> get(RefT ref) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT, RefT> List<NsReadResponse<RecT>> getList(List<RefT> refs) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT, RefT> NsWriteResponse<RefT> add(RecT record) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT, RefT> List<NsWriteResponse<RefT>> addList(List<RecT> records) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT, RefT> NsWriteResponse<RefT> update(RecT record) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT, RefT> List<NsWriteResponse<RefT>> updateList(List<RecT> records) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT, RefT> NsWriteResponse<RefT> upsert(RecT record) throws NetSuiteException {
        return null;
    }

    @Override
    public <RecT, RefT> List<NsWriteResponse<RefT>> upsertList(List<RecT> records) throws NetSuiteException {
        return null;
    }

    @Override
    public <RefT> NsWriteResponse<RefT> delete(RefT ref) throws NetSuiteException {
        return null;
    }

    @Override
    public <RefT> List<NsWriteResponse<RefT>> deleteList(List<RefT> refs) throws NetSuiteException {
        return null;
    }

    @Override
    public BasicMetaData getBasicMetaData() {
        return TestBasicMetaDataImpl.getInstance();
    }

    @Override
    public CustomMetaDataSource createDefaultCustomMetaDataSource() {
        return new EmptyCustomMetaDataSource();
    }

    @Override
    protected void doLogout() throws NetSuiteException {

    }

    @Override
    protected void doLogin() throws NetSuiteException {

    }

    @Override
    protected boolean errorCanBeWorkedAround(Throwable t) {
        return false;
    }

    @Override
    protected boolean errorRequiresNewLogin(Throwable t) {
        return false;
    }

    @Override
    protected String getPlatformMessageNamespaceUri() {
        return NS_URI_PLATFORM_MESSAGES;
    }

    @Override
    protected <T> T createNativePreferences(NsPreferences nsPreferences) {
        return null;
    }

    @Override
    protected <T> T createNativeSearchPreferences(NsSearchPreferences nsSearchPreferences) {
        return null;
    }

    @Override
    protected <T> T createNativeApplicationInfo(NetSuiteCredentials nsCredentials) {
        return null;
    }

    @Override
    protected <T> T createNativePassport(NetSuiteCredentials nsCredentials) {
        return null;
    }

    @Override
    protected NetSuitePortType getNetSuitePort(String defaultEndpointUrl, String account) throws NetSuiteException {
        return null;
    }

    public static <RefT> List<NsWriteResponse<RefT>> toNsWriteResponseList(WriteResponseList writeResponseList) {
        List<NsWriteResponse<RefT>> nsWriteResponses = new ArrayList<>(writeResponseList.getWriteResponse().size());
        for (WriteResponse writeResponse : writeResponseList.getWriteResponse()) {
            nsWriteResponses.add((NsWriteResponse<RefT>) toNsWriteResponse(writeResponse));
        }
        return nsWriteResponses;
    }

    public static <RecT> List<NsReadResponse<RecT>> toNsReadResponseList(ReadResponseList readResponseList) {
        List<NsReadResponse<RecT>> nsReadResponses = new ArrayList<>(readResponseList.getReadResponse().size());
        for (ReadResponse readResponse : readResponseList.getReadResponse()) {
            nsReadResponses.add((NsReadResponse<RecT>) toNsReadResponse(readResponse));
        }
        return nsReadResponses;
    }

    public static <RecT> NsSearchResult<RecT> toNsSearchResult(SearchResult result) {
        NsSearchResult nsResult = new NsSearchResult(toNsStatus(result.getStatus()));
        nsResult.setSearchId(result.getSearchId());
        nsResult.setTotalPages(result.getTotalPages());
        nsResult.setTotalRecords(result.getTotalRecords());
        nsResult.setPageIndex(result.getPageIndex());
        nsResult.setPageSize(result.getPageSize());
        if (result.getRecordList() != null) {
            List<Record> nsRecordList = new ArrayList<>(result.getRecordList().getRecord().size());
            for (Record record : result.getRecordList().getRecord()) {
                nsRecordList.add(record);
            }
            nsResult.setRecordList(nsRecordList);
        } else {
            nsResult.setRecordList(Collections.emptyList());
        }
        return nsResult;
    }

    public static <RefT> NsWriteResponse<RefT> toNsWriteResponse(WriteResponse writeResponse) {
        NsWriteResponse<RefT> nsWriteResponse = new NsWriteResponse(
                toNsStatus(writeResponse.getStatus()),
                writeResponse.getBaseRef());
        return nsWriteResponse;
    }

    public static <RecT> NsReadResponse<RecT> toNsReadResponse(ReadResponse readResponse) {
        NsReadResponse<RecT> nsReadResponse = new NsReadResponse(
                toNsStatus(readResponse.getStatus()),
                readResponse.getRecord());
        return nsReadResponse;
    }

    public static <RecT> List<Record> toRecordList(List<RecT> nsRecordList) {
        List<Record> recordList = new ArrayList<>(nsRecordList.size());
        for (RecT nsRecord : nsRecordList) {
            Record r = (Record) nsRecord;
            recordList.add(r);
        }
        return recordList;
    }

    public static <RefT> List<BaseRef> toBaseRefList(List<RefT> nsRefList) {
        List<BaseRef> baseRefList = new ArrayList<>(nsRefList.size());
        for (RefT nsRef : nsRefList) {
            BaseRef r = (BaseRef) nsRef;
            baseRefList.add(r);
        }
        return baseRefList;
    }

    public static NsStatus toNsStatus(Status status) {
        if (status == null) {
            return null;
        }
        NsStatus nsStatus = new NsStatus();
        nsStatus.setSuccess(status.getIsSuccess());
        for (StatusDetail detail : status.getStatusDetail()) {
            nsStatus.getDetails().add(toNsStatusDetail(detail));
        }
        return nsStatus;
    }

    public static NsStatus.Detail toNsStatusDetail(StatusDetail detail) {
        NsStatus.Detail nsDetail = new NsStatus.Detail();
        nsDetail.setType(NsStatus.Type.valueOf(detail.getType().value()));
        nsDetail.setCode(detail.getCode().value());
        nsDetail.setMessage(detail.getMessage());
        return nsDetail;
    }
}
