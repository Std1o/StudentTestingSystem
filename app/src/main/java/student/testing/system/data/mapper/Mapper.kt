package student.testing.system.data.mapper

interface Mapper<T, R> {

    fun map(input: T): R
}