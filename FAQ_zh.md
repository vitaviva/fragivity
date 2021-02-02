# FAQ

## 为什么使用单Activity架构？
在Activity之间进行页面跳转、数据传递的成本较高，单Activity架构有利于降低成本，提高性能，适合在一些中小型项目中使用。
但是单Activity并非银弹，并不一定适合所有项目。


## 相对于Jetpack Navigation的优势？
相对于Navigation主要优势如下：
* 无需配置`NavGraph`
* 生命周期进行了合理优化
* API更加易用


## 项目是否稳定？是否会持续维护?
Fragivity底层基于Navigation实现，这保证了基础能力的稳定性；
在没有更好的替代方案出现之前，项目会长期维护，也许Compose未来会成为好的替代者


## 是否支持在Java中使用
项目会提供对Java侧的兼容，但是更推荐使用Kotlin


## 对于DI的支持？
框架不影响Fragment对于其他三方库的使用，Koin、Hilt等DI框架都可以正常使用
