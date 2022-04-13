fun intSetOf(vararg values: Int): IntSet {
    val capacity = (values.maxOrNull() ?: -1) + 1
    val set = IntSet(capacity)
    for (value in values) {
        set.add(value)
    }

    return set
}
class IntSet(n: Int): IntIterable {

    private val pos = IntArray(n)
    private val elem = IntArray(n + 1)

    operator fun contains(n: Int): Boolean {
        val k = pos[n]
        return (k in 1..size && elem[k] == n)
    }

    val size: Int get() = elem[0]

    fun isEmpty() = size == 0
    fun isNotEmpty() = size != 0

    fun clean(): IntSet {
        elem[0] = 0
        return this
    }

    fun add(n: Int): IntSet {
        if (contains(n)) return this
        elem[0] += 1
        elem[elem[0]] = n
        pos[n] = elem[0]
        return this
    }

    fun rem(n: Int): Boolean {
        if (!contains(n)) return false
        val w = elem[elem[0]]
        elem[pos[n]] = w
        pos[w] = pos[n]
        pos[n] = 0
        elem[0] -= 1
        return true
    }

    fun clone(): IntSet {
        val clone = IntSet(pos.size)
        for (element in this) {
            clone.add(element)
        }
        return clone
    }

    override fun iterator() = Iterator(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntSet

        for (i in 1..size) {
            if (elem[i] !in other) return false
        }

        return true
    }

    override fun hashCode(): Int {
        var hash = 0
        for (i in 1..size) {
            hash = hash xor elem[i]
        }
        hash = hash shl 16
        hash = hash xor size
        return hash
    }

    override fun toString() = buildString {
        append('{')

        val iter = this@IntSet.iterator()
        if (iter.hasNext()) {
            append(iter.nextInt())
        }
        while (iter.hasNext()) {
            append(", ")
            append(iter.nextInt())
        }

        append('}')
    }

    class Iterator(private val set: IntSet): IntIterator() {
        private var i = 1
        override fun nextInt() = set.elem[i++]
        override fun hasNext() = i <= set.size
    }
}