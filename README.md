# Fragivity  :  Use Fragment like Activity
![Bintray](https://img.shields.io/bintray/v/vitaviva/maven/core)  ![Language](https://img.shields.io/badge/language-kotlin-green.svg)  ![License](https://img.shields.io/badge/License-MIT-blue.svg)

<img src="app/src/main/res/drawable-xxxhdpi/ic_launcher.png" width=150 align=right>

- 无需配置NavGraph即可实现页面跳转
- 支持多种launchMode
- 支持Transiton、SharedElement动画
- Fragment之间更高效的通信方式
- 支持backPress事件拦截、滑动返回


## Installation

```groovy
implementation 'com.github.fragivity:core:$latest_version'
```

## Quick start

### 1. declare NavHostFragment in layout
Like Navigation, Fragivity needs a NavHostFragment as the host of ChildFragments

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

    }
}
```
### 3. navigate to any target Fragment 
```kotlin
//in HomeFragment
val bundle = bundleOf(KEY_ARGUMENT1 to "Hello", KEY_ARGUMENT2 to "World")
navigator.push(TargetFragment::class, bundle)
```

## Launch Mode
Support multiple startup modes
```kotlin
navigator.push(TargetFragment::class, bundle) {
  launchMode = LaunchMode.STANDARD //default
  //launchMode = LaunchMode.SINGLE_TOP
  //launchMode = LaunchMode.SINGLE_TASK
}
```

## Transition Animation
```kotlin
navigator.push(LaunchModeFragment::class) {
    enterAnim = R.anim.slide_in
    exitAnim = R.anim.slide_out
    popEnterAnim = R.anim.slide_in_pop
    popExitAnim = R.anim.slide_out_pop
}
```


## Communication
You can simply setup communication between two fragments
### 1. start target Fragment with a callback
```kotlin
class HomeFragment : Fragment(){
  private val cb: (Int) -> Unit = { checked ->
    //...
  }

  //...

  navigator.push {
      TargetFragment(cb)
  }
  //...
}
```
### 2. callback to source Fragment
```kotlin
class TargetFragment(val cb: (Int) -> Unit : Fragment() {
    //...
    cb.invoke(xxx)
    //...
}

```


## Show Dialog
### 1. delclare a DialogFragment
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




## Deeplinks

### 1. add kapt dependencies
 ```groovy
kapt 'com.github.fragivity:processor:$latest_version'
```

### 2. declare URI with @Deeplink annotation
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

### 4. start Activity with uri

```kotlin
val intent = Intent(Intent.ACTION_VIEW, Uri.parse("myapp://fragitiy.github.com/"))
startActivity(intent)
```

## License
Fragivity is licensed under the [MIT License](LICENSE).

