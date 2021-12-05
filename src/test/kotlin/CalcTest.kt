import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tokenizer.tokenize
import visitor.CalcVisitor
import visitor.ParserVisitor
import visitor.accept
import kotlin.test.assertEquals

class CalcTest {
    @ParameterizedTest
    @CsvSource(
        "1,1",
        "1+(2+3)*4+5*6+7-8+9/10+11-12+((13)-14*15)*16,-3103",
        "1/2,0",
        "2+2*2,6"
    )
    fun test(input: String, expectedOutput: String) {
        val parseVisitor = ParserVisitor()
        input.byteInputStream().use { it.tokenize() }.accept(parseVisitor)

        val calcVisitor = CalcVisitor()
        parseVisitor.getTokens().accept(calcVisitor)
        assertEquals(expectedOutput.toBigInteger(), calcVisitor.getOutput())
    }
}