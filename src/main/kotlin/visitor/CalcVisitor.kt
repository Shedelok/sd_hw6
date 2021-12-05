package visitor

import tokenizer.*
import java.math.BigInteger

class CalcVisitor : RPNTokenVisitor() {
    private val stack = ArrayDeque<BigInteger>()

    override fun visit(token: NumberToken) {
        stack.add(token.value)
    }

    override fun visit(token: Operation) {
        check(stack.size >= 2) { "Invalid input" }
        val right = stack.removeLast()
        val left = stack.removeLast()
        stack.add(
            when (token) {
                Plus -> BigInteger::plus
                Minus -> BigInteger::minus
                Multiply -> BigInteger::times
                Divide -> BigInteger::div
            }(left, right)
        )
    }

    fun getOutput(): BigInteger {
        check(stack.size == 1) { "Invalid input" }
        return stack.last()
    }
}