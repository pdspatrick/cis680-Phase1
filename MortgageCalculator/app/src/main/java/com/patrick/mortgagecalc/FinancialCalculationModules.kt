package com.patrick.mortgagecalc
import android.os.Parcel
import android.os.Parcelable
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.pow


class Mortgage() : Parcelable {
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

    constructor(parcel: Parcel) : this() {
        length = parcel.readInt()
        remainLength = parcel.readInt()
        custName = parcel.readString().toString()
        initialBalance = parcel.readFloat()
        currentBalance = parcel.readFloat()
        downpay = parcel.readFloat()
        downpercent = parcel.readFloat()
        interestRate = parcel.readFloat()
        downPayAsPercent = parcel.readByte() != 0.toByte()
        amTableExist = parcel.readByte() != 0.toByte()
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(length)
        parcel.writeInt(remainLength)
        parcel.writeString(custName)
        parcel.writeFloat(initialBalance)
        parcel.writeFloat(currentBalance)
        parcel.writeFloat(downpay)
        parcel.writeFloat(downpercent)
        parcel.writeFloat(interestRate)
        parcel.writeByte(if (downPayAsPercent) 1 else 0)
        parcel.writeByte(if (amTableExist) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mortgage> {
        override fun createFromParcel(parcel: Parcel): Mortgage {
            return Mortgage(parcel)
        }

        override fun newArray(size: Int): Array<Mortgage?> {
            return arrayOfNulls(size)
        }
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
    var currentbal = initbal
    table.add(0, mutableListOf())
    table[0].add(0, 0.toFloat())
    table[0].add(1, 0.toFloat())
    table[0].add(2, 0.toFloat())
    table[0].add(3, 0.toFloat())
    table[0].add(4, 0.toFloat())
    var periodicinterest = ((interestrate/12)/100)
    var payment = ((initbal * periodicinterest) / (1 - (1 / (1 + periodicinterest).pow(length)))).toFloat()
    if (extrapayenabled) payment += extrapayment
    var balanceWillBeZero: Boolean = false // used to flag when balance will be zero for the case of extra payments shortening the timeline of repayments
    for (i in kotlin.ranges.IntRange(1,length)){
        var interestPayment = (currentbal * (periodicinterest)).toFloat()
        var principalPayment = (payment - interestPayment)
        if (extrapayenabled){
            if (principalPayment >= currentbal){
                principalPayment = currentbal
                balanceWillBeZero = true
            }
        }
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
        if (balanceWillBeZero){
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



