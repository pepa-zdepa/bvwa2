@file:Suppress("DuplicatedCode")

package cz.upce.bvwa2.plugins

import cz.upce.bvwa2.models.CreateUserRequest
import cz.upce.bvwa2.models.MessageRequest
import cz.upce.bvwa2.models.UpdateUserRequest
import cz.upce.bvwa2.repository.UserRepository
import cz.upce.bvwa2.utils.Validator
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun Application.configureRequestValidation() {
    val userRepository by closestDI().instance<UserRepository>()
    val genders = userRepository.getAllGenders()

    install(RequestValidation) {
        validate<CreateUserRequest> { user ->
            if (!Validator.validatePassword(user.password))
                return@validate ValidationResult.Invalid("Neplatné heslo")
            if (!Validator.validateEmail(user.email))
                return@validate ValidationResult.Invalid("Neplatný email")
            if (!Validator.validatePhone(user.phone))
                return@validate ValidationResult.Invalid("Neplatný telefon")
            if (!Validator.validateUsername(user.user))
                return@validate ValidationResult.Invalid("Neplatné uživatelské jméno")
            if (!Validator.notEmpty(user.firstName))
                return@validate ValidationResult.Invalid("Neplatné jméno")
            if (!Validator.notEmpty(user.lastName))
                return@validate ValidationResult.Invalid("Neplatné příjmení")
            if (user.gender !in genders)
                return@validate ValidationResult.Invalid("Neplatné pohlaví")

            return@validate ValidationResult.Valid
        }

        validate<UpdateUserRequest> { user ->
            if (!Validator.validateEmail(user.email))
                return@validate ValidationResult.Invalid("Neplatný email")
            if (!Validator.validatePhone(user.phone))
                return@validate ValidationResult.Invalid("Neplatný telefon")
            if (!Validator.notEmpty(user.firstName))
                return@validate ValidationResult.Invalid("Prázdné jméno")
            if (!Validator.notEmpty(user.lastName))
                return@validate ValidationResult.Invalid("Prázdné příjmení")
            if (user.gender !in genders)
                return@validate ValidationResult.Invalid("Neplatné pohlaví")

            return@validate ValidationResult.Valid
        }

        validate<MessageRequest> { message ->
            if (!Validator.notEmpty(message.message))
                return@validate ValidationResult.Invalid("Prázdná zpráva")
            if (!Validator.notEmpty(message.subject))
                return@validate ValidationResult.Invalid("Prázdný předmět")
            if (!Validator.notEmpty(message.to))
                return@validate ValidationResult.Invalid("Prázdný adresát")

            return@validate ValidationResult.Valid
        }
    }
}