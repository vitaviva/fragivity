package com.github.fragivity;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import kotlin.Pair;


public class NavOptionsBuilder {

    NavOptionsBuilder() {}

    private $NavOptionsDefault navOptions = new $NavOptionsDefault();

    @NonNull
    public NavOptionsBuilder setLaunchMode(LaunchMode mode) {
        navOptions.setLaunchMode(mode);
        return this;
    }

    @NonNull
    public NavOptionsBuilder setArguments(Bundle bundle) {
        navOptions.setArguments(bundle);
        return this;
    }

    @NonNull
    public NavOptionsBuilder setEnterAnim(@AnimRes @AnimatorRes int enterAnim) {
        navOptions.setEnterAnim(enterAnim);
        return this;
    }

    @NonNull
    public NavOptionsBuilder setExitAnim(@AnimRes @AnimatorRes int exitAnim) {
        navOptions.setExitAnim(exitAnim);
        return this;
    }

    @NonNull
    public NavOptionsBuilder setPopEnterAnim(@AnimRes @AnimatorRes int popEnterAnim) {
        navOptions.setPopEnterAnim(popEnterAnim);
        return this;
    }

    @NonNull
    public NavOptionsBuilder setPopExitAnim(@AnimRes @AnimatorRes int popExitAnim) {
        navOptions.setPopExitAnim(popExitAnim);
        return this;
    }

    @NonNull
    public NavOptionsBuilder setSharedElements(@NonNull Map<View, String> sharedElements) {
        List<Pair<View, String>> list = new ArrayList();
        for (Map.Entry<View, String> sharedElement : sharedElements.entrySet()) {
            View view = sharedElement.getKey();
            String name = sharedElement.getValue();
            if (view != null && name != null) {
                list.add(new Pair<>(view, name));
            }
        }
        navOptions.setSharedElements(list);
        return this;
    }


    public NavOptions build() {
        return navOptions;
    }

}