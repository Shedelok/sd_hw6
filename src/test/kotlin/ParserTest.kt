import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import tokenizer.*
import visitor.ParserException
import visitor.ParserVisitor
import visitor.accept
import java.util.stream.Stream
import kotlin.test.assertEquals

internal class ParserTest {
    @ParameterizedTest
    @MethodSource("provideArgumentsForValidInput")
    fun validInput(input: String, expectedOutput: List<Token>) {
        val parserVisitor = ParserVisitor()
        input.byteInputStream().use { it.tokenize().accept(parserVisitor) }
        assertEquals(expectedOutput, parserVisitor.getTokens())
    }

    @ParameterizedTest
    @CsvSource(
        "1),Unmatched right brace",
        "1 1,Incorrect input expression",
        "(1,There is unmatched brace"
    )
    fun invalidInput(input: String, expectedExceptionMessage: String) {
        val exception = Assertions.assertThrows(ParserException::class.java) {
            val parserVisitor = ParserVisitor()
            input.byteInputStream().use { it.tokenize().accept(parserVisitor) }
            parserVisitor.getTokens()
        }
        assertEquals(expectedExceptionMessage, exception.message)
    }

    companion object {
        @JvmStatic
        fun provideArgumentsForValidInput(): Stream<Arguments> = Stream.of(
            Arguments.of("1 + 1", listOf("1".toNumberToken(), "1".toNumberToken(), Plus)),
            Arguments.of(
                "2+2*2",
                listOf("2".toNumberToken(), "2".toNumberToken(), "2".toNumberToken(), Multiply, Plus)
            ),
            Arguments.of(
                "2*2+2",
                listOf("2".toNumberToken(), "2".toNumberToken(), Multiply, "2".toNumberToken(), Plus)
            ),
            Arguments.of(
                "1+(2+3)*4+5*6+7-8+9/10+11-12+((13)-14*15)*16",
                listOf(
                    "1".toNumberToken(),
                    "2".toNumberToken(),
                    "3".toNumberToken(),
                    Plus,
                    "4".toNumberToken(),
                    Multiply,
                    Plus,
                    "5".toNumberToken(),
                    "6".toNumberToken(),
                    Multiply,
                    Plus,
                    "7".toNumberToken(),
                    Plus,
                    "8".toNumberToken(),
                    Minus,
                    "9".toNumberToken(),
                    "10".toNumberToken(),
                    Divide,
                    Plus,
                    "11".toNumberToken(),
                    Plus,
                    "12".toNumberToken(),
                    Minus,
                    "13".toNumberToken(),
                    "14".toNumberToken(),
                    "15".toNumberToken(),
                    Multiply,
                    Minus,
                    "16".toNumberToken(),
                    Multiply,
                    Plus
                )
            )
        )
    }
}