<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
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
          v-hasPermi="['mbti:mbtiUserProfile:add']"
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
          v-hasPermi="['mbti:mbtiUserProfile:edit']"
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
          v-hasPermi="['mbti:mbtiUserProfile:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mbti:mbtiUserProfile:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mbtiUserProfileList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="MBTI类型" align="center" prop="mbtiType" />
      <el-table-column label="最近测试时间" align="center" prop="latestTestTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.latestTestTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="E/I维度得分" align="center" prop="eIScore" />
      <el-table-column label="S/N维度得分" align="center" prop="sNScore" />
      <el-table-column label="T/F维度得分" align="center" prop="tFScore" />
      <el-table-column label="J/P维度得分" align="center" prop="jPScore" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mbti:mbtiUserProfile:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mbti:mbtiUserProfile:remove']"
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

    <!-- 添加或修改用户MBTI档案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="最近测试时间" prop="latestTestTime">
          <el-date-picker clearable
            v-model="form.latestTestTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择最近测试时间">
          </el-date-picker>
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
import { listMbtiUserProfile, getMbtiUserProfile, delMbtiUserProfile, addMbtiUserProfile, updateMbtiUserProfile } from "@/api/mbti/mbtiUserProfile";

export default {
  name: "MbtiUserProfile",
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
      // 用户MBTI档案表格数据
      mbtiUserProfileList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        mbtiType: null,
        latestTestTime: null,
        eIScore: null,
        sNScore: null,
        tFScore: null,
        jPScore: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userId: [
          { required: true, message: "用户ID不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户MBTI档案列表 */
    getList() {
      this.loading = true;
      listMbtiUserProfile(this.queryParams).then(response => {
        this.mbtiUserProfileList = response.rows;
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
        profileId: null,
        userId: null,
        mbtiType: null,
        latestTestTime: null,
        eIScore: null,
        sNScore: null,
        tFScore: null,
        jPScore: null,
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
      this.ids = selection.map(item => item.profileId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加用户MBTI档案";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const profileId = row.profileId || this.ids
      getMbtiUserProfile(profileId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户MBTI档案";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.profileId != null) {
            updateMbtiUserProfile(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMbtiUserProfile(this.form).then(response => {
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
      const profileIds = row.profileId || this.ids;
      this.$modal.confirm('是否确认删除用户MBTI档案编号为"' + profileIds + '"的数据项？').then(function() {
        return delMbtiUserProfile(profileIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mbti/mbtiUserProfile/export', {
        ...this.queryParams
      }, `mbtiUserProfile_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
