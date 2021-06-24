<template>
  <a-layout>
    <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '440px' }">

      <a-row :gutter="24">
        <a-col :span="8">
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
              v-if="takeLevel.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="takeLevel"
              :loading="loading"
              :pagination="false"
              size="small"
              :defaultExpandAllRows="true"
          >
<!--            default--- 默认展开所有的页面分支，将文档树中的所有分支全部展示出来-->
            <template #name="{ text, record }">
              {{record.sort}} {{text}}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认要删除文档吗？"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>

              </a-space>
            </template>
          </a-table>
        </a-col>

        <a-col :span="16">
          <p>
            <a-form layout="inline" :model="param">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" layout="vertical">
            <a-form-item >
              <a-input v-model:value="doc.name" placeholder="名称"/>
            </a-form-item>
            <a-form-item >
              <a-tree-select
                  v-model:value="doc.parent"
                  style="width: 100%"
                  :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                  :tree-data="treeSelectData"
                  placeholder="请选择父文档"
                  tree-default-expand-all
                  :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              >
              </a-tree-select>
            </a-form-item>
            <a-form-item >
              <a-input v-model:value="doc.sort" placeholder="顺序"/>
            </a-form-item>
            <a-form-item >
              <a-button type="primary" @click="handlePreviewContent()">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>

      </a-row>

      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>


    </a-layout-content>
  </a-layout>

<!--  <a-modal-->
<!--    title="文档表单"-->
<!--    v-model:visible="moduleVisible"-->
<!--    :confirm-loading="moduleLoading"-->
<!--    @ok="handleModalOk"-->
<!--  >-->
<!--  </a-modal>-->

</template>

<script lang="ts">
import {createVNode, defineComponent, onMounted, ref} from "vue";
import axios from 'axios';
import {message, Modal} from 'ant-design-vue';
import {Tool} from "@/utils/tool";
import {useRoute} from "vue-router";
import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
import E from "wangeditor";

export default defineComponent({
  name:'AdminDoc',
  setup() {
    //使用路由类来获取到跳转页面时传入的ebookId参数
    const route = useRoute();
    // console.log("路由：", route);
    // console.log("route.path：", route.path);
    // console.log("route.query：", route.query.ebookId);
    // console.log("route.param：", route.params);
    // console.log("route.fullPath：", route.fullPath);
    // console.log("route.name：", route.name);
    // console.log("route.meta：", route.meta);
    const param = ref();
    param.value = {};
    const docs = ref();
    const loading = ref(false);
    const treeSelectDate = ref();
    treeSelectDate.value = [];

    const columns = [
    {
      title: '名称',
      dataIndex: 'name',
      slots: {customRender: 'name'},
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
    takeLevel.value = [];


    /*
    进行#数据查询#,对文档的页面不需要进行分页功能
    将文档的数据转换为树形结构
    * */
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        if (data.success) {
          docs.value = data.content;
          //使用工具类使从后端获得的文档变为树型结构
          // console.log(docs.value);
          takeLevel.value = [];
          takeLevel.value = Tool.arrayToTree(docs.value,0);
          // console.log(takeLevel.value);

          // 父文档下拉框初始化，相当于点击新增
          treeSelectData.value = Tool.copy(takeLevel.value) || [];
          // 为选择树添加一个"无"
          treeSelectData.value.unshift({id: 0, name: '无'});
        } else {
          message.error(data.message);``
        }

      });
    };



    const handleDelete = (id: number) => {
      getDelete(takeLevel.value, id);
      Modal.confirm({
        title: '重要提醒',
        icon: createVNode(ExclamationCircleOutlined),
        content: '将删除：【' + deleteName.join("，") + "】删除后不可恢复，确认删除？",
        onOk() {
          axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
            const data = response.data;
            if (data.success) {
              //重新加载数据列表
              handleQuery();
            }
          });
        },
      });
    };

    //编辑表单
    //因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
    const treeSelectData = ref();
    treeSelectDate.value = [];

    const doc = ref();
    doc.value = {ebookId: route.query.ebookId};

    const moduleVisible = ref(false);
    const moduleLoading = ref(false);

    //富文本框初始化
    const editor = new E("#content");
    editor.config.zIndex = 0;


    //ok键按下时触发，也即是提交表单之后的函数
    const handleSave = () => {
      moduleLoading.value = true;
      doc.value.content = editor.txt.html();
      axios.post("/doc/save", doc.value).then((response) => {
        moduleLoading.value = false;
        const data = response.data;  //对应的是后端返回给前端的数据commonResp

        if (data.success) {
          // moduleVisible.value = false;
          message.success("保存成功！")

          //再重新加载已经更新到后端的数据到达前端页面
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };

    //将末节点及其子节点全部置为disabled
    const setDisable =(treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };

    const ids : Array<string> = [];
    const deleteName : Array<string> = [];
    //*
    //查找整根树枝
    //*/
    const getDelete =(treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      ids == null ;
      deleteName.length == 0;
      // console.log("ids的数值" + ids.toString());
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          //将要删除的子节点的id放到ids结果数组中
          ids.push(id);
          deleteName.push(node.name);

          //遍历所有子节点中的id并将其放入到ids中
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let i = 0; i < children.length; i ++) {
              getDelete(children,children[i].id)
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDelete(children, id);
          }
        }
      }
    };

    //从后台获取到文档内容
    const handleQueryContent = () => {
      axios.get("/doc/get-content/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          editor.txt.html(data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    //编辑逻辑
    const edit = (record: any) => {
      editor.txt.html("");
      moduleVisible.value = true;
      doc.value = Tool.copy(record); //通过一个复制的json对象来使写入的值不会直接映射到页面上
      handleQueryContent();

      //不能选择自己节点的子节点为父节点，会使递归失效
      treeSelectData.value = Tool.copy(takeLevel.value);
      setDisable(treeSelectData.value,record.id);

      //添加一个无作为父节点
      treeSelectData.value.unshift({id:0, name: '无'});
    };

    //新增函数
    const add = () => {
      editor.txt.html("");
      moduleVisible.value = true;
      doc.value={
        ebookId: route.query.ebookId,
      };

      treeSelectData.value = Tool.copy(takeLevel.value) || [];
      //添加一个无作为父节点
      treeSelectData.value.unshift({id:0, name: '无'});

    };

    // ----------------富文本预览--------------
    const drawerVisible = ref(false);
    const previewHtml = ref();
    const handlePreviewContent = () => {
      previewHtml.value = editor.txt.html();
      drawerVisible.value = true;
    };
    const onDrawerClose = () => {
      drawerVisible.value = false;
    };

    //初始加载时触发
    onMounted(() => {
      handleQuery();
      // const editor = new E('#content');
      // editor.config.zIndex = 0;
      editor.create();
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
      handleSave,
      handleDelete,
      handleQuery,

      treeSelectData,

      drawerVisible,
      previewHtml,
      handlePreviewContent,
      onDrawerClose,
    }
  }
});

<img src="../../../../../../../AppData/Local/Temp/5994168_1502781028253_921DFCDC1FAA7527BB554C8F6AE77DD2.png"
     height="92" width="553"/></script>
<style scoped>
  img {
    width: 50px;
    height: 50px;
  ;
  }
</style>
