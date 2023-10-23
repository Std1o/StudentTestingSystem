package stdio.godofappstates.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Paths

internal class StatesViewModelKClassVisitor(
    private val codeGenerator: CodeGenerator,
    private val dependencies: Dependencies,
    private val operationStatePackage: String,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {

        val packageName = "godofappstates.presentation.viewmodel"

        val outputStream: OutputStream = codeGenerator.createNewFile(
            dependencies = dependencies,
            packageName,
            fileName = "StatesViewModel"
        )

        val content = this.javaClass.classLoader.getResource("StatesViewModel.txt")?.readText()

        outputStream.write(
            ("""
    |package $packageName

    |import student.testing.system.domain.states.LoadableData
    |import student.testing.system.domain.states.OperationState
    """.trimMargin() + "\n" + content).trimMargin().toByteArray()
        )

    }
}