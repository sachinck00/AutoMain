package com.example.components

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getCurrentDateTime(): String {
    // Retrieve the current date and time
    val current = LocalDateTime.now()

    // Define the desired format
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")

    // Format the current date and time
    return current.format(formatter)
}
