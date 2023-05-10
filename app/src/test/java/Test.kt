import android.provider.ContactsContract.CommonDataKinds.Email

val num : Int = 1;

class Test {
}


class User3(name: String) {
    constructor(name: String, count: Int) : this(name){

    }
}


class User2(name: String,count: Int) {

    var name = "lmj"
    var count = 100
    init {
        println("init 호출 주 생성자 매개변수 사용 : $name, $count")
    }

    fun someFun() {
        println("init 호출 주 생성자 매개변수 사용 : $name, $count")
    }
}
class User {
    var name1 = "rmj"
    var name = "rmj"
    constructor(name: String, name1: String) {
        this.name1 = name1
        this.name = name
    }
    fun someFun() {
        println("someFun()의 name : $name")
        println("someFun()의 name1 : $name1")
    }
}
//부모 클래스는 open설정 해야함
//protected는 자식 클래스에서만 사용가능
open class Super {
    open var superData = 10
    protected var protectedData = 20
    open fun superFun() {
        println("superFun 호출")
    }
}
class Sub : Super() {
    override var superData = 20
    override fun superFun() {
        println("superFun 호출")
        protectedData++
    }
}

class NonDataClass(val name:String, val pw:String) {

}

data class DataClass(val name:String, val pw:String, val email: String) {
    /*lateinit var email:String
    constructor(name: String, pw: String, email: String) : this(name, pw) {
        this.email = email
    }*/
}

open class Super2 {
    open var data = 10
    open fun some() {
        println(" I am Super2 : $data")
    }
}

val obj = object : Super2() {
    override var data = 20
    override fun some() {
        println("I am Super2 재사용한 값 : $data")
    }
}

class companionClassTest {
    companion object {
        var data = 10
        fun some() {
            println("data의 값 : $data")
        }
    }
}

fun testH(arg:(Int) -> Boolean):()->String {
    val result = if (arg(10)) {
        "valid"
    } else {
        "invalid"
    }
    return {"testH result 확인 : $result"}
}

fun main() {
    val data20:String? = "lmj"
    println("data20의 길이 : ${data20?.length ?: 0}")

    val result16 = testH({no -> no > 0  })
    println("result16의 값 조회 : $result16")

    val some2 = {no1:Int, no2:Int -> println("출력")
        no1 * no2
    }
    println("익명함수 출력 확인: ${some2(1,2)}")


    val result15 : (Int) -> Unit =  {println(it) }
    val z = result15(100)

    val result14 = {no1:Int -> println("no1 출력 : $no1") }
    val y = result14(100)

    fun some(no1:Int, no2:Int):Int {
        return no1 + no2
    }

    val result13 = {no1:Int, no2:Int -> no1 + no2}
    val x = result13(1,2)
    println("x의 값 : $x")

    companionClassTest.data
    companionClassTest.some()

    println(obj.data)
    obj.some()

    val data13 = DataClass("lmg", "1234", "email1")
    val data14 = DataClass("lmg", "1234", "email2")
    println("data13 의 주소값 : $data13")
    println("data14 의 주소값 : $data14")
    println("equals 이용한 값 비교 : ${data13.equals(data14)}")

    val non1 = NonDataClass("lmg", "1234")
    val non2 = NonDataClass("lmg", "1234")
    println("non1 의 주소값 : $non1")
    println("non2 의 주소값 : $non2")
    println("equals 이용한 값 비교 : ${non1.equals(non2)}")

    val obj = Sub()
    println( obj.superData)
    obj.superFun()

    val user2 = User2("rmj", 10)
    println("user2 사용 " + user2.name + ", " + user2.count)
    val user = User("류명조", "rmj")
    println("user의 name :  " + user.name)
    println("user의 name1 :  " + user.name1)
    user.someFun()

    var data12 = arrayOf<Int>(1,2,3)
    for ((index, value) in data12.withIndex()) {
        print("인덱스의 값")
        println(index)
        print("value의 값")
        println(value)
        if (index !== data12.size-1) print(",")
    }
    println()

    fun sum10 ():Int {
        val result = 0
        // in 1..10, in 1 until 10, in 1..10 sept 2
        // 10 downTo 1
        for (i in 1..10) {
            val sum = 0
            val result = sum+ i
            println("반복문 result의 값은 : $result")
        }
        return result
    }
    sum10()


    var data11 = arrayOf<Int>(1,2,3)
    for (i in data11.indices) {
        print(data11[i])
        if (i !== data11.size-1) print(",")
    }
    println()

    var data10 = 5

    var result10 = when {
        data10 < 10 -> "data10 < 10"
        else -> {
            "data10의 값은 ??"

        }
    }
    println("data10 조건으로 result10 출력하기 : $result10")

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