package student.testing.system.domain.usecases

import student.testing.system.domain.repository.TestsRepository
import student.testing.system.domain.states.operationStates.TestCreationState
import student.testing.system.models.Test
import student.testing.system.models.TestCreationReq
import javax.inject.Inject

class CreateTestUseCase @Inject constructor(private val repository: TestsRepository,) {

    suspend operator fun invoke(testCreationReq: TestCreationReq): TestCreationState<Test> {
        if (testCreationReq.name.isEmpty()) return TestCreationState.EmptyName
        if (testCreationReq.questions.isEmpty()) return TestCreationState.NoQuestions // TODO обрабатывать этот стейт
        return repository.createTest(testCreationReq)
    }
}