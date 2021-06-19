<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '440px' }">

      <p>
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.name" aria-placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery()">
              搜索
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()" >
              新增
            </a-button>
          </a-form-item>

        </a-form>
      </p>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="takeLevel"
          :loading="loading"
          :pagination="false"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
              title="删除后不可恢复，确认要删除文档吗？"
              ok-text="是"
              cancel-text="否"
              @confirm="handleDelete(record.id)"
            >
              <a-button type="danger" >
                删除
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
      </a-table>

    </a-layout-content>
  </a-layout>

  <a-modal
    title="文档表单"
    v-model:visible="moduleVisible"
    :confirm-loading="moduleLoading"
    @ok="handleModalOk"
  >
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name" />
      </a-form-item>
      <a-form-item label="父文档">
        <a-select
            v-model:value="doc.parent"
            ref="select"
        >
          <a-select-option value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in takeLevel" :key="c.id" :value="c.id" :disabled="doc.id === c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort" />
      </a-form-item>
    </a-form>
  </a-modal>

</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from 'axios';
import { message } from 'ant-design-vue';
import {Tool} from "@/utils/tool";

export default defineComponent({
  name:'AdminDoc',
  setup() {
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);

    const columns = [
    {
      title: '名称',
      dataIndex: 'name',
    },
    {
      title: '父文档',
      key: 'parent',
      dataIndex: 'parent',
    },
    {
      title: '顺序',
      dataIndex: 'sort',
    },
    {
      title: 'Action',
      key: 'action',
      slots: { customRender: 'action' }
    }
  ];

    /**
     * 一级文档树，children属性就是二级文档
     * [{
     *   id: "",
     *   name: "",
     *   children: [{
     *     id: "",
     *     name: "",
     *   }]
     * }]
     */
    const takeLevel = ref();

    /*
    进行#数据查询#,对文档的页面不需要进行分页功能
    * */
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all", {
        params: {
          name: param.value.name,
        }

      }).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          //使用工具类使从后端获得的文档变为树型结构
          // console.log(docs.value);
          takeLevel.value = [];
          takeLevel.value = Tool.arrayToTree(docs.value,0);
          // console.log(takeLevel.value);
        } else {
          message.error(data.message);``
        }

      });
    };



    const handleDelete = (id: number) => {
        axios.delete("/doc/delete/" + id).then((response) => {
          const data = response.data;
          if (data.success) {
            //重新加载数据列表
            handleQuery();
          }
        })
    };

    //编辑表单
    const doc = ref({});
    const moduleVisible = ref(false);
    const moduleLoading = ref(false);
    //ok键按下时触发，也即是提交表单之后的函数
    const handleModalOk = () => {
      moduleLoading.value = true;
      //使用post新增或修改后端的数据
      axios.post("/doc/save", doc.value).then((response) => {
        moduleLoading.value = false;
        const data = response.data;  //对应的是后端返回给前端的数据commonResp

        if (data.success) {
          //关闭编辑弹出的页面
          moduleVisible.value = false;

          //再重新加载已经更新到后端的数据到达前端页面
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    //编辑逻辑
    const edit = (record: any) => {
      moduleVisible.value = true;
      doc.value = Tool.copy(record); //通过一个复制的json对象来使写入的值不会直接映射到页面上
    };

    //新增函数
    const add = () => {
      moduleVisible.value = true;
      doc.value={};
    };


    //初始加载时触发
    onMounted(() => {
      handleQuery();
    });

    return{

      takeLevel,
      // docs,
      columns,
      loading,
      param,

      edit,
      add,


      doc ,
      moduleVisible,
      moduleLoading,
      handleModalOk,
      handleDelete,
      handleQuery
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
