fun intMatrixOf(n: Int, m: Int, vararg values: Int) = IntMatrix(n, m, values)
class IntMatrix(val n: Int, val m: Int, private val values: IntArray) {

    private fun absoluteIndex(i: Int, j: Int) = i * m + j
    private fun row(ij: Int) = ij / m
    private fun col(ij: Int) = ij - row(ij) * m

    operator fun get(i: Int, j: Int) = values[absoluteIndex(i, j)]
    operator fun set(i: Int, j: Int, value: Int) {
        values[absoluteIndex(i, j)] = value
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntMatrix

        if (n != other.n) return false
        if (m != other.m) return false
        if (!values.contentEquals(other.values)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = n
        result = 31 * result + m
        result = 31 * result + values.contentHashCode()
        return result
    }

    override fun toString() = buildString {
        for (row in 0 until n) {
            for (col in 0 until m) {
                append(values[absoluteIndex(row, col)])
                append(' ')
            }
            appendLine()
        }
    }

}