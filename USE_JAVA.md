# Using in Java


### Load root in NavHostFramgent 

```java
//loadRoot with class
Fragivity.loadRoot(navHostFragment, HomeFragment.class);

//loadRoot with factory
Fragivity.loadRoot(navHostFragment, HomeFragment.class, () -> {
                    HomeFragment fragment = new HomeFragment();
                    return fragment;
                }));

```

## Navigate to destination fragment

```java
class MyFragment extends Fagment {

    //navigate with class
    Fragivity.of(this).push(DestinationFragment.class);

    //navigate with NavOptions
    Bundle bundle = new Bundle();
    bundle.putString("key", "value");
    Fragivity.of(this).push(DestinaionFragment.class,
            navOptionsBuilder()
                .setArguments(bundle)
                .setEnterAnim(R.anim.slide_in)
                .setExitAnim(R.anim.slide_out)
                .setPopEnterAnim(R.anim.slide_in_pop)
                .setPopExitAnim(R.anim.slide_out_pop)
                .setLaunchMode(LaunchMode.STANDARD)
                .build());


    //navigate with factory
    Fragivity.of(this).push(DestFragment.class, () -> {
        return new DestFragment();
    }, navOptionsBuilder()
        .setArguments(bundle)
        .build());

}

```

## Pop
```java
Fragivity.of(fragment).pop();
```


## Show dialog
```java
Fragivity.of(fragment).showDialog(DialogFragment.class, bundle);
```

## Enable SwipeBack

```java
class MyFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SwipeBackUtil.getSwipeBackLayout(this).setEnableGesture(true);
    }
}
```






