interface Constraint {

    fun isValid(values: IntArray, instanciated: IntSet): Boolean

}