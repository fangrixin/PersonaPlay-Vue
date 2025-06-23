<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="140px">
      <el-form-item label="第一个MBTI类型" prop="typeCode1">
        <el-input
          v-model="queryParams.typeCode1"
          placeholder="请输入第一个MBTI类型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="第二个MBTI类型" prop="typeCode2">
        <el-input
          v-model="queryParams.typeCode2"
          placeholder="请输入第二个MBTI类型"
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
          v-hasPermi="['mbti:mbtiCompatibility:add']"
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
          v-hasPermi="['mbti:mbtiCompatibility:edit']"
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
          v-hasPermi="['mbti:mbtiCompatibility:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mbti:mbtiCompatibility:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mbtiCompatibilityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="第一个MBTI类型" align="center" prop="typeCode1" />
      <el-table-column label="第二个MBTI类型" align="center" prop="typeCode2" />
      <el-table-column label="兼容性评分(0-100)" align="center" prop="compatibilityScore" />
      <el-table-column label="关系分析" align="center" prop="relationshipAnalysis" />
      <el-table-column label="优势互补" align="center" prop="advantages" />
      <el-table-column label="潜在挑战" align="center" prop="challenges" />
      <el-table-column label="相处建议" align="center" prop="recommendations" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mbti:mbtiCompatibility:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mbti:mbtiCompatibility:remove']"
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

    <!-- 添加或修改MBTI类型兼容性对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="第一个MBTI类型" prop="typeCode1">
          <el-input v-model="form.typeCode1" placeholder="请输入第一个MBTI类型" />
        </el-form-item>
        <el-form-item label="第二个MBTI类型" prop="typeCode2">
          <el-input v-model="form.typeCode2" placeholder="请输入第二个MBTI类型" />
        </el-form-item>
        <el-form-item label="兼容性评分(0-100)" prop="compatibilityScore">
          <el-input v-model="form.compatibilityScore" placeholder="请输入兼容性评分(0-100)" />
        </el-form-item>
        <el-form-item label="关系分析" prop="relationshipAnalysis">
          <el-input v-model="form.relationshipAnalysis" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="优势互补" prop="advantages">
          <el-input v-model="form.advantages" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="潜在挑战" prop="challenges">
          <el-input v-model="form.challenges" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="相处建议" prop="recommendations">
          <el-input v-model="form.recommendations" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listMbtiCompatibility, getMbtiCompatibility, delMbtiCompatibility, addMbtiCompatibility, updateMbtiCompatibility } from "@/api/mbti/mbtiCompatibility";

export default {
  name: "MbtiCompatibility",
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
      // MBTI类型兼容性表格数据
      mbtiCompatibilityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        typeCode1: null,
        typeCode2: null,
        compatibilityScore: null,
        relationshipAnalysis: null,
        advantages: null,
        challenges: null,
        recommendations: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        typeCode1: [
          { required: true, message: "第一个MBTI类型不能为空", trigger: "blur" }
        ],
        typeCode2: [
          { required: true, message: "第二个MBTI类型不能为空", trigger: "blur" }
        ],
        compatibilityScore: [
          { required: true, message: "兼容性评分(0-100)不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询MBTI类型兼容性列表 */
    getList() {
      this.loading = true;
      listMbtiCompatibility(this.queryParams).then(response => {
        this.mbtiCompatibilityList = response.rows;
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
        compatibilityId: null,
        typeCode1: null,
        typeCode2: null,
        compatibilityScore: null,
        relationshipAnalysis: null,
        advantages: null,
        challenges: null,
        recommendations: null,
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
      this.ids = selection.map(item => item.compatibilityId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加MBTI类型兼容性";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const compatibilityId = row.compatibilityId || this.ids
      getMbtiCompatibility(compatibilityId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改MBTI类型兼容性";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.compatibilityId != null) {
            updateMbtiCompatibility(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMbtiCompatibility(this.form).then(response => {
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
      const compatibilityIds = row.compatibilityId || this.ids;
      this.$modal.confirm('是否确认删除MBTI类型兼容性编号为"' + compatibilityIds + '"的数据项？').then(function() {
        return delMbtiCompatibility(compatibilityIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mbti/mbtiCompatibility/export', {
        ...this.queryParams
      }, `mbtiCompatibility_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
