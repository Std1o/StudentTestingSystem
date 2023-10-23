package stdio.godofappstates.processors.operationState

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import stdio.godofappstates.UsedPackages.operationStatePackage
import stdio.godofappstates.Utils.getStrParents
import stdio.godofappstates.annotations.OperationState
import stdio.godofappstates.visitors.OperationStateKClassVisitor

class OperationStateProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {

        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        val symbols = resolver
            .getSymbolsWithAnnotation(OperationState::class.qualifiedName!!)
            .filter { it is KSClassDeclaration && it.validate() }
        val operationStateParents = getStrParents(symbols)
        symbols.firstOrNull()?.accept(
            OperationStateKClassVisitor(
                codeGenerator, logger, dependencies, operationStateParents
            ) { mPackage ->
                operationStatePackage = mPackage
            },
            Unit
        )
        return symbols.filterNot { it.validate() }.toList()
    }
}