<template>
  <div class="content">
    <div class="formWrap">
      <a-input-search
        v-model:value="search"
        :placeholder="t('job.action.jobSearch')"
        style="width: 300px"
        @search="handleSearch"
      />
      <a-button
        type="primary"
        style="width: 160px; margin-left: 30px"
        @click="addJob"
      >
        <template #icon> <PlusOutlined /></template
        >{{ t("job.action.createJob") }}
      </a-button>
      <a-upload
        action="/api/rest_j/v1/exchangis/job/import"
        @change="handleImport"
        :showUploadList="false"
      >
        <a-button type="primary" style="width: 160px; margin-left: 30px">
          <template #icon> <DownloadOutlined /></template
          >{{ t("job.action.import") }}
        </a-button>
      </a-upload>
    </div>
    <a-spin :spinning="spinning">
      <div class="tabWrap">
        <a-tabs v-model:activeKey="activeKey">
          <a-tab-pane key="1">
            <template #tab>
              <span>
                <ApiOutlined />
                {{ t("job.type.offline") }}
              </span>
            </template>
            <div class="cardWrap">
              <div v-if="offlineList.length === 0" class="emptyTab">
                暂无任务
              </div>
              <div v-for="item in offlineList" :key="item.id" class="card">
                <job-card
                  :jobData="item"
                  type="OFFLINE"
                  @showJobDetail="showJobDetail"
                  @handleJobCopy="handleJobCopy"
                  @refreshList="getJobs"
                />
              </div>
            </div>
          </a-tab-pane>
          <a-tab-pane key="2">
            <template #tab>
              <span>
                <NodeIndexOutlined />
                {{ t("job.type.stream") }}
              </span>
            </template>
            <div class="cardWrap">
              <div v-if="streamList.length === 0" class="emptyTab">
                暂无任务
              </div>
              <div v-for="item in streamList" :key="item.id" class="card">
                <job-card
                  :jobData="item"
                  type="STREAM"
                  @showJobDetail="showJobDetail"
                  @handleJobCopy="handleJobCopy"
                  @refreshList="getJobs"
                />
              </div>
            </div>
          </a-tab-pane>
        </a-tabs>
      </div>
    </a-spin>
    <CreateJob
      :visible="visible"
      :editData="editJobData"
      :projectId="projectId"
      @handleJobAction="handleJobAction"
    />
  </div>
</template>
<script>
import {
  DownloadOutlined,
  NodeIndexOutlined,
  ApiOutlined,
  PlusOutlined,
} from "@ant-design/icons-vue";
import { defineAsyncComponent } from "vue";
import { message } from "ant-design-vue";
import { useI18n } from "@fesjs/fes";
import { getJobs } from "@/common/service";

export default {
  components: {
    DownloadOutlined,
    NodeIndexOutlined,
    ApiOutlined,
    PlusOutlined,
    CreateJob: defineAsyncComponent(() => import("./createJob.vue")),
    JobCard: defineAsyncComponent(() => import("./job_card.vue")),
  },
  data() {
    const { t } = useI18n({ useScope: "global" });
    return {
      t,
      search: "",
      userName: "safdsaf",
      activeKey: "1",
      visible: false,
      loading: false,
      editJobData: {},
      offlineList: [],
      streamList: [],
      offlineListOrigin: [],
      streamListOrigin: [],
      projectId: this.$route.query.id,
      spinning: false,
    };
  },
  mounted() {
    this.getJobs("OFFLINE");
    this.getJobs("STREAM");
  },
  methods: {
    async getJobs(type) {
      this.spinning = true;
      const list = await getJobs(this.projectId, type);
      this.spinning = false;
      const result = (list && list.result) || [];
      if (type === "OFFLINE") {
        this.offlineList = result;
        this.offlineListOrigin = result;
      } else {
        this.streamList = result;
        this.streamListOrigin = result;
      }
      this.search = "";
      this.handleSearch();
    },
    handleSearch() {
      const search = this.search;
      if (!search) {
        this.offlineList = [...this.offlineListOrigin];
        this.streamList = [...this.streamListOrigin];
      } else {
        this.offlineList = [...this.offlineListOrigin].filter((item) =>
          item.jobName.toLowerCase().includes(search.toLowerCase())
        );
        this.streamList = [...this.streamListOrigin].filter((item) =>
          item.jobName.toLowerCase().includes(search.toLowerCase())
        );
      }
      console.log(this.search);
    },
    addJob() {
      console.log(122);
      this.visible = true;
    },
    handleJobAction(newJobData) {
      this.visible = false;
      this.editJobData = {};
      if (newJobData) {
        const newKey = newJobData.jobType === "OFFLINE" ? "1" : "2";
        if (newKey !== this.activeKey) {
          this.activeKey = newKey;
        }
        this.getJobs(newJobData.jobType);
      }
      console.log(status);
      this.$emit("changeType");
    },
    handleJobCopy(data) {
      this.visible = true;
      console.log(data);
      this.editJobData = data;
    },
    showJobDetail(data) {
      console.log(data);
      this.$emit("showJobDetail", data);
    },
    handleImport(info) {
      if (info.file.status !== "uploading") {
        console.log(info.file);
      }
      if (info.file.status === "done") {
        message.success(this.t("job.action.fileUpSuccess"));
        this.getJobs("OFFLINE");
        this.getJobs("STREAM");
      } else if (info.file.status === "error") {
        message.error(this.t("job.action.fileUpFailed"));
      }
    },
  },
  watch: {
    activeKey: {
      handler: function (newVal) {
        console.log(newVal);
      },
      deep: true,
    },
  },
};
</script>
<style scoped lang="less">
.formWrap {
  margin-top: 20px;
  padding: 0px 15px;
}
.tabWrap {
  margin-top: 10px;
  padding: 0px 15px;
}
.cardWrap {
  display: flex;
  flex-wrap: wrap;
  padding-bottom: 30px;
  .emptyTab {
    font-size: 16px;
    height: 100px;
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .card {
    margin: 10px 20px 10px 0px;
  }
}
</style>
