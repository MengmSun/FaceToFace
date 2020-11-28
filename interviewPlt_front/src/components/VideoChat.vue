<template>
  <div style="text-align:center" class="videoChat-total">
    <button @click="openOrJoin">点击此处进入房间</button>
    <input v-model="userDefineRoomId" />
    <div
      v-for="(item, index) in videoList"
      :key="index"
      :video="item"
      class="videoChat-video"
    >
      <video
        controls
        autoplay
        playsinline
        ref="videos"
        :height="200"
        :width="280"
        :muted="true"
        :id="item.id"
      ></video>
    </div>
    <h1 v-if= "videoList.length === 1">等待面试开始</h1>
  </div>
</template>

<script>
import RTCMultiConnection from 'rtcmulticonnection'
require('adapterjs')
const io = require('socket.io-client')
global.io = io
export default {
  components: {
    RTCMultiConnection
  },
  data: function () {
    return {
      showLocalVideo: false,
      showRemoteVideo: false,
      videoList: [],
      userDefineRoomId: '',
      msgList: [],
      chatMessage: ''
    }
  },
  props: {
    roomId: {
      type: String,
      default: 'interview-room'
    },
    socketURL: {
      type: String,
      default: 'https://rtcmulticonnection.herokuapp.com:443/'
    },
    connection: {
      default: null
    }
  },
  mounted: function () {
    this.userDefineRoomId = this.connection.token()
  },
  methods: {
    onstream: function (event) {
      const that = this
      const video = event.mediaElement
      video.id = event.type + '-video-container'
      this.videoList.push({ id: event.streamid, userid: event.userid })
      const THOUSAND = 1000
      setTimeout(function () {
        for (
          let index = 0, len = that.$refs.videos.length;
          index < len;
          index++
        ) {
          if (that.$refs.videos[index].id === event.streamid) {
            that.$refs.videos[index].srcObject = event.stream
            break
          }
        }
      }, THOUSAND)
    },
    onclose: function (event) {
      const that = this
      const THOUSAND = 1000
      setTimeout(function () {
        for (let index = 0, len = that.videoList.length; index < len; index++) {
          if (that.videoList[index].userid === event.userid) {
            that.videoList.splice(index)
            break
          }
        }
      }, THOUSAND)
    },
    openOrJoin: function () {
      this.connection.openOrJoin(this.userDefineRoomId || this.roomId)
    }
  }
}
</script>
<style scoped>
.videoChat-total {
  width: 580px;
}
.videoChat-video {
  display: inline-block;
}
</style>
