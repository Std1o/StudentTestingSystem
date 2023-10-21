package stdio.godofappstates.processor

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.google.devtools.ksp.validate
import stdio.godofappstates.annotations.MyEvent
import stdio.godofappstates.annotations.OperationState
import java.io.OutputStream

class MyEventProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger,
    private val options: Map<String, String>
): SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(OperationState::class.qualifiedName!!)
        val unableToProcess = symbols.filterNot { it.validate() }

        val dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray())

        symbols.filter { it is KSClassDeclaration && it.validate() }
            .forEach { it.accept(MyEventKClassVisitor(dependencies), Unit) }

        return unableToProcess.toList()
    }

    private inner class MyEventKClassVisitor(val dependencies: Dependencies) : KSVisitorVoid() {

        override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

            val packageName = classDeclaration.packageName.asString()

            if (classDeclaration.classKind != ClassKind.INTERFACE) {
                logger.error(
                    "||Class Annotated with OperationState should be interface", classDeclaration)
            }

            val className = classDeclaration.simpleName.getShortName()
            val classPackage = classDeclaration.packageName.asString() + "." + className

            logger.warn("package $classPackage")

            val interfaceName = classDeclaration.simpleName.getShortName()

            val outputStream: OutputStream = codeGenerator.createNewFile(
                dependencies = dependencies,
                packageName,
                fileName = "OperationState"
            )



            outputStream.write(
                """
    |package $packageName

    |import $classPackage
    |import android.os.Bundle

    |sealed interface OperationState<out R> : $interfaceName<R> {
    |
    |data class Success<out T>(
    |    val data: T
    ) : OperationState<T>
    |  
    |   /**
    | * Must be converted to Success with own local value in ViewModel
    | */
    |data class Empty(
    |    val code: Int
    ) : OperationState<Nothing>
    |
    | data class Error(val exception: String, val code: Int = -1) : OperationState<Nothing>
    | 
    |  object Loading : OperationState<Nothing>
    |}
    """.trimMargin().toByteArray()
            )

        }
    }
}

fun KSValueParameter.isNotKotlinPrimitive(): Boolean {

    return when (type.element?.toString()) {
        "String", "Int", "Short", "Number", "Boolean", "Byte", "Char", "Float", "Double", "Long", "Unit", "Any" -> false
        else -> true
    }
}

fun KSValueParameter.getPrimitiveTypeName(): String {

    return type.element?.toString() ?: throw IllegalAccessException()
}