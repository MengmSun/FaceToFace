<template>
  <div>
    <el-dialog
      v-dialogDrag
      title="聊天框"
      class="FaceToFace-dialog__body"
      :close-on-click-modal="false"
      :visible="showChatDialog"
      :before-close="handleClose"
      width="40%"
      :modal="false"
    >
      <div>
        <div class="FaceToFace-Chatarea">
          <el-scrollbar ref="scrollbar">
            <div
              v-for="(msgItem, msgIndex) in msgList"
              :key="msgIndex"
            >
              <div
                v-html="msgItem.msg"
                :style="msgItem.end==='local' ? 'background:#cbffcb':''"
              ></div>
            </div>
          </el-scrollbar>
          <div style="padding-top:10px;">
            <td>
              <el-input
                style="width:430px;padding-right:5px;"
                v-model="chatMessage"
                placeholder="请输入内容"
              >
              </el-input>
            </td>
            <td>
              <el-button @click="sendMsg">发送</el-button>
            </td>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    showChatDialog: {
      type: Boolean,
      default: false
    },
    connection: {
      default: null
    },
    unreadMsg: {
      type: Number,
      default: 0
    }
  },
  data: function () {
    return {
      chatMessage: '',
      msgList: []
    }
  },
  updated: function () {
    this.scrollDown()
  },
  methods: {
    handleClose (done) {
      const ZERO = 0
      this.$emit('update:showChatDialog', false)
      this.$emit('update:unreadMsg', ZERO)
      done()
    },
    onMsg: function (event) {
      if (!this.showChatDialog) {
        const ONE = 1
        this.$emit('update:unreadMsg', this.unreadMsg + ONE)
      }
      this.appendMsg(event)
    },
    appendMsg: function (event, checkmarkId) {
      if (event.data) {
        this.msgList.push({
          end: 'remote',
          msg:
            '<b>' +
            (event.extra.userFullname || event.userid) +
            ':</b><br>' +
            event.data.chatMessage
        }
        )
        if (event.data.checkmarkId) {
          this.connection.send({
            checkmark: 'received',
            checkmarkId: event.data.checkmarkId
          })
        }
      } else {
        this.msgList.push({
          end: 'local',
          msg: '<b>You:</b>' + '<br>' + event
        })
      }
    },
    sendMsg: function () {
      const message = this.chatMessage
      const id = this.connection.userid + this.connection.token()
      if (message) {
        this.appendMsg(message, id)
        this.connection.send({
          chatMessage: message,
          checkmarkId: id
        })
      }
    },
    scrollDown: function () {
      this.$refs.scrollbar.wrap.scrollTop = this.$refs.scrollbar.wrap.scrollHeight
    }
  }
}
</script>

<style scoped>
.FaceToFace-dialog__body /deep/ .el-dialog__body {
  padding: 10px 20px;
}

.FaceToFace-Chatarea {
  height: 300px;
  overflow: hidden;
}
.FaceToFace-Chatarea /deep/ .el-scrollbar {
  height: 250px;
}
.FaceToFace-Chatarea /deep/ .el-scrollbar__wrap {
  overflow: scroll;
  overflow-x: auto;
}
</style>
