import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.*
import org.junit.*

class TestClient {
    @Test
    fun testAmTable() {
        var table: MutableList<MutableList<Float>> = AmmortTable(360, 312500.0f, 3.5f)
        assertEquals(192676.28.toFloat(), table[0][2])
        }

    @Test
    fun testAmTableExtraPayment() {
        var table: MutableList<MutableList<Float>> = AmmortTable(360, 312500.0f, 3.5f, 0.0f, 1000f, true)
        assertEquals(80978.83.toFloat(), table[0][2])
        assertEquals(164.toFloat(), table.last()[0])
        }

    @Test
    fun testRounding() {
        var number: Float = 3.1415926535F
        number = number.roundToTwoDecimalPlace()
        assertEquals("3.14".toFloat(), number)
    }
}
