<template>
  <div>
    <div v-if="flag">
      <el-tag   v-for="(item, index) in dataList" v-if="item.dictValue==dictValue" :type="item.dictSkin">{{item.dictLabel}}</el-tag>
    </div>
  </div>
</template>

<script>
export default {
  name: "index",
  props:{
    dataList:{
      type: Array,
      default:[]
    },
    value:{
      type:Number,
      default: null
    }
  },
  watch: {
    value: {
      immediate: true,
      handler: function(newVal, oldVal) {
        this.$nextTick(() => {
          this.flag = true;
          this.dictValue=this.value
        });
      }
    }
  },
  data(){
    return{
      flag:false,
      dictValue:undefined
    }
  }
}
</script>
<style scoped>
</style>
