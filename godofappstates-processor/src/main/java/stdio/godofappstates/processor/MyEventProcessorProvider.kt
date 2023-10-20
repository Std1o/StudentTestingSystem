package stdio.godofappstates.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import stdio.godofappstates.processor.MyEventProcessor

class MyEventProcessorProvider : SymbolProcessorProvider {
    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return MyEventProcessor(
            environment.codeGenerator,
            environment.logger,
            environment.options
        )
    }
}