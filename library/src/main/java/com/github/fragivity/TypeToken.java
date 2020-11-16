package com.github.fragivity;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class TypeToken<T> extends TypeCapture<T> implements Serializable {

    private final Type runtimeType;

    protected TypeToken() {
        this.runtimeType = capture();
        checkState(
                !(runtimeType instanceof TypeVariable),
                "Cannot construct a TypeToken for a type variable.\n"
                        + "You probably meant to call new TypeToken<%s>(getClass()) "
                        + "that can resolve the type variable for you.\n"
                        + "If you do need to create a TypeToken of a type variable, "
                        + "please use TypeToken.of() instead.",
                runtimeType);
    }

    public Type getType() {
        return this.runtimeType;
    }

    private static void checkState(
            boolean b, String errorMessageTemplate, Object p1) {
        if (!b) {
            throw new IllegalStateException(format(errorMessageTemplate, p1));
        }
    }

    static String format(@Nullable String template, @Nullable Object... args) {
        template = String.valueOf(template); // null -> "null"

        args = args == null ? new Object[]{"(Object[])null"} : args;

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template, templateStart, template.length());

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}