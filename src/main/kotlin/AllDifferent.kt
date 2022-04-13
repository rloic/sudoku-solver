class AllDifferent(
    private val variables: IntArray
) : Constraint {
    override fun isValid(values: IntArray, instanciated: IntSet): Boolean {
        for (i in 0 until variables.size) {
            if (variables[i] in instanciated) {
                for (j in i + 1 until variables.size) {
                    if (variables[j] in instanciated) {
                        if (values[variables[i]] == values[variables[j]]) return false
                    }
                }
            }
        }
        return true
    }
}