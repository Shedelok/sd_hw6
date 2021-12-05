import tokenizer.tokenize
import visitor.CalcVisitor
import visitor.ParserVisitor
import visitor.accept

fun main() {
    val input = readLine()!!
    val tokens = input.byteInputStream().use { it.tokenize() }

    val parseVisitor = ParserVisitor()
    tokens.accept(parseVisitor)
    val parsed = parseVisitor.getTokens()

    val calcVisitor = CalcVisitor()
    parsed.accept(calcVisitor)
    println(calcVisitor.getOutput())
}