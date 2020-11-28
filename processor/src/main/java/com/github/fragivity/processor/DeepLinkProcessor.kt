package com.github.fragivity.processor

import com.github.fragivity.annotation.DeepLink
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.TypeElement

@Suppress("unused")
@AutoService(Processor::class)
class DeepLinkProcessor : KotlinAbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(ANNOTATION.canonicalName)
    }

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (annotations.isEmpty()) {
            return false
        }
        val map = mutableMapOf<String, String>()
        roundEnv.getElementsAnnotatedWith(ANNOTATION).forEach {
            if (it !is TypeElement) return@forEach
            val className = it.qualifiedName.toString()
            val key: String = it.getAnnotation(ANNOTATION).uri
            map[key] = "$className::class"
        }

        FileSpec.builder("com.github.fragivity", "_RouteLoader")
            .addFunction(
                FunSpec.builder("initRoute").apply {
                    map.forEach {
                        addStatement(
                            """ $ADD_FUN("${it.key}", ${it.value})  """
                        )
                    }
                }.build()
            )
            .addImport(ADD_FUN_PKG, ADD_FUN)
            .build()
            .writeTo(generatedDir!!)

        return true

    }

    companion object {
        private val ANNOTATION = DeepLink::class.java
        private const val ADD_FUN = "addRoute"
        private const val ADD_FUN_PKG = "com.github.fragivity.deeplink"
    }
}