<template>
  <div class="document">
    <el-select v-model="selectLanguage" class="option" @change="onChange">
      <el-option v-for="item in languageList" :key="item" :label="item" :value="item"></el-option>
    </el-select>
    <el-select v-model="selectTabSize" class="option" @change="onChange">
      <el-option v-for="item in tabSizeList" :key="item" :label="item" :value="item"></el-option>
    </el-select>
    <codemirror ref="cm" v-model="text" :options="cmOptions" class="code" @input="codeChanges" />
  </div>
</template>

<script>
import 'codemirror/lib/codemirror.css'
import 'codemirror/mode/clike/clike'

export default {
  name: 'CodeEditor',
  data: function () {
    return {
      text: '',
      cmOptions: {
        tabSize: 4,
        mode: 'text/x-csrc',
        theme: 'default',
        lineNumbers: true,
        line: true,
        readOnly: false,
        smartIndent: true,
        matchBrackets: true,
        indentUnit: 4,
        autofocus: true
      },
      selectLanguage: 'C',
      selectTabSize: 4,
      languageList: ['C', 'C++', 'Java'],
      tabSizeList: [2, 4, 8],
      updateFlag: false,
      changes: null,

      showLocalVideo: false,
      showRemoteVideo: false,
      videoList: [],
      userDefineRoomId: '',
      msgList: [],
      chatMessage: ''
    }
  },
  props: {
    connection: {
      default: null,
      required: true
    },
    permission: {
      type: Boolean,
      default: false
    }
  },
  watch: {
    permission: {
      immediate: true,
      handler (val) {
        this.cmOptions.readOnly = !val
      }
    }
  },
  mounted: function () {
    const self = this
    setInterval(function () {
      if (self.updateFlag & self.permission) {
        const message = { type: 'code_update', data: self.changes }
        self.connection.send(message)
        self.updateFlag = false
      }
    }, 1000)
  },
  methods: {
    pushCurrentCode (userID) {
      if (userID === this.connection.userid) {
        const message = { type: 'code_update', data: this.text }
        this.connection.send(message)
      }
    },
    updateCode (data) {
      this.text = data
    },
    refresh () {
      this.$refs.cm.refresh()
    },
    codeChanges (changes) {
      this.updateFlag = true
      this.changes = changes
    },
    onChange () {
      switch (this.selectLanguage) {
        case 'C++':
          this.cmOptions.mode = 'text/x-c++src'
          break
        case 'Java':
          this.cmOptions.mode = 'text/x-java'
          break
        default:
          this.cmOptions.mode = 'text/x-csrc'
      }
      this.cmOptions.tabSize = this.selectTabSize
      this.cmOptions.indentUnit = this.selectTabSize
    }
  }
}
</script>
<style>
.document {
  min-height: 670px;
}
.cm-s-default.CodeMirror {
  min-height: 600px;
  font-size: 14px;
  border: 1px solid gainsboro;
}

.option {
  margin: 10px 30px 15px 15px;
  height: 35px;
  width: 100px;
}
</style>
