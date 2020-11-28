<template>
  <div>
    <el-menu
      mode="horizontal"
      @select="handleSelect"
      background-color="#2e3134"
      text-color="#fff"
      active-text-color="#ffd04b"
      menu-trigger="click"
      :unique-opened="true"
      :default-active="activeIndex"
    >
      <el-menu-item index="1">分配候选人</el-menu-item>
      <el-menu-item index="2">正在进行的面试</el-menu-item>
      <el-menu-item index="3">已完成的面试</el-menu-item>
      <el-submenu index="4">
        <template slot="title">个人信息</template>
        <el-menu-item index="4-1">修改用户名</el-menu-item>
        <el-menu-item index="4-2">修改密码</el-menu-item>
        <el-menu-item index="4-3">退出登录</el-menu-item>
      </el-submenu>
    </el-menu>

    <el-dialog :visible.sync="quitDialogVisible" width="30%">
      <span class="dialog-prompt">
        <i class="el-icon-warning"></i>确定要退出登录吗？
      </span>
      <span slot="footer">
        <el-button type="danger" @click="quit">确定</el-button>
        <el-button @click="quitDialogVisible = false">取消</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改用户名" :visible.sync="alterUsernameVisible" width="30%">
      <el-form ref="alterUsernameForm" :rules="alterUsernameRules" :model="alterUsernameForm">
        <span class="alter-prompt">新用户名</span>
        <el-form-item prop="newUsername">
          <el-input type="text" placeholder="请输入新用户名" v-model="alterUsernameForm.newUsername" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button type="primary" @click="alterUsername">确定</el-button>
        <el-button @click="alterUsernameVisible = false">取消</el-button>
      </span>
    </el-dialog>

    <el-dialog title="修改密码" :visible.sync="alterPasswordVisible" width="30%">
      <el-form ref="alterPasswordForm" :rules="alterPasswordRules" :model="alterPasswordForm">
        <span class="alter-prompt">旧密码</span>
        <el-form-item prop="oldPassword">
          <el-input type="password" placeholder="请输入旧密码" v-model="alterPasswordForm.oldPassword" />
        </el-form-item>
        <span class="alter-prompt">新密码</span>
        <el-form-item prop="newPassword">
          <el-input type="password" placeholder="请输入新密码" v-model="alterPasswordForm.newPassword" />
        </el-form-item>
      </el-form>
      <span slot="footer">
        <el-button type="primary" @click="alterPassword">确定</el-button>
        <el-button @click="alterPasswordVisible = false">取消</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'NavMenu',
  props: {
    activeIndex: String
  },
  data () {
    return {
      quitDialogVisible: false,
      alterUsernameVisible: false,
      alterPasswordVisible: false,
      alterUsernameForm: {
        newUsername: ''
      },
      alterPasswordForm: {
        oldPassword: '',
        newPassword: ''
      },
      alterUsernameRules: {
        newUsername: [
          { required: true, message: '新用户名不可为空', trigger: 'blur' }
        ]
      },
      alterPasswordRules: {
        oldPassword: [
          { required: true, message: '旧密码不可为空', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '新密码不可为空', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      if (key === '1') {
        this.$router.push('/applicant')
      } else if (key === '2') {
        this.$router.push('/ongoing_interview')
      } else if (key === '3') {
        this.$router.push('/finished_interview')
      } else if (key === '4-1') {
        this.alterUsernameVisible = true
      } else if (key === '4-2') {
        this.alterPasswordVisible = true
      } else if (key === '4-3') {
        this.quitDialogVisible = true
      }
    },
    quit () {
      this.$router.push('/login')
    },
    alterUsername () {
      const self = this
      this.$refs.alterUsernameForm.validate((valid) => {
        if (valid) {
          const path = self.$BASE_URL + '/admin/user/username'
          self.$axios.post(path, null,
            {
              params: {
                old_username: sessionStorage.getItem('id'),
                new_username: self.alterUsernameForm.newUsername
              },
              headers: {
                token: sessionStorage.getItem('token')
              }
            })
            .then(function (response) {
              if (response.data.status === 'success') {
                sessionStorage.setItem('id', self.alterUsernameForm.newUsername)
                self.alterUsernameForm.newUsername = ''
                self.alterUsernameVisible = false
                self.$message({
                  message: '修改用户名成功',
                  type: 'success',
                  duration: '3000',
                  showClose: true
                })
              } else {
                self.$message({
                  dangerouslyUseHTMLString: true,
                  message: '修改用户名失败<br>' + response.data.msg,
                  type: 'error',
                  duration: '3000',
                  showClose: true
                })
              }
            })
            .catch(function (error) {
              self.$message({
                dangerouslyUseHTMLString: true,
                message: '修改用户名失败<br>' + error,
                type: 'error',
                duration: '3000',
                showClose: true
              })
            })
        } else {
          self.$message({
            message: '输入信息错误',
            type: 'error',
            duration: '3000',
            showClose: true
          })
        }
      })
    },
    alterPassword () {
      const self = this
      this.$refs.alterPasswordForm.validate((valid) => {
        if (valid) {
          const path = self.$BASE_URL + '/admin/user/password'
          self.$axios.post(path, null,
            {
              params: {
                username: sessionStorage.getItem('id'),
                old_password: self.alterPasswordForm.oldPassword,
                new_password: self.alterPasswordForm.newPassword
              },
              headers: {
                token: sessionStorage.getItem('token')
              }
            })
            .then(function (response) {
              if (response.data.status === 'success') {
                self.alterPasswordForm.oldPassword = ''
                self.alterPasswordForm.newPassword = ''
                self.alterPasswordVisible = false
                self.$message({
                  message: '修改密码成功',
                  type: 'success',
                  showClose: true,
                  duration: 3000
                })
              } else {
                self.$message({
                  dangerouslyUseHTMLString: true,
                  message: '修改密码失败<br>' + response.data.msg,
                  type: 'error',
                  duration: '3000',
                  showClose: true
                })
              }
            })
            .catch(function (error) {
              self.$message({
                dangerouslyUseHTMLString: true,
                message: '修改密码失败<br>' + error,
                type: 'error',
                duration: '3000',
                showClose: true
              })
            })
        } else {
          self.$message({
            message: '输入信息错误',
            type: 'error',
            duration: '3000',
            showClose: true
          })
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
/deep/ .el-menu.el-menu--horizontal {
  border-bottom: 0;
}

/deep/ .el-dialog {
  border-radius: 10px;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
}

.alter-prompt {
  display: block;
  text-align: left;
  margin: 0 0 5px;
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  font-size: 15px;
  color: black;
}

.dialog-prompt {
  font-size: 20px;
}

/deep/ .el-icon-warning {
  display: inline-block;
  color: red;
  font-size: 20px;
  margin-right: 10px;
}
</style>
