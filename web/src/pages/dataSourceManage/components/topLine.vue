<template>
  <div class="top-line">
    <span>
      <a-select v-model:value="seartParams.typeId" @change="changeType" style="width: 140px" :allowClear="true" :placeholder="$t('dataSource.topLine.searchBar.dataTypePlaceholder')">
        <a-select-option v-for="item of sourceTypeList" :value="Number(item.id)" :key="item.id">{{ item.name }}</a-select-option>
      </a-select>
      <a-input v-model:value="seartParams.name" style="width: 220px" :placeholder="$t('dataSource.topLine.searchBar.namePlaceholder')" />
      <a-button :loading="loading" type="primary" @click="$emit('search', seartParams)">
        <template v-slot:icon> <icon-searchOutlined /></template>
        {{ $t("dataSource.topLine.searchBar.searchButtonText") }}
      </a-button>
    </span>
    <a-space>
      <a-button :loading="loading" type="primary" @click="$emit('create')">
        <template v-slot:icon> <icon-plusOutlined /></template>
        {{ $t("dataSource.topLine.createDataSourceButton") }}
      </a-button>
    </a-space>
  </div>
</template>

<script>
import { PlusOutlined, ExportOutlined, ImportOutlined, SearchOutlined } from "@ant-design/icons-vue";
import { getDataSourceTypes } from "@/common/service";

export default {
  name: "topLine",
  components: {
    iconPlusOutlined: PlusOutlined,
    iconExportOutlined: ExportOutlined,
    iconImportOutlined: ImportOutlined,
    iconSearchOutlined: SearchOutlined,
  },
  props: ["sourceTypeList", "loading"],
  emits: ["search", "export", "import", "create"],
  data() {
    return {
      seartParams: {
        typeId: undefined,
        name: "",
      },
    };
  },
};
</script>

<style scoped lang="less"></style>
