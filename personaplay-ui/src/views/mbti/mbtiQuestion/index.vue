<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">

      <el-form-item label="测试版本代码" prop="versionCodes">
        <el-input
          v-model="queryParams.versionCodes"
          placeholder="请输入测试版本代码"
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
          v-hasPermi="['mbti:mbtiQuestion:add']"
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
          v-hasPermi="['mbti:mbtiQuestion:edit']"
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
          v-hasPermi="['mbti:mbtiQuestion:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mbti:mbtiQuestion:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mbtiQuestionList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="问题内容" align="center" prop="questionText" />
      <el-table-column label="维度" align="center" prop="dimension" />
      <el-table-column label="选项A" align="center" prop="optionA" />
      <el-table-column label="选项B" align="center" prop="optionB" />
      <el-table-column label="选项A对应维度" align="center" prop="optionAValue" />
      <el-table-column label="选项B对应维度" align="center" prop="optionBValue" />
      <el-table-column label="问题权重" align="center" prop="weight" />
      <el-table-column label="适用测试版本" align="center" prop="versionCodes" />
      <el-table-column label="题目顺序" align="center" prop="sequence" />
      <el-table-column label="状态(0启用 1禁用)" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mbti:mbtiQuestion:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mbti:mbtiQuestion:remove']"
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

    <!-- 添加或修改MBTI测试问题对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="问题内容" prop="questionText">
          <el-input v-model="form.questionText" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="维度" prop="dimension">
          <el-input v-model="form.dimension" placeholder="请输入维度(EI, SN, TF, JP)" />
        </el-form-item>
        <el-form-item label="选项A" prop="optionA">
          <el-input v-model="form.optionA" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="选项B" prop="optionB">
          <el-input v-model="form.optionB" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="选项A对应维度" prop="optionAValue">
          <el-input v-model="form.optionAValue" placeholder="请输入选项A对应的维度值(如E、S、T、J)" />
        </el-form-item>
        <el-form-item label="选项B对应维度" prop="optionBValue">
          <el-input v-model="form.optionBValue" placeholder="请输入选项B对应的维度值(如I、N、F、P)" />
        </el-form-item>
        <el-form-item label="问题权重" prop="weight">
          <el-input v-model="form.weight" placeholder="请输入问题权重" />
        </el-form-item>
        <el-form-item label="适用测试版本" prop="versionCodes">
          <el-input v-model="form.versionCodes" placeholder="请输入适用的测试版本代码(多个用逗号分隔)" />
        </el-form-item>
        <el-form-item label="题目顺序" prop="sequence">
          <el-input v-model="form.sequence" placeholder="请输入题目顺序" />
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
import { listMbtiQuestion, getMbtiQuestion, delMbtiQuestion, addMbtiQuestion, updateMbtiQuestion } from "@/api/mbti/mbtiQuestion";

export default {
  name: "MbtiQuestion",
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
      // MBTI测试问题表格数据
      mbtiQuestionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        questionText: null,
        dimension: null,
        optionA: null,
        optionB: null,
        optionAValue: null,
        optionBValue: null,
        weight: null,
        versionCodes: null,
        sequence: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        questionText: [
          { required: true, message: "问题内容不能为空", trigger: "blur" }
        ],
        dimension: [
          { required: true, message: "维度(E/I, S/N, T/F, J/P)不能为空", trigger: "blur" }
        ],
        optionA: [
          { required: true, message: "选项A不能为空", trigger: "blur" }
        ],
        optionB: [
          { required: true, message: "选项B不能为空", trigger: "blur" }
        ],
        optionAValue: [
          { required: true, message: "选项A对应的维度值(如E、S、T、J)不能为空", trigger: "blur" }
        ],
        optionBValue: [
          { required: true, message: "选项B对应的维度值(如I、N、F、P)不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询MBTI测试问题列表 */
    getList() {
      this.loading = true;
      listMbtiQuestion(this.queryParams).then(response => {
        this.mbtiQuestionList = response.rows;
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
        questionId: null,
        questionText: null,
        dimension: null,
        optionA: null,
        optionB: null,
        optionAValue: null,
        optionBValue: null,
        weight: null,
        versionCodes: null,
        sequence: null,
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
      this.ids = selection.map(item => item.questionId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加MBTI测试问题";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const questionId = row.questionId || this.ids
      getMbtiQuestion(questionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改MBTI测试问题";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.questionId != null) {
            updateMbtiQuestion(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMbtiQuestion(this.form).then(response => {
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
      const questionIds = row.questionId || this.ids;
      this.$modal.confirm('是否确认删除MBTI测试问题编号为"' + questionIds + '"的数据项？').then(function() {
        return delMbtiQuestion(questionIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mbti/mbtiQuestion/export', {
        ...this.queryParams
      }, `mbtiQuestion_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
