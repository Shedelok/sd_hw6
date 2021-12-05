package tokenizer

import java.io.InputStream
import java.math.BigInteger

private const val EOF = -1
private const val LEFT_BRACE_BYTE = '('.code
private const val RIGHT_BRACE_BYTE = ')'.code
private const val PLUS_BYTE = '+'.code
private const val MINUS_BYTE = '-'.code
private const val MULTIPLY_BYTE = '*'.code
private const val DIVIDE_BYTE = '/'.code

private interface State {
    fun accept(byte: Int)
    fun getErrorMessage(): String? = null
}

private class TokenizerContext : State {
    private val tokens = mutableListOf<Token>()

    val startState = StartState(this)
    val endState = EndState()

    var currentState: State = startState

    fun addToken(token: Token) = tokens.add(token)

    override fun accept(byte: Int): Unit = currentState.accept(byte)
    override fun getErrorMessage() = currentState.getErrorMessage()
    fun getTokens(): List<Token> {
        getErrorMessage()?.let {
            throw TokenizerException(it)
        }
        return tokens
    }
}

private class NumberState(private val context: TokenizerContext, firstDigitByte: Int) : State {
    private val stringBuilder: StringBuilder = StringBuilder()

    init {
        require(Character.isDigit(firstDigitByte))
        accept(firstDigitByte)
    }

    override fun accept(byte: Int) {
        if (Character.isDigit(byte)) {
            stringBuilder.append(byte.toChar())
        } else {
            context.addToken(NumberToken(BigInteger(stringBuilder.toString())))
            context.currentState = context.startState
            context.accept(byte)
        }
    }
}

private class StartState(private val context: TokenizerContext) : State {
    override fun accept(byte: Int) {
        when {
            byte == EOF -> context.currentState = context.endState
            Character.isDigit(byte) -> context.currentState = NumberState(context, byte)
            byte == LEFT_BRACE_BYTE -> context.addToken(LeftBrace)
            byte == RIGHT_BRACE_BYTE -> context.addToken(RightBrace)
            byte == PLUS_BYTE -> context.addToken(Plus)
            byte == MINUS_BYTE -> context.addToken(Minus)
            byte == MULTIPLY_BYTE -> context.addToken(Multiply)
            byte == DIVIDE_BYTE -> context.addToken(Divide)
            Character.isWhitespace(byte) -> {}
            else -> context.currentState = ErrorState("Unexpected input byte $byte (char ${byte.toChar()})")
        }
    }
}

private class EndState : State {
    override fun accept(byte: Int) {}
}

private class ErrorState(private val message: String) : State {
    override fun accept(byte: Int) {}

    override fun getErrorMessage() = message
}

class TokenizerException(message: String) : Exception(message)

fun InputStream.tokenize(): List<Token> {
    val tokenizer = TokenizerContext()
    do {
        val char = read()
        tokenizer.accept(char)
    } while (char != EOF)
    return tokenizer.getTokens()
}