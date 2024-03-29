package com.sd.lib.service.compiler.processor

import com.google.devtools.ksp.isAbstract
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFile
import com.sd.lib.service.compiler.fIsAnnotationPresent
import com.sd.lib.service.compiler.fReplaceDot
import com.sd.lib.service.compiler.mapping.FService
import com.sd.lib.service.compiler.mapping.FServiceImpl
import com.sd.lib.service.compiler.mapping.LibPackage
import com.sd.lib.service.compiler.mapping.ModuleServiceInfo
import com.sd.lib.service.compiler.mapping.className
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

class FServiceImplProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FServiceImplProcessor(environment)
    }
}

private class FServiceImplProcessor(
    env: SymbolProcessorEnvironment
) : BaseProcessor(env) {

    private val _serviceHolder: MutableMap<KSClassDeclaration, MutableSet<KSClassDeclaration>> = hashMapOf()
    private val _createdHolder: MutableSet<KSClassDeclaration> = hashSetOf()

    override fun processImpl(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(FServiceImpl.fullName).toList()
        log("---------- $moduleName process symbols:${symbols.size} ----------")
        symbols.forEach {
            if (it is KSClassDeclaration) {
                findServiceInterface(it).also { service ->
                    addService(service, it)
                }
            }
        }
        createFiles()
        return listOf()
    }

    private fun addService(service: KSClassDeclaration, impl: KSClassDeclaration) {
        _serviceHolder.let { map ->
            val holder = map[service] ?: hashSetOf<KSClassDeclaration>().also {
                map[service] = it
            }
            holder.add(impl)
        }
    }

    private fun createFiles() {
        for ((key, value) in _serviceHolder) {
            createFile(
                service = key,
                listImpl = value,
            )
        }
        _serviceHolder.clear()
    }

    private fun createFile(
        service: KSClassDeclaration,
        listImpl: Set<KSClassDeclaration>,
    ) {
        if (_createdHolder.contains(service)) return
        _createdHolder.add(service)

        val filename = "${moduleName}_${service.qualifiedName!!.asString().fReplaceDot()}"
        log("createFile $filename impl:${listImpl.size}")

        val typeSpec = TypeSpec.classBuilder(filename)
            .addModifiers(KModifier.INTERNAL)
            .addAnnotation(
                AnnotationSpec.builder(ModuleServiceInfo.className())
                    .addMember("module = %S", moduleName)
                    .addMember("service = %S", service.qualifiedName!!.asString())
                    .addMember("impl = %S", listImpl.joinToString(separator = ",") { it.qualifiedName!!.asString() })
                    .build()
            )
            .build()

        val fileSpec = FileSpec.builder(LibPackage.registerModule, filename)
            .addType(typeSpec)
            .build()

        val listKsFile = mutableListOf<KSFile>()
        service.containingFile?.let { listKsFile.add(it) }
        listImpl.mapNotNullTo(listKsFile) { it.containingFile }

        fileSpec.writeTo(env.codeGenerator, true, listKsFile)
    }
}

private fun findServiceInterface(source: KSClassDeclaration): KSClassDeclaration {
    require(source.classKind == ClassKind.CLASS) { "@${FServiceImpl.simpleName} should be used in ClassKind.CLASS" }
    require(!source.isAbstract()) { "@${FServiceImpl.simpleName} should not be used in abstract class" }

    var ret: KSClassDeclaration? = null

    var current: KSClassDeclaration = source
    while (true) {
        val superInfo = current.getSuperInfo()
        val interfaces = superInfo.interfaces
        if (interfaces.isEmpty()) break

        for (item in interfaces) {
            if (item.fIsAnnotationPresent(FService.fullName)) {
                if (ret == null) {
                    ret = item
                } else {
                    error("More than one service interface present in ${source.qualifiedName?.asString()} : ${ret.qualifiedName?.asString()}, ${item.qualifiedName?.asString()}")
                }
            }
        }

        current = superInfo.parent ?: break
    }

    return checkNotNull(ret) {
        "Interface marked with annotation @${FService.simpleName} was not found in ${source.qualifiedName?.asString()} super types"
    }
}

private fun KSClassDeclaration.getSuperInfo(): SuperInfo {
    var parent: KSClassDeclaration? = null
    val interfaces = hashSetOf<KSClassDeclaration>()

    for (item in superTypes) {
        val declaration = item.resolve().declaration
        if (declaration !is KSClassDeclaration) continue
        when (declaration.classKind) {
            ClassKind.CLASS -> {
                check(parent == null)
                parent = declaration
            }

            ClassKind.INTERFACE -> interfaces.add(declaration)
            else -> {}
        }
    }
    return SuperInfo(
        parent = parent,
        interfaces = interfaces,
    )
}

private data class SuperInfo(
    val parent: KSClassDeclaration?,
    val interfaces: Collection<KSClassDeclaration>,
)