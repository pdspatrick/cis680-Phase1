package com.patrick.mortgagecalc
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt


class Mortgage {
    var length: Int = 0
    var remainLength: Int = 0
    var custName: String = ""
    var initialBalance: Double = 0.0
    var currentBalance: Double = 0.0
    var downpay: Double = 0.0
    var downpercent: Double = 0.0
    var interestRate: Double = 0.0
    var downPayAsPercent: Boolean = false
    var amTableExist = false
    lateinit var amTableData: MutableList<MutableList<Float>>

    fun setDownPayment(payment: Double){
        if (payment <= 0){
            downPayAsPercent = false
            downpay = 0.0
            downpercent = 0.0
        }
        else if (payment > 100){ // odds are if a payment is less than $100 for a mortgage, the customer is entering a percentage
            downpay = payment
            downpercent = (payment / initialBalance)
            downPayAsPercent = false
            }
        else {
            if (payment < 1){
                downpay = (payment * initialBalance)
                downpercent = (payment * 100)
                downPayAsPercent = true
            }
            else {
                downpay = ((payment / 100) * initialBalance)
                downpercent = (payment * 100)
                downPayAsPercent = true
            }
        amTableExist = false
        }
        }
    fun setInitLength(len: Int){
        length = len
        amTableExist = false
    }

    fun setRemLength(length: Int){
        remainLength = length
    }
    
    fun setCusName(name: String){
        custName = name
    }
    fun amTable(): MutableList<MutableList<Float>> {
        var table: MutableList<MutableList<Float>>
        if (amTableExist){
            table = amTableData
        }
        else {
            table = AmmortTable(length, initialBalance, interestRate)
            amTableData = table
            amTableExist = true
        }
        return table
    }

    }
fun AmmortTable(length: Int, initbal: Double, interestrate: Double): MutableList<MutableList<Float>> {
    var table: MutableList<MutableList<Float>> = mutableListOf()
    var currentbal = initbal
    table.add(0, mutableListOf())
    table[0].add(0, 0.toFloat())
    table[0].add(1, 0.toFloat())
    table[0].add(2, 0.toFloat())
    table[0].add(3, 0.toFloat())
    table[0].add(4, 0.toFloat())
    var periodicinterest = ((interestrate/12)/100)
    var payment = ((initbal * periodicinterest) / (1 - (1 / (1 + periodicinterest).pow(length)))).toFloat()
    for (i in kotlin.ranges.IntRange(1,length)){
        var interestPayment = (currentbal * (periodicinterest)).toFloat()
        var principalPayment = (payment - interestPayment)
        println("Month " + i + "   Interest: " + interestPayment.roundToTwoDecimalPlace() + "    Principal Payment: " + principalPayment.roundToTwoDecimalPlace() + "   Overall Payment: " + payment.roundToTwoDecimalPlace())
        currentbal -= principalPayment
        var listyBoi: MutableList<Float> = mutableListOf() //I hate this but it worked
        listyBoi.add(0, i.toFloat().roundToTwoDecimalPlace())
        listyBoi.add(1, payment.roundToTwoDecimalPlace())
        listyBoi.add(2, interestPayment.roundToTwoDecimalPlace())
        listyBoi.add(3, principalPayment.roundToTwoDecimalPlace())
        listyBoi.add(4, currentbal.toFloat().roundToTwoDecimalPlace())
        table.add(i,listyBoi)
        table[0][2] += interestPayment//Table[0] is where overall calculations will live, for comparisons sake
        table[0][3] += principalPayment //Storing the calculation of principal payments for validation, if need be
    }
    return table
}

fun Float.roundToTwoDecimalPlace(): Float {
    val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH)).apply {
        roundingMode = RoundingMode.HALF_UP
    }
    return df.format(this).toFloat()
}

fun main(){
    print(AmmortTable(360, 312500.00, 3.5)[0])
}

