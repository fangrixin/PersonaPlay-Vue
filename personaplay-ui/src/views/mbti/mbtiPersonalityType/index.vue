<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="MBTI类型" prop="typeCode">
        <el-input
          v-model="queryParams.typeCode"
          placeholder="请输入MBTI类型"
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
          v-hasPermi="['mbti:mbtiPersonalityType:add']"
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
          v-hasPermi="['mbti:mbtiPersonalityType:edit']"
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
          v-hasPermi="['mbti:mbtiPersonalityType:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mbti:mbtiPersonalityType:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mbtiPersonalityTypeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="MBTI类型" align="center" prop="typeCode" />
      <el-table-column label="类型名称" align="center" prop="typeName" />
      <el-table-column label="类型详细描述" align="center" prop="description" />
      <el-table-column label="性格特点" align="center" prop="characteristics" />
      <el-table-column label="优势" align="center" prop="strengths" />
      <el-table-column label="弱点" align="center" prop="weaknesses" />
      <el-table-column label="职业建议" align="center" prop="careerSuggestions" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mbti:mbtiPersonalityType:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mbti:mbtiPersonalityType:remove']"
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

    <!-- 添加或修改MBTI人格类型对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="MBTI类型" prop="typeCode">
          <el-input v-model="form.typeCode" placeholder="请输入MBTI类型" />
        </el-form-item>
        <el-form-item label="类型名称" prop="typeName">
          <el-input v-model="form.typeName" placeholder="请输入类型名称" />
        </el-form-item>
        <el-form-item label="类型详细描述" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="性格特点" prop="characteristics">
          <el-input v-model="form.characteristics" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="优势" prop="strengths">
          <el-input v-model="form.strengths" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="弱点" prop="weaknesses">
          <el-input v-model="form.weaknesses" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="职业建议" prop="careerSuggestions">
          <el-input v-model="form.careerSuggestions" type="textarea" placeholder="请输入内容" />
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
import { listMbtiPersonalityType, getMbtiPersonalityType, delMbtiPersonalityType, addMbtiPersonalityType, updateMbtiPersonalityType } from "@/api/mbti/mbtiPersonalityType";

export default {
  name: "MbtiPersonalityType",
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
      // MBTI人格类型表格数据
      mbtiPersonalityTypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        typeCode: null,
        typeName: null,
        description: null,
        characteristics: null,
        strengths: null,
        weaknesses: null,
        careerSuggestions: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        typeCode: [
          { required: true, message: "MBTI类型不能为空", trigger: "blur" }
        ],
        typeName: [
          { required: true, message: "类型名称不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询MBTI人格类型列表 */
    getList() {
      this.loading = true;
      listMbtiPersonalityType(this.queryParams).then(response => {
        this.mbtiPersonalityTypeList = response.rows;
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
        typeId: null,
        typeCode: null,
        typeName: null,
        description: null,
        characteristics: null,
        strengths: null,
        weaknesses: null,
        careerSuggestions: null,
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
      this.ids = selection.map(item => item.typeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加MBTI人格类型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const typeId = row.typeId || this.ids
      getMbtiPersonalityType(typeId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改MBTI人格类型";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.typeId != null) {
            updateMbtiPersonalityType(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMbtiPersonalityType(this.form).then(response => {
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
      const typeIds = row.typeId || this.ids;
      this.$modal.confirm('是否确认删除MBTI人格类型编号为"' + typeIds + '"的数据项？').then(function() {
        return delMbtiPersonalityType(typeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mbti/mbtiPersonalityType/export', {
        ...this.queryParams
      }, `mbtiPersonalityType_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
