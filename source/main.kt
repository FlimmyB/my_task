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
    println("Search - finds pair")
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
    println("Type \"key *your key*\" or \"value *your value*\" split by exactly one space " +
            "to find all pairs which contain specified key or value")
    val comds = nextLine().split(" ")
    if (comds.size != 2) {
        println("Please split arguments correctly")
        return find()
    }
    val type = comds[0]
    val word = comds[1]
    if (type != "key" && type != "value") {
        println("Please, type \"key\" or \"value\"")
        return find()
    }
    var found = Array(0, { MyPair("", "") })
    pairs!!.forEach {
        if (type == "key") {
            if (word in it.key) found += it
        } else if (word in it.value) found += it
    }
    if (found.isNotEmpty()) {
        if (found.size == 1) println("I found one element:") else
            println("I found ${found.size} elements:")
        found.forEach { println("key:${it.key}    value:${it.value}") }
    } else println("Nothing found!")
}

fun main(args: Array<String>) {
    pairs = readCache()
    println("Enter your command. Type Help for list of commands")
    var workTime = true
    while (workTime) {
        when (nextLine().toLowerCase()) {
            "show" -> showAll()
            "help" -> showHelp()
            "add" -> addNew()
            "delete" -> delete()
            "search" -> find()
            "exit" -> workTime = false
            "your command" -> println("haha")
            "your command." -> println("haha")
            else -> println("No such command. Type Help for command list.")
        }
        if (workTime) println("Enter your command.")
    }
    writeCache(pairs!!)
}