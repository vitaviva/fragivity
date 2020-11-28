package com.github.fragivity.annotation

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class DeepLink(val uri: String)