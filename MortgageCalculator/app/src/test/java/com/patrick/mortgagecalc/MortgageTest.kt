package com.patrick.mortgagecalc

import junit.framework.TestCase
import org.junit.Test

class MortgageTest : TestCase() {

    fun testAmTable() {
        var table: MutableList<MutableList<Float>> = AmmortTable(360, 312500.00, 3.5)
        assertEquals(192675.19.toFloat(), table[0][2])
    }
}