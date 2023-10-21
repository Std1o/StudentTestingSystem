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
import stdio.godofappstates.annotations.LoadableData
import stdio.godofappstates.annotations.OperationState
import stdio.godofappstates.visitors.LoadableDataKClassVisitor
import stdio.godofappstates.visitors.OperationStateKClassVisitor

class OperationStateProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        // OperationState
        val symbolsOperationState = resolver
            .getSymbolsWithAnnotation(OperationState::class.qualifiedName!!)
        symbolsOperationState.filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                it.accept(OperationStateKClassVisitor(codeGenerator, logger, dependencies), Unit)
            }

        // LoadableData
        val symbolsLoadableData = resolver
            .getSymbolsWithAnnotation(LoadableData::class.qualifiedName!!)
        symbolsLoadableData.filter { it is KSClassDeclaration && it.validate() }
            .forEach {
                it.accept(LoadableDataKClassVisitor(codeGenerator, logger, dependencies), Unit)
            }

        val symbolsOperationStateList = symbolsOperationState.toList()
        val symbolsLoadableDataList = symbolsLoadableData.toList()
        // Processor is runs twice. First runs with annotations and second start without.
        // That's why we can't ruin building relying on annotation symbols emptiness
        if ((symbolsOperationStateList + symbolsLoadableDataList).isNotEmpty()) {
            if (symbolsOperationStateList.isEmpty()) logger.error(Constants.NO_OPERATION_STATE_ANNOTATION)
            if (symbolsLoadableDataList.isEmpty()) logger.error(Constants.NO_LOADABLE_DATA_ANNOTATION)
        }

        val unableToProcessOperationsState = symbolsOperationState.filterNot { it.validate() }
        val unableToProcessLoadableData = symbolsLoadableData.filterNot { it.validate() }
        return unableToProcessOperationsState.toList() + unableToProcessLoadableData.toList()
    }
}