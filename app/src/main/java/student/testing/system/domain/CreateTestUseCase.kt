package student.testing.system.domain

import student.testing.system.domain.states.TestCreationState
import student.testing.system.models.Question
import student.testing.system.models.Test
import student.testing.system.models.TestCreationReq
import javax.inject.Inject

class CreateTestUseCase @Inject constructor(private val repository: MainRepository,) {

    suspend operator fun invoke(testCreationReq: TestCreationReq): TestCreationState<Test> {
        if (testCreationReq.name.isEmpty()) return TestCreationState.EmptyName
        if (testCreationReq.questions.isEmpty()) return TestCreationState.NoQuestions // TODO обрабатывать этот стейт
        return repository.createTest(testCreationReq)
    }
}