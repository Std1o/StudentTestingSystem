package student.testing.system.data

import student.testing.system.models.*
import javax.inject.Inject

class MainRemoteData @Inject constructor(private val mainService : MainService) {
    suspend fun auth(request: String) = mainService.auth(request)
    suspend fun signUp(request: SignUpReq) = mainService.signUp(request)
    suspend fun getCourses() = mainService.getCourses()
    suspend fun createCourse(request: CourseCreationReq) = mainService.createCourse(request)
    suspend fun joinCourse(courseCode: String) = mainService.joinCourse(courseCode)
    suspend fun deleteCourse(courseId: Int, courseOwnerId: Int) = mainService.deleteCourse(courseId, courseOwnerId)
    suspend fun getTests(courseId: Int) = mainService.getTests(courseId)
    suspend fun createTest(request: TestCreationReq) = mainService.createTest(request)
    suspend fun deleteTest(testId: Int, courseId: Int, courseOwnerId: Int) = mainService.deleteTest(testId, courseId, courseOwnerId)
    suspend fun calculateResult(testId: Int, courseId: Int, request: List<UserQuestion>) = mainService.calculateResult(testId, courseId, request)
    suspend fun calculateDemoResult(courseId: Int, testId: Int, courseOwnerId: Int, request: List<UserQuestion>) = mainService.calculateDemoResult(courseId, testId, courseOwnerId, request)
    suspend fun getResult(testId: Int, courseId: Int) = mainService.getResult(testId, courseId)
    suspend fun getResults(testId: Int, courseId: Int, courseOwnerId: Int, showOnlyMaxResults: Boolean) = mainService.getResults(testId, courseId, courseOwnerId, showOnlyMaxResults)
    suspend fun addModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) = mainService.addModerator(courseId, courseOwnerId, moderatorId)
    suspend fun deleteModerator(courseId: Int, courseOwnerId: Int, moderatorId: Int) = mainService.deleteModerator(courseId, courseOwnerId, moderatorId)
    suspend fun deleteParticipant(courseId: Int, courseOwnerId: Int, participantId: Int) = mainService.deleteParticipant(courseId, courseOwnerId, participantId)
}