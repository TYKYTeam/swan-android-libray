# Android 组件库

[![](https://jitpack.io//v/stars-one/android-component-libray.svg)](https://github.com/stars-one/android-component-libray)
![](https://img.shields.io/badge/java-8-orange)
![](https://img.shields.io/badge/androix-8e24aa)

Android原生组件库，为Html提供Android原生能力，增强H5

<meta name="referrer" content="no-referrer">

## Module目录及引用说明

|module名	|必选	|描述														|
|--			|--		|--															|
|webViewBase|√		|核心库，包含文件上传										|
|update		|×		|自动更新库													|
|media		|×		|媒体库，包含拨打电话，发短信，拍照，录制视频等功能			|
|listener	|×		|监听回调类的开源库，包含来电监听，网络状态监听，返回键监听	|
|share		|×		|分享库，包含QQ，微信等第三方社交软件的分享					|


依赖引入（需要添加jitpack仓库源）：
```
implementation 'com.github.stars-one.android-component-libray:module名:版本号'
```

例：
```
implementation 'com.github.stars-one.android-component-libray:0.2'
```

**注：以下使用的传参都是需要Json的字符串，需要调用`JSON.stringify()`方法将对象转为字符串**

## webViewBase使用
提供一个CustomWebViewActivity，首页的MainActivity需要继承于此Activity，之后再onCreate方法中调用以下两种方法之一，来加载url

- `loadLocalUrl` 设置加载的url并加载
- `loadWebUrl` 加载url

> **PS：loadLocalUrl主要是自动拼接了assets里的本地资源html文件，如下图**

![](https://img2022.cnblogs.com/blog/1210268/202202/1210268-20220208150005668-1077064887.png)

## media使用

### 1.获取剪切板内容

`getTextFromClipboard()`

用来获取手机剪切板的文本内容

**传参：无需传参**

**返回结果：**
```
{"code":200,"desc":"","result":"剪切板的内容"}
```

**H5调用示例：**
```
if (window.android_media) {
    let result = window.android_media.getTextFromClipboard()
    let text = JSON.parse(result).result
    console.log(text)
}
```
### 2.复制文本

`copyTextToClipboard(paramStr)` 

往手机的剪切板写入内容，实现H5复制功能

**传参：**
```
{
    content:"hello world"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.android_media) {
    let content = {content:"hello world"}
    let result = window.android_media.copyTextToClipboard(JSON.stringify(content))
    console.log(result);
}
```

### 3.拨打电话
`callPhone(paramStr)` 

拨打电话，会直接拨号

> 使用前需要声明电话权限，方法内已作了动态权限申请适配
```
<uses-permission android:name="android.permission.CALL_PHONE" />
```

**传参：**
```
{
    phone:"10086"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.android_media) {
    let content = {
        phone: "10086"
    }
    let result = window.android_media.callPhone(JSON.stringify(content))
    console.log(result);
}
```

### 4.跳转拨号页面
goToCall

会跳转到拨号页面，无需申请电话权限

**传参：**
```
{
    phone:"10086"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.android_media) {
    let content = {
        phone: "10086"
    }
    let result = window.android_media.goToCall(JSON.stringify(content))
    console.log(result);
}
```

### 5.发送短信
sendSms 会跳转到短信发送页面，不会立即发送短信

**传参：**
```
{
    phone:"10086", //手机号码
    content:"111" //短信内容
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.android_media) {
    let content = {
        phone: "10086",
        content: "111"
    }
    let result = window.android_media.sendSms(JSON.stringify(content))
    console.log(result)
}
```

### 6.图片预览

previewPicture

预览单张图片，之后点击图片会关闭

**传参：**
```
{
    content: "",
    type: 2 
}
```
> 当`type`为1，`content`传base64数据；
> 
> 当`type`为2，`content`传图片地址

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.android_media) {
    let content = {
        content: "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fimg.jj20.com%2Fup%2Fallimg%2Ftp01%2F1ZZQ214233446-0-lp.jpg&refer=http%3A%2F%2Fimg.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1647143194&t=b67961882a5e7d01f6ecd8a503a9db47",
        type: 2
    }
    let result = window.android_media.previewPicture(JSON.stringify(content))
    console.log(result)
}
```

### 还未实现功能清单

8. 二维码扫描
3. 图片压缩
4. 拍照
5. 录制视频
6. 录音
7. 图片选择
9. 安装apk
10. 文件下载
11. 文件预览(文件先下载再预览)
## listener使用
1. 网络状态监听
2. 来电监听
3. 返回键监听

## share
1. 分享到微信好友
2. 分享给朋友圈
3. 分享到QQ
4. 使用浏览器打开
5. 单纯复制链接

## map

1. 获取当前位置地址
2. 地图选择
3. 地图显示当前位置
4. 路径规划
5. 路径导航

## device
1. 获取原生APP的版本信息 （版本号，版本名）
2. 获取设备mac地址
3. 获取唯一设备ID
4. 判断是否是平板
5. 判断是否是模拟器

## handware
1. NFC
2. 蓝牙配对
3. 指纹识别
4. 人脸识别（包含活体检测）
5. 获取当前电量数值
6. 设置屏幕亮度
7. 获取当前网络状态（数据网络还是Wifi）

## debugger
开发辅助配置功能

1. bugly SDK接入
2. 提供配置webview加载的地址（本地资源或是网络url地址）
3. h5是否也要做成可配置的方式，来实现更换接口地址（优先读storege里的数据），入口设计思路为**依次按下音量键`+`和`-`次**
4. 360加固快捷打包

## update使用
# 功能清单
## 设备

1. 获取原生APP的版本信息 （版本号，版本名）
2. 获取设备mac地址
3. 获取唯一设备ID
4. 判断是否是平板
5. 判断是否是模拟器

## 地图

考虑使用哪种地图SDK？目前只对接过百度地图SDK，各款地图SDK使用的坐标系有所差异

1. 获取当前位置地址
2. 地图选择
3. 地图显示当前位置
4. 路径规划
5. 路径导航

## 分享

1. 分享到微信好友
2. 分享给朋友圈
3. 分享到QQ
4. 使用浏览器打开
5. 单纯复制链接

## 监听回调

1. 网络状态监听
2. 来电监听
3. 返回键监听

## 媒体

1. 拍照
2. 录制视频
3. 录音
4. 图片选择
5. 图片预览
6. 二维码扫描
7. 拨打电话
8. 发送短信
9. 图片压缩
10. 安装apk
11. 文件下载
12. 获取剪切板内容
13. 写入剪切板内容



## 页面

1. 设置页面标题
2. 打开第三方链接
3. 设置页面全屏
4. 设置隐藏导航条及还原
5. 退出APP

## 通知

1. 提供一个固定格式可发通知，后端服务或者是前端也可以调用实现弹出通知
2. 预先对接友盟SDK推送通知功能

## 登录（如果有原生实现登录）

1. 提供登录用户信息

2. 判断用户是否登录

## 页面跳转

1. 跳转登录页面（如果有原生登录功能）

2. 跳转微信小程序

## APP存储

1. 提供读写SharePerfences的功能，信息可永久保存（不卸载APP的情况下）

2. 提供判断是否第一次进入APP

## 硬件

1. NFC
2. 蓝牙配对
3. 指纹识别
4. 人脸识别（包含活体检测）
5. 获取当前电量数值
6. 设置屏幕亮度
7. 获取当前网络状态（数据网络还是Wifi）

## 开发辅助配置功能（不是提供给H5的)

1. bugly SDK接入
2. 提供配置webview加载的地址（本地资源或是网络url地址）
3. h5是否也要做成可配置的方式，来实现更换接口地址（优先读storege里的数据），入口设计思路为**依次按下音量键`+`和`-`次**
4. 360加固快捷打包



## 困难点和疑惑点

### 1.人脸识别（活体检测）

百度人脸识别 

需要对接具体SDK，且SDK无法固定（客户可能会有所要求），无对接经验，需要参考具体SDK文档和demo进行编写，封装  

集成SDK导致APP体积过大，是否考虑分离多模块进行打包？

单独库，项目配置文件，自定义gradle脚本，组件化module开发

### 2.地图导航和路径规划

各个地图采用的定位坐标系不一致，需要应该明确使用哪个平台的地图？还是多套都有所对接，然后按需使用？

地图导航和路径规划是否有必要做？

### 3.指纹识别

功能是否有需要？无相关开发经验

### 4.打包问题

是固定使用一个包名来打包H5？固定包名是否会对相关的SDK使用有所影响？

如何动态变更包名，保证是不同的applicationId？

关于动态打包

### 5.后续更新

拆分module，依赖，后续如何更新？

### 6.腾讯x5内核

集成之后是否要从网络中重新下载？

能否支持js接口注入，二次开发？

## 建设目标

1.支持任意的前端框架，提供原生的能力

2.学习成本低，轻量

3.