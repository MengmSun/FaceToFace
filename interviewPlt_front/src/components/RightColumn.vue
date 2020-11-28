<template>
  <div class="right-column">
    <el-tabs
      tab-position="right"
      v-model="activeName"
      @tab-click="handleClick"
    >
      <el-tab-pane
        label="面试信息"
        name="info"
      >
        <div class="grid-content bg-purple"></div>
      </el-tab-pane>
      <el-tab-pane
        label="代码编辑"
        name="code"
      >
        <div class="grid-content">
          <CodeEditor
            ref="editor"
            :permission="permission"
            :connection="connection"
          ></CodeEditor>
          <div class="button-div">
            <el-button
              :class="`${permission ? 'enabled' : 'disabled'}`"
              type="primary"
              :disabled="permission"
              round
              @click="getPermission"
            >{{ permission ? '允许编辑' : '点击以开始编辑' }}</el-button>
            <el-button
              type="primary"
              round
            >提交</el-button>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane
        label="白板"
        name="board"
      >
        <div>
          <joint-canvas
            :connection="connection"
            ref="canvas"
          ></joint-canvas>
        </div>
      </el-tab-pane>
      <el-tab-pane label="评价" name="remark">
        <div class="grid-content">
          <div class="score">
            <span class="rate_prompt">评级</span>
            <el-rate class="rate" v-model="rate_value" :colors="colors" :texts="texts" show-text></el-rate>
          </div>
          <div class="remark">
            <span class="remark_prompt">评价</span>
            <el-input type="textarea" rows="26" v-model="remark_text" resize="none"></el-input>
          </div>
          <div class="button-div">
            <el-button type="primary" round @click="submitRemark">提交</el-button>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import CodeEditor from '@/components/CodeEditor'
import JointCanvas from '@/components/JointCanvas'
export default {
  components: {
    CodeEditor,
    JointCanvas
  },
  props: {
    connection: {
      default: null
    }
  },
  data () {
    return {
      remark_text: '',
      rate_value: null,
      colors: ['#99A9BF', '#F7BA2A', '#FF9900'],
      texts: ['D', 'C', 'B', 'A', 'S'],
      activeName: 'info',
      permission: false,
      firstLoadIframe: true
    }
  },
  methods: {
    validate () {
      if (!this.rate_value) {
        this.$message({
          dangerouslyUseHTMLString: true,
          message: '提交失败<br>评级不可为空',
          type: 'error',
          duration: '3000',
          showClose: true
        })
        return false
      } else if (this.remark_text === '') {
        this.$message({
          dangerouslyUseHTMLString: true,
          message: '提交失败<br>评价不可为空',
          type: 'error',
          duration: '3000',
          showClose: true
        })
        return false
      } else return true
    },
    updatePermission (editorID) {
      if (editorID !== this.connection.userid) {
        this.permission = false
      }
    },
    getPermission () {
      const message = { type: 'code_permission' }
      this.connection.send(message)
      this.permission = true
    },
    handleClick (tab, event) {
      const ZERO = 0
      if (tab.name === 'code') {
        const totalUserID = this.connection.getAllParticipants()
        if (totalUserID.length > ZERO) {
          const message = { type: 'code_connect', data: totalUserID[ZERO] }
          this.connection.send(message)
        }
        this.$nextTick(() => {
          this.$refs.editor.refresh()
        })
      } else if (tab.name === 'board') {
        if (this.firstLoadIframe) {
          document.getElementById('FaceToFace-iframe').contentWindow.location.reload()
          const self = this
          this.firstLoadIframe = false
          const pointsLength = self.$refs.canvas.getpointsLength()
          const ZERO = 0
          const THOUSAND = 1000
          if (pointsLength <= ZERO) {
            setTimeout(function () {
              self.connection.send({ type: 'sync_canvas' })
            }, THOUSAND)
          }
        }
      }
    },
    submitRemark () {
      if (this.validate()) {
        let result = ''
        switch (this.rate_value) {
          case 1:
            result = 'D'
            break
          case 2:
            result = 'C'
            break
          case 3:
            result = 'B'
            break
          case 4:
            result = 'A'
            break
          case 5:
            result = 'S'
            break
        }
        const self = this
        const path = this.$BASE_URL + '/admin/record/update'
        this.$axios.post(path, null,
          {
            params: {
              remark: self.remark_text,
              result: result
            },
            headers: {
              token: sessionStorage.getItem('token')
            }
          })
          .then(function (response) {
            if (response.data.status === 'success') {
              self.$message({
                message: '提交成功',
                type: 'success',
                duration: '3000',
                showClose: true
              })
            } else {
              self.$message({
                dangerouslyUseHTMLString: true,
                message: '提交失败<br>' + response.data.msg,
                type: 'error',
                duration: '3000',
                showClose: true
              })
            }
          })
          .catch(function (error) {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '提交失败<br>' + error,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          })
      }
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang='scss' scoped>
.el-col {
  border-radius: 4px;
}
.bg-purple-dark {
  background: #99a9bf;
}
.bg-purple {
  background: #d3dce6;
}
.bg-purple-light {
  background: #e5e9f2;
}
.grid-content {
  position: relative;
  border-radius: 4px;
  min-height: 720px;
}
.button-div {
  position: absolute;
  right: 5px;
  bottom: 0;
}
.disabled {
  border: 0;
  background-color: #d8a008;
}
.enabled {
  border: 0;
  background-color: #67c23a;
}
.score {
  height: 50px;
}
.rate {
  float: left;
  height: 50px;
  line-height: 60px;
}
/deep/ .el-rate__text {
  font-weight: bold;
  line-height: 50px;
  float: right;
  margin-left: 10px;
  font-size: 18px;
}
.rate_prompt {
  font-weight: bold;
  margin-right: 20px;
  margin-left: 20px;
  line-height: 50px;
  float: left;
}
.remark {
  font-weight: bold;
  margin-right: 20px;
  margin-left: 20px;
  line-height: 50px;
}
</style>
