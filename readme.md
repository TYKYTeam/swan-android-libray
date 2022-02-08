# Android 组件库

[![](https://jitpack.io//v/stars-one/android-component-libray.svg)](https://github.com/stars-one/android-component-libray)
![](https://img.shields.io/badge/java-8-orange)
![](https://img.shields.io/badge/androix-blue)

Android原生组件库，为Html提供Android原生能力，增强H5

<meta name="referrer" content="no-referrer">

## Module目录及引用说明

|module名	|必选	|描述	|
|--			|--		|--		|
|webViewBase|√		|	核心库，包含文件上传	|
|update|×	|	可选库，包含自动更新	|

依赖引入（需要添加jitpack仓库源）：
```
implementation 'com.github.stars-one.android-component-libray:module名:版本号'
```

例：
```
implementation 'com.github.stars-one.android-component-libray:0.2'
```

## webViewBase使用
提供一个CustomWebViewActivity，首页的MainActivity需要继承于此Activity，之后再onCreate方法中调用以下两种方法之一，来加载url

- `loadLocalUrl` 设置加载的url并加载
- `loadWebUrl` 加载url

> **PS：loadLocalUrl主要是自动拼接了assets里的本地资源html文件，如下图**

![](https://img2022.cnblogs.com/blog/1210268/202202/1210268-20220208150005668-1077064887.png)

## update使用