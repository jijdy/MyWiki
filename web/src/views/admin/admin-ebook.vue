<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '440px' }">

      <a-table

          :columns="columns"
          :row-key="record => record.id"
          :data-source="ebooks"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-button type="danger">
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
    title="电子书表单"
    v-model:visible="moduleVisible"
    :confirm-loading="moduleLoading"
    @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.desc" type="textarea" />
      </a-form-item>
    </a-form>
    <p>test</p>
  </a-modal>

</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from 'axios';

export default defineComponent({
  name:'AdminEbook',
  setup() {
    const ebooks = ref();
    const pagination = ref({
      current: 1,
      pageSize:4,
      total: 0,
    });
    const loading = ref(false);

    const columns = [{
      title: '封面',
      dataIndex: 'cover',
      slots: { customRender: 'cover' }
    },
    {
      title: '名称',
      dataIndex: 'name',
    },
    {
      title: '分类一',
      key: 'category1Id',
      dataIndex: 'category1Id',
    },
    {
      title: '分类二',
      dataIndex: 'catagory2Id',
    },
    {
      title: '文档数',
      dataIndex: 'docCount',
    },
    {
      title: '阅读数',
      dataIndex: 'viewCount',
    },
    {
      title: '点赞数',
      dataIndex: 'voteCount',
    },
    {
      title: 'Action',
      key: 'action',
      slots: { customRender: 'action' }
    }
  ];
    /*
    进行数据查询,从后端调用数据库信息，一次拿出所有的数据，在前端进行分页
    通过get传参数到后端，改变参数params中的数据属性,只传递分页需要的属性
    * */
    const handleQuery = (params: any) => {
      loading.value = true;
      axios.get("/ebook/list", {
        params: {
          page: params.page,
          size: params.size,
        }

      }).then((response) => {
        loading.value = false;
        const data = response.data;
        ebooks.value = data.content.list;

      //  重置分页按钮，在点击换页之后将当前页码转换并转换显示内容
        pagination.value.current = params.page;
        pagination.value.total = data.content.total;
      });
    };

    /*
    * 表格点击页码时出现
    * */
    const handleTableChange = (pagination: any) => {
      console.log("看看自带的分页参数为：" + pagination);
      handleQuery({
        page: pagination.current,
        size: pagination.pagination,
      });
    };

    //表单
    const ebook = ref({});
    const moduleVisible = ref(false);
    const moduleLoading = ref(false);
    const handleModalOk = () => {
      moduleLoading.value = true;
      setTimeout(() => {
        moduleVisible.value = false;
        moduleLoading.value = false;
      }, 1000);
    };

    //编辑逻辑
    const edit = (record: any) => {
      moduleVisible.value = true;
      ebook.value = record; //将编辑页的内容返回到ebook的值中
    };


    //初始加载时触发
    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      });
    });

    return{
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      edit,
      ebook,
      moduleVisible,
      moduleLoading,
      handleModalOk,
    }
  }
});

</script>
<style scoped>
  img {
    width: 50px;
    height: 50px;
  ;
  }
</style>
