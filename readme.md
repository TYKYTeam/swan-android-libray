# Android 组件库


[![](https://jitpack.io/v/stars-one/android-component-libray.svg)](https://github.com/stars-one/android-component-libray)
![](https://img.shields.io/badge/java-8-orange)
![](https://img.shields.io/badge/androix-8e24aa)
![](https://img.shields.io/badge/minSdkVersion-21-blue)
![](https://img.shields.io/badge/targetSdkVersion-32-yellow)

Android原生组件库，为Html提供Android原生能力，增强H5

<meta name="referrer" content="no-referrer">

## Module目录及引用说明

|module名		|必选	|描述														|
|--				|--		|--															|
|webViewBase	|√		|核心库，包含文件上传										|
|media			|×		|媒体库，包含拨打电话，发短信，拍照，录制视频等功能			|
|share			|×		|分享库，使用Android系统内置分享功能，进行文本或图片的分享	|
|device			|×		|设备信息库，用来获取当前设备的系统等信息					|
|notification	|×		|通知库，用来弹出通知栏通知									|
|storage		|×		|存储库，调用SharePerfence进行简单数据的存储（键值对形式）				|
|listener		|×		|监听回调类的开源库，包含来电监听，网络状态监听，返回键监听	|
|debugger			|×		|开发调试辅助库													|
|update			|×		|自动更新库													|


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


由于文件选择的功能与Webview比较耦合，无法单独划分出一个module，所以内置在主module中

包含拍照，选择相册图片，录制视频，选择本地视频文件，录音，选择本地音频文件，和选择其他文件

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
sendSms 

会跳转到短信发送页面，不会立即发送短信

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
### 7.扫描二维码

qrScan 扫描二维码

**传参：**
```
{
    callBackMethod: "" //扫描二维码成功后的回调js方法
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
//object格式与返回结果一样，result即为二维码的文本数据
window.qrScanResult = function(object) {
    console.log(object);
}

function qrScan() {
    if (window.android_media) {
        let content = {
            callBackMethod: "qrScanResult"
        }
        window.android_media.qrScan(JSON.stringify(content))
    }
}
```

### 8.语音播放文本

speakText 播放文本（如果没有tts引擎，会没法播放语音）

**传参：**
```
{
   content: "这是一段测试语音的文本"
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
        content: "这是一段测试语音的文本"
    }
    let result = window.android_media.speakText(JSON.stringify(content))
    console.log(result)
}
```


### 还未实现功能清单

8. 图片压缩（H5传参）

9. 文件下载
10. 文件预览(文件先下载再预览)
11. 安装apk
## notification通知
### 1.发送通知
sendNotification 状态栏显示一条新通知

**传参：**
```
{
    content: "测试内容11111",
    title:"测试标题"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.notification) {
    let content = {
        content: "测试内容11111",
        title:"测试标题"
    }
    let result = window.notification.sendNotification(JSON.stringify(content))
    console.log(result)
}
```

## share
默认分享会弹出分享方式（如分享给微信好友，复制文本，分享到QQ等选项）

### 1.分享文本

shareText 弹出分享的对话框，选择某一应用进行分享

**传参：**
```
{
    content: "这是分享的文本内容"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.share) {
    let content = {
        content: "这是分享的文本内容"
    }
    let result = window.share.shareText(JSON.stringify(content))
    console.log(result)
}
```

### 2.分享图片
shareImage 弹出分享的对话框，选择某一应用进行分享

目前只支持传base64图片数据

**传参：**
```
{
    content: "data:image/png;base64,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"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.share) {
    let content = {
        content: "data:image/png;base64,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"
    }
    let result = window.share.shareImage(JSON.stringify(content))
    console.log(result)
}
```

### 3.分享图文
shareImage 弹出分享的对话框，选择某一应用进行分享

目前只支持传base64图片数据,测试发现文字没法获取，和分享图片效果一样

**传参：**
```
{
    title:"分享的标题",
    content: "data:image/png;base64,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"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.share) {
    let content = {
        title:"分享的标题",
        content: "data:image/png;base64,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"
    }
    let result = window.share.shareTextImage(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

### 4.分享文本给微信好友
shareToWechatFriend

分享文本给微信好友，会直接弹出选择好友的列表页面

**传参：**
```
{
    content: "分享的文本内容"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.share) {
    let content = {
        content: "分享的文本内容"
    }
    let result = window.share.shareToWechatFriend(JSON.stringify(content))
    console.log(result)
}
```
## device设备

### 1.获取设备mac地址

getMacAddress 

获取设备mac地址

**传参：无需**

**返回结果：**
```
//成功 result中即为mac地址数据
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.device) {
    let result = window.device.getMacAddress()
    alert(result);
}
```

### 2.获取唯一设备id
getUniqueDeviceId 

获取唯一设备id

**传参：无需**

**返回结果：**
```
//成功 
{"code":200,"desc":"","result":"2196aa50b16cd36d39df62298561809a1"}
```

**H5调用示例：**
```
if (window.device) {
    let result = window.device.getUniqueDeviceId()
    alert(result);
}
```

### 3.判断当前设备是否为平板
isTablet 

当前设备是否为平板

**传参：无需**

**返回结果：**
```
//成功 
{"code":200,"desc":"","result":true}
```

**H5调用示例：**
```
if (window.device) {
    let result = window.device.isTablet()
    alert(result);
}
```
### 4.判断当前设备是否为模拟器
isEmulator 

判断当前设备是否为模拟器

**传参：无需**

**返回结果：**
```
//成功 
{"code":200,"desc":"","result":false}
```

**H5调用示例：**
```
if (window.device) {
    let result = window.device.isEmulator()
    alert(result);
}
```
### 5.获取当前设备系统信息

getDeviceInfo 

获取当前设备系统信息

**传参：无需**

**返回结果：**
```
//成功 
{
    "code":200,
    "desc":"",
    "result":{
        "model":"ZUKZ2121",//设备型号
        "manufacturer":"ZUK" //设备厂商
        "versionName":"11",//系统版本名
        "versionCode":30,//系统版本号
    }
}
```

**H5调用示例：**
```
if (window.device) {
    let result = window.device.getDeviceInfo()
    alert(result);
}
```

## storage使用
### 1.保存数据

save 

保存数据到Android内置的SharePerfence

**传参：**

```
{
    key: "test", 
    value: "hello woll" //value可以是布尔值，字符串，整数，不能是一个JsonObject对象
}
```

**返回结果：**
```
//成功 
{"code":200,"desc":"","result":false}
```

**H5调用示例：**
```
if (window.storage) {
    let content = {
        key: "test",
        value: "hello woll"
    }
    let result = window.storage.save(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

### 2.读取数据

get

根据key值去获取保存的数据

**传参：**

```
{
    key: "test"
}
```

**返回结果：**
```
//成功  若result为空，则表示该key没有对应的数据
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.storage) {
    let content = {
        key: "test"
    }
    let result = window.storage.get(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

## listener使用
### 1. 注册网络状态监听

registerNetworkListener

注册网络状态的监听回调

**传参：**

```
//传两个js回调的方法名，之后Android原生会执行对应的JS方法
{
    "callBackMethod": "networkDisconnect,networkConnect"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
window.networkDisconnect = function() {
    alert("网络已断开")
}
window.networkConnect = function() {
    alert("网络已连接")
}

if (window.listener) {
    let content = {
        "callBackMethod": "networkDisconnect,networkConnect"
    }
    let result = window.listener.registerNetworkListener(JSON.stringify(content))
    console.log(result)
}
```

### 2.来电监听

registerPhoneListener

来电时候的监听回调

**传参：**

```
//传递个js回调的方法名，之后Android原生会执行对应的JS方法
{
    "callBackMethod": "phoneCall"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
window.phoneCall = function() {
    alert("来电了。。")
}

if (window.listener) {
    let content = {
        "callBackMethod": "phoneCall"
    }
    let result = window.listener.registerPhoneListener(JSON.stringify(content))
    console.log(result)
}
```


## map

### 1. 获取当前地址

startLocation

获取当前地址

**传参：**

```
//传递个js回调的方法名，之后Android原生会执行对应的JS方法
{
    "callBackMethod": "getLocation"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```


**H5调用示例：**
```
window.getLocation = function(object) {
    //object内容为{"addrStr":"中国广东省深圳市福田区梅林街道梅坳八路","city":"深圳市","district":"福田区","province":"广东省","street":"梅坳八路"}
    console.log(object);
    alert(JSON.stringify(object))
}

function startLocation() {
    if (window.map) {
        let content = {
            "callBackMethod": "getLocation"
        }
        let result = window.map.startLocation(JSON.stringify(content))
        alert(result)
        console.log(result)
    }
}
```

### 2.地图显示当前位置

showLocationInMap

打开地图页面，并展示当前的位置

**传参：无需**


**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```


**H5调用示例：**
```
if (window.map) {
    let result = window.map.showLocationInMap()
    alert(result)
    console.log(result)
}
```

### 待完成功能

4. 路径导航
5. 地图选点 
6. 路径规划


## handware
### 1.获取当前电量数值

getBattery

获取当前电量数值

**传参：无需**

**返回结果：**
```
//成功
{"code":200,"desc":"","result":"98%"}
```

**H5调用示例：**
```
if (window.hardware) {
    let result = window.hardware.getBattery()
    alert(result)
    console.log(result)
}
```
### 2.获取当前网络状态

getNetworkState

获取当前网络状态

**传参：无需**

**返回结果：**
```
//成功
{"code":200,"desc":"","result":"NETWORK_WIFI"}
```

result字段的数值如下：
- `NETWORK_ETHERNET` 以太网
- `NETWORK_WIFI`     Wifi
- `NETWORK_4G`       
- `NETWORK_3G`       
- `NETWORK_2G`       
- `NETWORK_UNKNOWN`  未知网络
- `NETWORK_NO`  无网络

**H5调用示例：**
```
if (window.hardware) {
    let result = window.hardware.getNetworkState()
    alert(result)
    console.log(result)
}
```

### 3.获取媒体最大音量
getMaxVolume

获取当前网络状态

**传参：无需**

**返回结果：**
```
//成功
{"code":200,"desc":"","result":"25"}
```

> PS:不同手机，返回的最大音量可能会不同

**H5调用示例：**
```
if (window.hardware) {
    let result = window.hardware.getMaxVolume()
    alert(result)
    console.log(result)
}
```


### 3.获取媒体音量
getVolume

获取当前媒体音量

**传参：无需**

**返回结果：**
```
//成功
{"code":200,"desc":"","result":"11"}
```


**H5调用示例：**
```
if (window.hardware) {
    let result = window.hardware.getVolume()
    alert(result)
    console.log(result)
}
```

### 4.设置媒体音量
setVolume

设置当前的媒体音量

**传参：**
```
{
    content: "11"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.hardware) {
    let content = {
        content: "11"
    }
    let result = window.hardware.setVolume(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```
### 5.获取屏幕亮度

getBrightness

获取当前屏幕亮度

**传参：无需**

**返回结果：**
```
//成功
{"code":200,"desc":"","result":"11"}
```


**H5调用示例：**
```
if (window.hardware) {
    let result = window.hardware.getBrightness()
    alert(result)
    console.log(result)
}
```

### 6.设置屏幕亮度

setBrightness

设置当前的屏幕亮度



**传参：**
```
{
    content: "11"
}
```

> 注：亮度范围为0-255

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

由于设置屏幕亮度需要权限的申请，所以第一次设置屏幕亮度会出现不成功（因为是申请权限操作），返回的结果为
```
"{"code":202,"desc":"未授予权限，设置亮度失败","result":""}"
```

需要注意判断一下

**H5调用示例：**
```
if (window.hardware) {
    let content = {
        content: "11"
    }
    let result = window.hardware.setBrightness(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```

### 未完成
0. NFC
1. 蓝牙配对
2. 指纹识别
3. 人脸识别（包含活体检测）

## debugger

> 注意：由于配置页面中需要扫一扫的功能，目前是复用了media的Module中的扫一扫页面，导致**使用此模块需要前提引入media的模块**

### 1.跳转到debugger页面
goSettingPage

跳转到debugger页面，可输入一个在线地址用来测试页面功能（也含有扫一扫功能）

**传参：无需**


**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.debugger) {
    let result = window.debugger.goSettingPage()
    console.log(result)
}
```

### 2.Bugly配置
目前引入了Bugly对接，**注意公司的网络环境对Bugly有限制，无法测试，建议使用移动网络**

**使用步骤：**

在AndroidManifest文件中，声明application，并修改Bugly的配置

![](https://img2022.cnblogs.com/blog/1210268/202203/1210268-20220301170652451-2078281283.png)

**注：debugger默认有个appId，下面这个是在APP的AndroidManifest.xml文件，会将debugger模块中的`AndroidManifest.xml`覆盖掉**

```
<!-- Bugly配置 -->
<!-- 配置APP ID -->
<meta-data
    tools:node="replace"
    android:name="BUGLY_APPID"
    android:value="4ec93574ce" />
<!-- 配置APP版本号 -->
<meta-data
    tools:node="replace"
    android:name="BUGLY_APP_VERSION"
    android:value="test-version" />
<!-- 配置APP渠道号 -->
<meta-data
    tools:node="replace"
    android:name="BUGLY_APP_CHANNEL"
    android:value="m-debug" />
<!-- 配置Bugly调试模式（true或者false）-->
<meta-data
    tools:node="replace"
    android:name="BUGLY_ENABLE_DEBUG"
    android:value="true" />
```

如果有需求需要改动，只需要在主Module中，修改上述数值即可

> 注意：BaseApplication中是使用了**反射+注解**的方式实现了Module自动初始化（假如Module中使用的库需要在Application中进行初始化）



### 剩余功能

3. h5是否也要做成可配置的方式，来实现更换接口地址（优先读storege里的数据），入口设计思路为**依次按下音量键`+`和`-`次**
4. 360加固快捷打包

## update使用

更新后台的部署操作，及更新

考虑几套更新策略

## page
### 1.跳转其他APP页面

gotoApp

跳转其他APP页面

**传参：**

```
{
    "packageName": "com.android.settings",//app应用应用包名
    "activityName": "com.android.settings.Settings"//具体的某个页面Activity（全包名）
}

```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.page) {
    //下面这里是进入设置页面
    let content = {
        "packageName": "com.android.settings",
        "activityName": "com.android.settings.Settings"
    }
    let result = window.page.gotoApp(JSON.stringify(content))
    console.log(result)
}
```


## 关于Module创建


若是想要创建一个module，需要进行以下几步
### 1.新建module
![](https://img2022.cnblogs.com/blog/1210268/202202/1210268-20220223145218325-33691126.png)

![](https://img2022.cnblogs.com/blog/1210268/202202/1210268-20220223145238780-1117709618.png)

![](https://img2022.cnblogs.com/blog/1210268/202202/1210268-20220223145308210-1011843895.png)

> **注意：**由于封装了自动扫描包实现了Js注入功能，规定包名一定要为`com.tyky`
> 
### 2.module依赖修改
由于新建的module，没有核心库`webViewBase`的依赖，所以，需要导入依赖

![](https://img2022.cnblogs.com/blog/1210268/202202/1210268-20220223145530826-890303716.png)

上面的截图，由于module都是与webviewBase在同一项目中，所以可以这样写

实际上，这部分源码在对应的项目中是不存在的，所以，需要导入jitpack上的那个`webViewBase`依赖

### 3.创建Js注入类
使用Gradle进行sync，之后在新建的module中创建一个类，并标上注解`WebViewInterface`

![](https://img2022.cnblogs.com/blog/1210268/202202/1210268-20220223145412353-1421518910.png)

`@WebViewInterface("share")`

此注解是个人自定义的一个注解，主要是实现了自动注入Js方法的功能，而无需去设置webview，可以十分方便的实现功能扩展

后面的参数为之后注入到Js中的使用方式

以上面为例：

之后注入到Js代码中，就可以在Js层通过`window.share.xx()`去调用我们原生Android的方法

### 4.创建Js注入方法

这里不再过多赘述，就写个注解即可

![](https://img2022.cnblogs.com/blog/1210268/202202/1210268-20220223150148932-555298842.png)

如果想要保持返回结果规范，可以参考上述代码

PS：之后使用不要忘记了依赖自己新建的module哦！！

### 补充
#### 需要Application进行初始化操作

目前是使用了注解+反射方式，实现了Application的初始化，下面介绍一下步骤：

1.新建类，标明注解`@ApplicationInit`
2.实现接口`ModuleInit`

```
@ApplicationInit
public class UpdateModuleInit implements ModuleInit {

    @Override
    public void init(Application application) {
        //写你的初始化操作
    }
}
```

> PS：由于采用的包名扫描方式，**需要注意包名得在`com.tyky`下**
#### 清单文件怎么写？

如果需求是要在module新建页面或者其他四大组件，可以在AndroidManifest文件中这样写对应的声明:

下面给出的Activity的声明，其他组件类似，这里不再赘述
```
<application>
    <activity android:name=".SettingActivity"></activity>
</application>
```

如果是需要权限的，也是在此声明对应的权限，同时也要考虑到动态权限申请的情况

比较好的方式就是卸载对应Js方法里,用到就声明

#### ParamModel实体类字段：
```
/**
 * 电话号码
 */
private String phone;

/**
 * 标题
 */
private String title;
/**
 * 内容
 */
private String content;
/**
 * 类型
 */
private Integer type;
/**
 * 回调方法，多个用逗号隔开
 */
private String callBackMethod;

/**
 * shareperfence存储需要用到的key和value
 */
private String key;
private String value;
```

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



## 推进流程疑惑点

### 1.文件预览（pdf，doc，ppt，xls），

目前网上的资料只有实现了pdf和doc的预览，其他的格式没有找到有方案，

pdf可以使用webview进行预览（使用pdf.js)，doc则是需要解压doc文件，然后实现渲染加载

自己实现比较困难，目前3个思路：
> 1.由后台+前端实现在线预览文档页面，app则可以直接通过访问url来预览文件
> 2.app内置实现，只能实现pdf或doc预览（需要先下载在本地），其他格式不行
> 3.与分享类似，打开一个第三方应用来查看文件（也是需要将文件下载在本地）

### 2.分享功能，目前是实现的打开一个分享的对话框，然后有用户选择对应的方式，这样是否满足需求？

目前支持文本，图片，图文分享

图片需要有图片的uri地址，但是h5只能闯过来数据，是否是要讲图片数据先下载在本机的缓存目录中？需要考虑传多张图片（可以是图片地址或者是base64)

### 3.下载

由于存储权限的变更，下载实现需要对多不同Android版本进行测试验证，预估可能会花费较多时间，

### 4.无法解析安装包

360加固导致的某些Android设备无法成功解析安装包，出问题的大多是 Android11或Android12，没有设备，无法复现，且没有解决思路

目前确认apk重新签名是无误的，包含了v1+v2+v3版本的签名

### 4.通知栏点击的pendintent如何封装

通知有时候需要点击跳转到具体页面，这个pendinginent该如何实现封装，接收前端传的参数？


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

集成之后要从网络中下载渲染引擎，约莫30M

## 建设目标

1.支持任意的前端框架，提供原生的能力

2.学习成本低，轻量

3.