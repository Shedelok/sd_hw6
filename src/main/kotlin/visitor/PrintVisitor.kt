package visitor

import tokenizer.NumberToken
import tokenizer.Operation
import java.io.Closeable
import java.io.OutputStream

class PrintVisitor(outputStream: OutputStream) : RPNTokenVisitor(), Closeable {
    private val writer = outputStream.writer()
    private var started = false

    private fun writeSpaceIfStarted() {
        if (started) {
            writer.write(" ")
        } else {
            started = true
        }
    }

    override fun visit(token: NumberToken) {
        writeSpaceIfStarted()
        writer.write(token.value.toString())
    }

    override fun visit(token: Operation) {
        writeSpaceIfStarted()
        writer.write(token.asString)
    }

    override fun close() {
        writer.close()
    }
}