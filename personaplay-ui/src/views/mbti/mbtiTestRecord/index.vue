<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结束时间">
        </el-date-picker>
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
          v-hasPermi="['mbti:mbtiTestRecord:add']"
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
          v-hasPermi="['mbti:mbtiTestRecord:edit']"
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
          v-hasPermi="['mbti:mbtiTestRecord:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mbti:mbtiTestRecord:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mbtiTestRecordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="测试类型(1单人 2双人)" align="center" prop="testType" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="测试时长(秒)" align="center" prop="duration" />
      <el-table-column label="MBTI结果" align="center" prop="mbtiResult" />
      <el-table-column label="E/I维度得分" align="center" prop="eIScore" />
      <el-table-column label="S/N维度得分" align="center" prop="sNScore" />
      <el-table-column label="T/F维度得分" align="center" prop="tFScore" />
      <el-table-column label="J/P维度得分" align="center" prop="jPScore" />
      <el-table-column label="匹配度得分(双人测试)" align="center" prop="compatibilityScore" />
      <el-table-column label="默契度得分(双人测试)" align="center" prop="chemistryScore" />
      <el-table-column label="相同答案数(双人测试)" align="center" prop="identicalAnswers" />
      <el-table-column label="总题目数" align="center" prop="totalQuestions" />
      <el-table-column label="状态(0进行中 1已完成 2中断)" align="center" prop="status" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mbti:mbtiTestRecord:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mbti:mbtiTestRecord:remove']"
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

    <!-- 添加或修改MBTI测试记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable
            v-model="form.startTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
            v-model="form.endTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="测试时长(秒)" prop="duration">
          <el-input v-model="form.duration" placeholder="请输入测试时长(秒)" />
        </el-form-item>
        <el-form-item label="MBTI结果" prop="mbtiResult">
          <el-input v-model="form.mbtiResult" placeholder="请输入MBTI结果" />
        </el-form-item>
        <el-form-item label="E/I维度得分" prop="eIScore">
          <el-input v-model="form.eIScore" placeholder="请输入E/I维度得分" />
        </el-form-item>
        <el-form-item label="S/N维度得分" prop="sNScore">
          <el-input v-model="form.sNScore" placeholder="请输入S/N维度得分" />
        </el-form-item>
        <el-form-item label="T/F维度得分" prop="tFScore">
          <el-input v-model="form.tFScore" placeholder="请输入T/F维度得分" />
        </el-form-item>
        <el-form-item label="J/P维度得分" prop="jPScore">
          <el-input v-model="form.jPScore" placeholder="请输入J/P维度得分" />
        </el-form-item>
        <el-form-item label="匹配度得分(双人测试)" prop="compatibilityScore">
          <el-input v-model="form.compatibilityScore" placeholder="请输入匹配度得分(双人测试)" />
        </el-form-item>
        <el-form-item label="默契度得分(双人测试)" prop="chemistryScore">
          <el-input v-model="form.chemistryScore" placeholder="请输入默契度得分(双人测试)" />
        </el-form-item>
        <el-form-item label="相同答案数(双人测试)" prop="identicalAnswers">
          <el-input v-model="form.identicalAnswers" placeholder="请输入相同答案数(双人测试)" />
        </el-form-item>
        <el-form-item label="总题目数" prop="totalQuestions">
          <el-input v-model="form.totalQuestions" placeholder="请输入总题目数" />
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
import { listMbtiTestRecord, getMbtiTestRecord, delMbtiTestRecord, addMbtiTestRecord, updateMbtiTestRecord } from "@/api/mbti/mbtiTestRecord";

export default {
  name: "MbtiTestRecord",
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
      // MBTI测试记录表格数据
      mbtiTestRecordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        testType: null,
        versionId: null,
        roomId: null,
        startTime: null,
        endTime: null,
        duration: null,
        mbtiResult: null,
        eIScore: null,
        sNScore: null,
        tFScore: null,
        jPScore: null,
        compatibilityScore: null,
        chemistryScore: null,
        identicalAnswers: null,
        totalQuestions: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
        testType: [
          { required: true, message: "测试类型(1单人 2双人)不能为空", trigger: "change" }
        ],
        versionId: [
          { required: true, message: "测试版本ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询MBTI测试记录列表 */
    getList() {
      this.loading = true;
      listMbtiTestRecord(this.queryParams).then(response => {
        this.mbtiTestRecordList = response.rows;
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
        recordId: null,
        userId: null,
        testType: null,
        versionId: null,
        roomId: null,
        startTime: null,
        endTime: null,
        duration: null,
        mbtiResult: null,
        eIScore: null,
        sNScore: null,
        tFScore: null,
        jPScore: null,
        compatibilityScore: null,
        chemistryScore: null,
        identicalAnswers: null,
        totalQuestions: null,
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
      this.ids = selection.map(item => item.recordId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加MBTI测试记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const recordId = row.recordId || this.ids
      getMbtiTestRecord(recordId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改MBTI测试记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.recordId != null) {
            updateMbtiTestRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMbtiTestRecord(this.form).then(response => {
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
      const recordIds = row.recordId || this.ids;
      this.$modal.confirm('是否确认删除MBTI测试记录编号为"' + recordIds + '"的数据项？').then(function() {
        return delMbtiTestRecord(recordIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mbti/mbtiTestRecord/export', {
        ...this.queryParams
      }, `mbtiTestRecord_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
