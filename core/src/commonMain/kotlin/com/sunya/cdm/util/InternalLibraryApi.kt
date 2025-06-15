package com.sunya.cdm.util

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.CLASS

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR, // can be relaxed to a WARNING
    message = "This is internal API for my library, please don't rely on it."
)
@Target(CLASS)
@Retention(BINARY)
annotation class InternalLibraryApi