package com.webank.wedatasphere.exchangis.job.server.web;

import com.webank.wedatasphere.exchangis.datasource.core.exception.ExchangisDataSourceException;
import com.webank.wedatasphere.exchangis.datasource.core.ui.ElementUI;
import com.webank.wedatasphere.exchangis.datasource.core.utils.Json;
import com.webank.wedatasphere.exchangis.job.builder.ExchangisJobBuilderContext;
import com.webank.wedatasphere.exchangis.job.builder.manager.ExchangisJobBuilderManager;
import com.webank.wedatasphere.exchangis.job.domain.ExchangisEngineJob;
import com.webank.wedatasphere.exchangis.job.domain.ExchangisJob;
import com.webank.wedatasphere.exchangis.job.domain.SubExchangisJob;
import com.webank.wedatasphere.exchangis.job.enums.EngineTypeEnum;
import com.webank.wedatasphere.exchangis.job.exception.ExchangisJobException;
import com.webank.wedatasphere.exchangis.job.exception.ExchangisJobExceptionCode;
import com.webank.wedatasphere.exchangis.job.launcher.builder.ExchangisLauncherJob;
import com.webank.wedatasphere.exchangis.job.server.builder.SpringExchangisJobBuilderManager;
import com.webank.wedatasphere.exchangis.job.server.builder.transform.ExchangisTransformJob;
import com.webank.wedatasphere.exchangis.job.server.dto.ExchangisJobBasicInfoDTO;
import com.webank.wedatasphere.exchangis.job.server.dto.ExchangisJobContentDTO;
import com.webank.wedatasphere.exchangis.job.server.exception.ExchangisJobErrorException;
import com.webank.wedatasphere.exchangis.job.server.service.ExchangisJobService;
import com.webank.wedatasphere.exchangis.job.server.service.ExchangisLaunchTaskService;
import com.webank.wedatasphere.exchangis.job.server.vo.ExchangisJobBasicInfoVO;
import com.webank.wedatasphere.exchangis.job.server.vo.ExchangisTaskSpeedLimitVO;
import com.webank.wedatasphere.linkis.server.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The type Exchangis job controller.
 *
 * @author yuxin.yuan
 * @date 2021/08/18
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("exchangis/job")
public class ExchangisJobController {

    @Autowired
    private ExchangisJobService exchangisJobService;

    @Autowired
    private ExchangisJobBuilderManager jobBuilderManager;

    @GET
    public Message getJobList(@QueryParam(value = "projectId") long projectId,
                              @QueryParam(value = "jobType") String jobType, @QueryParam(value = "name") String name) {
        List<ExchangisJobBasicInfoVO> joblist = exchangisJobService.getJobList(projectId, jobType, name);
        return Message.ok().data("result", joblist);
    }

    @GET
    @Path("/engineType")
    public Message getEngineList() {
        return Message.ok().data("result", EngineTypeEnum.values());
    }

    @POST
    public Message createJob(@Context HttpServletRequest request, @RequestBody ExchangisJobBasicInfoDTO exchangisJobBasicInfoDTO) {
        ExchangisJobBasicInfoVO job = exchangisJobService.createJob(request, exchangisJobBasicInfoDTO);
        return Message.ok().data("result", job);
    }

    @POST
    @Path("/{sourceJobId}/copy")
    public Message copyJob(@PathParam("sourceJobId") Long sourceJobId,
                           @RequestBody ExchangisJobBasicInfoDTO exchangisJobBasicInfoDTO) {
        ExchangisJobBasicInfoVO job = exchangisJobService.copyJob(exchangisJobBasicInfoDTO, sourceJobId);
        return Message.ok().data("result", job);
    }

    @PUT
    @Path("/{id}")
    public Message updateJob(@PathParam("id") Long id, @RequestBody ExchangisJobBasicInfoDTO exchangisJobBasicInfoDTO) {
        ExchangisJobBasicInfoVO job = exchangisJobService.updateJob(exchangisJobBasicInfoDTO, id);
        return Message.ok().data("result", job);
    }

    @POST
    @Path("/import")
    public Message importSingleJob(@RequestPart("multipartFile") MultipartFile multipartFile) {
        ExchangisJobBasicInfoVO job = exchangisJobService.importSingleJob(multipartFile);
        return Message.ok().data("result", job);
    }

    @DELETE
    @Path("/{id}")
    public Message deleteJob(@PathParam("id") Long id) {
        exchangisJobService.deleteJob(id);
        return Message.ok("job deleted");
    }

    @GET
    @Path("/{id}")
    public Message getJob(@Context HttpServletRequest request, @PathParam("id") Long id) throws ExchangisJobErrorException {
        ExchangisJob job = exchangisJobService.getJob(request, id);
        return Message.ok().data("result", job);
    }

    @PUT
    @Path("/{id}/config")
    public Message saveJobConfig(@PathParam("id") Long id,
                                 @RequestBody ExchangisJobContentDTO exchangisJobContentDTO) throws ExchangisJobErrorException {
        ExchangisJob exchangisJob = exchangisJobService.updateJobConfig(exchangisJobContentDTO, id);
        return Message.ok().data("result", exchangisJob);
    }

    @PUT
    @Path("/{id}/content")
    public Message saveSubjobs(@PathParam("id") Long id,
                               @RequestBody ExchangisJobContentDTO exchangisJobContentDTO) throws ExchangisJobErrorException, ExchangisDataSourceException {
        ExchangisJob exchangisJob = exchangisJobService.updateJobContent(exchangisJobContentDTO, id);
        return Message.ok().data("result", exchangisJob);
    }

//    @POST
//    @Path("/{id}/save")
//    public Message saveJobConfigAndSubjobs(@PathParam("id") Long id,
//                                           @RequestBody ExchangisJobContentDTO exchangisJobContentDTO) throws ExchangisJobErrorException {
//        ExchangisJob exchangisJob = exchangisJobService.updateJob(exchangisJobContentDTO, id);
//        return Message.ok().data("result", exchangisJob);
//    }

    @Autowired
    private ExchangisLaunchTaskService exchangisLaunchTaskService;

    @POST
    @Path("/{id}/action/execute")
    public Message executeJob(@PathParam("id") Long id) throws Exception {
        ExchangisJob job = exchangisJobService.getById(id);
        ExchangisJobBuilderContext ctx = new ExchangisJobBuilderContext();
        ctx.setOriginalJob(job);
        // ExchangisJob -> ExchangisTransformJob(SubExchangisJob)
        ExchangisTransformJob transformJob = jobBuilderManager.doBuild(job, ExchangisTransformJob.class, ctx);
        List<ExchangisEngineJob> engineJobs = new ArrayList<>();
        // ExchangisTransformJob(SubExchangisJob) -> List<ExchangisEngineJob>
        for(SubExchangisJob subExchangisJob : transformJob.getSubJobSet()){
            Optional.ofNullable(jobBuilderManager.doBuild(subExchangisJob,
                    SubExchangisJob.class, ExchangisEngineJob.class, ctx)).ifPresent(engineJobs::add);
        }
        //  List<ExchangisEngineJob> -> List<ExchangisLauncherJob>
        List<ExchangisLauncherJob> launcherJobs = new ArrayList<>();
        for(ExchangisEngineJob engineJob : engineJobs){
            Optional.ofNullable(jobBuilderManager.doBuild(engineJob,
                    ExchangisEngineJob.class, ExchangisLauncherJob.class, ctx)).ifPresent(launcherJobs::add);
        }
        if(launcherJobs.isEmpty()){
            throw new ExchangisJobException(ExchangisJobExceptionCode.JOB_BUILDER_ERROR.getCode(),
                    "The result set of launcher job is empty, please examine your job entity, [ 生成LauncherJob为空 ]", null);
        }
        launcherJobs.forEach(launcherJob ->{
            String launchName = launcherJob.getLaunchName();
            //LauncherJob launcherjob = LaunchManager.getLauncher(launchName);
            //launcherJob.launch(LauncherJob)
        });
        //TODO do launch
        return Message.ok();
    }

    @GET
    @Path("{id}/speedlimit/{task_name}/params/ui")
    public Response getSpeedLimitSettings(@PathParam("id") Long id, @PathParam("task_name") String taskName) {
        List<ElementUI> speedLimitSettings = this.exchangisJobService.getSpeedLimitSettings(id, taskName);
        Message message = Message.ok().data("ui", speedLimitSettings);
        return Message.messageToResponse(message);
    }

//    @PUT
//    @Path("{id}/speedlimit/{task_name}")
//    public Response setSpeedLimitSettings(@PathParam("id") Long id, @PathParam("task_name") String taskName, @RequestBody ExchangisTaskSpeedLimitVO settings) {
//        this.exchangisJobService.setSpeedLimitSettings(id, taskName, settings);
//        return Message.messageToResponse(Message.ok());
//    }



}
