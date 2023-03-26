<template>

  <div>
    <!--  收件箱  -->
    <template>
      <router-link :to="'/system/msg1/receive/'" style="font-size: 40px; font-weight: 800; color: green; margin-left: 200px ">收件箱</router-link>
    </template>

    <!--  发件箱  -->
    <template>
      <router-link :to="'/system/msg2/send/'" style="font-size: 40px; font-weight: 800; color: red; margin-left: 200px ">发件箱</router-link>
    </template>

    <!--  垃圾箱  -->
    <template>
      <router-link :to="'/system/msg3/dustbin/'" style="font-size: 40px; font-weight: 800; color: blue; margin-left: 200px ">垃圾箱</router-link>
    </template>

    <h1 align="center" style="margin: 20% auto">下午好</h1>
  </div>
</template>

<script>
export default {
  name: 'sys-msg-list'
}
</script>

<style scoped>

</style>
