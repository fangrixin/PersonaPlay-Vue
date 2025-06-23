<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="版本名称" prop="versionName">
        <el-input
          v-model="queryParams.versionName"
          placeholder="请输入版本名称(简短版/标准版/完整版)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['mbti:mbtiTestVersion:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mbti:mbtiTestVersion:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mbti:mbtiTestVersion:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mbti:mbtiTestVersion:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mbtiTestVersionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="版本代码(short/standard/full)" align="center" prop="versionCode" />
      <el-table-column label="版本名称(简短版/标准版/完整版)" align="center" prop="versionName" />
      <el-table-column label="题目数量" align="center" prop="questionCount" />
      <el-table-column label="时间限制(秒)" align="center" prop="timeLimit" />
      <el-table-column label="版本描述" align="center" prop="description" />
      <el-table-column label="状态(0启用 1禁用)" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mbti:mbtiTestVersion:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mbti:mbtiTestVersion:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改MBTI测试版本对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="版本代码(short/standard/full)" prop="versionCode">
          <el-input v-model="form.versionCode" placeholder="请输入版本代码(short/standard/full)" />
        </el-form-item>
        <el-form-item label="版本名称(简短版/标准版/完整版)" prop="versionName">
          <el-input v-model="form.versionName" placeholder="请输入版本名称(简短版/标准版/完整版)" />
        </el-form-item>
        <el-form-item label="题目数量" prop="questionCount">
          <el-input v-model="form.questionCount" placeholder="请输入题目数量" />
        </el-form-item>
        <el-form-item label="时间限制(秒)" prop="timeLimit">
          <el-input v-model="form.timeLimit" placeholder="请输入时间限制(秒)" />
        </el-form-item>
        <el-form-item label="版本描述" prop="description">
          <el-input v-model="form.description" placeholder="请输入版本描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMbtiTestVersion, getMbtiTestVersion, delMbtiTestVersion, addMbtiTestVersion, updateMbtiTestVersion } from "@/api/mbti/mbtiTestVersion";

export default {
  name: "MbtiTestVersion",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // MBTI测试版本表格数据
      mbtiTestVersionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        versionCode: null,
        versionName: null,
        questionCount: null,
        timeLimit: null,
        description: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        versionCode: [
          { required: true, message: "版本代码(short/standard/full)不能为空", trigger: "blur" }
        ],
        versionName: [
          { required: true, message: "版本名称(简短版/标准版/完整版)不能为空", trigger: "blur" }
        ],
        questionCount: [
          { required: true, message: "题目数量不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询MBTI测试版本列表 */
    getList() {
      this.loading = true;
      listMbtiTestVersion(this.queryParams).then(response => {
        this.mbtiTestVersionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        versionId: null,
        versionCode: null,
        versionName: null,
        questionCount: null,
        timeLimit: null,
        description: null,
        status: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.versionId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加MBTI测试版本";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const versionId = row.versionId || this.ids
      getMbtiTestVersion(versionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改MBTI测试版本";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.versionId != null) {
            updateMbtiTestVersion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMbtiTestVersion(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const versionIds = row.versionId || this.ids;
      this.$modal.confirm('是否确认删除MBTI测试版本编号为"' + versionIds + '"的数据项？').then(function() {
        return delMbtiTestVersion(versionIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mbti/mbtiTestVersion/export', {
        ...this.queryParams
      }, `mbtiTestVersion_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
