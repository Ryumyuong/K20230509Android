val num : Int = 1;

class Test {
}

fun main() {
    var data9 : Any = 5
    when(data9) {
        is String -> println("data9 의 값은 문자열")
        10 -> println("data9 의 값은 10")
        in 1..10 -> println("data9의 값은 숫자 : $data9")
        else -> {
            println("data9의 값은 ??")

        }
    }

    var data8 = "abc"
    when(data8) {
        "10" -> println("data8 의 값은 10")
        "abc" -> println("data8 의 값은 abc")
        else -> {
            println("data8의 값은 ??")

        }
    }

    var data7 = 30
    when(data7) {
        10 -> println("data7 의 값은 10")
        20 -> println("data7 의 값은 20")
        else -> {
            println("data7의 값은 ??")

        }
    }

    var data5 = 20;
    var data6 = if(data5 >10) {
        println("표현식 확인")
        30
    }else {

    }
    println("data6 : $data6")

    var map = mapOf<String, String>(Pair("one", "hello"), "two" to "2")
    println("""
        list size: ${map.size}
        list data : ${map.get("one")}, ${map.get("two")}
    """.trimIndent())

//  가변
    var mL = mutableListOf<Int>(1,2,3)
    mL.add(3,100)
    println("""
        list size: ${mL.size}
        list data : 인덱스 3 ${mL[3]}
    """.trimIndent())
//  불변
    var list = listOf<Int>(1, 2, 3)
    println("""
        list size: ${list.size}
        list data : ${list[0]}, ${list.get(1)}
    """.trimIndent())

    val data4 = intArrayOf(1, 2, 3)
    val data3 = arrayOf<Int>(1, 2, 3)
    println("data3의 값 조회 : ${data3[0]}")
    println("data4의 값 조회 : ${data4[0]}")

    val data2 : IntArray = IntArray(3,{0})
    data2[0] = 100
    println("data2의 값 조회 : ${data2[0]}")

    val data1 :Array<Int> = Array(3,{0})
    println("data1의 값 조회 : ${data1[0]}")

    fun sum1():Nothing? {
        return null
    }

    val num3 : Any = "이상용"
    fun sum2(no:Int, no2: Int) {
        val result = no + no2
        println("no + no2 = $result")
    }
    sum2(10,20)

    fun sum(no:Int):Int {
        var sum = 0
        for (i in 1..no) {
            sum += i
        }
        return sum
    }
    val result = sum(10)
    println("result의 결과값 : $result")

    println("hi android")
    println("num의 값 : $num")
}