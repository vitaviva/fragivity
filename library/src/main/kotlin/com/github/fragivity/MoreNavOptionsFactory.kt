package com.github.fragivity

typealias MoreNavOptionsFactory = MoreNavOptionsBuilder.() -> Unit

fun MoreNavOptions.Companion.setFactory(factory: MoreNavOptionsFactory) {
    this.commonFactory = factory
}
