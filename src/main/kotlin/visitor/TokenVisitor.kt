package visitor

import tokenizer.Brace
import tokenizer.NumberToken
import tokenizer.Operation
import tokenizer.Token

interface TokenVisitor {
    fun visit(token: NumberToken)
    fun visit(token: Brace)
    fun visit(token: Operation)
}

fun List<Token>.accept(visitor: TokenVisitor) {
    for (token in this) {
        when (token) {
            is NumberToken -> visitor.visit(token)
            is Brace -> visitor.visit(token)
            is Operation -> visitor.visit(token)
        }
    }
}