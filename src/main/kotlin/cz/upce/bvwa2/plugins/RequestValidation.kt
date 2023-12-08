@file:Suppress("DuplicatedCode")

package cz.upce.bvwa2.plugins

import cz.upce.bvwa2.models.CreateUserRequest
import cz.upce.bvwa2.models.MessageRequest
import cz.upce.bvwa2.models.UpdateUserRequest
import cz.upce.bvwa2.utils.Validator
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<CreateUserRequest> { user ->
            if (!Validator.validatePassword(user.password))
                ValidationResult.Invalid("Neplatné heslo")
            if (!Validator.validateEmail(user.email))
                ValidationResult.Invalid("Neplatný email")
            if (!Validator.validatePhone(user.phone))
                ValidationResult.Invalid("Neplatný telefon")
            if (!Validator.validateUsername(user.user))
                ValidationResult.Invalid("Neplatné uživatelské jméno")
            if (!Validator.notEmpty(user.firstName))
                ValidationResult.Invalid("Neplatné jméno")
            if (!Validator.notEmpty(user.lastName))
                ValidationResult.Invalid("Neplatné příjmení")
            if (!Validator.notEmpty(user.gender))
                ValidationResult.Invalid("Neplatné pohlaví")
            else ValidationResult.Valid
        }

        validate<UpdateUserRequest> { user ->
            if (!Validator.validateEmail(user.email))
                ValidationResult.Invalid("Neplatný email")
            if (!Validator.validatePhone(user.phone))
                ValidationResult.Invalid("Neplatný telefon")
            if (!Validator.notEmpty(user.firstName))
                ValidationResult.Invalid("Prázdné jméno")
            if (!Validator.notEmpty(user.lastName))
                ValidationResult.Invalid("Prázdné příjmení")
            if (!Validator.notEmpty(user.gender))
                ValidationResult.Invalid("Prázdné pohlaví")
            else ValidationResult.Valid
        }

        validate<MessageRequest> { message ->
            if (!Validator.notEmpty(message.message))
                ValidationResult.Invalid("Prázdná zpráva")
            if (!Validator.notEmpty(message.subject))
                ValidationResult.Invalid("Prázdný předmět")
            if (!Validator.notEmpty(message.to))
                ValidationResult.Invalid("Prázdný adresát")
            else ValidationResult.Valid
        }
    }
}