### 定义
`get_config`可以实现在没有服务器资源的情况，借助第三方分享平台帮助应用获取配置信息或者其他数据的工具封装。

### 使用
1. 在某个内容分享平台分享自己想获取的文本内容并产生网页链接
2. 导入aar
3. 引入代码

```kotlin
var getC = GetConfig(context)
var requestTask = GecTaskBuilder().apply {
            // 设置目标网页
            targetUrl = "http://xxx.xxx"
            // 设置解析脚本
            script = ScriptSource.xxx
            // 超时时间（默认5s）
            timeout = 15000
            // 注册结果监听
            resultCallback = object :
                GecResultCallback {
                override fun onResult(result: String?) {
                    Log.i("oh", "result ===>$result")
                }
                override fun onError(error: String?) {
                    Log.i("oh", "onError ===>$error")
                }
            }
        }
getC.process(requestTask)
```

### 示例
可以参考sample实现

### 示例效果
![](https://upload-images.jianshu.io/upload_images/1948083-e418c9b24c573590.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/200)


### 存在的意义
1. 学习与好玩
2. 自己弄个服务器每次自己修改配置生效貌似比较麻烦？（不太熟）
3. 分享的平台资源免费、稳定？
4. ???

### todo features:
- 其他方式实现请求
- 并发请求

### 其他
该实现仅用于日常技术学习探讨，欢迎联系批评指教。
