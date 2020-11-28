<template>
  <div>
    <NavMenu activeIndex="2" />
    <el-tabs type="border-card" class="tabs">
      <el-tab-pane label="正在进行中的面试">
        <div class="tag" v-if="noData">
          <i class="el-icon-warning-outline"></i>
          目前暂无正在进行中的面试
        </div>
        <el-table :data="tableData" :height="684" v-if="!noData">
          <el-table-column prop="interviewee.name" label="候选人" min-width="20%" align="center"></el-table-column>
          <el-table-column prop="interviewer.name" label="面试官" min-width="20%" align="center"></el-table-column>
          <el-table-column prop="index" label="面试轮次" min-width="10%" align="center">
            <template slot-scope="scope">
              <span>{{ (scope.row.index === 1) ? '第一轮' : '第二轮' }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="timeInterval.startTime"
            label="开始时间"
            min-width="30%"
            align="center"
          ></el-table-column>
          <el-table-column prop="link" min-width="20%" align="center">
            <template slot-scope="scope">
              <a :href="scope.row.link" target="_blank">
                <el-button size="mini" type="primary">前往面试房间</el-button>
              </a>
            </template>
          </el-table-column>
        </el-table>

        <el-pagination
          layout="total, prev, pager, next, jumper"
          :total="total_data"
          :page-size="page_size"
          :current-page.sync="current_page"
          @current-change="getOngoingInterview"
        ></el-pagination>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import NavMenu from './NavMenu'
export default {
  name: 'OngoingInterview',
  components: {
    NavMenu
  },
  methods: {
    /*
    getDuration (startTime) {
      var startInfo = new Date(startTime)
      var current = new Date()
      var total = Math.floor((current.getTime() - startInfo.getTime()) / 1000)
      var hour = Math.floor(total / 3600) + ''
      var min = Math.floor((total % 3600) / 60) + ''
      hour = (hour.length < 2) ? ('0' + hour) : hour
      min = (min.length < 2) ? ('0' + min) : min
      return hour + ':' + min
    }
    */
    getOngoingInterview () {
      const self = this
      const path = this.$BASE_URL + '/admin/record/ongoing'
      this.$axios.get(path,
        {
          params: {
            page: self.current_page,
            limit: self.page_size
          },
          headers: {
            token: sessionStorage.getItem('token')
          }
        })
        .then(function (response) {
          if (response.data.status === 'success') {
            self.tableData = response.data.data
            self.total_data = response.data.total
          } else {
            self.$message({
              dangerouslyUseHTMLString: true,
              message: '获取正在进行的面试信息失败<br>' + response.data.msg,
              type: 'error',
              duration: '3000',
              showClose: true
            })
          }
        })
        .catch(function (error) {
          self.$message({
            dangerouslyUseHTMLString: true,
            message: '获取正在进行的面试信息失败<br>' + error,
            type: 'error',
            duration: '3000',
            showClose: true
          })
        })
    }
  },
  data () {
    return {
      current_page: 1,
      page_size: 12,
      total_data: 0,
      tableData: []
    }
  },
  computed: {
    noData: function () {
      return this.tableData.length === 0
    }
  },
  mounted () {
    document.title = 'Ongoing Interview'
    this.getOngoingInterview()
  }
}
</script>

<style lang="scss" scoped>
.tabs {
  width: 80%;
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
</style>
