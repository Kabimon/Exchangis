// 服务端接口管理
import { request } from "@fesjs/fes";
////////////////////////////////////////////////////////////////////
export const getProjectList = (name) => {
  return request("/projects", { name }, { method: "POST" });
};

export const createProject = (body) => {
  return request("/createProject", body);
};

export const deleteProject = (id) => {
  return request("/projects/" + id, null, {
    method: "DELETE",
  });
};

export const getProjectById = (id) => {
  return request("/projects/" + id, null, {
    method: "GET",
  });
};

export const updateProject = (body) => {
  return request("/updateProject", body, {
    method: "PUT",
  });
};

export const getDataSourceList = (params) => {
  return request("/datasources/query", { ...params }, { method: "POST" });
};

export const getDataSourceTypes = () => {
  return request(
    `/datasources/type?t=_${new Date().getTime()}`,
    {},
    { method: "GET" }
  );
};

// 查询数据源
export const getDataSource = (body) => {
  return request("/datasources/query", body, { method: "POST" });
};

export const getDBs = (type, id) => {
  return request(`/datasources/${type}/${id}/dbs`, {}, { method: "GET" });
};

export const getTables = (type, id, dbName) => {
  return request(
    `/datasources/${type}/${id}/dbs/${dbName}/tables`,
    {},
    { method: "GET" }
  );
};

/*export const getFields = (type, id, dbName, tableName) => {
  return request(
    `/datasources/${type}/${id}/dbs/${dbName}/tables/${tableName}/fields`,
    {},
    { method: "GET" }
  );
};*/

export const getFields = (params) => {
  return request(
    `/datasources/fieldsmapping`,
    { ...params },
    { method: "POST" }
  );
};

export const createDataSource = (params) => {
  return request("/datasources", { ...params }, { method: "POST" });
};

export const updateDataSource = (id, params) => {
  return request("/datasources/" + id, { ...params }, { method: "PUT" });
};

export const deleteDataSource = (id) => {
  return request(`/datasources/${id}`, {}, { method: "DELETE" });
};

export const getDataSourceVersionList = (id) => {
  return request(`/datasources/${id}/versions`, {}, { method: "GET" });
};

export const testDataSourceConnect = (type, id) => {
  return request(`/datasources/${type}/${id}/connect`, {}, { method: "PUT" });
};

export const getDataSourceById = (id) => {
  return request(`/datasources/${id}`, {}, { method: "GET" });
};

export const getJobInfo = (id) => {
  return request(`/job/${id}`, null, {
    method: "GET",
  });
};

//获取任务列表
export const getJobList = (query) => {
  return request(`/job?${query}`, null, {
    method: "GET",
  });
};

//获取执行引擎列表
export const getEngineType = () => {
  return request(`/job/engineType`, null, {
    method: "GET",
  });
};

//新建任务
export const createJob = (params) => {
  return request(
    `/job`,
    { ...params },
    {
      method: "POST",
    }
  );
};

//复制任务
export const copyJob = (id, params) => {
  return request(
    `/job/${id}/copy`,
    { ...params },
    {
      method: "POST",
    }
  );
};

//删除任务
export const deleteJob = (id) => {
  return request(`/job/${id}`, null, {
    method: "DELETE",
  });
};

//导入任务
export const importJob = (id, params) => {
  return request(
    `/job/import`,
    { ...params },
    {
      method: "POST",
    }
  );
};

//执行任务
export const executeTask = (id) => {
  return request(`/job/${id}`, null, {
    method: "POST",
  });
};

export const getJobs = (id, jobType) => {
  return request(`/job?projectId=${id}&jobType=${jobType}`, null, {
    method: "GET",
  });
};

export const saveProject = (id, body) => {
  return request(`/job/${id}/content`, body, {
    method: "PUT",
  });
};

// 保存/更新任务配置
export const updateTaskConfiguration = (id, body) => {
  return request(`/job/${id}/config`, body, {
    method: "PUT",
  });
};

export const expireDataSource = (id) => {
  return request(`/datasources/${id}/expire`, {}, { method: "PUT" });
};

export const publishDataSource = (id, versionId) => {
  return request(
    `/datasources/${id}/${versionId}/publish`,
    {},
    { method: "PUT" }
  );
};

export const getSourceParams = (engineType, type, ds) => {
  return request(
    `/datasources/${engineType}/${type}/params/ui?dir=${ds}`,
    {},
    { method: "GET" }
  );
};

export const getSettingsParams = (engineType) => {
  return request(
    `/jobs/engine/${engineType}/settings/ui`,
    {},
    { method: "GET" }
  );
};

// 同步历史
export const getSyncHistory = (body) => {
  return request("/tasks", body, {
    method: "GET",
  });
};
// 删除同步历史
export const delSyncHistory = (taskId) => {
  return request(`/tasks/${taskId}`, null, {
    method: "DELETE",
  });
};
// 读取Task限速配置
export const getSpeedLimit = (params) => {
  return request(
    `/job/${params.jobId}/speedlimit/${params.taskName}/params/ui`,
    {},
    {
      method: "GET",
    }
  );
};
// 保存Task限速配置
export const saveSpeedLimit = (params, body) => {
  return request(`/job/${params.jobId}/speedlimit/${params.taskName}`, body, {
    method: "PUT",
  });
};

// 首页相关

// 任务状态
export const getTaskState = () => {
  return request("/metrics/taskstate", {}, { method: "GET" });
};

// 任务进度
export const getTaskProcess = () => {
  return request("/metrics/taskprocess", {}, { method: "GET" });
};

// 流量监控
export const getDataSourceFlow = () => {
  return request("/metrics/datasourceflow", {}, { method: "GET" });
};

// 资源使用
export const getEngineriesSource = () => {
  return request("/metrics/engineresource", {}, { method: "GET" });
};

export const getEngineriesSourceCpu = () => {
  return request("/metrics/engineresourcecpu", {}, { method: "GET" });
};

export const getEngineriesSourceMem = () => {
  return request("/metrics/engineresourcemem", {}, { method: "GET" });
};
