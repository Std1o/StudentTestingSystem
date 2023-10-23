package stdio.godofappstates.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import stdio.godofappstates.Constants
import stdio.godofappstates.UsedPackages.loadableDataPackage
import stdio.godofappstates.UsedPackages.operationStatePackage
import stdio.godofappstates.annotations.LoadableData
import stdio.godofappstates.annotations.OperationState
import stdio.godofappstates.visitors.LoadableDataKClassVisitor
import stdio.godofappstates.visitors.OperationStateKClassVisitor

class AppStatesProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())
        val viewModelPackage = resolver.getAllFiles()
            .firstOrNull { it.fileName.contains("ViewModel") }?.packageName?.asString()
        if (viewModelPackage == null) logger.warn(Constants.VIEW_MODEL_PACKAGE_NOT_FOUND)

        // OperationState
        val symbolsOperationState = resolver
            .getSymbolsWithAnnotation(OperationState::class.qualifiedName!!)
            .filter { it is KSClassDeclaration && it.validate() }
        val operationStateParents = getStrParents(symbolsOperationState)
        symbolsOperationState.firstOrNull()?.accept(
            OperationStateKClassVisitor(
                codeGenerator, logger, dependencies, operationStateParents
            ) { mPackage ->
                operationStatePackage = mPackage
            },
            Unit
        )

        // LoadableData
        val symbolsLoadableData = resolver
            .getSymbolsWithAnnotation(LoadableData::class.qualifiedName!!)
            .filter { it is KSClassDeclaration && it.validate() }
        val loadableDataParents = getStrParents(symbolsLoadableData)
        symbolsLoadableData.firstOrNull()?.accept(
            LoadableDataKClassVisitor(
                codeGenerator, logger, dependencies, loadableDataParents
            ) { mPackage ->
                loadableDataPackage = mPackage
            },
            Unit
        )

        val unableToProcessOperationsState = symbolsOperationState.filterNot { it.validate() }
        val unableToProcessLoadableData = symbolsLoadableData.filterNot { it.validate() }
        return unableToProcessOperationsState.toList() + unableToProcessLoadableData.toList()
    }

    private fun getStrParents(annotatedClasses: Sequence<KSAnnotated>): String {
        val parents = annotatedClasses.toList().map { it.toString() }
        var strParents = ""
        parents.forEach { strParents += "$it<R>, " }
        strParents = strParents.dropLast(2)
        return strParents
    }
}