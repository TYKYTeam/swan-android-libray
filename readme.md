<meta name="referrer" content="no-referrer">

# Android 组件库

[![](https://jitpack.io/v/TYKYTeam/swan-android-libray.svg)](https://github.com/TYKYTeam/swan-android-libray)
![](https://img.shields.io/badge/java-8-orange)
![](https://img.shields.io/badge/androix-8e24aa)
![](https://img.shields.io/badge/minSdkVersion-21-blue)
![](https://img.shields.io/badge/targetSdkVersion-32-yellow)

Android原生组件库，为Html提供Android原生能力，增强H5


map：5M左右
tim: 14M左右

## Module目录及引用说明

依赖引入（需要添加jitpack仓库源）：
```
implementation 'com.github.TYKYTeam.swan-android-libray:module名:版本号'
```

例：
```
implementation 'com.github.TYKYTeam.swan-android-libray:0.2'
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

### 1.清除缓存等数据

`clearWebviewData()`

清除webview缓存，localstorage，历史记录和cookies

**传参：无需传参**

**返回结果：**
```
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.webviewBase) {
    let result = window.webviewBase.clearWebviewData()
    alert(result)
    console.log(result)
}
```

### 2.webview重载

`reloadWebview()`

webview重新加载首页

**传参：无需传参**

**返回结果：**
```
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.webviewBase) {
    let result = window.webviewBase.reloadWebview()
    alert(result)
    console.log(result)
}
```
### 3.获取APP信息
`getAppInfo()`

获取当前APP相关版本号等信息

**传参：无需传参**

**返回结果：**
```
"{"code":200,"desc":"","result":{"appName":"天鹅基座","pkgName":"com.tyky.acl","versionName":"1.0","versionCode":1}}"
```

**H5调用示例：**
```
if (window.webviewBase) {
    let result = window.webviewBase.getAppInfo()
    alert(result)
    console.log(result)
}
```
## filePreview 文件预览
此模块由于依赖的pdf库较大（15M） 所以单独出来

### 1.pdf文件预览

`previewPdf()`

传递pdf文件的下载链接或base64，实现预览

**传参：**
```
{
    content:"https://down.wss.show/lyxttjh/8/ub/8ubglyxttjh?cdn_sign=1658285364-45-0-de4aa017d38d8c9202042c11d67b9dfe&exp=240&response-content-disposition=attachment%3B%20filename%3D%22Android%20%E7%BB%84%E4%BB%B6%E5%BA%93.pdf%22%3B%20filename%2A%3Dutf-8%27%27Android%2520%25E7%25BB%2584%25E4%25BB%25B6%25E5%25BA%2593.pdf"
}
```

content传下载地址或base64字符串（base64不需要逗号），上面示例的下载链接可能已经过期，测试的时候记得更改

**返回结果：**
```
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.filePreview) {
    let content = {
        content:"https://down.wss.show/lyxttjh/8/ub/8ubglyxttjh?cdn_sign=1658285364-45-0-de4aa017d38d8c9202042c11d67b9dfe&exp=240&response-content-disposition=attachment%3B%20filename%3D%22Android%20%E7%BB%84%E4%BB%B6%E5%BA%93.pdf%22%3B%20filename%2A%3Dutf-8%27%27Android%2520%25E7%25BB%2584%25E4%25BB%25B6%25E5%25BA%2593.pdf"
    }
    let result = window.filePreview.previewPdf(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

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

### 9.获取手机通讯录

getContacts

**传参：**
```
{
    callBackMethod: "myContacts" //回调方法
}
```

**返回结果：**
```
{"code":200,"desc":"","result":[{"name":"xing wan","number":"18778089853"},{"name":"one stars","number":"147663358935"}]}
```

**H5调用示例：**
```
window.myContacts = function(object) {
    console.log(object);
}

if (window.android_media) {
    let content = {
        callBackMethod: "myContacts"
    }
    let result = window.android_media.getContacts(JSON.stringify(content))
    alert(result);
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
    title:"测试标题"，
    url: "https://stars-one.site"
}
```

可传入url，点击之后会新开页面加载url

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
        title:"测试标题"，
        url: "https://stars-one.site"
    }
    let result = window.notification.sendNotification(JSON.stringify(content))
    console.log(result)
}
```
### 2. 设置桌面小红点

setBargeCount 

**传参：**
```
{
    content: "11" //未读数
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
        content: "11"
    }
    let result = window.notification.setBargeCount(JSON.stringify(content))
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
shareTextImage 弹出分享的对话框，选择某一应用进行分享

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

### 6.判断是否为某个机型品牌

isBrand 

判断当前机型是否为某个品牌

**传参：**

```
{
    "content": "1"//型号类型，详见下表
}
```

|	类型|	品牌|
|--	|--	|
|1  | 360|
|2  | 酷派|
|3  | 金立|
|4  | 谷歌|
|5  | HTC|
|6  | 华为|
|7  | 乐视|
|8  | 联想|
|9  | LG|
|10 |魅族|
|11 |摩托罗拉|
|12 |努比亚|
|13 |一加|
|14 |Oppo|
|15 |三星|
|16 |锤子|
|17 |索尼|
|18 |Vivo|
|19 |小米|
|20 |中兴|

**返回结果：**

```
("code":200,"desc":","result":false)
```

**H5调用示例：**

```
//判断是否为360品牌手机
if (window.device) {
    let content = {
        "content": "1"
    }
    let result = window.device.isBrand(JSON.stringify(content))
    console.log(result);
    alert(result);
}
```

### 7.截图

takeScreenshot

**传参：**

```
{
    callBackMethod: "getScreenshot"
}
```

**返回结果：**

```
//getScreenshot中回调的result为图片本地路径
{"code":200,"desc":"","result":"/storage/emulated/0/DCIM/com.tyky.acl/1653012630717_100.JPG"}
```

**H5调用示例：**

```
window.getScreenshot = function(res){
    console.log(res);
}

if (window.device) {
    let content = {
        callBackMethod: "getScreenshot"
    }
    let result = window.device.takeScreenshot(JSON.stringify(content))
    console.log(result);
    alert(result);
}
```

### 8.获取当前屏幕方向状态

getScreenOrientation

**传参：无需**

**返回结果：**

```
//0:竖屏 1：横屏
{"code":200,"desc":"","result":0}
```

**H5调用示例：**

```
if (window.device) {
    let result = window.device.getScreenOrientation()
    console.log(result);
    alert(result);
}
```
### 9.获取屏幕分辨率

getScreenWH

**传参：无需**

**返回结果：**

```
"{"code":200,"desc":"","result":{"width":1080,"height":1920}}"
```

**H5调用示例：**

```
if (window.device) {
    let result = window.device.getScreenWH()
    console.log(result);
    alert(result);
}
```

## storage使用
### 1.保存数据

saveStorage 

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
    let result = window.storage.saveStorage(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

### 2.读取数据

getStorage

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
    let result = window.storage.getStorage(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

### 3.数据库创表

createTable

数据库创表操作,如果表已存在，不会再次创表表

如果需要使用到数据库功能，建议在JS每次初始化前调用一次此方法

**传参：**

```js
{
    tableName:"student",
    columns:"id,name,age",
    columnTypes:"int,varcahr(10),int",
}
```
- `tableName` 表名
- `columns` 字段数组
- `columnTypes` 字段类型数组

> **PS：字段数据要与字段类型一一对应**

上面示例相当于以下sql：
```sql
create table if not exists student(id int,name varchar(10),age int)
```

**返回结果：**
```js
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```js
if (window.storage) {
    let content = {
        tableName:"student",
        columns:"id,name,age",
        columnTypes:"int,varcahr(10),int",
    }
    let result = window.storage.createTable(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```
### 4.数据库插入数据
insertTable

往数据库指定表插入数据操作

**传参：**

```js
{
    tableName:"student",
    columns:"id,name,age",
    values:"1,'张三',18",
}
```
- `tableName` 表名
- `columns` 字段数组
- `values` 字段数值数组

> **PS：字段数据要与字段数值数组一一对应**

上面示例相当于以下sql：
```sql
insert into student(id,name,age) values(1,'张三',18)
```

**返回结果：**
```js
//成功 result返回插入数据成功后表中存在数据记录条数
{"code":200,"desc":"","result":8}
```

**H5调用示例：**
```js
if (window.storage) {
    let content = {
        tableName:"student",
        columns:"id,name,age",
        values:"1,'张三',18",
    }
    let result = window.storage.insertTable(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

### 5.数据库更新数据
updateTable

往数据库指定表更新数据操作

**传参：**

```js
{
   tableName:"student",
   columns:"name,age",
   values:"'张三',23",
   where:"id = ? and age < ?",
   whereValues:"1,22"
}
```

- `tableName` 表名
- `columns` 字段数组
- `values` 字段更新数值数组
- `where` 查询条件
- `whereValues` 条件数值数组（主要与where里出现的？号顺序对应）

上面示例相当于以下sql：

```sql
update student set name='张三',age=23 where id = 1 and age = 22
```

**返回结果：**
```js
//成功 result返回成功插入数据条数
{"code":200,"desc":"","result":1}
```

**H5调用示例：**
```js
if (window.storage) {
   let content = {
       tableName:"student",
       columns:"name,age",
       values:"'张三',23",
       where:"id = ? and age < ?",
       whereValues:"1,22"
   }
   let result = window.storage.updateTable(JSON.stringify(content))
   alert(result);
   console.log(result)
}
```

### 6.数据库查询数据

queryTable

查询数据库指定表数据

**传参：**

```js
{
   tableName:"student",
   columns："",
   where:"age <= ?",
   whereValues:"28",
   orderBy:"age desc",
   groupBy:"",
   having:"",
   limit:"1"
}
```

- `tableName` 表名
- `columns` 字段数组(不传则是返回表的所有列)
- `values` 字段更新数值数组
- `where` 查询条件
- `whereValues` 条件数值数组（主要与where里出现的？号顺序对应）

上面示例相当于以下sql：

```sql
select * from student where age <=28 order by age desc limit 1
```

**返回结果：**
```js
//成功 result返回列表数据
{"code":200,"desc":"","result":[{"name":"'张三'","id":"1","age":"19"}]}
```

**H5调用示例：**
```js
if (window.storage) {
   let content = {
       tableName:"student",
       where:"age <= ?",
       whereValues:"28",
       orderBy:"age desc",
       groupBy:"",
       having:"",
       limit:"1"
   }
   let result = window.storage.queryTable(JSON.stringify(content))
   alert(result);
   console.log(result)
}
```

### 7.数据库删除数据

deleteRecord

删除数据库指定表的数据

**传参：**

```js
{
   tableName:"student",
   where:"age = ?",
   whereValues:"19"
}
```

- `tableName` 表名
- `where` 查询条件
- `whereValues` 条件数值数组（主要与where里出现的?号顺序对应）

上面示例相当于以下sql：

```sql
delete student where age = 19
```

**返回结果：**
```js
//成功 result返回删除的条数
{"code":200,"desc":"","result":2}
```

**H5调用示例：**
```js
if (window.storage) {
    let content = {
       tableName:"student",
       where:"age = ?",
       whereValues:"19"
   }
   let result = window.storage.deleteRecord(JSON.stringify(content))
   alert(result);
   console.log(result)
}
```
### 8.数据库判断表是否存在

isTableExists

数据库判断表是否存在

**传参：**

```js
{
    tableName:"student"
}
```

- `tableName` 表名

上面示例相当于以下sql：

```sql
delete student where age = 19
```

**返回结果：**
```js
//成功 result返回表是否存在的boolean
{"code":200,"desc":"","result":true}
```

**H5调用示例：**
```js
if (window.storage) {
    let content = {
        tableName:"student"
    }
    let result = window.storage.isTableExists(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```
### 9.数据库执行sql
rowQuery

执行原生的sql语句，不限于增删改查操作，创表语句也可以

**传参：**

```js
{
    content:"select * from student"
}
```

**content传sql语句即可**


**返回结果：**
```js
//成功 
{"code":200,"desc":"","result":""}
```

如果是增删改查的sql语句，result返回数据可参考上面4-7的接口返回数据

如果是创表等sql语句，只返回一个空白字符串

**H5调用示例：**
```js
if (window.storage) {
    let content = {
        content:"select * from student"
    }
    let result = window.storage.rowQuery(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

## listener使用
### 1. 注册网络断开连接监听

registerNetworkDisconnectionListener

注册网络断开连接的监听回调

**传参：**

```
//传两个js回调的方法名，之后Android原生会执行对应的JS方法
{
    "callBackMethod": "networkDisconnect"
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

if (window.listener) {
    let content = {
        "callBackMethod": "networkDisconnect"
    }
    let result = window.listener.registerNetworkDisconnectionListener(JSON.stringify(content))
    console.log(result)
}
```

### 2. 注册网络成功连接监听

registerNetworkConnectionListener

注册网络成功连接监听回调

**传参：**

```
//传两个js回调的方法名，之后Android原生会执行对应的JS方法
{
     "callBackMethod": "networkConnect"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
window.networkConnect = function() {
    alert("网络已连接")
}

if (window.listener) {
    let content = {
        "callBackMethod": "networkConnect"
    }
    let result = window.listener.registerNetworkConnectionListener(JSON.stringify(content))
    console.log(result)
}
```

### 3.来电监听

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

getLocation

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
        let result = window.map.getLocation(JSON.stringify(content))
        alert(result)
        console.log(result)
    }
}
```


### 2.地图检索

poiSearch

打开地图页面，并展示当前的位置

**传参：**
```
{
    "keyword": "小吃",
    "city": "深圳市",
    "tags": "火锅",
    "callBackMethod": "getLocation"
}
```

**返回结果：**
```
//成功
{
 "code": 200,
 "desc": "",
 "result": [
      {
           "address": "广东深圳市罗湖区人民路二横街12号负1层",
           "area": "罗湖区",
           "city": "深圳市",
           "name": "东门小吃街",
           "latitude": 22.552882,
           "longitude": 114.127109,
           "phoneNum": "(0755)82221797",
           "province": "广东省"
      }
 ]
}
```


**H5调用示例：**
```
window.getLocation = function(object) {
    console.log(object);
    alert(JSON.stringify(object))
}
        
let content = {
    "keyword": "小吃",
    "city": "深圳市",
    "tags": "火锅",
    "callBackMethod": "getLocation",
}
if (window.map) {
    window.map.poiSearch(JSON.stringify(content))
} 
```
### 3.判断当前是否在某地点附近
ptInCircle

传入经纬度坐标，转为具体地址信息

**传参：**
```
{
    "latitude": 22.57871696606192,
    "longitude": 114.06792497377637,
    "distance": 70,
    "callBackMethod": "getLocation"
}
```

- `latitude` 目标点纬度
- `longitude` 目标点经度
- `distance` 目标点范围半径（单位为m）

**返回结果：**
```
//成功
{
     "code": 200,
     "desc": "",
     "result": {
          "address": "广东省深圳市福田区梅坳三路",
          "area": "福田区",
          "city": "深圳市",
          "latitude": 22.5790620003812,
          "longitude": 114.06740399999997,
          "name": null,
          "phoneNum": null,
          "province": "广东省",
          "street": "梅坳三路"
     }
}
```

**H5调用示例：**
```
window.getLocation = function(object) {
    console.log(object);
    alert(JSON.stringify(object))
}
        
let content = {
    "latitude": 22.57871696606192,
    "longitude": 114.06792497377637,
    "distance": 70,
    "callBackMethod": "getLocation"
}

if (window.map) {
    let result = window.map.ptInCircle(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```

### 4.地址转坐标
`geoCode`

传入地址，转为经纬度坐标

**传参：**
```
{
    "endName": "广东省深圳市福田区梅林街道梅都社区中康路136号深圳新一代产业园4栋M层沿街商铺(手扶梯旁)",
    "city": "深圳",
    "callBackMethod": "getLocation"
}
```

**返回结果：**
```
//成功
{
     "code": 200,
     "desc": "",
     "result": {
          "address": null,
          "area": null,
          "city": null,
          "latitude": 22.57871696606192,
          "longitude": 114.06792497377637,
          "name": null,
          "phoneNum": null,
          "province": null,
          "street": ""
     }
}
```


**H5调用示例：**
```
window.getLocation = function(object) {
    console.log(object);
    alert(JSON.stringify(object))
}
        
let content = {
    "endName": "广东省深圳市福田区梅林街道梅都社区中康路136号深圳新一代产业园4栋M层沿街商铺(手扶梯旁)",
    "city": "深圳",
    "callBackMethod": "getLocation"
}

if (window.map) {
    let result = window.map.geoCode(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```

### 5.坐标转地址

reverseGeoCode

传入经纬度坐标，转为具体地址信息

**传参：**
```
{
    "latitude": 22.579062,
    "longitude": 114.067404,
    "callBackMethod": "getLocation"
}
```

**返回结果：**
```
//成功
{
     "code": 200,
     "desc": "",
     "result": {
          "address": "广东省深圳市福田区梅坳三路",
          "area": "福田区",
          "city": "深圳市",
          "latitude": 22.5790620003812,
          "longitude": 114.06740399999997,
          "name": null,
          "phoneNum": null,
          "province": "广东省",
          "street": "梅坳三路"
     }
}
```


**H5调用示例：**
```
window.getLocation = function(object) {
    console.log(object);
    alert(JSON.stringify(object))
}
        
let content = {
    "latitude": 22.579062,
    "longitude": 114.067404,
    "callBackMethod": "getLocation"
}

if (window.map) {
    let result = window.map.reverseGeoCode(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```
### 6.计算两点距离

getDistance

闯入两个点的经纬度，计算两点的距离

**传参：**
```
{
    "startLatitude": 22.579054,
    "startLongitude": 114.067389,
    "endLatitude": 22.57871696606192,
    "endLongitude": 114.06792497377637
}
```

- `startLatitude` 起点纬度
- `startLongitude` 起点经度
- `endLatitude` 终点纬度
- `endLongitude` 终点经度

**返回结果：**
```
//成功 单位为m
{"code":200,"desc":"","result":67.15}"
```


**H5调用示例：**
```
//公司位置和荣耀体验店的距离
let content = {
    "startLatitude": 22.579054,
    "startLongitude": 114.067389,
    "endLatitude": 22.57871696606192,
    "endLongitude": 114.06792497377637
}
if (window.map) {
    let result = window.map.getDistance(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```

### 待完成功能




## mapNav 地图导航
### 1.地图显示当前位置

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
if (window.mapNav) {
    let result = window.mapNav.showLocationInMap()
    alert(result)
    console.log(result)
}
```

### 2.步行规划
walkingRouteSearch

**传参：**
```
{
    "startName": "腾讯大厦",
    "startCityName": "深圳",
    "endName": "新一代产业园",
    "endCityName": "深圳",
}
```
- `startName` 起点名称
- `endName` 终点名称
- `startCityName` 起点所在城市名
- `endCityName` 终点所在城市名

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```


**H5调用示例：**
```
if (window.mapNav) {
    let content = {
        "startName": "腾讯大厦",
        "startCityName": "深圳",
        "endName": "新一代产业园",
        "endCityName": "深圳",
    }
    let result = window.mapNav.walkingRouteSearch(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```
### 3.骑行规划
ridingRouteSearch

**传参：**
```
{
    "startName": "腾讯大厦",
    "startCityName": "深圳",
    "endName": "新一代产业园",
    "endCityName": "深圳",
}
```
- `startName` 起点名称
- `endName` 终点名称
- `startCityName` 起点所在城市名
- `endCityName` 终点所在城市名

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```


**H5调用示例：**
```
if (window.mapNav) {
    let content = {
        "startName": "腾讯大厦",
        "startCityName": "深圳",
        "endName": "新一代产业园",
        "endCityName": "深圳",
    }
    let result = window.mapNav.ridingRouteSearch(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```
### 4.驾车规划
drivingRouteSearch

**传参：**
```
{
    "startName": "腾讯大厦",
    "startCityName": "深圳",
    "endName": "新一代产业园",
    "endCityName": "深圳",
}
```
- `startName` 起点名称
- `endName` 终点名称
- `startCityName` 起点所在城市名
- `endCityName` 终点所在城市名

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```


**H5调用示例：**
```
if (window.mapNav) {
    let content = {
        "startName": "腾讯大厦",
        "startCityName": "深圳",
        "endName": "新一代产业园",
        "endCityName": "深圳",
    }
    let result = window.mapNav.drivingRouteSearch(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```

### 5.步行导航

walkNavigation

**传参：**
```
{
    "endName": "新一代产业园",
}
```

- `endName` 终点地址名称（限制为当前位置的城市）

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```


**H5调用示例：**
```
if (window.mapNav) {
    let content = {
        "endName": "三九花园",
    }
    let result = window.mapNav.walkNavigation(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```

### 6.骑行导航

cycleNavigation

**传参：**
```
{
    "endName": "新一代产业园",
}
```

- `endName` 终点地址名称（限制为当前位置的城市）

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```


**H5调用示例：**
```
if (window.mapNav) {
    let content = {
        "endName": "三九花园",
    }
    let result = window.mapNav.cycleNavigation(JSON.stringify(content))
    alert(result)
    console.log(result)
}
```

### 待完成功能

1. 地图选点 


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

### 1.弹出更新提示框
`showUpdateDialog`

弹出更新版本的提示框

如果是强制更新，则不会显示取消按钮

**传参：**

```js
{
    "versionCode": 2,
    "versionName": "1.1",
    "updateContent": "1.基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点1.基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点",
    "downloadUrl": "http://10.232.107.44:9060/swan-business/file/testDownload",
    "forceUpdate": false,
    "updateTime": "2022-08-18 19:12",
    "fileSize": "85.8MB"
}
```

- `updateContent` 更新内容
- `downloadUrl` 下载地址
- `forceUpdate` 是否强制更新
- `updateTime` 更新时间
- `fileSize` 文件大小

> 如果是使用我们的移动后台系统进行的更新，实际上是JS调用完接口后，接口返回的数据。如果是自定义的，需要自己调整下字段即可

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**

```java
if (window.update) {
    let content = {
        "versionCode": 2,
        "versionName": "1.1",
        "updateContent": "1.基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点1.基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点\n2.基阿斯蒂设置桌面小红点",
        "downloadUrl": "http://10.232.107.44:9060/swan-business/file/testDownload",
        "forceUpdate": false,
        "updateTime": "2022-08-18 19:12",
        "fileSize": "85.8MB"
    }
    let result = window.update.showUpdateDialog(JSON.stringify(content))
    // alert(result);
    console.log(result)
}
```
### 2.下载文件并显示下载进度框
`showDownloadDialog`

下载文件并显示一个对话框，用来真实下载进度条，下载完文件会进入apk安装的页面

> 主要考虑到前端H5有自定义更新提示框的需求，这个时候H5点击了下载，即可调用此方法来下载apk更新文件

**传参：**

```js
{
    "downloadUrl": "http://10.232.107.44:9060/swan-business/file/testDownload",
    "forceUpdate": false,
}
```

- `downloadUrl` 下载地址
- `forceUpdate` 是否强制更新

> 如果是使用我们的移动后台系统进行的更新，实际上是JS调用完接口后，接口返回的数据。如果是自定义的，需要自己调整下字段即可

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**

```java
if (window.update) {
    let content = {
        "downloadUrl": "http://10.232.107.44:9060/swan-business/file/testDownload",
        "forceUpdate": false
    }
    let result = window.update.showDownloadDialog(JSON.stringify(content))
    // alert(result);
    console.log(result)
}
```

### 3.静默下载apk文件
`downloadFileBackground`

在Wifi环境下静默下载apk文件，不会有任何的提示

**传参：**

```js
{
    "downloadUrl": "http://10.232.107.44:9060/swan-business/file/testDownload",
    "forceUpdate": false,
}
```

- `downloadUrl` 下载地址
- `forceUpdate` 是否强制更新

> 如果是使用我们的移动后台系统进行的更新，实际上是JS调用完接口后，接口返回的数据。如果是自定义的，需要自己调整下字段即可

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**

```java
if (window.update) {
    let content = {
        "downloadUrl": "http://10.232.107.44:9060/swan-business/file/testDownload",
        "forceUpdate": false
    }
    let result = window.update.downloadFileBackground(JSON.stringify(content))
    // alert(result);
    console.log(result)
}
```

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

### 2.使用手机外部浏览器打开链接

openUrlByBrowser

使用手机的浏览器，打开链接

**传参：**

```
{
    "content": "https://www.baidu.com",
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
        "content": "https://www.baidu.com/",
    }
    let result = window.page.openUrlByBrowser(JSON.stringify(content))
    console.log(result)
}
```
### 3.重启APP
restartApp

重启APP

**传参：无**

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**

```
if (window.page) {
    let result = window.page.restartApp()
    console.log(result)
}
```

### 4.更改状态栏颜色

`changeStatusBar`

将状态栏设置为沉浸式状态栏，同时设置状态栏和底部导航条的颜色

**传参：**

```
{
    "type":"2",
    "content": "#4283f2",
}
```

- `type` 1:自动取色 2:设置状态栏颜色
- `content` 当type=2，传颜色值；type=1，传值无效

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**

```
if (window.page) {
    let content = {
        "type":"2",
        "content": "#4283f2",
    }
    let result = window.page.changeStatusBar(JSON.stringify(content))
    console.log(result)
}
```

## tim
腾讯云IM模块

目前使用此模块，会弹出有个错误提示（应用频繁停止），怀疑是存储权限没有给到导致，但之后授权重新打开APP，就不会出现此问题

依赖之后，需要配置主module的Androidmainfest文件

```
android:name="com.tencent.qcloud.tim.demo.DemoApplication"
tools:replace="android:allowBackup"
```

### 1.进入登录页
gotoImLogin 进入IM内置的登录页

**传参：无**

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.tim) {
    window.tim.gotoImLogin()
}
```
### 2.IM登录
login

**传参：**

```
{
    "userId": "5913635",//im的userId
    "userSig": "",//im的签名，不传会使用内置的sdkid计算
    "isGotoPage": true //是否登录后跳转到IM的会话列表页面
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.tim) {
    let content = {
        "userId": "5913635",
        "userSig": "",
        "isGotoPage": true
    }
    let result = window.tim.login(JSON.stringify(content))
    alert(result)
}
```

### 3.IM退出
logout

**传参：无需**


**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.tim) {
    let result = window.tim.logout()
    alert(result)
}
```

### 4.获取消息未读数

getUnreadCount

使用前，需要调用上述2中的IM登录

**传参：**

```
{
    "callBackMethod": "loadUnreadCount",
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
//返回数据以回调方式的回传给h5
window.loadUnreadCount = function(object){
    alert(JSON.stringify(object))
}

if (window.tim) {
    let content = {
        "callBackMethod": "loadUnreadCount",
    }
    let result = window.tim.getUnreadCount(JSON.stringify(content))
    console.log(result)
}
```


## baiduface
百度人脸采集功能

[证书申请文档](https://ai.baidu.com/ai-doc/FACE/pkd3xtfgn#211-申请license)

证书文件路径： `baiduface/src/main/assets/idl-license.face-android`
### 1.人脸采集

facialRecognite

进入人脸采集页面进行采集图片

**传参：**

```
{
    "isOpenSound": true,
    "qualityLevel": 1,
    "isActionLive": true,
    "isLivenessRandom": true,
    "iivenessAction": [0, 1,2],
    "callBackMethod":"getImg"
}
```

- `qualityLevel` 质量等级 0：正常、1：宽松、2：严格， 实名认证场景推荐使用『严格』或『正常』模式，人脸比对场景推荐使用『正常』或『宽松』模式
- `isOpenSound` 语音播报开关
- `isActionLive` 活体检测开关
- `isLivenessRandom` 活体动作是否顺序随机
- `iivenessAction` 数组，可选0:眨眼 1:张嘴 2：向右摇头 3：向左摇头 4：向上抬头 5：向下低头
- `callBackMethod` 回调方法

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
//object.result是图片的base64字符串
window.getImg = function(object) {
    console.log(object);
}

function facialRecognite() {
    if (window.baiduface) {
        let content = {
            "isOpenSound": true,
            "qualityLevel": 1,
            "isActionLive": true,
            "isLivenessRandom": true,
            "iivenessAction": [0, 1, 2],
            "callBackMethod": "getImg"
        }
        let result = window.baiduface.facialRecognite(JSON.stringify(content))
        alert(result);
        console.log(result)
    }
}
```

## auth
### 1.设置手势解锁
setGesture

设置手势解锁

**传参：**

```
{
    errorColor:"#dd001b",
    errorWidth:5,
    InsideCircleColor:"#3b5cf0",
    InsideCircleWidth:5,
    circleColor:"#aaaa7f",
    circleWidth:5
}
```

- `errorColor` 错误展示的颜色
- `errorWidth` 错误展示的宽度
- `InsideCircleColor` 内圆心和连线的颜色
- `InsideCircleWidth` 内圆心和连线的宽度
- `circleColor` 圆形颜色
- `circleWidth` 圆形宽度

上述参数字段都为可选配置，可以依照需求传递

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```
if (window.auth) {
    let content = {
        errorColor:"#dd001b",
        errorWidth:5,
        InsideCircleColor:"#3b5cf0",
        InsideCircleWidth:5,
        circleColor:"#aaaa7f",
        circleWidth:5
    }
    let result = window.auth.setGesture(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```

### 2.手势解锁
gestureUnlocking

手势解锁,需要设置了手势解锁才能打开页面

**传参：**

```
{
    errorColor:"#dd001b",
    errorWidth:5,
    InsideCircleColor:"#3b5cf0",
    InsideCircleWidth:5,
    circleColor:"#aaaa7f",
    circleWidth:5
}
```

- `errorColor` 错误展示的颜色
- `errorWidth` 错误展示的宽度
- `InsideCircleColor` 内圆心和连线的颜色
- `InsideCircleWidth` 内圆心和连线的宽度
- `circleColor` 圆形颜色
- `circleWidth` 圆形宽度

上述参数字段都为可选配置，可以依照需求传递

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```js
if (window.auth) {
    let content = {
       errorColor:"#dd001b",
       errorWidth:5,
       InsideCircleColor:"#3b5cf0",
       InsideCircleWidth:5,
       circleColor:"#aaaa7f",
       circleWidth:5
    }
    let result = window.auth.gestureUnlocking(JSON.stringify(content))
    alert(result);
    console.log(result)
}
```
### 3.电子签名

signature

打开电子签名页面进行手写签名

**传参：**

```
{
    callBackMethod:"getSignature"
}
```

**返回结果：**
```
//成功
{"code":200,"desc":"","result":""}
```

**H5调用示例：**
```js
window.getSignature = function(res){
    //res.result为签名图片的base64字符串
    console.log("签名图片",res)
}

if (window.auth) {
    let content = {
        callBackMethod:"getSignature"
    }
    let result = window.auth.signature(JSON.stringify(content))
    alert(result);
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