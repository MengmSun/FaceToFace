<template>
  <div class="app">
    <el-container>
      <el-header>
        <img src="./assets/logo.png" height="45" />
        <div style="float: right; margin-top: 9px">
          <CountDown></CountDown>
        </div>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import CountDown from '@/components/CountDown'

export default {
  components: {
    CountDown
  }
}

</script>

<style>
.el-header {
  /* background: rgb(255, 251, 219); */
  height: 70px;
  box-shadow: 0 1px 10px rgb(236, 236, 234);
  z-index: 10;
  padding-top: 11px;
  padding-bottom: 4px;
}

.el-main {
  text-align: left;
  color: #2c3e50;
  background: #f8f8fa;
}

.app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
</style>
