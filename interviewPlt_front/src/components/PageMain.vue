<template>
  <div class="page-main">
    <el-row :gutter="20">
      <el-col :span="11">
        <LeftColumn
          ref="left"
          :connection="connection"
        ></LeftColumn>
      </el-col>
      <el-col :span="13">
        <RightColumn
          ref="right"
          :connection="connection"
        ></RightColumn>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import LeftColumn from '@/components/LeftColumn'
import RightColumn from '@/components/RightColumn'
import RTCMultiConnection from 'rtcmulticonnection'
require('adapterjs')
const io = require('socket.io-client')
global.io = io

export default {
  components: {
    LeftColumn,
    RightColumn,
    RTCMultiConnection
  },
  data: function () {
    return {
      socketURL: 'https://rtcmulticonnection.herokuapp.com:443/',
      connection: null,
      roomID: 'interview_room',
      pointsLength: 0
    }
  },
  beforeMount: function () {
    this.connection = new RTCMultiConnection()
  },
  mounted: function () {
    const self = this
    this.connection.socketURL = this.socketURL
    // keep room opened even if owner leaves
    this.connection.autoCloseEntireSession = true
    // one to one interview
    this.connection.maxParticipantsAllowed = 3
    // basic config
    this.connection.chunkSize = 16000
    this.connection.enableFileSharing = true
    this.connection.session = {
      audio: true,
      video: true,
      data: true
    }
    // ask broswer for some Authority
    this.connection.sdpConstraints.mandatory = {
      OfferToReceiveAudio: true,
      OfferToReceiveVideo: true
    }

    this.connection.onstream = this.$refs.left.$refs.vc.onstream
    this.connection.onclose = this.connection.onleave = this.$refs.left.$refs.vc.onclose

    this.connection.onopen = function (event) {
      const pointsLength = self.$refs.right.$refs.canvas.getpointsLength()
      const ZERO = 0
      const THOUSAND = 1000
      if (pointsLength <= ZERO) {
        setTimeout(function () {
          self.connection.send({ type: 'sync_canvas' })
        }, THOUSAND)
      }
    }

    this.connection.onmessage = function (event) {
      if (event.data.chatMessage) {
        self.$refs.left.$refs.tc.onMsg(event)
      } else if (event.data.type) {
        if (event.data.type === 'code_update') {
          self.$refs.right.$refs.editor.updateCode(event.data.data)
        } else if (event.data.type === 'code_permission') {
          self.$refs.right.updatePermission(event.userid)
        } else if (event.data.type === 'code_connect') {
          self.$refs.right.$refs.editor.pushCurrentCode(event.data.data)
        } else if (event.data.type === 'sync_canvas') {
          self.$refs.right.$refs.canvas.sync()
        } else if (event.data.type === 'canvas_data') {
          self.$refs.right.$refs.canvas.syncData(event)
        }
      }
    }
    this.$emit('update:connection', this.connection)
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
