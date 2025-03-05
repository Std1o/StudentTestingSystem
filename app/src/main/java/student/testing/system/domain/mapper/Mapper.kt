package student.testing.system.domain.mapper

interface Mapper<I, O> {
    fun map(input: I): O
}