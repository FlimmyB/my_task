fun main(asd: Array<String>) {
    var kek = Array<UrPair>(0, { UrPair("", "") })

    kek.forEach { println(it) }
}

data class UrPair(val key: String, val value: String)
