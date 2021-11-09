package com.webank.wedatasphere.exchangis.appconn.project;

import com.webank.wedatasphere.dss.standard.app.sso.builder.SSOUrlBuilderOperation;
import com.webank.wedatasphere.dss.standard.app.structure.StructureService;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectCreationOperation;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectRequestRef;
import com.webank.wedatasphere.dss.standard.app.structure.project.ProjectResponseRef;
import com.webank.wedatasphere.dss.standard.common.exception.operation.ExternalOperationFailedException;
import com.webank.wedatasphere.exchangis.appconn.config.ExchangisConfig;
import com.webank.wedatasphere.exchangis.appconn.model.ExchangisPostAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangisProjectCreationOperation implements ProjectCreationOperation {
    private static Logger logger = LoggerFactory.getLogger(ExchangisProjectCreationOperation.class);


    private String getAppName() {
        return ExchangisConfig.EXCHANGIS_APPCONN_NAME;
    }
    @Override
    public ProjectResponseRef createProject(ProjectRequestRef projectRequestRef) throws ExternalOperationFailedException {
        String url=ExchangisConfig.BASEURL+"";
        SSOUrlBuilderOperation ssoUrlBuilderOperation = projectRequestRef.getWorkspace().getSSOUrlBuilderOperation().copy();
        ssoUrlBuilderOperation.setAppName(getAppName());
        ssoUrlBuilderOperation.setReqUrl(url);
        ssoUrlBuilderOperation.setWorkspace(projectRequestRef.getWorkspace().getWorkspaceName());

        ExchangisPostAction exchangisPostAction = new ExchangisPostAction();
        exchangisPostAction.setUser(projectRequestRef.getCreateBy());


        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public void setStructureService(StructureService structureService) {

    }
}
