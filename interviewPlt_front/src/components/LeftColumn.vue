<template>
  <div class="left-column">
    <div class="video">
      <video-chat
        ref="vc"
        :connection="connection"
      ></video-chat>
    </div>
    <div>
      <div>
        <text-chat
          ref="tc"
          :connection="connection"
          :unreadMsg.sync="unreadMsg"
          :showChatDialog.sync="showChatDialog"
        >
        </text-chat>
      </div>
    </div>
    <transition name="el-zoom-in-bottom">
      <div
        v-show="isViewQuestion"
        class="question"
      >
        <p>这是一道题目</p>
        <p>行数过多时出现滚动条</p>
      </div>
    </transition>
    <div class="bottom-panel">
      <el-button
        circle
        class="panel-button"
        @click="turnMicro"
        v-html="micHtml"
      ></el-button>
      <el-button
        circle
        class="panel-button"
        @click="turnVideo"
        v-html="videoHtml"
      ></el-button>
      <el-badge
        :hidden="unreadMsg == 0"
        :value="unreadMsg"
        :max="99"
        class="item  FaceToFace-ChatButton"
      >
        <el-button
          circle
          class="panel-button"
          @click="clickChatDialog"
        >
          <i class="iconfont icon-chat"></i>
        </el-button>
      </el-badge>
      <div style="float: right; margin-right: 12px">
        <el-link
          :underline="false"
          style="font-size: 16px"
          @click="viewQuestion"
          v-html="viewHtml"
        ></el-link>
      </div>
    </div>
  </div>
</template>

<script>
import VideoChat from './VideoChat'
import RTCMultiConnection from 'rtcmulticonnection'
import TextChat from './TextChat'
require('adapterjs')
const io = require('socket.io-client')
global.io = io
export default {
  components: { VideoChat, RTCMultiConnection, TextChat },
  data () {
    return {
      isMicOn: true,
      isVideoOn: true,
      isViewQuestion: false,
      micHtml: '<i class="iconfont icon-mic-off"></i>',
      videoHtml: '<i class="iconfont icon-video-off"></i>',
      viewHtml: '<i class="iconfont icon-view"></i>显示题目',
      unreadMsg: 0,
      showChatDialog: false
    }
  },
  props: {
    connection: {
      default: null
    }
  },
  methods: {
    turnMicro () {
      this.isMicOn = !this.isMicOn
      if (this.isMicOn) {
        const ZERO = 0
        const localStream = this.connection.attachStreams[ZERO]
        localStream.mute('both')
        this.micHtml = '<i class="iconfont icon-mic-off"></i>'
      } else {
        this.micHtml = '<i class="iconfont icon-mic"></i>'
      }
    },
    turnVideo () {
      this.isVideoOn = !this.isVideoOn
      if (this.isVideoOn) {
        this.videoHtml = '<i class="iconfont icon-video-off"></i>'
      } else {
        this.videoHtml = '<i class="iconfont icon-video"></i>'
      }
    },
    viewQuestion () {
      this.isViewQuestion = !this.isViewQuestion
      if (this.isViewQuestion) {
        this.viewHtml = '<i class="iconfont icon-view-off"></i>隐藏题目'
      } else {
        this.viewHtml = '<i class="iconfont icon-view"></i>显示题目'
      }
    },
    clickChatDialog () {
      this.showChatDialog = true
      this.unreadMsg = 0
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.left-column {
  position: relative;
  border-radius: 4px;
  min-height: 720px;
}
.bottom-panel {
  position: absolute;
  background: #e5e9f2;
  border-radius: 0 0 4px 4px;
  bottom: 0;
  height: 60px;
  width: 100%;
  line-height: 60px;
  margin: 0;
}
.panel-button {
  vertical-align: middle;
  margin-left: 10px;
}
.question {
  position: absolute;
  background: #ffffff;
  border-radius: 4px 4px 0 0;
  bottom: 60px;
  width: 100%;
  min-height: 200px;
  max-height: 660px;
  overflow: auto;
  box-shadow: 0 2px 5px #d3dce6;
}
.video {
  position: absolute;
  background: #d3dce6;
  border-radius: 4px 4px 0 0;
  bottom: 60px;
  width: 100%;
  min-height: 660px;
}
.FaceToFace-ChatButton /deep/ .el-badge__content {
  margin-top: 6px;
}
</style>
