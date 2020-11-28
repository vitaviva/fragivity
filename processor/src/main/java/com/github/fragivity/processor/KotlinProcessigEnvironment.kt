package com.github.fragivity.processor

import java.util.*
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

interface KotlinProcessingEnvironment {
    val processingEnv: ProcessingEnvironment

    val options: Map<String, String> get() = processingEnv.options
    val messager: Messager get() = processingEnv.messager
    val filer: Filer get() = processingEnv.filer
    val elementUtils: Elements get() = processingEnv.elementUtils
    val typeUtils: Types get() = processingEnv.typeUtils
    val sourceVersion: SourceVersion get() = processingEnv.sourceVersion
    val locale: Locale get() = processingEnv.locale
}
