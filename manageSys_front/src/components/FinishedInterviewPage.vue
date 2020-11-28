<template>
  <div>
    <el-dialog title="决定面试结果" :visible.sync="decideVisible" width="30%">
      <el-radio v-model="decideResult" label="录用">录用</el-radio>
      <el-radio v-model="decideResult" label="拒绝">拒绝</el-radio>
      <el-radio
        v-model="decideResult"
        label="进入下一轮"
        :disabled="this.noUndecidedData || this.undecidedData[this.currentIndex].score2 !== ''"
      >进入下一轮</el-radio>
      <span slot="footer">
        <el-button type="primary" @click="handleConfirm">确定</el-button>
        <el-button @click="decideVisible = false">返回</el-button>
      </span>
    </el-dialog>

    <el-dialog
      id="videoPlayer"
      :visible.sync="videoVisible"
      width="90%"
      @opened="displayVideo()"
      @close="closeVideo()"
      :close-on-click-modal="false"
      class="videoContainer"
    >
      <video id="myVideo" class="video-js vjs-default-skin vjs-big-play-centered">
        <p class="vjs-no-js">此视频暂时无法播放，请稍后再试</p>
      </video>
    </el-dialog>

    <NavMenu activeIndex="3" />
    <el-dialog title="面试官评价" :visible.sync="dialogVisible" width="40%">
      <p class="dialog">{{ dialogText }}</p>
      <span slot="footer">
        <el-button type="primary" @click="dialogVisible = false">返回</el-button>
      </span>
    </el-dialog>
    <el-tabs type="border-card" class="tabs" @tab-click="handleClick">
      <el-tab-pane label="待处理面试结果">
        <div class="tag" v-if="noUndecidedData">
          <i class="el-icon-warning-outline"></i>
          目前暂无待处理面试结果
        </div>
        <el-table :data="undecidedData" :height="684" v-if="!noUndecidedData">
          <el-table-column prop="interviewee.name" label="候选人" min-width="8%" align="center"></el-table-column>
          <el-table-column prop="interviewer.name" label="面试官" min-width="8%" align="center"></el-table-column>
          <el-table-column prop="index" label="面试轮次" min-width="8%" align="center">
            <template slot-scope="scope">
              <span>{{ (scope.row.index === 1) ? '第一轮' : '第二轮' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="timeInterval.startTime"
            label="面试时间"
            min-width="10%"
            align="center"
          ></el-table-column>

          <el-table-column prop="score1" label="面试结果" min-width="10%" align="center">
            <template slot-scope="scope">
              <span :class="getRateClass(scope.row.score1)">{{scope.row.score1}}</span>
              <el-link type="primary" @click="showRemark(1,scope.$index)">面试官评价</el-link>
            </template>
          </el-table-column>
          <el-table-column min-width="10%" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="decide(scope.$index)">决定面试结果</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="undecidedData.length"
          :page-size="page_size"
        ></el-pagination>
      </el-tab-pane>

      <el-tab-pane label="已处理面试结果">
        <div class="tag" v-if="noDecidedData">
          <i class="el-icon-warning-outline"></i>
          目前暂无已处理面试结果
        </div>
        <el-table :data="decidedData" :height="624" v-if="!noDecidedData">
          <el-table-column prop="interviewee.name" label="候选人" min-width="20%" align="center"></el-table-column>
          <el-table-column prop="interviewer.name" label="面试官" min-width="20%" align="center"></el-table-column>
          <el-table-column prop="index" label="面试轮次" min-width="20%" align="center">
            <template slot-scope="scope">
              <span>{{ (scope.row.index === 1) ? '第一轮' : '第二轮' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="timeInterval.startTime"
            label="面试时间"
            min-width="20%"
            align="center"
          ></el-table-column>
          <el-table-column prop="executeTime" label="处理时间" min-width="20%" align="center"></el-table-column>
          <el-table-column prop="result" label="结果" min-width="20%" align="center"></el-table-column>
        </el-table>

        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="decidedData.length"
          :page-size="page_size"
        ></el-pagination>
      </el-tab-pane>

      <el-tab-pane label="面试录像">
        <div class="tag" v-if="noRecordData">
          <i class="el-icon-warning-outline"></i>
          目前暂无面试录像
        </div>
        <el-table :data="recordData" :height="636" v-if="!noRecordData">
          <el-table-column prop="interviewee.name" label="候选人" min-width="20%" align="center"></el-table-column>
          <el-table-column prop="interviewer.name" label="面试官" min-width="20%" align="center"></el-table-column>
          <el-table-column prop="index" label="面试轮次" min-width="20%" align="center">
            <template slot-scope="scope">
              <span>{{ (scope.row.index === 1) ? '第一轮' : '第二轮' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="timeInterval.startTime"
            label="面试时间"
            min-width="20%"
            align="center"
          ></el-table-column>
          <el-table-column min-width="10%" align="center">
            <template slot-scope="scope">
              <i class="el-icon-video-play" @click="playVideo(scope.$index)" style="cursor:pointer"></i>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="recordData.length"
          :page-size="page_size"
        ></el-pagination>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import NavMenu from './NavMenu'
import videojs from 'video.js'
export default {
  name: 'FinishedInterview',
  components: {
    NavMenu
  },
  methods: {
    getRateClass (score) {
      return 'rate' + score
    },
    showRemark (round, index) {
      const self = this
      const path = this.$BASE_URL + '/admin/interview/undecided/remark'
      this.$axios.post(path, null,
        {
          params: {
            applicant_id: self.undecidedData[index].applicant_id,
            interviewer_id: (round === 1) ? self.undecidedData[index].interviewer_id1 : self.undecidedData[index].interviewer_id2,
            index: round
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.dialogVisible = true
            self.dialogText = response.data.data
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取面试官评价失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取面试官评价失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    decide (index) {
      this.decideVisible = true
      this.currentIndex = index
    },
    handleConfirm () {
      const self = this
      const path = this.$BASE_URL + '/admin/interview/undecided/result'
      const round = (this.undecidedData[this.currentIndex].score2 === '') ? 1 : 2
      this.$axios.post(path, null,
        {
          params: {
            applicant_id: self.undecidedData[self.currentIndex].applicant_id,
            interviewer_id: (round === 1) ? self.undecidedData[self.currentIndex].interviewer_id1 : self.undecidedData[self.currentIndex].interviewer_id2,
            index: round,
            result: self.decideResult
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            this.decideVisible = false
            this.$message({
              message: '操作成功',
              type: 'success',
              showClose: true,
              duration: 3000
            })
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '操作失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '操作失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    playVideo (index) {
      this.videoVisible = true
      this.currentIndex = index
    },
    displayVideo () {
      const self = this
      this.myVideo = videojs(document.getElementById('myVideo'), {
        controls: true, // 是否显示控制条
        preload: 'auto',
        autoplay: false,
        fluid: true, // 自适应宽高
        language: 'zh-CN', // 设置语言
        muted: false, // 是否静音
        inactivityTimeout: false,
        controlBar: { // 设置控制条组件
          /* 设置控制条里面组件的相关属性及显示与否  */
          currentTimeDisplay: true,
          timeDivider: true,
          durationDisplay: true,
          remainingTimeDisplay: false,
          volumePanel: {
            inline: false
          },
          /* 使用children的形式可以控制每一个控件的位置，以及显示与否 */
          children: [
            { name: 'playToggle' }, // 播放/暂停按钮
            { name: 'currentTimeDisplay' }, // 视频当前已播放时间
            { name: 'progressControl' }, // 播放进度条
            { name: 'durationDisplay' }, // 视频播放总时间
            { // 倍数播放，可以自己设置
              name: 'playbackRateMenuButton',
              playbackRates: [0.5, 1, 1.5, 2, 2.5]
            },
            {
              name: 'volumePanel', // 音量控制
              inline: false // 不使用水平方式
            },
            { name: 'FullscreenToggle' } // 全屏
          ]
        },
        sources: [ // 视频来源路径
          {
            src: self.recordData[self.currentIndex].link,
            type: 'video/mp4'
          }
        ]
      })
    },
    closeVideo () {
      this.myVideo.dispose()
      this.myVideo = null
      var videoPlayer = document.getElementById('videoPlayer')
      videoPlayer.childNodes[0].childNodes[1].innerHTML = '<video id="myVideo" class="video-js vjs-default-skin vjs-big-play-centered"><p class="vjs-no-js">此视频暂时无法播放，请稍后再试</p></video>'
    },
    getUndecidedData () {
      const self = this
      const path = this.$BASE_URL + '/admin/record/undecided'
      this.$axios.get(path,
        {
          params: {
            page: self.undecided_current_page,
            limit: self.page_size
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.undecidedData = response.data.data
            console.log(self.undecidedData)
            self.total_undecided = response.data.total
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取待处理面试信息失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取待处理面试信息失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    getDecidedData () {
      const self = this
      const path = this.$BASE_URL + '/admin/record/decided'
      this.$axios.get(path,
        {
          params: {
            page: self.decided_current_page,
            limit: self.page_size
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.decidedData = response.data.data
            self.total_decided = response.data.total
            console.log(self.decidedData)
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取已处理面试信息失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取已处理面试信息失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    getRecordData () {
      const self = this
      const path = this.$BASE_URL + '/admin/record/list'
      this.$axios.get(path,
        {
          params: {
            page: self.record_current_page,
            limit: self.page_size
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.recordData = response.data.data
            self.total_record = response.data.total
            console.log(self.recordData)
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取面试录像失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取面试录像失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    handleClick (tab, event) {
      if (tab.label === '待处理面试结果') {
        this.undecided_current_page = 1
        this.getUndecidedData()
      } else if (tab.label === '已处理面试结果') {
        this.decided_current_page = 1
        this.getDecidedData()
      } else if (tab.label === '面试录像') {
        this.record_current_page = 1
        this.getRecordData()
      }
    }
  },
  data () {
    return {
      page_size: 12,
      undecided_current_page: 1,
      decided_current_page: 1,
      record_current_page: 1,
      total_undecided: 0,
      total_decided: 0,
      total_record: 0,
      undecidedData: [],
      recordData: [],
      decidedData: [],
      dialogVisible: false,
      dialogText: '',
      currentIndex: 0,
      decideResult: '',
      decideVisible: false,
      videoVisible: false,
      myVideo: null
    }
  },
  computed: {
    noUndecidedData: function () {
      return this.undecidedData.length === 0
    },
    noRecordData: function () {
      return this.recordData.length === 0
    },
    noDecidedData: function () {
      return this.decidedData.length === 0
    }
  },
  mounted () {
    document.title = 'Finished Interview'
    this.getUndecidedData()
  }
}
</script>

<style lang="scss" scoped>
/deep/ .videoContainer > .el-dialog {
  margin: auto;
}

.dialog {
  text-align: left;
  word-wrap: break-word;
  white-space: pre-line;
}

/deep/ .el-icon-video-play {
  font-size: 20px;
}

.tabs {
  width: 90%;
  margin: 50px auto 0;
}

/deep/ .el-tabs--border-card > .el-tabs__content {
  padding: 0;
}

/deep/ .el-table__body-wrapper.is-scrolling-none {
  height: 100% !important;
}

/deep/ .el-table .has-gutter {
  color: black !important;
}

/deep/ .el-dialog__body {
  padding: 20px;
}

/deep/ .vjs-paused .vjs-big-play-button,
.vjs-paused.vjs-has-started .vjs-big-play-button {
  display: block;
}

/deep/ .video-js .vjs-duration,
.vjs-no-flex .vjs-duration {
  display: block;
}

/deep/ .video-js .vjs-current-time,
.vjs-no-flex .vjs-current-time {
  display: block;
}

/deep/ .video-js .vjs-remaining-time {
  display: none;
}

/deep/ .video-js .vjs-volume-level:before {
  left: -4.5px;
}

/deep/ .video-js .vjs-play-progress:before {
  top: -4px;
}

.tag {
  width: 400px;
  height: 50px;
  margin: 50px auto;
  background-color: #e8f2ff;
  border: 2px solid #a5cdfe;
  border-radius: 10px;
  font-family: "PingFang SC", "Microsoft YaHei", Arial, sans-serif;
  font-size: 20px;
  line-height: 50px;
  text-align: center;
  font-weight: bold;
  color: #3389ff;
}

.rateS {
  font-weight: bold;
  font-size: 20px;
  color: red;
  display: inline-block;
  margin-right: 20px;
}

.rateA {
  font-weight: bold;
  font-size: 20px;
  color: orange;
  display: inline-block;
  margin-right: 20px;
}

.rateB {
  font-weight: bold;
  font-size: 20px;
  color: green;
  display: inline-block;
  margin-right: 20px;
}

.rateC {
  font-weight: bold;
  font-size: 20px;
  color: chocolate;
  display: inline-block;
  margin-right: 20px;
}

.rateD {
  font-weight: bold;
  font-size: 20px;
  color: black;
  display: inline-block;
  margin-right: 20px;
}
</style>
