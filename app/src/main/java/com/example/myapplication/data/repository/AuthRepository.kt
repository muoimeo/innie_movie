package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.entities.User
import java.util.UUID
import java.util.regex.Pattern

// Specific Auth Errors
sealed class AuthError {
    // Validation errors
    object EmptyUsername : AuthError()
    object EmptyEmail : AuthError()
    object EmptyPassword : AuthError()
    object InvalidEmailFormat : AuthError()
    object PasswordTooShort : AuthError()
    object PasswordTooWeak : AuthError()
    object UsernameTooShort : AuthError()
    object UsernameInvalidCharacters : AuthError()
    
    // Database errors
    object UsernameAlreadyExists : AuthError()
    object EmailAlreadyExists : AuthError()
    object UserNotFound : AuthError()
    object IncorrectPassword : AuthError()
    
    // System errors
    data class DatabaseError(val details: String) : AuthError()
    data class UnknownError(val details: String) : AuthError()
    
    // Convert to user-friendly message (English)
    fun toMessage(): String = when (this) {
        EmptyUsername -> "Username cannot be empty"
        EmptyEmail -> "Email cannot be empty"
        EmptyPassword -> "Password cannot be empty"
        InvalidEmailFormat -> "Invalid email format"
        PasswordTooShort -> "Password must be at least 8 characters"
        PasswordTooWeak -> "Password must contain uppercase, lowercase, and digit"
        UsernameTooShort -> "Username must be at least 3 characters"
        UsernameInvalidCharacters -> "Username can only contain letters, numbers, and underscores"
        UsernameAlreadyExists -> "Username is already taken"
        EmailAlreadyExists -> "Email is already registered"
        UserNotFound -> "User not found"
        IncorrectPassword -> "Incorrect password"
        is DatabaseError -> "Database error: $details"
        is UnknownError -> "An error occurred: $details"
    }
}

// Auth Result
sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val error: AuthError) : AuthResult()
}

// Validator
object AuthValidator {
    
    private val EMAIL_PATTERN = Pattern.compile(
        "[a-zA-Z0-9+._%\\-]{1,256}" +
        "@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+"
    )
    
    private val USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$")
    
    fun validateUsername(username: String): AuthError? {
        return when {
            username.isBlank() -> AuthError.EmptyUsername
            username.length < 3 -> AuthError.UsernameTooShort
            !USERNAME_PATTERN.matcher(username).matches() -> AuthError.UsernameInvalidCharacters
            else -> null
        }
    }
    
    fun validateEmail(email: String): AuthError? {
        return when {
            email.isBlank() -> AuthError.EmptyEmail
            !EMAIL_PATTERN.matcher(email).matches() -> AuthError.InvalidEmailFormat
            else -> null
        }
    }
    
    fun validatePassword(password: String): AuthError? {
        return when {
            password.isBlank() -> AuthError.EmptyPassword
            password.length < 8 -> AuthError.PasswordTooShort
            !hasUpperCase(password) || !hasLowerCase(password) || !hasDigit(password) -> 
                AuthError.PasswordTooWeak
            else -> null
        }
    }
    
    private fun hasUpperCase(str: String) = str.any { it.isUpperCase() }
    private fun hasLowerCase(str: String) = str.any { it.isLowerCase() }
    private fun hasDigit(str: String) = str.any { it.isDigit() }
}

class AuthRepository(
    private val userDao: UserDao
) {

    // Improved password hashing with salt
    private fun hashPassword(password: String, salt: String = ""): String {
        val saltedPassword = salt + password
        val bytes = saltedPassword.toByteArray()
        val md = java.security.MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
    
    // Generate random salt
    private fun generateSalt(): String {
        return UUID.randomUUID().toString().take(16)
    }

    suspend fun signUp(username: String, email: String, password: String): AuthResult {
        return try {
            // Validate fields
            AuthValidator.validateUsername(username)?.let {
                return AuthResult.Error(it)
            }
            
            AuthValidator.validateEmail(email)?.let {
                return AuthResult.Error(it)
            }
            
            AuthValidator.validatePassword(password)?.let {
                return AuthResult.Error(it)
            }

            // Check if username exists
            if (userDao.getUserByUsername(username) != null) {
                return AuthResult.Error(AuthError.UsernameAlreadyExists)
            }

            // Check if email exists
            if (userDao.getUserByEmail(email) != null) {
                return AuthResult.Error(AuthError.EmailAlreadyExists)
            }

            // Create new user with salt
            val salt = generateSalt()
            val user = User(
                userId = UUID.randomUUID().toString(),
                username = username.trim(),
                email = email.trim().lowercase(),
                passwordHash = hashPassword(password, salt),
                salt = salt
            )

            userDao.insertUser(user)
            AuthResult.Success(user)
            
        } catch (e: Exception) {
            AuthResult.Error(AuthError.DatabaseError(e.message ?: "Unknown error"))
        }
    }

    suspend fun login(identifier: String, password: String): AuthResult {
        return try {
            // Validate input
            if (identifier.isBlank()) {
                return AuthResult.Error(AuthError.EmptyUsername)
            }
            
            if (password.isBlank()) {
                return AuthResult.Error(AuthError.EmptyPassword)
            }

            // Find user
            val user = userDao.getUserByUsernameOrEmail(identifier.trim())
                ?: return AuthResult.Error(AuthError.UserNotFound)

            // Verify password with salt
            val hashedInput = hashPassword(password, user.salt ?: "")
            
            if (hashedInput == user.passwordHash) {
                AuthResult.Success(user)
            } else {
                AuthResult.Error(AuthError.IncorrectPassword)
            }
            
        } catch (e: Exception) {
            AuthResult.Error(AuthError.DatabaseError(e.message ?: "Unknown error"))
        }
    }
}

// Extension functions
fun AuthResult.onSuccess(action: (User) -> Unit): AuthResult {
    if (this is AuthResult.Success) {
        action(user)
    }
    return this
}

fun AuthResult.onError(action: (AuthError) -> Unit): AuthResult {
    if (this is AuthResult.Error) {
        action(error)
    }
    return this
}
