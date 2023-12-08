package cz.upce.bvwa2.utils

import org.apache.commons.validator.routines.EmailValidator
import org.passay.*
import java.io.InputStream
import javax.imageio.ImageIO


object Validator {
    private val passwordValidator = PasswordValidator(
        LengthRule(8, 16), // length between 8 and 16 characters
        CharacterRule(EnglishCharacterData.UpperCase, 1), // at least one upper-case character
        CharacterRule(EnglishCharacterData.LowerCase, 1), // at least one lower-case character
        CharacterRule(EnglishCharacterData.Digit, 1), // at least one digit character
        WhitespaceRule() // no whitespaces
    )
    private val emailValidator = EmailValidator.getInstance()
    private val phoneRegex = """^(\+\d{1,3}( )?)?((\(\d{3}\))|\d{3})[- .]?\d{3}[- .]?\d{4}$|^(\+\d{1,3}( )?)?(\d{3}[ ]?){2}\d{3}$|^(\+\d{1,3}( )?)?(\d{3}[ ]?)(\d{2}[ ]?){2}\d{2}$""".toRegex()

    fun validateEmail(email: String) = emailValidator.isValid(email)
    fun validatePassword(password: String) = passwordValidator.validate(PasswordData(password)).isValid
    fun validatePhone(phone: String) = phoneRegex.matches(phone)
    fun notEmpty(value: String?) = value.isNullOrBlank()
    fun validateImage(imageStream: InputStream) = ImageIO.read(imageStream).width == 800
}