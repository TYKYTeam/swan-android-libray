(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["pagesA-webview-index"],{"42f1":function(e,n,t){"use strict";(function(e){t("7a82"),Object.defineProperty(n,"__esModule",{value:!0}),n.default=void 0;var i={data:function(){return{webUrl:""}},onLoad:function(e){e.webUrl&&(this.webUrl=decodeURIComponent(e.webUrl),uni.hideHomeButton())},methods:{onPostMessage:function(n){e.log("webview收到信息",n.detail.data)}},created:function(){uni.hideNavigationBarLoading(),uni.hideHomeButton()}};n.default=i}).call(this,t("5a52")["default"])},b94f:function(e,n,t){"use strict";t.r(n);var i=t("42f1"),a=t.n(i);for(var u in i)["default"].indexOf(u)<0&&function(e){t.d(n,e,(function(){return i[e]}))}(u);n["default"]=a.a},c3a3:function(e,n,t){"use strict";var i;t.d(n,"b",(function(){return a})),t.d(n,"c",(function(){return u})),t.d(n,"a",(function(){return i}));var a=function(){var e=this,n=e.$createElement,t=e._self._c||n;return t("v-uni-view",{staticClass:"webview-"},[e.webUrl?t("v-uni-web-view",{attrs:{src:e.webUrl},on:{message:function(n){arguments[0]=n=e.$handleEvent(n),e.onPostMessage.apply(void 0,arguments)}}}):e._e()],1)},u=[]},cdb1:function(e,n,t){"use strict";t.r(n);var i=t("c3a3"),a=t("b94f");for(var u in a)["default"].indexOf(u)<0&&function(e){t.d(n,e,(function(){return a[e]}))}(u);var o,r=t("f0c5"),c=Object(r["a"])(a["default"],i["b"],i["c"],!1,null,"15632566",null,!1,i["a"],o);n["default"]=c.exports}}]);