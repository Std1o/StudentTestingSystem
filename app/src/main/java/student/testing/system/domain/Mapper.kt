package student.testing.system.domain

interface Mapper<I, O> {
    fun map(input: I): O
}