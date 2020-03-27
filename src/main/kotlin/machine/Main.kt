package machine

import java.util.*

enum class Status(val msg: String) {
    ACTION("Write action (buy, fill, take, remaining, exit): "),
    BEVERAGE_CHOICE("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: "),
    FILLING_WATER("Write how many ml of water do you want to add: "),
    FILLING_MILK("Write how many ml of milk do you want to adds: "),
    FILLING_BEANS("Write how many grams of coffee beans do you want to add: "),
    FILLING_CUPS("Write how many disposable cups of coffee do you want to add: ")
}

enum class Beverage(val num: String, val water: Int, val milk: Int, val beans: Int, val price: Int) {
    ESPRESSO("1", 250, 0, 16, 4),
    LATTE("2", 350, 75, 20, 7),
    CAPUCCINO("3", 200, 100, 12, 6),
    NULL("", 0, 0, 0, 0);

    companion object {
        fun getBeverageByValue(value: String): Beverage {
            for (beverage in values()) {
                if (beverage.num == value) return beverage
            }
            return NULL
        }
    }

}

class CoffeeMachine(private var water: Int, private var milk: Int, private var beans: Int, private var cups: Int, private var money: Int) {
    private var machineStatus: Status = Status.ACTION

    fun init(): CoffeeMachine {
        print(Status.ACTION.msg)
        return this
    }

    private fun status() {
        println("""The coffee machine has:
        $water of water
        $milk of milk
        $beans of coffee beans
        $cups of disposable cups
        $money of money
    
    """.trimIndent())
        changeStatus(Status.ACTION)
    }

    private fun take() {
        println("I gave you $$money")
        money = 0
        changeStatus(Status.ACTION)
    }

    private fun changeStatus(newStatus: Status) {
        machineStatus = newStatus
        print(newStatus.msg)
    }

    private fun fill() {
        changeStatus(Status.FILLING_WATER)
    }

    private fun fill(amount: Int) {
        when (machineStatus) {
            Status.FILLING_WATER -> {
                water += amount
                changeStatus(Status.FILLING_MILK)
            }
            Status.FILLING_MILK -> {
                milk += amount
                changeStatus(Status.FILLING_BEANS)
            }
            Status.FILLING_BEANS -> {
                beans += amount
                changeStatus(Status.FILLING_CUPS)
            }
            Status.FILLING_CUPS -> {
                cups += amount
                changeStatus(Status.ACTION)
            }
            else -> {
            }
        }
    }


    private fun buy() {
        changeStatus(Status.BEVERAGE_CHOICE)
    }

    private fun buy(beverageNum: String) {
        when (beverageNum) {
            Beverage.ESPRESSO.num, Beverage.LATTE.num, Beverage.CAPUCCINO.num -> {
                val beverage = Beverage.getBeverageByValue(beverageNum)
                val res = checkResources(beverage.water, beverage.milk, beverage.beans)
                if (res == "") {
                    money += beverage.price
                    println("I have enough resources, making you a coffee!")
                } else {
                    println(res)
                }
            }
            "back" -> {
            }
        }
        changeStatus(Status.ACTION)
    }

    private fun checkResources(waterNeed: Int, milkNeed: Int, beansNeed: Int): String {
        var res = "Sorry, not enough "
        when {
            water < waterNeed -> {
                res += "water!"
            }
            milk < milkNeed -> {
                res += "milk!"
            }
            beans < beansNeed -> {
                res += "beans!"
            }
            cups == 0 -> {
                res += "cups!"
            }
            else -> {
                water -= waterNeed
                milk -= milkNeed
                beans -= beansNeed
                cups--
                res = ""
            }
        }
        return res
    }

    fun processInput(input: String): Boolean {
        when (machineStatus) {
            Status.ACTION -> {
                if (input == "exit") return true
                when (input) {
                    "buy" -> buy()
                    "fill" -> fill()
                    "take" -> take()
                    "remaining" -> status()
                }

            }
            in Status.FILLING_WATER..Status.FILLING_CUPS -> {
                fill(input.toInt())
            }
            Status.BEVERAGE_CHOICE -> {
                buy(input)
            }
            else -> {
            }
        }
        return false
    }

}

fun main() {
    val scanner = Scanner(System.`in`)
    val machine = CoffeeMachine(400, 540, 120, 9, 550).init()
    do {
        val exit = machine.processInput(scanner.next())
    } while (!exit)


}





