# Fragivity  :  Use Fragment like Activity

[English](https://github.com/vitaviva/fragivity/blob/master/README.md) | 中文文档

![Bintray](https://img.shields.io/bintray/v/vitaviva/maven/core)  ![Language](https://img.shields.io/badge/language-kotlin-green.svg)  ![License](https://img.shields.io/badge/License-MIT-blue.svg)

<img src="app/src/main/res/drawable-v24/ic_launcher.png" width=150 align=right>

Fragivity可以帮助开发者基于Fragment打造单Activty架构的APP

- **更合理的Lifecycle：** 页面跳转、返回等Lifecycle表现与Activity一致
- **多种启动模式：** 支持Standard、SingleTop、SingleTask等多种LaunchMode
- **转场动画：** 支持Transition、SharedElement等动画方式实现页面切换
- **更高效的通信：** 可以基于Callback通信，简单直接
- **更友好的回退处理：** 支持OnBackPressed事件拦截、支持滑动返回
- **Deep Links：** 通过URI跳转到指定Fragment
- **Dialog：** 支持DialogFragment显示

参考文档:

- [Fragivity：像使用Activity一样使用Fragment](https://juejin.cn/post/6918693610359619592)
- [Fragivity：对Fragment生命周期的优化处理](https://juejin.cn/post/6924320585325477895)
- [Fragivity：如何处理BackStack的销毁重建](https://juejin.cn/post/6924323543043801101)


## Installation

```groovy
implementation 'com.github.fragivity:core:$latest_version'
```

## Quick start

### 1. declare NavHostFragment in layout
Like `Navigation`, Fragivity needs a `NavHostFragment` as the host of ChildFragments

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:fitsSystemWindows="true">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/nav_host"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true" />
</FrameLayout>
```

### 2. load HomeFragment in Activity
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host) as NavHostFragment

        navHostFragment.loadRoot(HomeFragment::class)
        
        //or loadRoot with factory
        //navHostFragment.loadRoot{ HomeFragment() }

    }
}
```
### 3. navigate to destination Fragment 
```kotlin
//in HomeFragment
navigator.push(DestinationFragment::class) {
    arguments = bundleOf(KEY_ARGUMENT1 to "arg1", KEY_ARGUMENT2 to "arg2")
    //or applyArguments(KEY_ARGUMENT1 to "arg1", KEY_ARGUMENT2 to "arg2")

}
```

## Launch Mode
Support multiple launch modes
```kotlin
navigator.push(DestinationFragment::class) {
    launchMode = LaunchMode.STANDARD //default
    //or LaunchMode.SINGLE_TOP, LaunchMode.SINGLE_TASK
}
```

## Transition Animation
```kotlin
navigator.push(DestinationFragment::class) {
    //animator
    enterAnim = R.anim.slide_in
    exitAnim = R.anim.slide_out
    popEnterAnim = R.anim.slide_in_pop
    popExitAnim = R.anim.slide_out_pop
    
    //sharedElements
    sharedElements = sharedElementsOf(imageView to "id")
}
```

<img src="screenshot/transition.gif" width=250 >


## Communication
You can simply setup communication between two fragments
### 1. start destination Fragment with a callback
```kotlin
class HomeFragment : Fragment(){
    private val cb: (Int) -> Unit = { checked ->
        //...
    }

    //...

    fun startDestination() {
        navigator.push {
            DestinationFragment(cb)
        }
    }
  
    //...
}
```
### 2. callback to source Fragment
```kotlin
class DestinationFragment(val cb: (Int) -> Unit) : Fragment() {
    //...
    cb.invoke(xxx)
    //...
}

```


## Show Dialog
### 1. declare a DialogFragment
```kotlin
class DialogFragment : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dialog, container, false)
        return root
    }
}
```
### 2. show it
```kotlin
navigator.showDialog(DialogFragment::class)
```




## Deep links

### 1. add kapt dependencies
 ```groovy
kapt 'com.github.fragivity:processor:$latest_version'
```

### 2. declare URI with `@Deeplink` annotation
```kotlin
@DeepLink(uri = "myapp://fragitiy.github.com/")
class DeepLinkFragment : Fragment() {
    //...
}
```

### 3. handle intent in MainActivity
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //...
        
        navHostFragment.handleDeepLink(intent)

    }
}
```

### 4. start Activity with URI

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("myapp://fragitiy.github.com/"))
startActivity(intent)
```

## 使用Java进行开发
Fragivity支持使用Java进行开发。参考:[APIs For Java](https://github.com/vitaviva/fragivity/blob/master/USE_JAVA.md)

## FAQ
[常见问题解答](https://github.com/vitaviva/fragivity/blob/master/FAQ_zh.md)

## License
Fragivity is licensed under the [MIT License](LICENSE).

