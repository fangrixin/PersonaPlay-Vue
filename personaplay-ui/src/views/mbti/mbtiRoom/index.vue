<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="房间号码" prop="roomCode">
        <el-input
          v-model="queryParams.roomCode"
          placeholder="请输入房间号码"
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
          v-hasPermi="['mbti:mbtiRoom:add']"
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
          v-hasPermi="['mbti:mbtiRoom:edit']"
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
          v-hasPermi="['mbti:mbtiRoom:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['mbti:mbtiRoom:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mbtiRoomList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="房间号码" align="center" prop="roomCode" />
      <el-table-column label="房间状态(0等待中 1测试中 2已完成 3已关闭)" align="center" prop="status" />
      <el-table-column label="成员上限" align="center" prop="memberLimit" />
      <el-table-column label="当前成员数" align="center" prop="currentMembers" />
      <el-table-column label="二维码URL" align="center" prop="qrCodeUrl" />
      <el-table-column label="过期时间" align="center" prop="expireTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.expireTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['mbti:mbtiRoom:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['mbti:mbtiRoom:remove']"
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

    <!-- 添加或修改MBTI测试房间对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="房间号码" prop="roomCode">
          <el-input v-model="form.roomCode" placeholder="请输入房间号码" />
        </el-form-item>
        <el-form-item label="成员上限" prop="memberLimit">
          <el-input v-model="form.memberLimit" placeholder="请输入成员上限" />
        </el-form-item>
        <el-form-item label="当前成员数" prop="currentMembers">
          <el-input v-model="form.currentMembers" placeholder="请输入当前成员数" />
        </el-form-item>
        <el-form-item label="二维码URL" prop="qrCodeUrl">
          <el-input v-model="form.qrCodeUrl" placeholder="请输入二维码URL" />
        </el-form-item>
        <el-form-item label="过期时间" prop="expireTime">
          <el-date-picker clearable
            v-model="form.expireTime"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择过期时间">
          </el-date-picker>
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
import { listMbtiRoom, getMbtiRoom, delMbtiRoom, addMbtiRoom, updateMbtiRoom } from "@/api/mbti/mbtiRoom";

export default {
  name: "MbtiRoom",
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
      // MBTI测试房间表格数据
      mbtiRoomList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roomCode: null,
        creatorId: null,
        versionId: null,
        status: null,
        memberLimit: null,
        currentMembers: null,
        qrCodeUrl: null,
        expireTime: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        roomCode: [
          { required: true, message: "房间号码不能为空", trigger: "blur" }
        ],
        creatorId: [
          { required: true, message: "创建者用户ID不能为空", trigger: "blur" }
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
    /** 查询MBTI测试房间列表 */
    getList() {
      this.loading = true;
      listMbtiRoom(this.queryParams).then(response => {
        this.mbtiRoomList = response.rows;
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
        roomId: null,
        roomCode: null,
        creatorId: null,
        versionId: null,
        status: null,
        memberLimit: null,
        currentMembers: null,
        qrCodeUrl: null,
        expireTime: null,
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
      this.ids = selection.map(item => item.roomId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加MBTI测试房间";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const roomId = row.roomId || this.ids
      getMbtiRoom(roomId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改MBTI测试房间";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.roomId != null) {
            updateMbtiRoom(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMbtiRoom(this.form).then(response => {
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
      const roomIds = row.roomId || this.ids;
      this.$modal.confirm('是否确认删除MBTI测试房间编号为"' + roomIds + '"的数据项？').then(function() {
        return delMbtiRoom(roomIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('mbti/mbtiRoom/export', {
        ...this.queryParams
      }, `mbtiRoom_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
