<template>
  <div>
    <span class="login-brand">FaceToFace</span>
    <el-form ref="LoginForm" :model="form" :rules="rules" class="login-box">
      <h3 class="login-title">欢迎登录</h3>
      <span class="login-prompt">用户名</span>
      <el-form-item prop="id">
        <el-input type="text" placeholder="请输入用户名" v-model="form.id" />
      </el-form-item>
      <span class="login-prompt">密码</span>
      <el-form-item prop="password">
        <el-input type="password" placeholder="请输入密码" v-model="form.password" />
      </el-form-item>
      <el-form-item>
        <el-button class="login-button" type="primary" v-on:click="onSubmit">登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data () {
    return {
      form: {
        id: '',
        password: ''
      },
      rules: {
        id: [
          { required: true, message: '用户名不可为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '密码不可为空', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    const self = this
    document.onkeydown = function (e) {
      const key = window.event.keyCode
      if (key === 13) {
        self.onSubmit()
      }
    }
  },
  methods: {
    onSubmit () {
      const self = this
      this.$refs.LoginForm.validate((valid) => {
        if (valid) {
          const path = self.$BASE_URL + '/login'
          self.$axios.post(path, null,
            {
              params: self.form
            })
            .then(function (response) {
              if (response.data.status === 'success') {
                sessionStorage.setItem('token', response.data.token)
                sessionStorage.setItem('id', self.form.id)
                self.$router.push('/main')
              } else {
                self.$message({
                  dangerouslyUseHTMLString: true,
                  message: '登录失败<br>' + response.data.msg,
                  type: 'error',
                  duration: '3000',
                  showClose: true
                })
              }
            })
            .catch(function (error) {
              self.$message({
                dangerouslyUseHTMLString: true,
                message: '登录失败<br>' + error,
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
  },
  mounted () {
    document.title = 'Login'
  }
}
</script>

<style lang="scss" scoped>
.login-brand {
  display: block;
  text-align: center;
  margin: 180px auto 25px;
  font-family: "Helvetica Neue", Helvetica, Arial, Sans-Serif;
  font-weight: bold;
  font-size: 50px;
  color: antiquewhite;
}

.login-prompt {
  display: block;
  text-align: left;
  margin: 0 0 5px;
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  font-weight: bold;
  font-size: 15px;
  color: aliceblue;
}

.login-box {
  background: rgba(0, 0, 0, 0.7);
  border: 0;
  width: 350px;
  margin: 0 auto 180px;
  padding: 35px 35px 15px 35px;
  border-radius: 5px;
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
}

.login-title {
  text-align: center;
  font-size: 20px;
  margin: 10px auto 20px auto;
  color: aliceblue;
}

.login-button {
  margin: 5px auto;
}

/deep/ .el-form-item__error {
  color: rgb(255, 0, 0);
  -webkit-filter: contrast(2);
  filter: contrast(2);
}
</style>
