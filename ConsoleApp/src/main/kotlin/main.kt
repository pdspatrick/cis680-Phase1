import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.pow
import java.util.InputMismatchException


@JvmOverloads
fun main() {
    var data = getDataFromUser()
    var extrapayenabled = false
    if (data[4] != 0.0f){
        extrapayenabled = true
    }
    var ammorttable1 = AmmortTable(data[1].toInt(), data[0], data[2], data[3], data[4], extrapayenabled)
    var formattedtable1 = toPrintableList(ammorttable1)
    print(formattedtable1)
}

fun getDataFromUser(): List<Float>{
    print("Please enter the total amount of mortgage:          $")
    var initbal = getInputFloat("amount of mortgage")
    print("Please enter term of mortgage, in months:            ")
    var term = getInputInt("mortgage term")
    print("Please enter interest rate of loan, as APR:         %")
    var rate = getInputFloat("interest rate (APR)")
    print("Please enter down payment amount, if any            $")
    var down = getInputFloatOrZero("down payment")
    print("Please enter monthly extra payment amount, if any:  $")
    var extra = getInputFloatOrZero("monthly extra payments")

    return listOf(initbal, term.toFloat(), rate, down, extra)

}

fun toPrintableList(table: MutableList<MutableList<Float>>): List<List<String>> {
    var table2: MutableList<List<String>> = mutableListOf()
    if (table[1].size == 6){
        table2.add(0, listOf<String>("Payment #", "Principal Payment", "Interest Payment", "Extra Payment", "Total Payment"))
        for (i in IntRange(1, table.size-1))
        {
            table2.add(i, listOf<String>(table[i][0].roundToTwoDecimalPlace().toString(), table[i][3].roundToTwoDecimalPlace().toString(), table[i][2].roundToTwoDecimalPlace().toString(), table[i][5].roundToTwoDecimalPlace().toString(), table[i][1].roundToTwoDecimalPlace().toString()))
        }}
    else
    {
        table2.add(0, listOf("Payment #", "Principal Payment", "Interest Payment", "Total Payment"))
        for (i in IntRange(1,table.size-1))
                {
                    table2.add(i, listOf<String>(table[i][0].toInt().toString(), 325.toString(), table[i][3].roundToTwoDecimalPlace().toString(), table[i][2].roundToTwoDecimalPlace().toString(), table[i][1].roundToTwoDecimalPlace().toString()))
                }
    }

            return table2
}


class Mortgage() {
    var extraPaymentsSet: Boolean = false
    var extraPaymentAmount: Float = 0.0f
    var length: Int = 0
    var remainLength: Int = 0
    var custName: String = ""
    var initialBalance: Float = 0.0f
    var currentBalance: Float = 0.0f
    var downpay: Float = 0.0f
    var downpercent: Float = 0.0f
    var interestRate: Float = 0.0f
    var downPayAsPercent: Boolean = false
    var amTableExist = false
    lateinit var amTableData: MutableList<MutableList<Float>>


    fun setDownPayment(payment: Double){
        if (payment <= 0){
            downPayAsPercent = false
            downpay = 0.0F
            downpercent = 0.0F
        }
        else if (payment > 100){ // odds are if a payment is less than $100 for a mortgage, the customer is entering a percentage
            downpay = payment.toFloat()
            downpercent = ((payment / initialBalance).toFloat())
            downPayAsPercent = false
        }
        else {
            if (payment < 1){
                downpay = ((payment * initialBalance).toFloat())
                downpercent = ((payment * 100).toFloat())
                downPayAsPercent = true
            }
            else {
                downpay = (((payment / 100) * initialBalance).toFloat())
                downpercent = ((payment * 100).toFloat())
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


    fun setExtraPayment(amount: Float, enabled: Boolean){
        amTableExist = false
        if (enabled){
            extraPaymentsSet = false
            extraPaymentAmount = amount
        }
        else {
            extraPaymentsSet = false
        }
    }

}
fun AmmortTable(length: Int, initbal: Float, interestrate: Float, downPayment: Float = 0.0f, extrapayment: Float = 0.0f, extrapayenabled: Boolean = false): MutableList<MutableList<Float>> {
    var table: MutableList<MutableList<Float>> = mutableListOf()
    var currentbal = initbal
    table.add(0, mutableListOf())
    table[0].add(0, 0.toFloat())


    var periodicinterest = ((interestrate/12)/100)
    var initbal = initbal // I also hate this
    if (downPayment != 0.0f){
        initbal -= downPayment // Deduct down payment from loan
    }
    var payment = ((initbal * periodicinterest) / (1 - (1 / (1 + periodicinterest).pow(length)))).toFloat()
    table[0].add(1, payment) // storing payment for later comparison
    table[0].add(2, 0.toFloat()) // total of interest payments
    table[0].add(3, 0.toFloat()) // counting principal payments
    table[0].add(4, 0.toFloat()) // to store the extra payment amount later.
    if (extrapayenabled) {
        payment += extrapayment
        table[0][4] = extrapayment //storing extra payment for later compare. best way to do this and maintain the list size
    }
    var balanceWillBeZero: Boolean = false // used to flag when balance will be zero for the case of extra payments shortening the timeline of repayments

    for (i in IntRange(1,length)){
        var interestPayment = (currentbal * (periodicinterest)).toFloat()
        var principalPayment = (payment - interestPayment)
        var listyBoi: MutableList<Float> = mutableListOf() //I hate this but it worked
        if (extrapayenabled){
            if (principalPayment >= currentbal){
                principalPayment = currentbal
                balanceWillBeZero = true //to know to end the loop and return the table
            }
        }
        currentbal -= principalPayment

        listyBoi.add(0, i.toFloat().roundToTwoDecimalPlace())
        listyBoi.add(1, payment.roundToTwoDecimalPlace())
        listyBoi.add(2, interestPayment.roundToTwoDecimalPlace())
        listyBoi.add(3, principalPayment.roundToTwoDecimalPlace())
        listyBoi.add(4, currentbal.roundToTwoDecimalPlace())
        if (extrapayenabled){
            listyBoi.add(5, extrapayment.roundToTwoDecimalPlace())
        }

        table.add(i,listyBoi)
        table[0][0] = i.toFloat() // count number of payments, useful for comparison later
        table[0][2] += interestPayment//Table[0] is where overall calculations will live, for comparisons sake
        table[0][3] += principalPayment //Storing the calculation of principal payments for validation, if need be

        if (balanceWillBeZero){
            listyBoi[5] = 0.0f.roundToTwoDecimalPlace()
            return table
        }
    }
    return table
}

fun Float.roundToTwoDecimalPlace(): Float {
    val df = DecimalFormat("#.##", DecimalFormatSymbols(Locale.ENGLISH)).apply {
        roundingMode = RoundingMode.HALF_UP
    }
    return df.format(this).toFloat()
}

fun getInputFloat(fieldname: String): Float{
    var input: Float
    val scan = Scanner(System.`in`)
    while (true) {
        try {
            input = scan.nextFloat()
            break
        } catch (e: InputMismatchException) {
            println("          Invalid input for " + fieldname + ".   Please Reenter:")
            scan.nextLine()
        }
    }
    return input
}

fun getInputInt(fieldname: String): Int{
    var input: Int
    val scan = Scanner(System.`in`)
    while (true) {
        try {
            input = scan.nextInt()
            break
        } catch (e: InputMismatchException) {
            println("          Invalid input for " + fieldname + ".   Please Reenter:  ")
            scan.nextLine()
        }
    }
    return input
}

fun getInputFloatOrZero(fieldname: String): Float{
    var input: Float
    var keys: String
    while (true) {
        try {
            keys = readLine().toString()
            val scan = Scanner(keys)
            input = scan.nextFloat()
            break
        } catch (e: InputMismatchException) {
            println("          Invalid input for " + fieldname + ".   Please Reenter:  ")
        }
         catch (e: NoSuchElementException) {
            println("          No input for " + fieldname + ".   Assuming 0.")
            return(0.0f)
        }
    }
    return input
}