import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import tokenizer.tokenize
import visitor.ParserVisitor
import visitor.PrintVisitor
import visitor.accept
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals

class PrintRPNTest {
    @ParameterizedTest
    @CsvSource(
        "1,1",
        "1+1,1 1 +",
        "((1)),1",
        "2+2*2,2 2 2 * +",
        "2*2+2,2 2 * 2 +",
        "1+(2+3)*4+5*6+7-8+9/10+11-12+((13)-14*15)*16,1 2 3 + 4 * + 5 6 * + 7 + 8 - 9 10 / + 11 + 12 - 13 14 15 * - 16 * +"
    )
    fun test(input: String, expectedOutput: String) {
        val parserVisitor = ParserVisitor()
        input.byteInputStream().use { it.tokenize().accept(parserVisitor) }

        ByteArrayOutputStream().use {
            val printVisitor = PrintVisitor(it)
            parserVisitor.getTokens().accept(printVisitor)
            printVisitor.close()
            assertEquals(expectedOutput, it.toString())
        }
    }
}