<template>
  <div class="container" style="width:95%; margin: 0 auto;">
    <FullCalendar
      :plugins="calendarPlugins"
      :all-day-slot="false"
      :header="{
        left:'prev',
        center:'title',
        right: 'today ,next'
      }"
      :slot-event-overlap="false"
      :events="eventList"
      :button-text="{
        today: '今天'
      }"
      :unselect-auto="false"
      :select-overlap="false"
      :business-hours="{
        startTime: '07:00',
        endTime:'18:00',
        daysOfWeek: [ 1, 2, 3, 4, 5, 6, 0 ]
      }"
      :select-allow="handlerSelectAllow"
      :height="500"
      :columnHeaderHtml="columnHeaderHtml"
      :validRange="validRange"
      select-mirror="true"
      now-indicator="true"
      min-time="07:00:00"
      max-time="22:00:00"
      selectable="true"
      slot-duration="00:15"
      slot-label-format="HH:mm"
      title-format="YYYY/MM/DD"
      default-view="timeGridWeek"
      locale="zh-cn"
      @select="handleSelect"
    />
  </div>
</template>

<script>
import FullCalendar from '@fullcalendar/vue'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlulgin from '@fullcalendar/timegrid'
import '@fullcalendar/core/locales/zh-cn' // 支持中文
import momentPlugin from '@fullcalendar/moment'
import interactionPlugin from '@fullcalendar/interaction'

export default {
  name: 'Timeline',
  components: {
    FullCalendar
  },
  props: {
    eventList: {
      default: []
    }
  },
  data () {
    return {
      selectTime: {
        startTime: '',
        endTime: ''
      },
      calendarPlugins: [dayGridPlugin, timeGridPlulgin, momentPlugin, interactionPlugin],
      handlerSelectAllow: info => {
        const currentDate = new Date()
        const start = info.start
        const end = info.end
        return (start <= end && start >= currentDate)
      },
      validRange: function () {
        var current = new Date()
        var year = current.getFullYear()
        var month = current.getMonth() + 1
        month = (month >= 10) ? month : '0' + month
        var day = current.getDate()
        day = (day >= 10) ? day : '0' + day
        return {
          start: year + '-' + month + '-' + day
        }
      }
    }
  },
  methods: {
    handleSelect (info) {
      this.selectTime.startTime = this.parseDate(info.start)
      this.selectTime.endTime = this.parseDate(info.end)
      this.$emit('selectTime', this.selectTime)
    },
    columnHeaderHtml (mom) {
      const array = ['周日', '周一', '周二', '周三', '周四', '周五', '周六']
      for (let i = 0; i <= 6; i++) {
        if (mom.getDay() === i) {
          return (mom.getMonth() + 1) + '/' + mom.getDate() + '&nbsp;&nbsp;' + array[i]
        }
      }
    },
    parseDate (date) {
      const year = date.getFullYear()
      let month = date.getMonth() + 1
      month = (month < 10) ? ('0' + month) : month
      let day = date.getDate()
      day = (day < 10) ? ('0' + day) : day
      let hour = date.getHours()
      hour = (hour < 10) ? ('0' + hour) : hour
      let min = date.getMinutes()
      min = (min < 10) ? ('0' + min) : min
      let sec = date.getSeconds()
      sec = (sec < 10) ? ('0' + sec) : sec
      return year + '-' + month + '-' + day + ' ' + hour + ':' + min + ':' + sec
    }
  }
}
</script>

<style lang="scss" scope>
@import "~@fullcalendar/core/main.css";
@import "~@fullcalendar/daygrid/main.css";
@import "~@fullcalendar/timegrid/main.css";
</style>
