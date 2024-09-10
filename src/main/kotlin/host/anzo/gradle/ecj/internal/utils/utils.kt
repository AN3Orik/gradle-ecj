package host.anzo.gradle.ecj.internal.utils

import kotlin.contracts.*

@OptIn(ExperimentalContracts::class)
internal inline fun <T> applyTo(receiver: T, block: T.() -> Unit) {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    receiver.block()
}