# 基座H5能力API文档


## 介绍
本文档主要是为H5前端开发人员介绍Android基座具备的H5能力以及具体的使用方法，功能大概有分为下面几类：

|分类				|说明											|
|--					|--												|
|媒体				|主要包含拨打电话，发短信，扫码等功能	|
|分享				|文本或图片分享到微信，QQ等应用					|
|设备信息			|可以获取当前设备的系统等相关信息				|
|通知				|手机内置的通知栏通知调用						|
|存储				|在手机内部存储简单数据							|
|监听状态			|实现来电监听，网络状态监听，返回键监听			|
|开发调试辅助		|主要是有些方便调试的功能						|
|更新				|自动更新app等相关功能							|
|页面				|页面设置状态栏颜色等功能						|
|地图				|百度地图定位及导航等功能						|
|硬件				|获取电量，音量，亮度等功能						|
|即时通讯（腾讯IM）	|集成了腾讯云IM，含IM登录，退出等功能							|

上面列举的功能，都是需要**H5前端使用JS代码进行调用**才可使用，除了上面的功能，有些功能无需H5进行任何代码的修改即可支持。

如常用的文件选择功能，支持拍照，选择相册图片，录制视频，选择本地视频文件，录音，选择本地音频文件和选择其他文件这几个选项，**只要H5使用的是input标签即可使用**

> 以上所说功能，**兼容所有的前端框架**，不管前端框架使用的是Vue还是其他前端框架，都可以调用（**前提是使用我们基座APP去加载**）
## 安装
## 快速入门


**注：以下使用的传参都是需要Json的字符串，需要调用`JSON.stringify()`方法将对象转为字符串**



## 媒体
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

## 通知
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
## 分享
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
        content: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACkAAAAmCAIAAADWVCV0AAAACXBIWXMAABJ0AAASdAHeZh94AAAAEXRFWHRTb2Z0d2FyZQBTbmlwYXN0ZV0Xzt0AAAiqSURBVFiFfVhbbx1XFf6+vfecq+1jO5fGhqapQUVVo6QBtZXSllAeGpCiIlGJ0D60LxEFiRf+AMpTRMUP4CUP5QFHQhQoBQotJdA0QBsaEkJxjEOTFJxYqW/Hl+NzZmavxcOemTN2SJcsn5k9M/tb98vmUnudgALF/0CqIElCVIvFYj1ckhRREqqlD/qbFe+Hy+wrzZ7AEARIICxpAUIoVFQNCfT/jGHYQBXihWTYnYYkw8bh6W2kAFTUMGPeKFShioy7nGEiR9N8J4UCKqIFhzTcDKKbFZPLo8jfUpCiCoCkIUAQmzWlyPQcbkkAmzSfL2YXKlpez5Wbv6bFNiT7H7qc1eyDAC8f3qodP2m7vWTnSLxtW7x9BNta0XBDaxWtVXVk0O3eYSouWFcViceV6xtxrJ/ZXavVjEIznnLQzBw5ajCTC4AsLRHwf7tSvfpfo2JvfuRoI5rU2MQ4cVFSraRDAzz04MDXPx/VKyRSwZ8urr78+6X1Lr5wYOArjw+1hqOywsvmyMUHoC63cjBGeAUDvzljVABYFQWVgBLik0St97oRxz99e6Mama89ZqyZnev98s3FhcXY07z2l/b6WvLcUzsaDVu43iZUZPYGgtwFUwSA9cs3BqavF/azKhDAQIO7KRWopHHy2p+7nxpbGh8/+eO5W/OxNVZVxcubF1c9efTwtu0th0xEFqFVNofr80UAkF46MvkrI5lj579KqCEM1QCGRo1ivr3+g1+8Ozxxi3usjUTUwpAk+fbF9bll/9TBoQP3N6OoBJzHerh1mXtlJievzNrL/yZUVH8Wrb5ve4W5BFRSyXAhoHx0LZm/sDIwltAGcRRQMNrxpX9dGzu5ED/b9Y99tmUt0He93O0IV8gcgje6MG2XOgBiyEWz8QfXwceTAqu3tqzZ1kOutrO9yh/9bkloHn9wwLmQgqiqJAypUAcQqhrkXu3YqauUGIBXTVVEZN++fXv37jXGAHjnnXdmZmYCQLPZfOKJJ4aHhycnJ0WkjE2fWBE1stLGD3+9MP1h71tPb8/yE/pWL8cYsbjiPrhh8n1ERET279//zDPPOOcAzM/PT09Ph6f1ev3IkSMTExOnTp3aiq3eqqgqKN0NnL7Q+eZX+ykFgIrS0OQZGICaazc4v1h+w3t/9uzZc+fOkXzllVcuXLjgcxIRa61zzt9GFDWqRr1RTY2NGi6kfSB3exKAyVmFAnz3sokTBYRMaVIR7/3MzMzVq1dF5Pz589evXy9jhE//D7aKVbEqsXVdE9WqdkvaDxyEvJgZILr5Uac17DvdiqY9miSF955kmqYA0jT13he7iEi4LvMRyIg3kK6prtqa0FTrLjh5IXrm51oqm2vf+/bqB4vu8qXht0/0uia+tuHbvkhPIhKYCCsBL1xswYZi3dUWqkMeRslqJSS0TRVdVV2ucgJwNTd832jl5tlk13/iXj294dIU27dvHxkZITk2NjY4OFiv1w8dOnTmzJldu3YNDg7Oz8+naRp4KmjFVeJoIDVWAnbNliQkVECT15KsaBJAdPOvZvq3ojaF86rey+HDh5988knn3AsvvLCwsDA+Pv78888fO3bMGNNqtSYnJ5Mk2SL3smtYEymtAkJTr0e5tpn7m6rCEVl1U6h2V3Hx5XRtIUGUMEp97L1fXl6+cuVKiLHV1dXXX399bGxsz549AC5dunTy5Mk4jrGZUhgYozBCKlivsJzVCyM6hZLMUvzcP/XqW4kyRiVBlEgvSZJXX3319OnT4evl5eVOp3P8+PHBwUEA7Xa71+vhNjJK0IqxIfU2a1SFIUQUpeLm8soEgr6zYuJegiiFS+BSL0mSLC4uLi4ulrdOkmRlZeV2yIIsoDRKo6DS1KqGoVUK4rOoJf1eQjHx6NqD3/CXfpLEcTcd2Oite9/+GIw7kQeRYwvZrCKP6aKVAEmX97RUqKk1Ko8e29j1kLw32bnc7qxvJMlso9GoVqu3AywtLd0J24FKq8YqKGCzgqIXLIRWVaeF0AQUrlYZfOBhMzqmcz8Xfy1N00ceeeTAgQMsp2MAwIsvvngn7BQEjeQ6b1b6rS0yb1cArtS3Mze8Vkd2xX5CU3jvDx48ePTo0VDHynTixIk7YXsg93OjZLPqixhT1VwBdFnxViD3OAC9TtJb7qpPvfcvvfTSG2+8cbvcW3NZWW7mfg4K2ahKnlO1KOGqea8YykzwBgDpWuyX18RLmqZTU1NTU1N3grmD3AYwkueWRk1KRSyjIq9p4f8Bu7Owlq7EX5744ufGDniogqFbVVCBDRct1ap/HxnvsNFF1fqsVVJSQbiK7tibWiuk0jiLiutDBqEDuaJ7DyNPwF+4skjjPtG6Z2frblGE9CRkbO3VHY1b9dZCbajJoZRDCZo2pdJIBm/EGHF1YQSj94+kz+2T7QMMg+OWltkFNSvDyMNQ6BZmbolxNC6iEUAiZ+vVaGzn+P679n6yfuq9pbm2gQwCLWVTUgqNZIoxnmhGeu8Inn3AP31f2mpaa7JgKldSBD9XbhpVu+3u0vUFNGrVgVrzrsGhPaOt3SMj94wO72zQAsDdd4++Ob328vt+eYUe1hsT4nioIveO4tHdemi3f3g8aVRIGmxujfuDEsCl5fVi9A6iL1xbnD0/O3rvtoGdA7XBStSoGEuyn4Kgmnj9x5z//ln942zNi7urqUc+7R/fo3t3YkcDFavGQKWfRMvUx15ur2fOnbfK4gWAcSZ0dP1hXou0QEC9YLYt332rElnznYeTiVFUrJqAFcpGP6D74gZmQrBxud3JJ95+84T+AUQxF/eH4iIcy7RloVwry3oubkmYABxmrezVrJhnwCTzBJyN/WQxY5cG9q2DJ7bUj6JvyMZ9hSmUnR99QKF5llFFdt6SQ/YTM4vDCfSn6JLQm5aZ20LDOBnOPJB5UPmIASwcr3wYoXn455FS6sECc5vgVSEi4bAhJNTiUdjlf0H/cmTEkykkAAAAAElFTkSuQmCC"
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
    content: "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACkAAAAmCAIAAADWVCV0AAAACXBIWXMAABJ0AAASdAHeZh94AAAAEXRFWHRTb2Z0d2FyZQBTbmlwYXN0ZV0Xzt0AAAiqSURBVFiFfVhbbx1XFf6+vfecq+1jO5fGhqapQUVVo6QBtZXSllAeGpCiIlGJ0D60LxEFiRf+AMpTRMUP4CUP5QFHQhQoBQotJdA0QBsaEkJxjEOTFJxYqW/Hl+NzZmavxcOemTN2SJcsn5k9M/tb98vmUnudgALF/0CqIElCVIvFYj1ckhRREqqlD/qbFe+Hy+wrzZ7AEARIICxpAUIoVFQNCfT/jGHYQBXihWTYnYYkw8bh6W2kAFTUMGPeKFShioy7nGEiR9N8J4UCKqIFhzTcDKKbFZPLo8jfUpCiCoCkIUAQmzWlyPQcbkkAmzSfL2YXKlpez5Wbv6bFNiT7H7qc1eyDAC8f3qodP2m7vWTnSLxtW7x9BNta0XBDaxWtVXVk0O3eYSouWFcViceV6xtxrJ/ZXavVjEIznnLQzBw5ajCTC4AsLRHwf7tSvfpfo2JvfuRoI5rU2MQ4cVFSraRDAzz04MDXPx/VKyRSwZ8urr78+6X1Lr5wYOArjw+1hqOywsvmyMUHoC63cjBGeAUDvzljVABYFQWVgBLik0St97oRxz99e6Mama89ZqyZnev98s3FhcXY07z2l/b6WvLcUzsaDVu43iZUZPYGgtwFUwSA9cs3BqavF/azKhDAQIO7KRWopHHy2p+7nxpbGh8/+eO5W/OxNVZVxcubF1c9efTwtu0th0xEFqFVNofr80UAkF46MvkrI5lj579KqCEM1QCGRo1ivr3+g1+8Ozxxi3usjUTUwpAk+fbF9bll/9TBoQP3N6OoBJzHerh1mXtlJievzNrL/yZUVH8Wrb5ve4W5BFRSyXAhoHx0LZm/sDIwltAGcRRQMNrxpX9dGzu5ED/b9Y99tmUt0He93O0IV8gcgje6MG2XOgBiyEWz8QfXwceTAqu3tqzZ1kOutrO9yh/9bkloHn9wwLmQgqiqJAypUAcQqhrkXu3YqauUGIBXTVVEZN++fXv37jXGAHjnnXdmZmYCQLPZfOKJJ4aHhycnJ0WkjE2fWBE1stLGD3+9MP1h71tPb8/yE/pWL8cYsbjiPrhh8n1ERET279//zDPPOOcAzM/PT09Ph6f1ev3IkSMTExOnTp3aiq3eqqgqKN0NnL7Q+eZX+ykFgIrS0OQZGICaazc4v1h+w3t/9uzZc+fOkXzllVcuXLjgcxIRa61zzt9GFDWqRr1RTY2NGi6kfSB3exKAyVmFAnz3sokTBYRMaVIR7/3MzMzVq1dF5Pz589evXy9jhE//D7aKVbEqsXVdE9WqdkvaDxyEvJgZILr5Uac17DvdiqY9miSF955kmqYA0jT13he7iEi4LvMRyIg3kK6prtqa0FTrLjh5IXrm51oqm2vf+/bqB4vu8qXht0/0uia+tuHbvkhPIhKYCCsBL1xswYZi3dUWqkMeRslqJSS0TRVdVV2ucgJwNTd832jl5tlk13/iXj294dIU27dvHxkZITk2NjY4OFiv1w8dOnTmzJldu3YNDg7Oz8+naRp4KmjFVeJoIDVWAnbNliQkVECT15KsaBJAdPOvZvq3ojaF86rey+HDh5988knn3AsvvLCwsDA+Pv78888fO3bMGNNqtSYnJ5Mk2SL3smtYEymtAkJTr0e5tpn7m6rCEVl1U6h2V3Hx5XRtIUGUMEp97L1fXl6+cuVKiLHV1dXXX399bGxsz549AC5dunTy5Mk4jrGZUhgYozBCKlivsJzVCyM6hZLMUvzcP/XqW4kyRiVBlEgvSZJXX3319OnT4evl5eVOp3P8+PHBwUEA7Xa71+vhNjJK0IqxIfU2a1SFIUQUpeLm8soEgr6zYuJegiiFS+BSL0mSLC4uLi4ulrdOkmRlZeV2yIIsoDRKo6DS1KqGoVUK4rOoJf1eQjHx6NqD3/CXfpLEcTcd2Oite9/+GIw7kQeRYwvZrCKP6aKVAEmX97RUqKk1Ko8e29j1kLw32bnc7qxvJMlso9GoVqu3AywtLd0J24FKq8YqKGCzgqIXLIRWVaeF0AQUrlYZfOBhMzqmcz8Xfy1N00ceeeTAgQMsp2MAwIsvvngn7BQEjeQ6b1b6rS0yb1cArtS3Mze8Vkd2xX5CU3jvDx48ePTo0VDHynTixIk7YXsg93OjZLPqixhT1VwBdFnxViD3OAC9TtJb7qpPvfcvvfTSG2+8cbvcW3NZWW7mfg4K2ahKnlO1KOGqea8YykzwBgDpWuyX18RLmqZTU1NTU1N3grmD3AYwkueWRk1KRSyjIq9p4f8Bu7Owlq7EX5744ufGDniogqFbVVCBDRct1ap/HxnvsNFF1fqsVVJSQbiK7tibWiuk0jiLiutDBqEDuaJ7DyNPwF+4skjjPtG6Z2frblGE9CRkbO3VHY1b9dZCbajJoZRDCZo2pdJIBm/EGHF1YQSj94+kz+2T7QMMg+OWltkFNSvDyMNQ6BZmbolxNC6iEUAiZ+vVaGzn+P679n6yfuq9pbm2gQwCLWVTUgqNZIoxnmhGeu8Inn3AP31f2mpaa7JgKldSBD9XbhpVu+3u0vUFNGrVgVrzrsGhPaOt3SMj94wO72zQAsDdd4++Ob328vt+eYUe1hsT4nioIveO4tHdemi3f3g8aVRIGmxujfuDEsCl5fVi9A6iL1xbnD0/O3rvtoGdA7XBStSoGEuyn4Kgmnj9x5z//ln942zNi7urqUc+7R/fo3t3YkcDFavGQKWfRMvUx15ur2fOnbfK4gWAcSZ0dP1hXou0QEC9YLYt332rElnznYeTiVFUrJqAFcpGP6D74gZmQrBxud3JJ95+84T+AUQxF/eH4iIcy7RloVwry3oubkmYABxmrezVrJhnwCTzBJyN/WQxY5cG9q2DJ7bUj6JvyMZ9hSmUnR99QKF5llFFdt6SQ/YTM4vDCfSn6JLQm5aZ20LDOBnOPJB5UPmIASwcr3wYoXn455FS6sECc5vgVSEi4bAhJNTiUdjlf0H/cmTEkykkAAAAAElFTkSuQmCC"
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
## 设备信息

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

## 存储
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

## 监听状态
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


## 地图

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


## 硬件
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

## 开发调试辅助

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

## 更新

更新后台的部署操作，及更新

考虑几套更新策略

## 页面
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

## 即时通讯（腾讯云IM）
腾讯云IM模块

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
