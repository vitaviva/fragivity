package com.github.fragivity;


import androidx.annotation.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeCapture<T> {

    /**
     * Returns the captured type.
     */
    final Type capture() {
        Type superclass = getClass().getGenericSuperclass();
        checkArgument(superclass instanceof ParameterizedType, "%s isn't parameterized", superclass);
        return ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    private static void checkArgument(
            boolean b, @Nullable String errorMessageTemplate, @Nullable Object p1) {
        if (!b) {
            throw new IllegalArgumentException(TypeToken.format(errorMessageTemplate, p1));
        }
    }
}