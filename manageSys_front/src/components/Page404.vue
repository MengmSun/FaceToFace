<template>
  <div>
    <img src="@/assets/404.png" class="not_found" />
  </div>
</template>

<script>
export default {
  name: 'Page404'
}
</script>

<style lang="scss" scoped>
.not_found {
  display: block;
  height: 80%;
  width: 80%;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  margin: auto;
}
</style>
