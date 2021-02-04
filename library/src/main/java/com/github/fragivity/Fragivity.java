package com.github.fragivity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Supplier;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentProviderMap;
import androidx.navigation.fragment.NavHostFragment;


import com.github.fragivity.dialog.DialogUtil;

import java.util.Objects;

import kotlin.jvm.JvmClassMappingKt;
import kotlin.reflect.KClass;

public class Fragivity {

    public static <T extends Fragment> void loadRoot(
            @NonNull NavHostFragment navHost,
            @NonNull Class<T> fragmentClazz) {
        KClass<T> kClass = toKClass(fragmentClazz);
        FragivityUtil.loadRoot(navHost, kClass);
    }

    public static <T extends Fragment> void loadRoot(
            @NonNull NavHostFragment navHost,
            @NonNull Class<T> fragmentClazz,
            @NonNull Supplier<T> factory) {
        KClass<T> kClass = toKClass(fragmentClazz);
        FragmentProviderMap.INSTANCE.put(Objects.requireNonNull(kClass.getQualifiedName()), () -> factory.get());
        FragivityUtil.loadRoot(navHost, kClass);
    }


    public static Navigator of(@NonNull Fragment fragment) {
        return new Navigator(fragment);
    }

    public static class Navigator {

        private MyNavHost myNavHost;

        public Navigator(Fragment fragment) {
            myNavHost = FragivityUtil.getNavigator(fragment);
        }

        public static NavOptionsBuilder navOptionsBuilder() {
            return new NavOptionsBuilder();
        }

        public <T extends Fragment> void push(@NonNull Class<T> fragmentClazz) {
            push(fragmentClazz, null);
        }

        public <T extends Fragment> void push(@NonNull Class<T> fragmentClazz,
                                              @Nullable NavOptions navOptions) {
            FragivityUtil.pushInternal(myNavHost, toKClass(fragmentClazz), navOptions);
        }

        public <T extends Fragment> void push(@NonNull Class<T> fragmentClazz,
                                              @NonNull Supplier<T> factory,
                                              @Nullable NavOptions navOptions) {
            FragmentProviderMap.INSTANCE.put(
                    Objects.requireNonNull(toKClass(fragmentClazz).getQualifiedName()), () -> factory.get());
            push(fragmentClazz, navOptions);
        }

        public <T extends DialogFragment> void showDialog(@NonNull Class<T> fragmentClazz, Bundle args) {
            DialogUtil.showDialog(myNavHost, toKClass(fragmentClazz), args);
        }

    }

    private static <T> KClass<T> toKClass(Class<T> clazz) {
        return JvmClassMappingKt.getKotlinClass(clazz);
    }

}

