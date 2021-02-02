# Fragivity  :  Use Fragment like Activity

English | [中文文档](https://github.com/vitaviva/fragivity/blob/master/README_zh.md)

![Bintray](https://img.shields.io/bintray/v/vitaviva/maven/core)  ![Language](https://img.shields.io/badge/language-kotlin-green.svg)  ![License](https://img.shields.io/badge/License-MIT-blue.svg)

<img src="app/src/main/res/drawable-v24/ic_launcher.png" width=150 align=right>

Fragivity is a library used to build APP with "Single Activity + Multi-Fragments" Architecture

- **Reasonable Lifecycle：** Lifecycle is consistent with Activity when screen changed
- **Multiple LaunchModes：** Supports multiple modes, such as Standard, SingleTop and SingleTask
- **Transition animation：** Supports Transition or SharedElement animation when switching screens
- **Efficient communication：** Simple and direct communication based on callback
- **Friendly Backpress：** Supports onBackPressed interception and SwipeBack
- **Deep Links：** Routes to the specified screen by URI
- **Dialog：** Supports DialogFragment

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
        
        //or load root with factory
        //navHostFragment.loadRoot{ HomeFragment() }

    }
}
```
### 3. navigate to destination Fragment 
```kotlin
//in HomeFragment
val bundle = bundleOf(KEY_ARGUMENT1 to "arg1", KEY_ARGUMENT2 to "arg2")
navigator.push(DestinationFragment::class, bundle)
```

## Launch Mode
Support multiple launch modes
```kotlin
navigator.push(DestinationFragment::class, bundle) {
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

  navigator.push {
      DestinationFragment(cb)
  }
  //...
}
```
### 2. callback to source Fragment
```kotlin
class DestinationFragment(val cb: (Int) -> Unit : Fragment() {
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

## FAQ
[Frequently Asked Question](https://github.com/vitaviva/fragivity/blob/master/FAQ.md)

## License
Fragivity is licensed under the [MIT License](LICENSE).

