<template>
  <div>
    <el-dialog title="选择面试时间" :visible.sync="dateVisible" width="70%">
      <timeline ref="TL" :eventList="eventList" @selectTime="handleSelectTime"></timeline>
      <span slot="footer">
        <el-button type="primary" @click="handleConfirm()">确定</el-button>
        <el-button @click="dateVisible = false">返回</el-button>
      </span>
    </el-dialog>

    <el-dialog title="可选面试官" :visible.sync="interviewerVisible" width="30%">
      <div class="tag" v-if="noInterviewerData">
        <i class="el-icon-warning-outline"></i>
        目前暂无可选面试官
      </div>
      <el-table :data="availInterviewerData" :height="419" v-if="!noInterviewerData">
        <el-table-column prop="name" label="姓名" min-width="50%" align="center"></el-table-column>
        <el-table-column label min-width="50%" align="center">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="chooseTime(scope.$index)">选择面试时间</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        layout="total, prev, pager, next, jumper"
        :total="total_interviewer"
        :page-size="interviewer_page_size"
        :current-page.sync="interviewer_current_page"
        @current-change="getInterviewerInfo"
      ></el-pagination>
      <span slot="footer">
        <el-button type="primary" @click="interviewerVisible = false">返回</el-button>
      </span>
    </el-dialog>

    <NavMenu activeIndex="1" />
    <el-tabs type="border-card" class="tabs" @tab-click="handleClick">
      <el-tab-pane label="待分配候选人">
        <div class="tag" v-if="noUnallocatedData">
          <i class="el-icon-warning-outline"></i>
          目前暂无待分配候选人
        </div>

        <el-table :data="unallocatedData" :height="684" v-if="!noUnallocatedData">
          <el-table-column prop="name" label="姓名" min-width="15%" align="center"></el-table-column>
          <el-table-column prop="email" label="邮箱" min-width="40%" align="center"></el-table-column>
          <el-table-column prop="index" label="面试轮次" min-width="15%" align="center">
            <template slot-scope="scope">
              <span>{{ (scope.row.index === 1) ? '第一轮' : '第二轮' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="cv" label="简历" min-width="10%" align="center">
            <template slot-scope="scope">
              <a :href="scope.row.cv" target="_blank">
                <i class="el-icon-document"></i>
              </a>
            </template>
          </el-table-column>
          <el-table-column label min-width="15%" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="allocate(scope.$index)">分配</el-button>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="total_unallocated"
          :page-size="page_size"
          :current-page.sync="unallocated_current_page"
          @current-change="getUnallocatedData"
        ></el-pagination>
      </el-tab-pane>

      <el-tab-pane label="已分配候选人">
        <div class="tag" v-if="noAllocatedData">
          <i class="el-icon-warning-outline"></i>
          目前暂无已分配候选人
        </div>
        <el-table :data="allocatedData" :height="684" v-if="!noAllocatedData">
          <el-table-column prop="name" label="姓名" min-width="15%" align="center"></el-table-column>
          <el-table-column prop="email" label="邮箱" min-width="40%" align="center"></el-table-column>
          <el-table-column prop="index" label="面试轮次" min-width="15%" align="center">
            <template slot-scope="scope">
              <span>{{ (scope.row.index === 1) ? '第一轮' : '第二轮' }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="cv" label="简历" min-width="10%" align="center">
            <template slot-scope="scope">
              <a :href="scope.row.cv" target="_blank">
                <i class="el-icon-document"></i>
              </a>
            </template>
          </el-table-column>
          <el-table-column prop="interviewer.name" label="面试官" min-width="15%" align="center"></el-table-column>
        </el-table>

        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="total_allocated"
          :page-size="page_size"
          :current-page.sync="allocated_current_page"
          @current-change="getAllocatedData"
        ></el-pagination>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import NavMenu from './NavMenu'
import Timeline from './Timeline.vue'
export default {
  name: 'Applicant',
  components: {
    NavMenu,
    Timeline
  },
  methods: {
    allocate (index) {
      this.interviewerVisible = true
      this.currentApplicantIndex = index
      this.interviewer_current_page = 1
      this.getInterviewerInfo()
    },
    chooseTime (index) {
      this.dateVisible = true
      this.currentInterviewerIndex = index
      this.getInterviewerTime()
    },
    handleSelectTime (selectTime) {
      this.selectTime = selectTime
    },
    handleConfirm () {
      const self = this
      const path = this.$BASE_URL + '/admin/record/add'
      console.log(self.unallocatedData[self.currentApplicantIndex].id)
      console.log(self.availInterviewerData[self.currentInterviewerIndex].id)
      console.log(self.selectTime.startTime)
      console.log(self.selectTime.endTime)
      this.$axios.post(path, null,
        {
          params: {
            intervieweeId: self.unallocatedData[self.currentApplicantIndex].id,
            interviewerId: self.availInterviewerData[self.currentInterviewerIndex].id,
            startTime: self.selectTime.startTime,
            endTime: self.selectTime.endTime,
            link: 'www.baidu.com'
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.$message({
              message: '分配成功',
              type: 'success',
              showClose: true,
              duration: 3000
            })
            self.getUnallocatedData()
            self.dateVisible = false
            self.interviewerVisible = false
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '分配失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '分配失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    getUnallocatedData () {
      const self = this
      const path = this.$BASE_URL + '/admin/applicant/unallocated'
      this.$axios.get(path,
        {
          params: {
            page: self.unallocated_current_page,
            limit: self.page_size
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.unallocatedData = response.data.data
            self.total_unallocated = response.data.total
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取待分配候选人信息失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取待分配候选人信息失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    getAllocatedData () {
      const self = this
      const path = this.$BASE_URL + '/admin/applicant/allocated'
      this.$axios.get(path,
        {
          params: {
            page: self.allocated_current_page,
            limit: self.page_size
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.allocatedData = response.data.data
            self.total_allocated = response.data.total
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取已分配候选人信息失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取已分配候选人信息失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    getInterviewerInfo () {
      const self = this
      const path = this.$BASE_URL + '/admin/interviewer/info'
      this.$axios.get(path,
        {
          params: {
            page: self.interviewer_current_page,
            limit: self.interviewer_page_size
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.availInterviewerData = response.data.data
            self.total_interviewer = response.data.total
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取面试官信息失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取面试官信息失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    getInterviewerTime () {
      const self = this
      const path = this.$BASE_URL + '/admin/interviewer/time'
      this.$axios.get(path,
        {
          params: {
            id: self.availInterviewerData[self.currentInterviewerIndex].id
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.eventList = []
            const tmp = (response.data.data) ? response.data.data.freeTimeList : []
            for (var i = 0; i < tmp.length; i++) {
              self.eventList.push({
                start: tmp[i].startTime,
                end: tmp[i].endTime,
                backgroundColor: 'black',
                borderColor: 'black'
              })
            }
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取面试官时间表失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取面试官时间表失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    },
    handleClick (tab, event) {
      if (tab.label === '待分配候选人') {
        this.unallocated_current_page = 1
        this.getUnallocatedData()
      } else if (tab.label === '已分配候选人') {
        this.allocated_current_page = 1
        this.getAllocatedData()
      }
    }
  },
  data () {
    return {
      selectTime: null,
      eventList: [],
      total_interviewer: 0,
      total_allocated: 0,
      total_unallocated: 0,
      page_size: 12,
      interviewer_page_size: 7,
      interviewer_current_page: 1,
      unallocated_current_page: 1,
      allocated_current_page: 1,
      unallocatedData: [],
      allocatedData: [],
      availInterviewerData: [],
      interviewerVisible: false,
      dateVisible: false,
      currentApplicantIndex: 0,
      currentInterviewerIndex: 0
    }
  },
  computed: {
    noUnallocatedData: function () {
      return this.unallocatedData.length === 0
    },
    noAllocatedData: function () {
      return this.allocatedData.length === 0
    },
    noInterviewerData: function () {
      return this.availInterviewerData.length === 0
    }
  },
  mounted () {
    document.title = 'Applicant'
    this.getUnallocatedData()
  }
}
</script>

<style lang="scss" scoped>
.tabs {
  width: 80%;
  margin: 50px auto;
}

/deep/ .el-tabs--border-card > .el-tabs__content {
  padding: 0;
}

/deep/ .el-table .has-gutter {
  color: black !important;
}

/deep/ .el-table__body-wrapper.is-scrolling-none {
  height: 100% !important;
}

/deep/ .el-dialog__body {
  padding: 10px 20px !important;
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

/deep/ .fc-time {
  white-space: normal !important;
  word-wrap: break-word;
}
</style>
