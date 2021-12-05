package tokenizer

import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import toNumberToken
import java.util.stream.Stream
import kotlin.test.assertEquals

internal class TokenizerTest {
    @ParameterizedTest
    @MethodSource("provideArgumentsForValidInput")
    fun validInput(input: String, expectedTokens: List<Token>) =
        assertEquals(expectedTokens, input.byteInputStream().use { it.tokenize() })

    @ParameterizedTest
    @CsvSource(
        "abc,a",
        "1b,b",
        "x+1,x"
    )
    fun invalidInput(input: String, unexpectedCharacter: Char) {
        val exception = assertThrows(TokenizerException::class.java) {
            input.byteInputStream().use { it.tokenize() }
        }
        assertEquals("Unexpected input byte ${unexpectedCharacter.code} (char $unexpectedCharacter)", exception.message)
    }

    companion object {
        @JvmStatic
        fun provideArgumentsForValidInput(): Stream<Arguments> = Stream.of(
            Arguments.of("+", listOf(Plus)),
            Arguments.of("-1", listOf(Minus, "1".toNumberToken())),
            Arguments.of(
                " \t\r\n1 \t\n* \t\n* \t\n1 \t\n",
                listOf("1".toNumberToken(), Multiply, Multiply, "1".toNumberToken())
            ),
            Arguments.of(
                "((1/1))",
                listOf(LeftBrace, LeftBrace, "1".toNumberToken(), Divide, "1".toNumberToken(), RightBrace, RightBrace)
            ),
            Arguments.of(
                "(30 + 2) / 8",
                listOf(
                    LeftBrace,
                    "30".toNumberToken(),
                    Plus,
                    "2".toNumberToken(),
                    RightBrace,
                    Divide,
                    "8".toNumberToken()
                )
            ),

            )
    }
}