<template>
  <div
    id="FaceToFace-canvas"
    ref="canvasDiv"
    style="height:720px;"
  ></div>
</template>

<script>
import { CanvasDesigner } from '../../static/canvas-designer-widget.js'
export default {
  props: {
    connection: {
      default: null
    }
  },
  data: function () {
    return {
      designer: null
    }
  },
  mounted: function () {
    const designer = new CanvasDesigner()
    const that = this
    designer.widgetHtmlURL = '../../static/widget.html'
    designer.widgetJsURL = '../../static/widget.js'

    designer.addSyncListener(function (data) {
      that.connection.send({ type: 'canvas_data', canvasData: data })
    })
    designer.setSelected('pencil')

    designer.setTools({
      pencil: true,
      text: true,
      image: true,
      pdf: true,
      eraser: true,
      line: true,
      arrow: true,
      dragSingle: true,
      dragMultiple: true,
      arc: true,
      rectangle: true,
      quadratic: false,
      bezier: true,
      marker: true,
      zoom: false,
      lineWidth: false,
      colorsPicker: false,
      extraOptions: false,
      code: false,
      undo: true
    })
    designer.appendTo(document.getElementById('FaceToFace-canvas'))
    this.designer = designer
  },
  methods: {
    sync: function () {
      this.designer.sync()
    },
    syncData: function (event) {
      this.designer.syncData(event.data.canvasData)
    },
    getpointsLength: function () {
      return this.designer.pointsLength
    }
  }
}
</script>
