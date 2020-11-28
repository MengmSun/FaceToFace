<template>
  <div>
    <NavMenu />
    <div class="main-prompt">
      欢迎使用
      <strong>FaceToFace</strong> 在线面试管理系统
      <br />您的身份为：
      <span class="main-character">HR</span>
    </div>
  </div>
</template>

<script>
import NavMenu from './NavMenu'
export default {
  name: 'Main',
  components: {
    NavMenu
  },
  mounted () {
    document.title = 'FaceToFace'
  }
}
</script>

<style lang="scss" scoped>
.main-prompt {
  width: 550px;
  height: 90px;
  background-color: rgba(0, 0, 0, 0.7);
  text-align: left;
  line-height: 50px;
  margin: 50px;
  padding: 20px;
  border-radius: 20px;
  -webkit-border-radius: 20px;
  -moz-border-radius: 20px;
  color: aliceblue;
  font-size: 30px;
}

.main-character {
  color: darkorange;
  font-weight: bold;
}
</style>
