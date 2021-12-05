import tokenizer.NumberToken

fun String.toNumberToken() = NumberToken(toBigInteger())