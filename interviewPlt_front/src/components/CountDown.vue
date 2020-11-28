<template>
  <div>
    <span v-show="isView">{{msg}}{{show | filterTime}}</span>
    <span>&nbsp;&nbsp;</span>
    <el-link :underline="false" @click="turnCountDown" type="primary" v-html="viewHtml"></el-link>
  </div>
</template>

<script>
export default {
  data () {
    return {
      isView: true,
      viewHtml: '<i class="iconfont icon-timer-off"></i>隐藏时间',
      msg: '',
      show: '',
      time: null,
      start: new Date(), // TODO: 面试开始时间
      end: new Date(), // TODO: 面试结束时间
      now: new Date()
    }
  },
  mounted () {
    this.start.setTime(this.start.getTime() + 10000)
    this.end.setTime(this.end.getTime() + 20000)

    this.time = setInterval(() => {
      this.now = Date.now()
      if (this.now < this.start) {
        this.msg = '\t距离面试开始：'
        this.show = this.start - this.now
      } else if (this.now < this.end) {
        this.msg = '\t距离面试结束：'
        this.show = this.end - this.now
      } else {
        this.show = 0
        clearInterval(this.timer)
        this.time = null
      }
    }, 1000)
  },
  filters: {
    filterTime (ms) {
      let h = parseInt(ms / (60 * 60 * 1000))
      if (h < 10) {
        h = '0' + h
      }
      let m = parseInt(ms / (60 * 1000)) % 60
      if (m < 10) {
        m = '0' + m
      }
      let s = parseInt(ms / 1000) % 60
      if (s < 10) {
        s = '0' + s
      }
      return h + '\t时\t' + m + '\t分\t' + s + '\t秒\t'
    }
  },
  methods: {
    turnCountDown () {
      this.isView = !this.isView
      if (this.isView) {
        this.viewHtml = '<i class="iconfont icon-timer-off"></i>隐藏时间'
      } else {
        this.viewHtml = '<i class="iconfont icon-timer"></i>显示时间'
      }
    }
  }

}
</script>

<style scoped>
</style>
