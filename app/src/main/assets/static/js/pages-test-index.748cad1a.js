(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pages-test-index"],{"02a2":function(e,t,n){"use strict";n.d(t,"b",(function(){return l})),n.d(t,"c",(function(){return a})),n.d(t,"a",(function(){return i}));var i={uLine:n("ee53").default},l=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("v-uni-view",{staticClass:"u-cell-group",class:[e.customClass],style:[e.$u.addStyle(e.customStyle)]},[e.title?n("v-uni-view",{staticClass:"u-cell-group__title"},[e._t("title",[n("v-uni-text",{staticClass:"u-cell-group__title__text"},[e._v(e._s(e.title))])])],2):e._e(),n("v-uni-view",{staticClass:"u-cell-group__wrapper"},[e.border?n("u-line"):e._e(),e._t("default")],2)],1)},a=[]},"24ae":function(e,t,n){"use strict";n.r(t);var i=n("8771"),l=n.n(i);for(var a in i)["default"].indexOf(a)<0&&function(e){n.d(t,e,(function(){return i[e]}))}(a);t["default"]=l.a},"2d8b":function(e,t,n){"use strict";n.r(t);var i=n("7e30"),l=n("5f8a");for(var a in l)["default"].indexOf(a)<0&&function(e){n.d(t,e,(function(){return l[e]}))}(a);n("7e55");var r,u=n("f0c5"),o=Object(u["a"])(l["default"],i["b"],i["c"],!1,null,"4da41000",null,!1,i["a"],r);t["default"]=o.exports},"2e47":function(e,t,n){"use strict";var i=n("4b3b"),l=n.n(i);l.a},3805:function(e,t,n){"use strict";n("7a82"),Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0,n("a9e3");var i={props:{color:{type:String,default:uni.$u.props.line.color},length:{type:[String,Number],default:uni.$u.props.line.length},direction:{type:String,default:uni.$u.props.line.direction},hairline:{type:Boolean,default:uni.$u.props.line.hairline},margin:{type:[String,Number],default:uni.$u.props.line.margin},dashed:{type:Boolean,default:uni.$u.props.line.dashed}}};t.default=i},"399f":function(e,t,n){"use strict";n.r(t);var i=n("b785"),l=n.n(i);for(var a in i)["default"].indexOf(a)<0&&function(e){n.d(t,e,(function(){return i[e]}))}(a);t["default"]=l.a},4215:function(e,t,n){"use strict";n.d(t,"b",(function(){return l})),n.d(t,"c",(function(){return a})),n.d(t,"a",(function(){return i}));var i={uIcon:n("f86b").default,uLine:n("ee53").default},l=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("v-uni-view",{staticClass:"u-cell",class:[e.customClass],style:[e.$u.addStyle(e.customStyle)],attrs:{"hover-class":e.disabled||!e.clickable&&!e.isLink?"":"u-cell--clickable","hover-stay-time":250},on:{click:function(t){arguments[0]=t=e.$handleEvent(t),e.clickHandler.apply(void 0,arguments)}}},[n("v-uni-view",{staticClass:"u-cell__body",class:[e.center&&"u-cell--center","large"===e.size&&"u-cell__body--large"]},[n("v-uni-view",{staticClass:"u-cell__body__content"},[e.$slots.icon||e.icon?n("v-uni-view",{staticClass:"u-cell__left-icon-wrap"},[e.$slots.icon?e._t("icon"):n("u-icon",{attrs:{name:e.icon,"custom-style":e.iconStyle,size:"large"===e.size?22:18}})],2):e._e(),n("v-uni-view",{staticClass:"u-cell__title"},[e._t("title",[e.title?n("v-uni-text",{staticClass:"u-cell__title-text",class:[e.disabled&&"u-cell--disabled","large"===e.size&&"u-cell__title-text--large"],style:[e.titleTextStyle]},[e._v(e._s(e.title))]):e._e()]),e._t("label",[e.label?n("v-uni-text",{staticClass:"u-cell__label",class:[e.disabled&&"u-cell--disabled","large"===e.size&&"u-cell__label--large"]},[e._v(e._s(e.label))]):e._e()])],2)],1),e._t("value",[e.$u.test.empty(e.value)?e._e():n("v-uni-text",{staticClass:"u-cell__value",class:[e.disabled&&"u-cell--disabled","large"===e.size&&"u-cell__value--large"]},[e._v(e._s(e.value))])]),e.$slots["right-icon"]||e.isLink?n("v-uni-view",{staticClass:"u-cell__right-icon-wrap",class:["u-cell__right-icon-wrap--"+e.arrowDirection]},[e.$slots["right-icon"]?e._t("right-icon"):n("u-icon",{attrs:{name:e.rightIcon,"custom-style":e.rightIconStyle,color:e.disabled?"#c8c9cc":"info",size:"large"===e.size?18:16}})],2):e._e()],2),e.border?n("u-line"):e._e()],1)},a=[]},"4b3b":function(e,t,n){var i=n("a34b");"string"===typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);var l=n("4f06").default;l("ac371e7a",i,!0,{sourceMap:!1,shadowMode:!1})},5111:function(e,t,n){"use strict";n("7a82");var i=n("4ea4");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var l=i(n("d5b0")),a={name:"u-cell",data:function(){return{}},mixins:[uni.$u.mpMixin,uni.$u.mixin,l.default],computed:{titleTextStyle:function(){return uni.$u.addStyle(this.titleStyle)}},methods:{clickHandler:function(e){this.disabled||(this.$emit("click",{name:this.name}),this.openPage(),this.stop&&this.preventEvent(e))}}};t.default=a},"5b03":function(e,t,n){"use strict";n.r(t);var i=n("4215"),l=n("d4ae");for(var a in l)["default"].indexOf(a)<0&&function(e){n.d(t,e,(function(){return l[e]}))}(a);n("9114");var r,u=n("f0c5"),o=Object(u["a"])(l["default"],i["b"],i["c"],!1,null,"4d47c3fb",null,!1,i["a"],r);t["default"]=o.exports},"5ed1":function(e,t,n){"use strict";n("7a82");var i=n("4ea4");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0,n("96cf");var l=i(n("1da1")),a={data:function(){return{menusList:[{id:2,name:"media媒体",children:[{api:"copyTextToClipboard",data:{name:null,content:"hello world"},name:"复制文本",id:"2-1"},{name:"获取文本",id:"2-2"},{name:"拨打电话",id:"2-3"},{api:"sendSms",data:{phone:"1008611",content:"hello world"},name:"发送短信",id:"2-4"},{name:"图片预览(base64)",id:"2-5"},{name:"图片预览",id:"2-6"},{name:"扫描二维码",id:"2-7",api:"qrScan"},{name:"文字转语音",id:"2-8"}]},{id:3,name:"通知",children:[{api:"sendNotification",data:{title:"标题",content:"hello world"},name:"发送通知",id:"3-1"}]},{id:4,name:"share分享",children:[{name:"发送通知",id:"4-1"}]},{id:5,name:"share分享",children:[{name:"发送通知",id:"5-1"}]},{id:6,name:"share分享",children:[{name:"发送通知",id:"6-1"}]}]}},onLoad:function(){console.log("router",this.$Route)},methods:{chooseIkmage:function(){uni.chooseImage({count:6,sizeType:["original","compressed"],sourceType:["album"],success:function(e){console.log("success res",e)}}).then((function(e){console.log("then  res",e)}))},handleTestApi:function(e){var t=this;return(0,l.default)(regeneratorRuntime.mark((function n(){return regeneratorRuntime.wrap((function(n){while(1)switch(n.prev=n.next){case 0:console.log("调用api",e.api||null),console.log("参数data",e.data||null),e.api&&(e.data?t.$swan[e.api]({data:e.data,success:function(e){console.log("success",e)},fail:function(e){console.log("error",e)},complete:function(){console.log("complete")}}):t.$swan[e.api]({success:function(e){console.log("success",e)},fail:function(e){console.log("error",e)},complete:function(){console.log("complete")}}));case 3:case"end":return n.stop()}}),n)})))()},back:function(){uni.navigateBack({})}}};t.default=a},"5f8a":function(e,t,n){"use strict";n.r(t);var i=n("5ed1"),l=n.n(i);for(var a in i)["default"].indexOf(a)<0&&function(e){n.d(t,e,(function(){return i[e]}))}(a);t["default"]=l.a},"757f":function(e,t,n){var i=n("97a3");"string"===typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);var l=n("4f06").default;l("56660fce",i,!0,{sourceMap:!1,shadowMode:!1})},"7e30":function(e,t,n){"use strict";n.d(t,"b",(function(){return l})),n.d(t,"c",(function(){return a})),n.d(t,"a",(function(){return i}));var i={uNavbar:n("87cd").default,uCellGroup:n("c149").default,uCell:n("5b03").default},l=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("v-uni-view",{staticClass:"api-test-wrapper"},[n("u-navbar",{attrs:{title:"api测试页面",placeholder:!0,autoBack:!0}}),e._l(e.menusList,(function(t){return n("u-cell-group",{key:t.id,attrs:{title:t.name,border:!0}},e._l(t.children,(function(t){return n("u-cell",{key:t.id,attrs:{icon:"share",title:t.name,"is-link":!0},on:{click:function(n){arguments[0]=n=e.$handleEvent(n),e.handleTestApi(t)}}})})),1)}))],2)},a=[]},"7e55":function(e,t,n){"use strict";var i=n("9480"),l=n.n(i);l.a},"816b":function(e,t,n){var i=n("e42a");"string"===typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);var l=n("4f06").default;l("cccaa382",i,!0,{sourceMap:!1,shadowMode:!1})},8771:function(e,t,n){"use strict";n("7a82");var i=n("4ea4");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var l=i(n("3805")),a={name:"u-line",mixins:[uni.$u.mpMixin,uni.$u.mixin,l.default],computed:{lineStyle:function(){var e={};return e.margin=this.margin,"row"===this.direction?(e.borderBottomWidth="1px",e.borderBottomStyle=this.dashed?"dashed":"solid",e.width=uni.$u.addUnit(this.length),this.hairline&&(e.transform="scaleY(0.5)")):(e.borderLeftWidth="1px",e.borderLeftStyle=this.dashed?"dashed":"solid",e.height=uni.$u.addUnit(this.length),this.hairline&&(e.transform="scaleX(0.5)")),e.borderColor=this.color,uni.$u.deepMerge(e,uni.$u.addStyle(this.customStyle))}}};t.default=a},9114:function(e,t,n){"use strict";var i=n("757f"),l=n.n(i);l.a},9480:function(e,t,n){var i=n("cb8f");"string"===typeof i&&(i=[[e.i,i,""]]),i.locals&&(e.exports=i.locals);var l=n("4f06").default;l("d15224b2",i,!0,{sourceMap:!1,shadowMode:!1})},"97a3":function(e,t,n){var i=n("4bad");t=i(!1),t.push([e.i,'@charset "UTF-8";\n/**\n * 这里是uni-app内置的常用样式变量\n *\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\n *\n */\n/**\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\n *\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\n */\n/* 颜色变量 */\n/* 行为相关颜色 */\n/* 文字基本颜色 */\n/* 背景颜色 */\n/* 边框颜色 */\n/* 尺寸变量 */\n/* 文字尺寸 */\n/* 图片尺寸 */\n/* Border Radius */\n/* 水平间距 */\n/* 垂直间距 */\n/* 透明度 */\n/* 文章场景相关 */uni-view[data-v-4d47c3fb], uni-scroll-view[data-v-4d47c3fb], uni-swiper-item[data-v-4d47c3fb]{display:flex;flex-direction:column;flex-shrink:0;flex-grow:0;flex-basis:auto;align-items:stretch;align-content:flex-start}.u-cell__body[data-v-4d47c3fb]{display:flex;flex-direction:row;box-sizing:border-box;padding:10px 15px;font-size:15px;color:#303133;align-items:center}.u-cell__body__content[data-v-4d47c3fb]{display:flex;flex-direction:row;align-items:center;flex:1}.u-cell__body--large[data-v-4d47c3fb]{padding-top:13px;padding-bottom:13px}.u-cell__left-icon-wrap[data-v-4d47c3fb], .u-cell__right-icon-wrap[data-v-4d47c3fb]{display:flex;flex-direction:row;align-items:center;font-size:16px}.u-cell__left-icon-wrap[data-v-4d47c3fb]{margin-right:4px}.u-cell__right-icon-wrap[data-v-4d47c3fb]{margin-left:4px;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s}.u-cell__right-icon-wrap--up[data-v-4d47c3fb]{-webkit-transform:rotate(-90deg);transform:rotate(-90deg)}.u-cell__right-icon-wrap--down[data-v-4d47c3fb]{-webkit-transform:rotate(90deg);transform:rotate(90deg)}.u-cell__title[data-v-4d47c3fb]{flex:1}.u-cell__title-text[data-v-4d47c3fb]{font-size:15px;line-height:22px;color:#303133}.u-cell__title-text--large[data-v-4d47c3fb]{font-size:16px}.u-cell__label[data-v-4d47c3fb]{margin-top:5px;font-size:12px;color:#909193;line-height:18px}.u-cell__label--large[data-v-4d47c3fb]{font-size:14px}.u-cell__value[data-v-4d47c3fb]{text-align:right;font-size:14px;line-height:24px;color:#606266}.u-cell__value--large[data-v-4d47c3fb]{font-size:15px}.u-cell--clickable[data-v-4d47c3fb]{background-color:#f3f4f6}.u-cell--disabled[data-v-4d47c3fb]{color:#c8c9cc;cursor:not-allowed}.u-cell--center[data-v-4d47c3fb]{align-items:center}',""]),e.exports=t},a34b:function(e,t,n){var i=n("4bad");t=i(!1),t.push([e.i,'@charset "UTF-8";\n/**\n * 这里是uni-app内置的常用样式变量\n *\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\n *\n */\n/**\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\n *\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\n */\n/* 颜色变量 */\n/* 行为相关颜色 */\n/* 文字基本颜色 */\n/* 背景颜色 */\n/* 边框颜色 */\n/* 尺寸变量 */\n/* 文字尺寸 */\n/* 图片尺寸 */\n/* Border Radius */\n/* 水平间距 */\n/* 垂直间距 */\n/* 透明度 */\n/* 文章场景相关 */uni-view[data-v-61315bde], uni-scroll-view[data-v-61315bde], uni-swiper-item[data-v-61315bde]{display:flex;flex-direction:column;flex-shrink:0;flex-grow:0;flex-basis:auto;align-items:stretch;align-content:flex-start}.u-cell-group[data-v-61315bde]{flex:1}.u-cell-group__title[data-v-61315bde]{padding:16px 16px 8px}.u-cell-group__title__text[data-v-61315bde]{font-size:15px;line-height:16px;color:#303133}.u-cell-group__wrapper[data-v-61315bde]{position:relative}',""]),e.exports=t},b785:function(e,t,n){"use strict";n("7a82");var i=n("4ea4");Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var l=i(n("d62d")),a={name:"u-cell-group",mixins:[uni.$u.mpMixin,uni.$u.mixin,l.default]};t.default=a},c149:function(e,t,n){"use strict";n.r(t);var i=n("02a2"),l=n("399f");for(var a in l)["default"].indexOf(a)<0&&function(e){n.d(t,e,(function(){return l[e]}))}(a);n("2e47");var r,u=n("f0c5"),o=Object(u["a"])(l["default"],i["b"],i["c"],!1,null,"61315bde",null,!1,i["a"],r);t["default"]=o.exports},cb8f:function(e,t,n){var i=n("4bad");t=i(!1),t.push([e.i,'@charset "UTF-8";\n/**\n * 这里是uni-app内置的常用样式变量\n *\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\n *\n */\n/**\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\n *\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\n */\n/* 颜色变量 */\n/* 行为相关颜色 */\n/* 文字基本颜色 */\n/* 背景颜色 */\n/* 边框颜色 */\n/* 尺寸变量 */\n/* 文字尺寸 */\n/* 图片尺寸 */\n/* Border Radius */\n/* 水平间距 */\n/* 垂直间距 */\n/* 透明度 */\n/* 文章场景相关 */.api-test-wrapper[data-v-4da41000]  .u-cell-group__title__text{font-weight:700}',""]),e.exports=t},d4ae:function(e,t,n){"use strict";n.r(t);var i=n("5111"),l=n.n(i);for(var a in i)["default"].indexOf(a)<0&&function(e){n.d(t,e,(function(){return i[e]}))}(a);t["default"]=l.a},d5b0:function(e,t,n){"use strict";n("7a82"),Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0,n("a9e3");var i={props:{title:{type:[String,Number],default:uni.$u.props.cell.title},label:{type:[String,Number],default:uni.$u.props.cell.label},value:{type:[String,Number],default:uni.$u.props.cell.value},icon:{type:String,default:uni.$u.props.cell.icon},disabled:{type:Boolean,default:uni.$u.props.cell.disabled},border:{type:Boolean,default:uni.$u.props.cell.border},center:{type:Boolean,default:uni.$u.props.cell.center},url:{type:String,default:uni.$u.props.cell.url},linkType:{type:String,default:uni.$u.props.cell.linkType},clickable:{type:Boolean,default:uni.$u.props.cell.clickable},isLink:{type:Boolean,default:uni.$u.props.cell.isLink},required:{type:Boolean,default:uni.$u.props.cell.required},rightIcon:{type:String,default:uni.$u.props.cell.rightIcon},arrowDirection:{type:String,default:uni.$u.props.cell.arrowDirection},iconStyle:{type:[Object,String],default:function(){return uni.$u.props.cell.iconStyle}},rightIconStyle:{type:[Object,String],default:function(){return uni.$u.props.cell.rightIconStyle}},titleStyle:{type:[Object,String],default:function(){return uni.$u.props.cell.titleStyle}},size:{type:String,default:uni.$u.props.cell.size},stop:{type:Boolean,default:uni.$u.props.cell.stop},name:{type:[Number,String],default:uni.$u.props.cell.name}}};t.default=i},d62d:function(e,t,n){"use strict";n("7a82"),Object.defineProperty(t,"__esModule",{value:!0}),t.default=void 0;var i={props:{title:{type:String,default:uni.$u.props.cellGroup.title},border:{type:Boolean,default:uni.$u.props.cellGroup.border}}};t.default=i},e0b4:function(e,t,n){"use strict";var i;n.d(t,"b",(function(){return l})),n.d(t,"c",(function(){return a})),n.d(t,"a",(function(){return i}));var l=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("v-uni-view",{staticClass:"u-line",style:[e.lineStyle]})},a=[]},e42a:function(e,t,n){var i=n("4bad");t=i(!1),t.push([e.i,'@charset "UTF-8";\n/**\n * 这里是uni-app内置的常用样式变量\n *\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\n *\n */\n/**\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\n *\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\n */\n/* 颜色变量 */\n/* 行为相关颜色 */\n/* 文字基本颜色 */\n/* 背景颜色 */\n/* 边框颜色 */\n/* 尺寸变量 */\n/* 文字尺寸 */\n/* 图片尺寸 */\n/* Border Radius */\n/* 水平间距 */\n/* 垂直间距 */\n/* 透明度 */\n/* 文章场景相关 */uni-view[data-v-09e9487a], uni-scroll-view[data-v-09e9487a], uni-swiper-item[data-v-09e9487a]{display:flex;flex-direction:column;flex-shrink:0;flex-grow:0;flex-basis:auto;align-items:stretch;align-content:flex-start}.u-line[data-v-09e9487a]{vertical-align:middle}',""]),e.exports=t},eb8f:function(e,t,n){"use strict";var i=n("816b"),l=n.n(i);l.a},ee53:function(e,t,n){"use strict";n.r(t);var i=n("e0b4"),l=n("24ae");for(var a in l)["default"].indexOf(a)<0&&function(e){n.d(t,e,(function(){return l[e]}))}(a);n("eb8f");var r,u=n("f0c5"),o=Object(u["a"])(l["default"],i["b"],i["c"],!1,null,"09e9487a",null,!1,i["a"],r);t["default"]=o.exports}}]);