import Vue from 'vue'
// refer from https://segmentfault.com/a/1190000016040327
Vue.directive('dialogDrag', {
  bind (el, binding, vnode, oldVnode) {
    const dialogHeaderEl = el.querySelector('.el-dialog__header')
    const dragDom = el.querySelector('.el-dialog')
    dialogHeaderEl.style.cssText += ';cursor:move;'
    dragDom.style.cssText += ';top:0px;'

    const style = (function () {
      if (window.document.currentStyle) {
        return (dom, attr) => dom.currentStyle[attr]
      } else {
        return (dom, attr) => getComputedStyle(dom, false)[attr]
      }
    })()
    dialogHeaderEl.onmousedown = (event) => {
      // calculate distance

      const disX = event.clientX - dialogHeaderEl.offsetLeft
      const disY = event.clientY - dialogHeaderEl.offsetTop
      // calculate height and width
      const screenWidth = document.body.clientWidth
      const screenHeight = document.documentElement.clientHeight
      const dragDomWidth = dragDom.offsetWidth
      const dragDomHeight = dragDom.offsetHeight

      const minDragDomLeft = dragDom.offsetLeft
      const maxDragDomLeft = screenWidth - dragDom.offsetLeft - dragDomWidth

      const minDragDomTop = dragDom.offsetTop
      const maxDragDomTop = screenHeight - dragDom.offsetTop - dragDomHeight

      let styleLeft = style(dragDom, 'left')
      let styleTop = style(dragDom, 'top')

      if (styleLeft.includes('%')) {
        // in this case, we convert 50% to 0.5*clientWidth
        // '+' convert string to int
        const Hundred = 100
        styleLeft = +document.body.clientWidth * (+styleLeft.replace(/%/g, '') / Hundred)
        styleTop = +document.body.clientHeight * (+styleTop.replace(/%/g, '') / Hundred)
      } else {
        styleLeft = +styleLeft.replace(/px/g, '')
        styleTop = +styleTop.replace(/px/g, '')
      }

      document.onmousemove = function (event) {
        let left = event.clientX - disX
        let top = event.clientY - disY

        if (-(left) > minDragDomLeft) {
          left = -minDragDomLeft
        } else if (left > maxDragDomLeft) {
          left = maxDragDomLeft
        }

        if (-(top) > minDragDomTop) {
          top = -minDragDomTop
        } else if (top > maxDragDomTop) {
          top = maxDragDomTop
        }

        dragDom.style.cssText += `;left:${left + styleLeft}px;top:${top + styleTop}px`
      }

      document.onmouseup = function (event) {
        document.onmousemove = null
        document.onmouseup = null
      }
    }
  }
})
