package com.github.fragivity.processor


import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Completion
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

/**
 * An [AbstractProcessor] that overrides every method to provide correct Kotlin types.
 *
 * Implements [KotlinProcessingEnvironment] for ease of use and extension.
 */
abstract class KotlinAbstractProcessor : AbstractProcessor(), KotlinProcessingEnvironment {

    override fun getSupportedOptions(): Set<String> = super.getSupportedOptions()
    override fun getSupportedSourceVersion(): SourceVersion = super.getSupportedSourceVersion()
    override fun getSupportedAnnotationTypes(): Set<String> = super.getSupportedAnnotationTypes()
    override fun init(processingEnv: ProcessingEnvironment) = super.init(processingEnv)

    override fun getCompletions(
        element: Element?,
        annotation: AnnotationMirror?,
        member: ExecutableElement?,
        userText: String?
    ): Iterable<Completion> =
        super.getCompletions(element, annotation, member, userText)

    override abstract fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean

    /** @see [AbstractProcessor.processingEnv] **/
    override val processingEnv: ProcessingEnvironment get() = super.processingEnv

    /**
     * Returns the directory where generated Kotlin sources should be placed in order to be compiled.
     *
     * If `null`, then this processor is probably not being run through kapt.
     */
    val generatedDir: File? get() = options[kaptGeneratedOption]?.let(::File)
}

private const val kaptGeneratedOption = "kapt.kotlin.generated"
