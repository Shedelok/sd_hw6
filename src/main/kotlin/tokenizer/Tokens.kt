package tokenizer

import visitor.TokenVisitor
import java.math.BigInteger

sealed interface Token {
    fun accept(visitor: TokenVisitor)
}

data class NumberToken(val value: BigInteger) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}

sealed class Brace : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}

object LeftBrace : Brace()
object RightBrace : Brace()

sealed class Operation(val priority: Int, val asString: String) : Token {
    override fun accept(visitor: TokenVisitor) = visitor.visit(this)
}

object Plus : Operation(2, "+")
object Minus : Operation(2, "-")
object Multiply : Operation(1, "*")
object Divide : Operation(1, "/")