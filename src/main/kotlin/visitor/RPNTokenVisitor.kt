package visitor

import tokenizer.Brace
import java.lang.IllegalArgumentException

abstract class RPNTokenVisitor : TokenVisitor {
    override fun visit(token: Brace) {
        throw IllegalArgumentException("RPN cannot contain braces")
    }
}