(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pagesA-test-detail"],{"08aa":function(t,e,n){"use strict";n.r(e);var a=n("8b9f"),i=n.n(a);for(var r in a)["default"].indexOf(r)<0&&function(t){n.d(e,t,(function(){return a[t]}))}(r);e["default"]=i.a},"08c4":function(t,e,n){"use strict";n("7a82"),Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var a={props:{bgColor:{type:String,default:uni.$u.props.statusBar.bgColor}}};e.default=a},"0c2f":function(t,e,n){var a=n("4a07");"string"===typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);var i=n("4f06").default;i("a28a1d40",a,!0,{sourceMap:!1,shadowMode:!1})},"2a54":function(t,e,n){"use strict";n.d(e,"b",(function(){return i})),n.d(e,"c",(function(){return r})),n.d(e,"a",(function(){return a}));var a={uStatusBar:n("3ad6").default,uIcon:n("f86b").default},i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("v-uni-view",{staticClass:"u-navbar"},[t.fixed&&t.placeholder?n("v-uni-view",{staticClass:"u-navbar__placeholder",style:{height:t.$u.addUnit(t.$u.getPx(t.height)+t.$u.sys().statusBarHeight,"px")}}):t._e(),n("v-uni-view",{class:[t.fixed&&"u-navbar--fixed"]},[t.safeAreaInsetTop?n("u-status-bar",{attrs:{bgColor:t.bgColor}}):t._e(),n("v-uni-view",{staticClass:"u-navbar__content",class:[t.border&&"u-border-bottom"],style:{height:t.$u.addUnit(t.height),backgroundColor:t.bgColor}},[n("v-uni-view",{staticClass:"u-navbar__content__left",attrs:{"hover-class":"u-navbar__content__left--hover","hover-start-time":"150"},on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.leftClick.apply(void 0,arguments)}}},[t._t("left",[t.leftIcon?n("u-icon",{attrs:{name:t.leftIcon,size:t.leftIconSize,color:t.leftIconColor}}):t._e(),t.leftText?n("v-uni-text",{staticClass:"u-navbar__content__left__text",style:{color:t.leftIconColor}},[t._v(t._s(t.leftText))]):t._e()])],2),t._t("center",[n("v-uni-text",{staticClass:"u-line-1 u-navbar__content__title",style:[{width:t.$u.addUnit(t.titleWidth)},t.$u.addStyle(t.titleStyle)]},[t._v(t._s(t.title))])]),t.$slots.right||t.rightIcon||t.rightText?n("v-uni-view",{staticClass:"u-navbar__content__right",on:{click:function(e){arguments[0]=e=t.$handleEvent(e),t.rightClick.apply(void 0,arguments)}}},[t._t("right",[t.rightIcon?n("u-icon",{attrs:{name:t.rightIcon,size:"20"}}):t._e(),t.rightText?n("v-uni-text",{staticClass:"u-navbar__content__right__text"},[t._v(t._s(t.rightText))]):t._e()])],2):t._e()],2)],1)],1)},r=[]},"2e5d":function(t,e,n){"use strict";n("7a82"),Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0,n("a9e3");var a={props:{safeAreaInsetTop:{type:Boolean,default:uni.$u.props.navbar.safeAreaInsetTop},placeholder:{type:Boolean,default:uni.$u.props.navbar.placeholder},fixed:{type:Boolean,default:uni.$u.props.navbar.fixed},border:{type:Boolean,default:uni.$u.props.navbar.border},leftIcon:{type:String,default:uni.$u.props.navbar.leftIcon},leftText:{type:String,default:uni.$u.props.navbar.leftText},rightText:{type:String,default:uni.$u.props.navbar.rightText},rightIcon:{type:String,default:uni.$u.props.navbar.rightIcon},title:{type:[String,Number],default:uni.$u.props.navbar.title},bgColor:{type:String,default:uni.$u.props.navbar.bgColor},titleWidth:{type:[String,Number],default:uni.$u.props.navbar.titleWidth},height:{type:[String,Number],default:uni.$u.props.navbar.height},leftIconSize:{type:[String,Number],default:uni.$u.props.navbar.leftIconSize},leftIconColor:{type:String,default:uni.$u.props.navbar.leftIconColor},autoBack:{type:Boolean,default:uni.$u.props.navbar.autoBack},titleStyle:{type:[String,Object],default:uni.$u.props.navbar.titleStyle}}};e.default=a},"323c":function(t,e,n){"use strict";n("7a82"),Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var a={data:function(){return{}},onLoad:function(){},methods:{back:function(){uni.navigateBack({})}}};e.default=a},3288:function(t,e,n){"use strict";n.r(e);var a=n("ed12"),i=n("3f83");for(var r in i)["default"].indexOf(r)<0&&function(t){n.d(e,t,(function(){return i[t]}))}(r);var u,o=n("f0c5"),l=Object(o["a"])(i["default"],a["b"],a["c"],!1,null,"1e19b893",null,!1,a["a"],u);e["default"]=l.exports},"3ad6":function(t,e,n){"use strict";n.r(e);var a=n("9da9"),i=n("08aa");for(var r in i)["default"].indexOf(r)<0&&function(t){n.d(e,t,(function(){return i[t]}))}(r);n("ac27");var u,o=n("f0c5"),l=Object(o["a"])(i["default"],a["b"],a["c"],!1,null,"38b9df1a",null,!1,a["a"],u);e["default"]=l.exports},"3f83":function(t,e,n){"use strict";n.r(e);var a=n("323c"),i=n.n(a);for(var r in a)["default"].indexOf(r)<0&&function(t){n.d(e,t,(function(){return a[t]}))}(r);e["default"]=i.a},"4a07":function(t,e,n){var a=n("4bad");e=a(!1),e.push([t.i,'@charset "UTF-8";\n/**\n * 这里是uni-app内置的常用样式变量\n *\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\n *\n */\n/**\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\n *\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\n */\n/* 颜色变量 */\n/* 行为相关颜色 */\n/* 文字基本颜色 */\n/* 背景颜色 */\n/* 边框颜色 */\n/* 尺寸变量 */\n/* 文字尺寸 */\n/* 图片尺寸 */\n/* Border Radius */\n/* 水平间距 */\n/* 垂直间距 */\n/* 透明度 */\n/* 文章场景相关 */html[data-v-38b9df1a],\nbody[data-v-38b9df1a]{height:100%;color:#000;font-size:%?28?%}uni-view[data-v-38b9df1a], *[data-v-38b9df1a]{padding:0;margin:0;box-sizing:border-box;-webkit-tap-highlight-color:rgba(0,0,0,0)}.u-status-bar[data-v-38b9df1a]{width:100%}',""]),t.exports=e},"62f2":function(t,e,n){"use strict";n("7a82");var a=n("4ea4");Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var i=a(n("2e5d")),r={name:"u-navbar",mixins:[uni.$u.mpMixin,uni.$u.mixin,i.default],data:function(){return{}},methods:{leftClick:function(){this.$emit("leftClick"),this.autoBack&&uni.navigateBack()},rightClick:function(){this.$emit("rightClick")}}};e.default=r},8252:function(t,e,n){"use strict";var a=n("efd8"),i=n.n(a);i.a},"87cd":function(t,e,n){"use strict";n.r(e);var a=n("2a54"),i=n("d54d");for(var r in i)["default"].indexOf(r)<0&&function(t){n.d(e,t,(function(){return i[t]}))}(r);n("8252");var u,o=n("f0c5"),l=Object(o["a"])(i["default"],a["b"],a["c"],!1,null,"1658ae1f",null,!1,a["a"],u);e["default"]=l.exports},"8b9f":function(t,e,n){"use strict";n("7a82");var a=n("4ea4");Object.defineProperty(e,"__esModule",{value:!0}),e.default=void 0;var i=a(n("08c4")),r={name:"u-status-bar",mixins:[uni.$u.mpMixin,uni.$u.mixin,i.default],data:function(){return{}},computed:{style:function(){var t={};return t.height=uni.$u.addUnit(uni.$u.sys().statusBarHeight,"px"),t.backgroundColor=this.bgColor,uni.$u.deepMerge(t,uni.$u.addStyle(this.customStyle))}}};e.default=r},"9da9":function(t,e,n){"use strict";var a;n.d(e,"b",(function(){return i})),n.d(e,"c",(function(){return r})),n.d(e,"a",(function(){return a}));var i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("v-uni-view",{staticClass:"u-status-bar",style:[t.style]},[t._t("default")],2)},r=[]},a72a:function(t,e,n){var a=n("4bad");e=a(!1),e.push([t.i,'@charset "UTF-8";\n/**\n * 这里是uni-app内置的常用样式变量\n *\n * uni-app 官方扩展插件及插件市场（https://ext.dcloud.net.cn）上很多三方插件均使用了这些样式变量\n * 如果你是插件开发者，建议你使用scss预处理，并在插件代码中直接使用这些变量（无需 import 这个文件），方便用户通过搭积木的方式开发整体风格一致的App\n *\n */\n/**\n * 如果你是App开发者（插件使用者），你可以通过修改这些变量来定制自己的插件主题，实现自定义主题功能\n *\n * 如果你的项目同样使用了scss预处理，你也可以直接在你的 scss 代码中使用如下变量，同时无需 import 这个文件\n */\n/* 颜色变量 */\n/* 行为相关颜色 */\n/* 文字基本颜色 */\n/* 背景颜色 */\n/* 边框颜色 */\n/* 尺寸变量 */\n/* 文字尺寸 */\n/* 图片尺寸 */\n/* Border Radius */\n/* 水平间距 */\n/* 垂直间距 */\n/* 透明度 */\n/* 文章场景相关 */html[data-v-1658ae1f],\nbody[data-v-1658ae1f]{height:100%;color:#000;font-size:%?28?%}uni-view[data-v-1658ae1f], *[data-v-1658ae1f]{padding:0;margin:0;box-sizing:border-box;-webkit-tap-highlight-color:rgba(0,0,0,0)}uni-view[data-v-1658ae1f], uni-scroll-view[data-v-1658ae1f], uni-swiper-item[data-v-1658ae1f]{display:flex;flex-direction:column;flex-shrink:0;flex-grow:0;flex-basis:auto;align-items:stretch;align-content:flex-start}.u-navbar--fixed[data-v-1658ae1f]{position:fixed;left:0;right:0;top:0;z-index:11}.u-navbar__content[data-v-1658ae1f]{\ndisplay:flex;\nflex-direction:row;align-items:center;height:44px;background-color:#9acafc;position:relative;justify-content:center}.u-navbar__content__left[data-v-1658ae1f], .u-navbar__content__right[data-v-1658ae1f]{padding:0 13px;position:absolute;top:0;bottom:0;\ndisplay:flex;\nflex-direction:row;align-items:center}.u-navbar__content__left[data-v-1658ae1f]{left:0}.u-navbar__content__left--hover[data-v-1658ae1f]{opacity:.7}.u-navbar__content__left__text[data-v-1658ae1f]{font-size:15px;margin-left:3px}.u-navbar__content__title[data-v-1658ae1f]{text-align:center;font-size:16px;color:#303133}.u-navbar__content__right[data-v-1658ae1f]{right:0}.u-navbar__content__right__text[data-v-1658ae1f]{font-size:15px;margin-left:3px}',""]),t.exports=e},ac27:function(t,e,n){"use strict";var a=n("0c2f"),i=n.n(a);i.a},d54d:function(t,e,n){"use strict";n.r(e);var a=n("62f2"),i=n.n(a);for(var r in a)["default"].indexOf(r)<0&&function(t){n.d(e,t,(function(){return a[t]}))}(r);e["default"]=i.a},ed12:function(t,e,n){"use strict";n.d(e,"b",(function(){return i})),n.d(e,"c",(function(){return r})),n.d(e,"a",(function(){return a}));var a={uNavbar:n("87cd").default,uButton:n("d9ad").default},i=function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("v-uni-view",{staticClass:"api-detail-wrapper"},[n("u-navbar",{attrs:{title:"测试详情",placeholder:!0,autoBack:!0}}),n("v-uni-view",[n("u-button",{attrs:{type:"primary",text:"测试详情页面"}})],1)],1)},r=[]},efd8:function(t,e,n){var a=n("a72a");"string"===typeof a&&(a=[[t.i,a,""]]),a.locals&&(t.exports=a.locals);var i=n("4f06").default;i("34a291a5",a,!0,{sourceMap:!1,shadowMode:!1})}}]);