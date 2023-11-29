package edu.miu.cs473de.lab6.foodiepal.errors

class ValidationException(val field: String, override val message: String?): Exception() {
}