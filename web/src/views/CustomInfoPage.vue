<template>
  <div class="custom-info-page">
    <div class="container">
      <div class="table-layout">
        <!-- 表格上方工具行 -->
        <div class="table-navigation-bar">
          <!-- 左边的搜索界面 -->
          <div class="table-navigation-bar-left">
            <a-input-search
              v-model:value="params.keyword"
              placeholder="关键字"
              enter-button
              @search="searchReloadData"
              style="width: 250px"
            ></a-input-search>
          </div>
          <!-- 右边的工具栏 -->
          <div class="table-navigation-bar-right">
            <!-- 多选操作 -->
            <template v-if="multiple">
              <a-button
                type="link"
                @click="() => deleteByIds(rowSelection.selectedRowKeys)"
                :disabled="!hasCheckItems"
                :loading="requesting.delete"
                danger
                ><DeleteOutlined />删除</a-button
              >
            </template>
            <!-- 控制是否多选 -->
            <a-switch
              v-model:checked="multiple"
              checked-children="批量"
              un-checked-children="正常"
              @change="changeMultiple"
            ></a-switch>
            <!-- 表格公共导出组件 -->
            <TableExport
              :columns="columns"
              :data="data"
              :params="params"
              :service="service"
            />
            <!-- 控制表格字段显示隐藏组件 -->
            <TableFieldController v-model="columns"></TableFieldController>
            <!-- 显示新增弹窗 -->
            <a-button type="link" @click="openAdd"
              ><PlusCircleOutlined />新增</a-button
            >
          </div>
        </div>
        <div class="table-context">
          <a-spin :spinning="requesting.list">
            <a-table
              :data-source="data"
              :columns="mergeColumns"
              rowKey="id"
              bordered
              :pagination="pagination"
              @change="tabelChange"
              :scroll="tableScroll"
              :rowSelection="multiple ? rowSelection : null"
              resizable
              @resizeColumn="(w, col) => (col.width = w)"
            >
              <template #headerCell="{ title, column }">
                <span style="color: #303133">
                  {{ title }}
                  <template v-if="column.help && column.help !== ''">
                    <!-- 表格帮助组件 -->
                    <TableHelp :help="column.help" :title="column.title" />
                  </template>
                </span>
              </template>
              <template #bodyCell="{ column, record }">
                <!-- 状态转换成开关形式 -->
                <template v-if="column.key === 'state'">
                  <a-switch
                    :checked="record.state"
                    checked-children="启用"
                    un-checked-children="禁用"
                    :checkedValue="0"
                    :unCheckedValue="1"
                    :loading="requesting.state.has(record.id)"
                    @change="(checked) => changeState(checked, record.id)"
                  ></a-switch>
                </template>
                <!-- 格式化日期时间 -->
                <template v-if="column.key === 'createTime'">
                  <span>{{ column.format(record.createTime, column) }}</span>
                </template>
                <!-- 格式化日期时间 -->
                <template v-if="column.key === 'updateTime'">
                  <span>{{ column.format(record.updateTime, column) }}</span>
                </template>
                <!-- 行操作列 -->
                <template v-if="column.key === 'operation'">
                  <div
                    style="
                      display: flex;
                      justify-content: space-between;
                      flex-wrap: wrap;
                      max-width: 190px;
                    "
                  >
                    <!-- 显示更新弹窗 -->
                    <a-button
                      type="link"
                      size="small"
                      @click="() => openUpdate(record.id)"
                      ><EditOutlined />更新</a-button
                    >
                    <!-- 显示删除弹窗 -->
                    <a-button
                      type="link"
                      danger
                      size="small"
                      @click="() => deleteByIds([record.id])"
                      ><DeleteOutlined />删除</a-button
                    >
                    <a-button
                      type="link"
                      size="small"
                      @click="() => gotoPortMapping(record.id)"
                      >映射列表</a-button
                    >
                  </div>
                </template>
              </template>
            </a-table>
          </a-spin>
        </div>
      </div>
    </div>
    <!-- 新增弹窗 -->
    <a-modal
      v-model:open="modalVisible.add"
      title="添加客户端信息"
      :footer="null"
      centered
      :maskClosable="false"
    >
      <AddCustomInfo @success="resetAndLast"></AddCustomInfo>
    </a-modal>
    <!-- 修改弹窗 -->
    <a-modal
      v-model:open="modalVisible.update"
      title="修改客户端信息"
      :footer="null"
      centered
      :maskClosable="false"
    >
      <UpdateCustomInfo
        :existId="checkId"
        @success="reloadChecked"
      ></UpdateCustomInfo>
    </a-modal>
  </div>
</template>
<script lang="ts">
/**
 * 客户信息界面
 */
import { defineComponent, ref, computed, onMounted, createVNode } from "vue";
import { Column } from "@/types/Column";
import { format as dateTimeFormat } from "@/util/DateUtil";
import {
  customInfoList as listRequest,
  customInfoDelete as deleteRequest,
  customInfoInfo as infoRequest,
  customInfoState as stateRequest,
} from "@/api/customInfoApi";
import { AddCustomInfo, UpdateCustomInfo } from "@/components/view/customInfo";
import {
  TableExport,
  TableFieldController,
  TableHelp,
} from "@/components/ExportFile";
import { idNotExist } from "@/util/HttpStatic";
import {
  listMessageTip,
  infoMessageTip,
  deleteMessageTip,
  updateStateMessageTip,
} from "@/util/TipUtil";
import {
  defaultTableScroll,
  defaultPagination,
  transform,
} from "@/util/TableUtil";
import { ConvertorType } from "@/util/ExportFile";
import modal from "@/components/ui/Modal";
import {
  WarningOutlined,
  PlusCircleOutlined,
  DeleteOutlined,
  EditOutlined,
} from "@ant-design/icons-vue";
import {
  ListCustomInfoResponse,
  InfoCustomInfoResponse,
} from "@/types/apiEntity/CustomInfo";
import { CommonResult, CommonPage } from "@/types/apiEntity/ApiResponse";
import router from "@/router";

/**
 * 默认的请求参数
 */
const defaultParams = () => {
  return {
    keyword: "",
  };
};
export default defineComponent({
  components: {
    AddCustomInfo,
    UpdateCustomInfo,
    TableExport,
    TableFieldController,
    TableHelp,
    PlusCircleOutlined,
    DeleteOutlined,
    EditOutlined,
  },
  name: "CustomInfo",
  setup() {
    //多选配置
    const rowSelection = ref({
      selectedRowKeys: [],
      onChange: (selectedRowKeys) => {
        rowSelection.value.selectedRowKeys = selectedRowKeys;
      },
    });
    //请求参数
    const params = ref(defaultParams());
    //字段
    const columns = ref<Column[]>([
      {
        title: "主键",
        key: "id",
        dataIndex: "id",
        align: "center",
        visible: false,
        resizable: true,
        show: true,
        width: 80,
        _sort: 10,
      },
      {
        title: "用户名称",
        key: "username",
        dataIndex: "username",
        align: "center",
        width: 200,
        visible: true,
        resizable: true,
        show: true,
        _sort: 20,
      },
      {
        title: "密钥",
        key: "secretKey",
        help: "用于识别客户端身份,密钥需要保持全表唯一,因为要根据密钥查找客户",
        dataIndex: "secretKey",
        align: "center",
        width: 400,
        visible: true,
        resizable: true,
        show: true,
        _sort: 40,
      },
      {
        title: "状态",
        key: "state",
        help: "启用或禁用该用户",
        dataIndex: "state",
        align: "center",
        width: 150,
        visible: true,
        resizable: true,
        show: true,
        _sort: 70,
        format(text, column: Column): unknown {
          let convertor = column.convertor;
          if (convertor && convertor.valueMap) {
            let val = convertor.valueMap[text as string | number];
            return val ? val : convertor.valueMap["default"];
          } else {
            return null;
          }
        },
        convertor: {
          type: ConvertorType.MAP,
          valueMap: {
            0: "启用",
            1: "禁用",
            default: "未知",
          },
        },
      },
      {
        title: "备注",
        key: "remark",
        dataIndex: "remark",
        align: "center",
        width: 300,
        visible: true,
        resizable: true,
        show: true,
        _sort: 90,
      },
      {
        title: "创建时间",
        key: "createTime",
        dataIndex: "createTime",
        width: 160,
        resizable: true,
        align: "center",
        visible: true,
        show: true,
        _sort: 100,
        format(text) {
          return dateTimeFormat(text as Date);
        },
        convertor: {
          type: ConvertorType.DATETIME,
        },
      },
      {
        title: "修改时间",
        key: "updateTime",
        dataIndex: "updateTime",
        width: 160,
        resizable: true,
        align: "center",
        visible: true,
        show: true,
        _sort: 110,
        format(text) {
          return dateTimeFormat(text as Date);
        },
        convertor: {
          type: ConvertorType.DATETIME,
        },
      },
      {
        title: "操作",
        align: "center",
        key: "operation",
        fixed: "right",
        visible: true,
        show: true,
        skip: true,
      },
    ]);
    //数据
    const data = ref<ListCustomInfoResponse[]>([]);
    //分页
    const pagination = ref(defaultPagination());
    //表格滚动配置
    const tableScroll = ref(defaultTableScroll());
    //基础参数
    const basicValue = ref({});
    //选择的id
    const checkId = ref(idNotExist);
    //弹窗显隐控制
    const modalVisible = ref({ add: false, update: false });
    //多选控制
    const multiple = ref(false);
    //计算属性:多选时是否有数据选择
    const hasCheckItems = computed(
      () => rowSelection.value.selectedRowKeys.length > 0
    );
    //请求控制
    const requesting = ref({
      list: false,
      delete: false,
      info: false,
      state: new Set(),
    });
    //计算属性:字段
    const mergeColumns = computed(() => {
      return columns.value.filter(
        //当字段是visible并且show时展示(多选时,行工具栏隐藏)
        (item) =>
          item.visible &&
          item.show &&
          (item.key === "operation" ? !multiple.value : true)
      );
    });
    //重置参数
    const reset = () => {
      params.value = defaultParams();
    };
    //列表查询
    const reloadData = () => {
      //无锁执行
      listMessageTip(requesting.value.list, () => {
        //加锁
        requesting.value.list = true;
        //查询list接口
        listRequest(transform(pagination.value, params.value))
          .then((res: CommonResult<CommonPage<ListCustomInfoResponse>>) => {
            //将接口数据赋值到data引用
            if (res.data && res.data.list) {
              data.value = res.data.list as ListCustomInfoResponse[];
            }
            //调用公共处理的方法
            if (res.successMethod) {
              res.successMethod();
            }
          })
          .catch((res: CommonResult<unknown>) => {
            //调用公共的catch方法
            if (res.catch) {
              res.catch();
            }
          })
          .finally(() => {
            //解锁
            requesting.value.list = false;
          });
      });
    };
    //重置查询
    const searchReloadData = () => {
      //重新赋值查询
      pagination.value = defaultPagination();
      reloadData();
    };
    //显示新增弹窗
    const openAdd = () => {
      modalVisible.value.add = true;
    };
    //显示修改弹窗
    const openUpdate = (id) => {
      //赋值选择的id
      checkId.value = id;
      modalVisible.value.update = true;
    };
    //显示删除确认窗
    const deleteByIds = (ids: number[]) => {
      modal.confirm({
        title: "删除该客户会将关联数据也一并删除,确定要删除吗?",
        icon: createVNode(WarningOutlined, {
          style: { color: "#ff0000" },
        }),
        //点击确认回调
        onOk() {
          //无锁执行
          deleteMessageTip(requesting.value.delete, () => {
            //加锁
            requesting.value.delete = true;
            //删除请求
            deleteRequest({ ids })
              .then((res: CommonResult<unknown>) => {
                //刷新界面 如果删除的条数大于等于当前数据的条数则分页减1(如果是1则不动),否则不动
                if (ids.length >= data.value.length) {
                  pagination.value.current =
                    pagination.value.current - 1 > 0
                      ? pagination.value.current - 1
                      : 1;
                }
                //清空数组所有的值
                ids.splice(0);
                //成功公共回调
                if (res.successMethod) {
                  res.successMethod();
                }
                //重载数据
                reloadData();
              })
              .catch((res: CommonResult<unknown>) => {
                //公共catch
                if (res.catch) {
                  res.catch();
                }
              })
              .finally(() => {
                //解锁
                requesting.value.delete = false;
              });
          });
        },
        okText: "确认",
        cancelText: "我在想想",
      });
    };
    //重置并请求最后一页
    const resetAndLast = () => {
      //隐藏新增弹窗
      modalVisible.value.add = false;
      //重置参数
      reset();
      //计算分页
      pagination.value.current =
        Math.floor(pagination.value.total / pagination.value.pageSize) + 1;
    };
    //更新选择id对应的数据
    const updateItem = () => {
      //无锁执行
      infoMessageTip(requesting.value.info, () => {
        //加锁
        requesting.value.info = true;
        //请求详情接口
        infoRequest({ id: checkId.value })
          .then((res: CommonResult<InfoCustomInfoResponse>) => {
            if (res.data) {
              const id = res.data.id;
              //查找选择id对应的索引下标
              const index = data.value.findIndex(
                (item: ListCustomInfoResponse) => item.id === id
              );
              //替换对应的对象
              if (index !== -1) {
                data.value[index] = { ...res.data } as ListCustomInfoResponse;
              }
            }
          })
          .catch((res: CommonResult<unknown>) => {
            //公共catch
            if (res.catch) {
              res.catch();
            }
          })
          .finally(() => {
            //解锁
            requesting.value.info = false;
          });
      });
    };
    //重载选择的数据
    const reloadChecked = () => {
      //隐藏修改弹窗
      modalVisible.value.update = false;
      //更新对应的数据
      updateItem();
    };
    //状态切换监听
    const changeState = (checked, id) => {
      //无锁执行
      updateStateMessageTip(requesting.value.state.has(id), () => {
        //加锁
        requesting.value.state.add(id);
        //调用状态请求
        stateRequest({ id: id, state: checked })
          .then((res: CommonResult<unknown>) => {
            //更新选中的id对应的状态
            data.value.forEach((item) => {
              if (item.id === id) {
                item.state = checked;
              }
            });
            //成功公共回调
            if (res.successMethod) {
              res.successMethod();
            }
          })
          .catch((res: CommonResult<unknown>) => {
            //公共catch
            if (res.catch) {
              res.catch();
            }
          })
          .finally(() => {
            //解锁
            requesting.value.state.delete(id);
          });
      });
    };
    //多选切换监听
    const changeMultiple = () => {
      if (multiple.value) {
        //清空已选择的数据项
        rowSelection.value.selectedRowKeys = [];
      }
    };
    const tabelChange = (p) => {
      pagination.value = { ...p };
      reloadData();
    };
    const gotoPortMapping = (userId) => {
      router.push({ name: "PortMapping", query: { userId } });
    };
    //挂载成功回调
    onMounted(() => {
      //加载列表数据
      searchReloadData();
    });
    //返回值
    return {
      params,
      multiple,
      hasCheckItems,
      columns,
      data,
      basicValue,
      checkId,
      pagination,
      mergeColumns,
      modalVisible,
      requesting,
      searchReloadData,
      openUpdate,
      openAdd,
      deleteByIds,
      reloadData,
      resetAndLast,
      updateItem,
      service: listRequest,
      tableScroll,
      reloadChecked,
      changeState,
      changeMultiple,
      rowSelection,
      tabelChange,
      gotoPortMapping,
    };
  },
});
</script>
