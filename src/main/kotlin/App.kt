import java.io.File

const val SIZE = 9
val DEFAULT_DOM = intSetOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

fun rows(): List<IntArray> {
    val rows = List(SIZE) { IntArray(SIZE) }
    for (i in 0 until 9) {
        for (j in 0 until 9) {
            rows[i][j] = i * 9 + j
        }
    }
    return rows
}

fun cols(): List<IntArray> {
    val cols = List(SIZE) { IntArray(SIZE) }
    for (i in 0 until 9) {
        for (j in 0 until 9) {
            cols[j][i] = i * 9 + j
        }
    }
    return cols
}

fun blocks(): List<IntArray> = listOf(
    intArrayOf(0, 1, 2, 9, 10, 11, 18, 19, 20),
    intArrayOf(3, 4, 5, 12, 13, 14, 21, 22, 23),
    intArrayOf(6, 7, 8, 15, 16, 17, 24, 25, 26),
    intArrayOf(27, 28, 29, 36, 37, 38, 45, 46, 47),
    intArrayOf(30, 31, 32, 39, 40, 41, 48, 49, 50),
    intArrayOf(33, 34, 35, 42, 43, 44, 51, 52, 53),
    intArrayOf(54, 55, 56, 63, 64, 65, 72, 73, 74),
    intArrayOf(57, 58, 59, 66, 67, 68, 75, 76, 77),
    intArrayOf(60, 61, 62, 69, 70, 71, 78, 79, 80)
)

operator fun <T> Array<T>.get(i: Int, j: Int) = this[i * SIZE + j]

fun main(args: Array<String>) {
    val size = 9
    val variables = IntArray(size * size) { it }
    val domains = Array(size * size) { DEFAULT_DOM.clone() }

    val grid = File(args[0]).readText()
    for ((i, line) in grid.split("\n").withIndex()) {
        for ((j, char) in line.withIndex()) {
            when (char) {
                in '1'..'9' -> domains[i, j].clean().add(char.code - '0'.code)
            }
        }
    }

    val constraints = mutableListOf<Constraint>()
    for (row in rows()) {
        constraints += AllDifferent(row.map { variables[it] }.toIntArray())
    }
    for (col in cols()) {
        constraints += AllDifferent(col.map { variables[it] }.toIntArray())
    }
    for (block in blocks()) {
       constraints += AllDifferent(block.map { variables[it] }.toIntArray())
    }

    forwardChecking(CSP(variables, domains, constraints)) { solution ->
        displaySolution(solution)
        println("=====================")
        true
    }
}

fun displaySolution(solution: IntArray) {
    for (i in 0 until SIZE) {
        if (i > 0 && i % 3 == 0) {
            println("------+-------+------")
        }
        for (j in 0 until SIZE) {
            if (j > 0 && j % 3 == 0) {
                print("| ")
            }
            print(solution[i * SIZE + j])
            print(' ')

        }
        println()
    }
}

class CSP(val variables: IntArray, val domains: Array<IntSet>, val constraints: List<Constraint>)

fun forwardChecking(
    csp: CSP,
    depth: Int = 0,
    values: IntArray = IntArray(csp.variables.size),
    instanciated: IntSet = IntSet(csp.variables.size),
    heuristic: (IntSlice) -> Unit = ::inputOrder,
    onSolution: (IntArray) -> Boolean = { true }
): Boolean {
    if (depth == csp.variables.size) {
        assert(csp.constraints.all(Check(values, instanciated)))
        return onSolution(values)
    } else {
        val currentVariable = csp.variables[depth]
        heuristic(IntSlice(csp.variables, depth + 1))
        val restore = values[currentVariable]
        instanciated.add(currentVariable)
        for (value in csp.domains[currentVariable]) {
            values[currentVariable] = value
            if (csp.constraints.all(Check(values, instanciated))) {
                if (forwardChecking(csp, depth + 1, values, instanciated, heuristic, onSolution)) {
                    return true
                }
            }
        }
        instanciated.rem(currentVariable)
        values[currentVariable] = restore
        return false
    }
}

fun inputOrder(slice: IntSlice) {}

class Check(private val values: IntArray, private val propagated: IntSet): (Constraint) -> Boolean {
    override fun invoke(c: Constraint) = c.isValid(values, propagated)
}