import java.io.*
import java.util.*
import com.google.gson.*

var br: BufferedReader = BufferedReader(InputStreamReader(System.`in`))
var st: StringTokenizer = StringTokenizer("")

fun nextToken(): String {
    while (!st.hasMoreTokens()) st = StringTokenizer(br.readLine())
    return st.nextToken()
}

fun nextLine(): String {
    return br.readLine()
}

data class MyPair(val key: String, val value: String)

var pairs: Array<MyPair>? = null

fun writeCache(values: Array<MyPair>) {
    with(PrintWriter("pairs.json")) {
        println(Gson().toJson(values))
        close()
    }
}

fun readCache(): Array<MyPair> {
    val jreader = BufferedReader(FileReader("pairs.json"))
    var values = ""
    jreader.forEachLine { values += it }
    return Gson().fromJson(values, Array<MyPair>::class.java)
}

fun addNew() {
    println("Enter key and value split by space or newline")
    val newPair = MyPair(key = nextToken(), value = nextToken())
    pairs = pairs!! + newPair
    println("Done!")
    writeCache(pairs!!)
}

fun showAll() {
    if (pairs!!.isNotEmpty()) pairs!!.forEach { println("${it.key} ${it.value}") } else println("Nothing to show!")
}

fun showHelp() {
    println("All commands don't require any arguments.")
    println("Show - shows you all pairs")
    println("Add - adds new pair")
    println("Delete - deletes pair")
    println("Find - finds pair")
    println("Exit - closes program")
}

fun delete() {
    println("Type \"key *your key*\" or \"value *your value*\" split by exactly one space " +
            "to delete all pairs where specified key or value equals the saved one")
    val comds = nextLine().split(" ")
    if (comds.size != 2) {
        println("Please split arguments correctly")
        return delete()
    }
    val type = comds[0]
    val word = comds[1]
    var new_pairs = Array(0, { MyPair("", "") })
    var deleted = Array(0, { MyPair("", "") })

    if (type == "key" || type == "value") {
        for (i in 0 until pairs!!.size) {
            if (type == "key") {
                if (pairs!![i].key != word) {
                    new_pairs += pairs!![i]
                } else {
                    deleted += pairs!![i]
                }
            } else {
                if (pairs!![i].value != word) {
                    new_pairs += pairs!![i]
                } else {
                    deleted += pairs!![i]
                }
            }
        }
    } else {
        println("Please, type \"key\" or \"value\"")
        delete()
        return
    }
    pairs = new_pairs
    writeCache(pairs!!)
    when {
        deleted.isEmpty() -> println("No pairs deleted")
        deleted.size == 1 -> {
            println("One pair with $word deleted:")
            println("key: ${deleted[0].key}   value:${deleted[0].value}")
        }
        else -> {
            println("${deleted.size} ${type}s with $word deleted:")
            deleted.forEach {
                println("key: ${it.key}   value:${it.value}")
            }
        }
    }
}

fun find() {
    println("Enter a word by which I shall search")
    val comds = nextLine().trim()

    var foundByKey = Array(0, { MyPair("", "") })
    var foundByValue = Array(0, { MyPair("", "") })
    pairs!!.forEach {
        if (comds in it.key) foundByKey += it
        if (comds in it.value) foundByValue += it
    }
    when {
        foundByKey.isEmpty() -> println("No pairs contain this word in keys")
        foundByKey.size == 1 -> println("One pair has this key:")
        else -> println("${foundByKey.size} pairs have this word in key:")
    }
    foundByKey.forEach { println("key:${it.key}    value:${it.value}") }
    println()
    when {
        foundByValue.isEmpty() -> println("No pairs contain this word in values")
        foundByValue.size == 1 -> println("One pair has this value:")
        else -> println("${foundByValue.size} pairs have this word in key:")
    }
    foundByValue.forEach { println("key:${it.key}    value:${it.value}") }


}

fun main(args: Array<String>) {
    pairs = readCache()
    println("Enter your command. Type Help for list of commands")
    var workTime = true
    while (workTime) {
        when (nextLine().toLowerCase().trim()) {
            "show" -> showAll()
            "help" -> showHelp()
            "add" -> addNew()
            "delete" -> delete()
            "find" -> find()
            "exit" -> workTime = false
            "your command" -> println("haha")
            "your command." -> println("haha")
            else -> println("No such command. Type Help for command list.")
        }
        if (workTime) println("Enter your command.")
    }
    writeCache(pairs!!)
}
