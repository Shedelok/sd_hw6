package visitor

import tokenizer.*

class ParserVisitor : TokenVisitor {
    private val stack = ArrayDeque<Token>()
    private val output = mutableListOf<Token>()

    fun getTokens(): List<Token> {
        while (stack.isNotEmpty()) {
            val token = stack.removeLast()
            if (token !is Operation) {
                throw ParserException("There is unmatched brace")
            }
            output.add(token)
        }
        checkRPN(output)
        return output
    }

    override fun visit(token: NumberToken) {
        output.add(token)
    }

    override fun visit(token: Brace) {
        when (token) {
            is LeftBrace -> stack.add(token)
            is RightBrace -> {
                while (true) {
                    when (val lastTokenOrNull = stack.removeLastOrNull()) {
                        null -> throw ParserException("Unmatched right brace")
                        LeftBrace -> break
                        is NumberToken, is Operation -> output.add(lastTokenOrNull)
                        else -> throw ParserException("Internal error. Unexpected token $token")
                    }
                }
            }
        }
    }

    override fun visit(token: Operation) {
        while (stack.isNotEmpty()) {
            val operationInStack = stack.last()
            if (operationInStack !is Operation || operationInStack.priority > token.priority) {
                break
            }
            output.add(stack.removeLast())
        }
        stack.add(token)
    }
}

private fun checkRPN(rpnTokens: List<Token>) {
    var operandsCount = 0
    for (token in rpnTokens) {
        when (token) {
            is NumberToken -> operandsCount++
            is Operation -> operandsCount--
            is LeftBrace -> throw ParserException("Unmatched left brace")
            else -> throw ParserException("Internal error. Unexpected token $token")
        }
        if (operandsCount < 0) {
            throw ParserException("Incorrect input expression")
        }
    }
    if (operandsCount != 1) {
        throw ParserException("Incorrect input expression")
    }
}

class ParserException(message: String) : Exception(message)