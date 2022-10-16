package com.kiryl.mathworkout.math.viewmodel.general

import android.content.Context
import com.kiryl.mathworkout.math.viewmodel.general.model.MathInstance
import com.kiryl.mathworkout.services.data.pref.AppData
import kotlin.math.pow
import kotlin.random.Random.Default.nextInt

class TaskGeneration(context: Context,private var mathInstance: MathInstance){
    private val appData = AppData(context)

    private var a = 0
    private var b = 0

    private var task = ""
    private var answer = 0

    companion object {
        const val RANGE_OF_OPERATIONS = 20

        const val DIFFICULTY_BABY = "Baby"
        const val DIFFICULTY_EASY = "Easy"
        const val DIFFICULTY_NORMAL = "Normal"
        const val DIFFICULTY_HARD = "Hard"
    }

    fun getTask(): String {
        task()
        return task
    }

    fun getAnswer(): Int{
        return answer
    }

    private fun task() {
        val difficulty = appData.getDifficulty()

        if (difficulty == DIFFICULTY_BABY) babyDifficulty()
        if (difficulty == DIFFICULTY_EASY) easyDifficulty()
        if (difficulty == DIFFICULTY_NORMAL) normalDifficulty()
        if (difficulty == DIFFICULTY_HARD) hardDifficulty()

    }

    private fun babyDifficulty() {
        val operator = nextInt(0, 2)

        if (operator == 0) generateAddition(RANGE_OF_OPERATIONS)
        if (operator == 1) generateSubtraction(RANGE_OF_OPERATIONS)
    }

    private fun easyDifficulty() {
        val operator = nextInt(0, 4)

        if (operator == 0) generateAddition(RANGE_OF_OPERATIONS * 5)
        if (operator == 1) generateSubtraction(RANGE_OF_OPERATIONS * 5)
        if (operator == 2) generateMultiplication(RANGE_OF_OPERATIONS * 5)
        if (operator == 3) generateDivision(RANGE_OF_OPERATIONS * 5)
    }

    private fun normalDifficulty(){
        val operator = nextInt(0, 6)

        if (operator == 0) generateAddition(RANGE_OF_OPERATIONS * 8)
        if (operator == 1) generateSubtraction(RANGE_OF_OPERATIONS * 8)
        if (operator == 2) generateMultiplication(RANGE_OF_OPERATIONS * 6)
        if (operator == 3) generateDivision(RANGE_OF_OPERATIONS * 6)
        if (operator == 4) generatePower(RANGE_OF_OPERATIONS * 8)
        if (operator == 5) generateRoot(RANGE_OF_OPERATIONS * 8)

    }

    private fun hardDifficulty(){
        val operator = nextInt(0, 6)

        if (operator == 0) generateAddition(RANGE_OF_OPERATIONS * 16)
        if (operator == 1) generateSubtraction(RANGE_OF_OPERATIONS * 16)
        if (operator == 2) generateMultiplication(RANGE_OF_OPERATIONS * 12)
        if (operator == 3) generateDivision(RANGE_OF_OPERATIONS * 12)
        if (operator == 4) generatePower(RANGE_OF_OPERATIONS * 16)
        if (operator == 5) generateRoot(RANGE_OF_OPERATIONS * 16)

    }



    private fun generateAddition(range: Int) {
        a = nextInt(0, range + 1)
        b = nextInt(0, range + 1)

        if((a + b) > range) {
            a = nextInt(0, range/10)
            b = nextInt(0, range/10)
        }

        task = "$a + $b ="
        answer = a + b
        mathInstance.additionUsed++
    }

    private fun generateSubtraction(range: Int) {
        a = nextInt(0, range + 1)
        b = nextInt(0, range + 1)


        if (0 > a - b ) {
            val tempA = a
            val tempB = b
            b = tempA
            a = tempB
        }

        task = "$a - $b ="
        answer = a - b

        mathInstance.subtractionUsed++
    }

    private fun generateMultiplication(range: Int) {
        a = nextInt(0, range / 10)
        b = nextInt(0, range / 10)

        task = "$a × $b ="
        answer = a * b
        mathInstance.multiplicationUsed++
    }

    private fun generateDivision(range: Int) {
        a = nextInt(1, range / 10)
        b = nextInt(1, range / 10)
        val c = nextInt(1, range / 10)
        a = b * c
        answer = c
        task = "$a ÷ $b ="
        mathInstance.divisionUsed++
    }

    private fun generatePower(range: Int) {
        a = nextInt(0, range/10)
        b = nextInt(1, 4)

        val c = a.toDouble().pow(b.toDouble())
        answer = c.toInt()

        if (c > range) b--



        task = "$a^$b ="
        mathInstance.powerUsed++
    }

    private fun generateRoot(range: Int) {
        a = nextInt(0, range/10)
        b = a
        a *= a

        task = "√$a ="
        answer = b
        mathInstance.powerUsed++
    }
}