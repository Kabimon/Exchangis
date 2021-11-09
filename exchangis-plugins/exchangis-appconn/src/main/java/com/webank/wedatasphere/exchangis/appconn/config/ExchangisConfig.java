package com.webank.wedatasphere.exchangis.appconn.config;

import com.webank.wedatasphere.linkis.common.conf.CommonVars;
import com.webank.wedatasphere.linkis.server.conf.ServerConfiguration;

public class ExchangisConfig {

    public final static String BASEURL = "/api/rest_s/" + ServerConfiguration.BDP_SERVER_VERSION() + "/exchangis";
    public final static String EXCHANGIS_APPCONN_NAME = CommonVars.apply("wds.dss.appconn.exchangis.name", "Exchangis").getValue();

    public final static String ID="id";
    public final static String WORKSPACE_NAME="workspaceName";
    public final static String PROJECT_NAME="projectName";
    public final static String DESCRIPTION="description";
    public final static String TAGS="tags";
    public final static String EDIT_USERS="editUsers";
    public final static String VIEW_USERS="viewUsers";
    public final static String EXEC_USERS="execUsers";


}
