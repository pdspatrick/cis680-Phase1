import org.w3c.dom.HTMLCollection
import org.w3c.dom.HTMLFormElement
import kotlin.math.pow
import kotlin.math.round
import kotlinx.browser.document

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
fun AmmortTable(length: Int, initbal: Float, interestrate: Float, extrapayment: Float = 0.0f, extrapayenabled: Boolean = false): MutableList<MutableList<Float>> {
    var table: MutableList<MutableList<Float>> = mutableListOf()
    var currentbal = initbal.roundToTwoDecimalPlace()
    table.add(0, mutableListOf())
    table[0].add(0, 0.toFloat())


    var periodicinterest = ((interestrate/12)/100)
    var payment = ((initbal * periodicinterest) / (1 - (1 / (1 + periodicinterest).pow(length))))
    table[0].add(1, payment) // storing payment for later comparison
    table[0].add(2, 0.toFloat()) // total of interest payments
    table[0].add(3, 0.toFloat().roundToTwoDecimalPlace()) // counting principal payments
    table[0].add(4, 0.toFloat().roundToTwoDecimalPlace()) // to store the extra payment amount later.
    if (extrapayenabled) {
        payment += extrapayment.roundToTwoDecimalPlace()
        table[0][4] = extrapayment.roundToTwoDecimalPlace() //storing extra payment for later compare. best way to do this and maintain the list size
    }
    var balanceWillBeZero: Boolean = false // used to flag when balance will be zero for the case of extra payments shortening the timeline of repayments

    for (i in kotlin.ranges.IntRange(1,length)){
        var interestPayment = ((currentbal * (periodicinterest)).roundToTwoDecimalPlace())
        var principalPayment = ((payment - interestPayment).roundToTwoDecimalPlace())
        if (extrapayenabled){
            if (principalPayment >= currentbal){
                principalPayment = currentbal
                balanceWillBeZero = true //to know to end the loop and return the table
            }
        }
        currentbal -= principalPayment
        var listyBoi: MutableList<Float> = mutableListOf() //I hate this but it worked
        listyBoi.add(0, i.toFloat().roundToTwoDecimalPlace())
        listyBoi.add(1, payment.roundToTwoDecimalPlace())
        listyBoi.add(2, interestPayment.roundToTwoDecimalPlace())
        listyBoi.add(3, principalPayment.roundToTwoDecimalPlace())
        listyBoi.add(4, currentbal.roundToTwoDecimalPlace())
        listyBoi.add(5, extrapayment.roundToTwoDecimalPlace())
        table.add(i,listyBoi)
        table[0][0] = i.toFloat() // count number of payments, useful for comparison later
        table[0][2] += interestPayment //Table[0] is where overall calculations will live, for comparisons sake
        table[0][3] += principalPayment //Storing the calculation of principal payments for validation, if need be

        if (balanceWillBeZero){
            table[0][2] = table[0][2].roundToTwoDecimalPlace()
            table[0][3] = table[0][3].roundToTwoDecimalPlace()
            return table
        }
    }
    table[0][2] = table[0][2].roundToTwoDecimalPlace()
    table[0][3] = table[0][3].roundToTwoDecimalPlace()
    return table
}

fun Float.roundToTwoDecimalPlace(): Float {
    var df: Float = (this * 100)
    df = (round(df))
    df = (df / 100)
    return df
}

fun dataSubmission(initbal: Float, term: Int, interestrate:Float, extrapayment:Float): Html {
    var table:  MutableList<MutableList<Float>>
    if ((extrapayment == 0F)){
        table = AmmortTable(term, initbal, interestrate)
    }
    else {
        table = AmmortTable(term, initbal, interestrate, extrapayment, true)
    }

    var resultantTable: Html = tableRender(table)

    return resultantTable
}

@JsName("tableRender")
fun tableRender(ammorttable: MutableList<MutableList<Float>>): Html {
    val result = html {
        table {
            tr{
                td{
                    text("Month")
                }
                td{
                    text("Interest Paid")
                }
                td{
                    text("Principal Paid")
                }
                td{
                    text("Extra Payment")
                }
                td{
                    text("Total Payment")
                }
            }
            val ammort = ammorttable
            for (item in ammort){
                tr {
                    td{
                        item[0]

                    }
                    td{
                        item[2]
                    }
                    td{
                        item[3]
                    }
                    td{
                        item[5]
                    }
                    td{
                        item[1]
                    }
                }
            }
        }
    }
    return result
}


open class Tag(val name: String) {
    val children = mutableListOf<Tag>()
    val attributes = mutableListOf<Attribute>()

    override fun toString(): String {
        return "<$name" +
                (if (attributes.isEmpty()) "" else attributes.joinToString(separator = " ", prefix = " ")) + ">" +
                (if (children.isEmpty()) "" else children.joinToString(separator = "")) +
                "</$name>"
    }
}

class Attribute(val name : String, val value : String) {
    override fun toString() = """$name="$value""""
}

fun <T: Tag> T.set(name: String, value: String?): T {
    if (value != null) {
        attributes.add(Attribute(name, value))
    }
    return this
}

fun <T: Tag> Tag.doInit(tag: T, init: T.() -> Unit): T {
    tag.init()
    children.add(tag)
    return tag
}

class Html: Tag("html")
class Table: Tag("table")
class Center: Tag("center")
class TR: Tag("tr")
class TD: Tag("td")
class Text(val text: String): Tag("b") {
    override fun toString() = text
}

fun html(init: Html.() -> Unit): Html = Html().apply(init)

fun Html.table(init : Table.() -> Unit) = doInit(Table(), init)
fun Html.center(init : Center.() -> Unit) = doInit(Center(), init)

fun Table.tr(color: String? = null, init : TR.() -> Unit) = doInit(TR(), init).set("bgcolor", color)

fun TR.td(color: String? = null, align : String = "left", init : TD.() -> Unit) = doInit(TD(), init).set("align", align).set("bgcolor", color)

fun Tag.text(s : Any?) = doInit(Text(s.toString()), {})