package com.example.appmilsabores.domain.usecase

import com.example.appmilsabores.data.repository.AddressRepositoryImpl
import com.example.appmilsabores.data.repository.PaymentRepositoryImpl
import com.example.appmilsabores.domain.model.SessionState
import com.example.appmilsabores.domain.repository.SessionRepository
import com.example.appmilsabores.domain.repository.UserRepository
import com.example.appmilsabores.network.ApiClient
import com.example.appmilsabores.network.LoginRequest
import com.example.appmilsabores.AppMilSaboresApplication

class ValidateUserLoginUseCase(
	private val userRepository: UserRepository,
	private val sessionRepository: SessionRepository,
	private val addressRepository: AddressRepositoryImpl = AddressRepositoryImpl(),
	private val paymentRepository: PaymentRepositoryImpl = PaymentRepositoryImpl()
) {

	sealed class Result {
		object Success : Result()
		data class Error(val message: String) : Result()
	}

	suspend operator fun invoke(email: String, password: String, rememberMe: Boolean): Result {
		val normalizedEmail = email.trim()
		if (normalizedEmail.isBlank()) {
			return Result.Error("El correo es obligatorio")
		}

		val candidate = userRepository.findUserByEmail(normalizedEmail)
			?: return Result.Error("Credenciales inválidas")

		val authenticatedUser = if (candidate.hasPassword) {
			if (password.isBlank()) {
				return Result.Error("Debes ingresar tu contraseña")
			}
			userRepository.authenticate(normalizedEmail, password)
				?: return Result.Error("Credenciales inválidas")
		} else {
			userRepository.authenticate(normalizedEmail, password) ?: candidate
		}

		sessionRepository.saveSession(
			SessionState(
				isLoggedIn = true,
				userId = authenticatedUser.id,
				email = if (rememberMe) authenticatedUser.email else null,
				fullName = authenticatedUser.fullName,
				rememberMe = rememberMe
			)
		)

			// Attempt remote login to obtain JWT and persist it for authenticated requests.
			try {
				val loginResp = ApiClient.service.login(LoginRequest(correo = normalizedEmail, password = password))
				// persist token in DataStore and update in-memory token
				AppMilSaboresApplication.sessionPreferences.saveJwtToken(loginResp.token)
			} catch (t: Throwable) {
				// remote auth failed (maybe server unreachable or credentials mismatch) — continue with local session
			}

		val profile = userRepository.getUserProfile()
		val primaryAddress = profile?.address?.trim().orEmpty()
		if (primaryAddress.isNotEmpty()) {
			addressRepository.setPrimaryAddress(primaryAddress)
		} else {
			addressRepository.clearAll()
		}

		return Result.Success
	}
}