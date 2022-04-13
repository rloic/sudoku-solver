class IntSlice(private val backed: IntArray, private val start: Int = 0, end: Int = backed.size) {

    val size = end - start

    operator fun get(i: Int): Int {
        assert(i in 0 until size)
        return backed[start + i]
    }

    operator fun set(i: Int, value: Int) {
        assert(i in 0 until size)
        backed[start + i] = value
    }

}