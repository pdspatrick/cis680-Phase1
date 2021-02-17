import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.pow
import kotlin.*

fun main() {
    val format1 = DecimalFormat("0.00")
    var data = getDataFromUser()
    var extrapayenabled = false
    if (data[4] != 0.0f) extrapayenabled = true
    var ammorttable1 = AmmortTable(data[1].toInt(), data[0], data[2], data[3], data[4], extrapayenabled)
    toPrintableList(ammorttable1)
    println("")
    println("")
    print("Would you like to compare to another mortgage? Y/N")
    var continuecompare: Boolean = getInputYesNo("compare to other mortgage")
    if (!continuecompare) return
    if (continuecompare){
        //var compareOutput: String = ""
        var data2 = getDataFromUser()
        extrapayenabled = false
        if (data2[4] != 0.0f) extrapayenabled = true
        var ammorttable2 = AmmortTable(data2[1].toInt(), data2[0], data2[2], data2[3], data2[4], extrapayenabled)
        toPrintableList(ammorttable2)
        var termDifference: Int = 0
        //var shorterLoan: String = ""
        var interestDifference: Float = 0.0f
        //var cheaperLoan: String = ""
        var cheaperString: String = ""
        var shorterString: String = ""
        if (ammorttable1[0][0] < ammorttable2[0][0]) {
            termDifference = (ammorttable2[0][0].toInt() - ammorttable1[0][0].toInt())
            var yeardifference = (termDifference / 12)
            shorterString += ("The first mortgage is " + termDifference.toString() + " months (about " + yeardifference.toInt() + " years) shorter.")
            //shorterLoan = "first"
        }
        if (ammorttable1[0][0] > ammorttable2[0][0]) {
            termDifference = (ammorttable1[0][0].toInt() - ammorttable2[0][0].toInt())
            var yeardifference = (termDifference / 12)
            shorterString += ("The second mortgage is " + termDifference.toString() + " months (about " + yeardifference.toInt() + " years) shorter.")
            //shorterLoan = "second"
        }
        if (ammorttable1[0][0] == ammorttable2[0][0]) {
            termDifference = 0
            shorterString += "Both mortgages are of the same length."
            //shorterLoan = "same"
        }
        if (ammorttable1[0][2] < ammorttable2[0][2]) {
            interestDifference = (ammorttable2[0][2] - ammorttable1[0][2]).roundToTwoDecimalPlace()
            cheaperString += ("The first mortgage is $" + format1.format(interestDifference.roundToTwoDecimalPlace()).toString() + " cheaper in interest.")
            //cheaperLoan = "first"
        }
        if (ammorttable1[0][2] > ammorttable2[0][2]) {
            interestDifference = (ammorttable1[0][2] - ammorttable2[0][2]).roundToTwoDecimalPlace()
            cheaperString += ("The second mortgage is $" + format1.format(interestDifference.roundToTwoDecimalPlace()).toString() + " cheaper in interest.")
            //cheaperLoan = "second"
        }
        if (ammorttable1[0][2] == ammorttable2[0][2]) {
            interestDifference = 0.0f
            cheaperString += ("Both mortgages are of the same interest cost.")
            //cheaperLoan = "same"
        }

        println(shorterString)
        println(cheaperString)

    }

}

fun getDataFromUser(): List<Float>{
    print("Please enter the total amount of mortgage:                                   $")
    var initbal = getInputFloat("amount of mortgage")
    print("Please enter term of mortgage, in months:                              Months ")
    var term = getInputInt("mortgage term")
    print("Please enter interest rate of loan, as APR:                                  %")
    var rate = getInputFloat("interest rate (APR)")
    print("Please enter down payment amount, if any                                     $")
    var down = getInputFloatOrZero("down payment")
    println("Please enter monthly extra payment amount, if any.")
    print("  (These are payments not otherwise required by lender at time of payment.)  $")
    var extra = getInputFloatOrZero("monthly extra payments")

    return listOf(initbal, term.toFloat(), rate, down, extra)

}

fun toPrintableList(table: MutableList<MutableList<Float>>){
    val format1 = DecimalFormat("0.00")
    val formatYear = DecimalFormat("00")
    //var table2: MutableList<List<String>> = mutableListOf()
    var tablehas6: Boolean = false
    if (table[1].size == 6){
        tablehas6 = true
    }
    if (table[1].size != 6){
        tablehas6 = false
    }
    println("")
    if (!tablehas6) {
        val leftAlignFormat = "| %-9s | $%16s |  $%11s |  $%10s  | $%13s |%n"
        System.out.format("+-----------+-------------------+---------------+---------------+----------------+%n")
        System.out.format("| Payment # | Principal Payment | Interest Paid | Total Payment |     Balance    |%n")
        System.out.format("+-----------+-------------------+---------------+---------------+----------------+%n")
        for (i in IntRange(1, table.size - 1)) {
            System.out.format(
                leftAlignFormat,
                table[i][0].toInt().toString(),
                format1.format(table[i][3].roundToTwoDecimalPlace()).toString(),
                format1.format(table[i][2].roundToTwoDecimalPlace()).toString(),
                format1.format(table[i][1].roundToTwoDecimalPlace()).toString(),
                format1.format(table[i][4].roundToTwoDecimalPlace()).toString()
            )
            if ((i.toFloat() % 12) == 0.0f) {
                var year = ((i / 12) + 1)
                if (i == table.size-1){
                    continue
                }
                else {
                    System.out.format("+-----------+-------------------+---------------+---------------+----------------+%n")
                    System.out.format("|  Year " + formatYear.format(year) + "  | Principal Payment | Interest Paid | Total Payment |     Balance    |%n")
                    System.out.format("+-----------+-------------------+---------------+---------------+----------------+%n")
                }
            }

        }
        var finalFormat = "| %-9s | $%16s |  $%11s |   $%27s |%n"
        System.out.format("+-----------+-------------------+---------------+---------------+----------------+%n")
        System.out.format("| Totals:   |   Principal Paid  | Interest Paid | Total of Payments Made         |%n")
        System.out.format("+-----------+-------------------+---------------+--------------------------------+%n")
        System.out.format(
            finalFormat,
            "",
            table[0][3].roundToTwoDecimalPlace().toString(),
            table[0][2].roundToTwoDecimalPlace().toString(),
            table[0][5].roundToTwoDecimalPlace().toString()
        )
        System.out.format("+-----------+-------------------+---------------+--------------------------------+%n")
    }
    if (tablehas6) {
        val leftAlignFormat = "| %-9s | $%16s |  $%11s | $%12s | $%13s |  $%12s |%n"
        System.out.format("+-----------+-------------------+---------------+---------------+---------------+----------------+%n")
        System.out.format("| Payment # | Principal Payment | Interest Paid | Extra Payment | Total Payment |     Balance    |%n")
        System.out.format("+-----------+-------------------+---------------+---------------+---------------+----------------+%n")
        for (i in IntRange(1, table.size-1)) {
            System.out.format(
                leftAlignFormat,
                table[i][0].toInt().toString(),
                format1.format(table[i][3].roundToTwoDecimalPlace()).toString(),
                format1.format(table[i][2].roundToTwoDecimalPlace()).toString(),
                format1.format(table[i][5].roundToTwoDecimalPlace()).toString(),
                format1.format(table[i][3].roundToTwoDecimalPlace() + table[i][5].roundToTwoDecimalPlace() + table[i][2].roundToTwoDecimalPlace()),
                format1.format(table[i][4].roundToTwoDecimalPlace()).toString()
            )
            if ((i.toFloat() % 12) == 0.0f){
                var year = ((i / 12) + 1)
                if (i == table.size-1){
                    continue
                }
                else {
                    System.out.format("+-----------+-------------------+---------------+---------------+----------------+----------------+%n")
                    System.out.format("|  Year " + formatYear.format(year) + "  | Principal Payment | Interest Paid | Extra Payment | Total Payment  |     Balance    |%n")
                    System.out.format("+-----------+-------------------+---------------+---------------+----------------+----------------+%n")
                }
            }
        }

        System.out.format("+-----------+-------------------+---------------+----------------+---------------+----------------+%n")
        System.out.format("| Totals:   |  Principal Paid   | Interest Paid | Extra Payments | Total of Payments Made         |%n")
        System.out.format("+-----------+-------------------+---------------+----------------+--------------------------------+%n")
        var finalFormat = "| %-9s | $%16s |  $%11s | $%13s |   $%27s |%n"
        var principal = table[0][3]
        var interest = table[0][2]
        var extra = table[0][4]
        var total = extra + interest + principal
        System.out.format(
            finalFormat,
            "",
            format1.format(principal),
            format1.format(interest),
            format1.format(extra),
            format1.format(total)
        )
        System.out.format("+-----------+-------------------+---------------+-------------------------------------------------+%n")
    }
    return
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
    val payment = ((initbal * periodicinterest) / (1 - (1 / (1 + periodicinterest).pow(length)))).roundToTwoDecimalPlace()
    print(payment)
    table[0].add(1, payment) // storing payment for later comparison
    table[0].add(2, 0.toFloat()) // total of interest payments
    table[0].add(3, 0.toFloat()) // counting principal payments
    table[0].add(4, 0.toFloat()) // to store the extra payment amount later.
    table[0].add(5, 0.toFloat()) // to store the total of payments
    if (extrapayenabled) {
        table[0].add(6, extrapayment) //storing extra payment for later compare. best way to do this and maintain the list size
    }
    var balanceWillBeZero: Boolean = false // used to flag when balance will be zero for the case of extra payments shortening the timeline of repayments
    var extrapayment = extrapayment // i hate this too
    for (i in IntRange(1,length)){
        var interestPayment = (currentbal * (periodicinterest)).toFloat()
        var principalPayment = (payment - interestPayment)
        var listyBoi: MutableList<Float> = mutableListOf() //I hate this but it worked
        var endPayment: Float = 0f
        if (principalPayment >= currentbal){
            principalPayment = currentbal
            extrapayment = 0.0f
            balanceWillBeZero = true //to know to end the loop and return the table
            currentbal = 0.0f
            endPayment = principalPayment + interestPayment
        }
        if ((!balanceWillBeZero) and (extrapayenabled)){
            if ((extrapayment + principalPayment) > currentbal){
                currentbal -= principalPayment
                extrapayment = currentbal
                balanceWillBeZero = true
                currentbal = 0.0f
            }
            else {
                currentbal -= (extrapayment + principalPayment)
            }
        }
        else currentbal -= principalPayment
        listyBoi.add(0, i.toFloat())
        listyBoi.add(1, (payment))
        listyBoi.add(2, interestPayment)
        listyBoi.add(3, principalPayment)
        listyBoi.add(4, currentbal)
        if (extrapayenabled){
            listyBoi.add(5, extrapayment)
        }

        table.add(i,listyBoi)
        table[0][0] = i.toFloat() // count number of payments, useful for comparison later
        table[0][2] += interestPayment //Table[0] is where overall calculations will live, for comparisons sake
        table[0][3] += principalPayment //Storing the calculation of principal payments for validation, if need be
        table[0][4] += extrapayment // storing total of extera payments
        table[0][5] += payment // total paid

        if (balanceWillBeZero){
            listyBoi[4] = 0.0f
            listyBoi[1] = endPayment
            break
        }
    }
    return table
}

fun Float.roundToTwoDecimalPlace(): Float {
    val df = DecimalFormat("0.00").apply {
        roundingMode = RoundingMode.HALF_UP
    }
    return df.format(this).toFloat()
}



fun getInputFloat(fieldname: String): Float{
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
            return getInputFloat(fieldname)
        }
        catch (e: NoSuchElementException) {
            println("          No input for " + fieldname + ".   This is a required value. Please enter:")
            return getInputFloat(fieldname)
        }
    }
    return input
}

fun getInputInt(fieldname: String): Int{
    var input: Int
    var keys: String
    while (true) {
        try {
            keys = readLine().toString()
            val scan = Scanner(keys)
            input = scan.nextInt()
            break
        } catch (e: InputMismatchException) {
            println("          Invalid input for " + fieldname + ".   Please Reenter:  ")
        }
        catch (e: NoSuchElementException) {
            println("          No input for " + fieldname + ".   This is a required value. Please enter:.")
            return getInputInt(fieldname)
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
            return(getInputFloatOrZero(fieldname))
        }
         catch (e: NoSuchElementException) {
            println("          No input for " + fieldname + ".   Assuming 0.")
            return(0.0f)
        }
    }
    return input
}

fun getInputYesNo(fieldname: String): Boolean{ // used to validate if an answer is yes or no, and returns as boolean
    var keys: String = readLine().toString()
    if ((keys[0].toString() == "y") or (keys[0].toString() == "Y") or (keys[0].toString() == "t") or (keys[0].toString() == "T")){ //check for yes or true
        return true
    }
    if ((keys[0].toString() == "n") or (keys[0].toString() == "N") or (keys[0].toString() == "f") or (keys[0].toString() == "F")) { //check for no or false
        return false
    }
    else { //call function again if it doesnt match above
        println("          Invalid input for " + fieldname + ".   Please answer Y/N:")
        return getInputYesNo(fieldname)
    }
}

