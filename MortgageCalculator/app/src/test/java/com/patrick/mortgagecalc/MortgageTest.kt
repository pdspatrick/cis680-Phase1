package com.patrick.mortgagecalc

import junit.framework.TestCase
import kotlin.collections.last

class MortgageTest : TestCase() {

    fun testAmTable() {
        var table: MutableList<MutableList<Float>> = AmmortTable(360, 312500.0f, 3.5f)
        assertEquals(192677.14.toFloat(), table[0][2])
    }
    fun testAmTableExtraPayment() {
        var table: MutableList<MutableList<Float>> = AmmortTable(360, 312500.0f, 3.5f, 300.toFloat(), true)
        assertEquals(135470.45.toFloat(), table[0][2])
        assertEquals(264.toFloat(), table.last()[0])
    }
}