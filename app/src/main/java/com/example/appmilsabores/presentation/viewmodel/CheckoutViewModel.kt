package com.example.appmilsabores.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmilsabores.data.repository.AddressRepositoryImpl
import com.example.appmilsabores.data.repository.CartRepositoryImpl
import com.example.appmilsabores.data.repository.PaymentRepositoryImpl
import com.example.appmilsabores.data.repository.SessionRepositoryImpl
import com.example.appmilsabores.data.repository.UserRepositoryImpl
import com.example.appmilsabores.domain.model.Address
import com.example.appmilsabores.domain.model.CartItem
import com.example.appmilsabores.domain.model.Order
import com.example.appmilsabores.domain.model.PaymentMethod
import com.example.appmilsabores.domain.repository.AddressRepository
import com.example.appmilsabores.domain.repository.CartRepository
import com.example.appmilsabores.domain.repository.PaymentRepository
import com.example.appmilsabores.domain.repository.SessionRepository
import com.example.appmilsabores.domain.repository.UserRepository
import com.example.appmilsabores.domain.usecase.CreateOrderUseCase
import com.example.appmilsabores.domain.usecase.GetCartUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CheckoutUiState(
    val items: List<CartItem> = emptyList(),
    val selectedAddress: Address? = null,
    val selectedPayment: PaymentMethod? = null,
    val subtotal: Double = 0.0,
    val shippingCost: Double = 0.0,
    val total: Double = 0.0,
    val promoCode: String? = null,
    val promoApplied: Boolean = false,
    val promoDiscount: Double = 0.0,
    val isProcessing: Boolean = false,
    val isLoadingSelections: Boolean = true,
    val errorMessage: String? = null
)

class CheckoutViewModel(
    private val cartRepository: CartRepository = CartRepositoryImpl(),
    private val sessionRepository: SessionRepository = SessionRepositoryImpl(),
    private val paymentRepository: PaymentRepository = PaymentRepositoryImpl(),
    private val addressRepository: AddressRepository = AddressRepositoryImpl(),
    private val userRepository: UserRepository = UserRepositoryImpl(),
    private val getCartUseCase: GetCartUseCase = GetCartUseCase(cartRepository, sessionRepository),
    private val createOrderUseCase: CreateOrderUseCase = CreateOrderUseCase(cartRepository, sessionRepository, userRepository)
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    init {
        observeCart()
        refreshSelections()
        observeUserPromo()
    }

    private fun observeUserPromo() {
        viewModelScope.launch {
            try {
                val profile = userRepository.getUserProfile()
                val promo = profile?.promoCode
                if (!promo.isNullOrBlank()) {
                    _uiState.update { it.copy(promoCode = promo) }
                }
            } catch (_: Exception) {
                // ignore
            }
        }
    }

    private fun observeCart() {
        viewModelScope.launch {
            getCartUseCase().collectLatest { items ->
                updateTotals(items)
            }
        }
    }

    private fun updateTotals(items: List<CartItem>) {
        val subtotal = items.sumOf { it.price * it.quantity }
        val shipping = if (subtotal > 0) SHIPPING_FEE_CLP else 0.0
        val currentPromo = _uiState.value.promoCode
        val isApplied = _uiState.value.promoApplied
        val discount = if (isApplied && !currentPromo.isNullOrBlank() && isValidPromo(currentPromo)) {
            subtotal * PROMO_DISCOUNT_RATE
        } else 0.0
        val total = subtotal + shipping - discount
        _uiState.update { state ->
            state.copy(
                items = items,
                subtotal = subtotal,
                shippingCost = shipping,
                total = total,
                promoDiscount = discount
            )
        }
    }

    private fun isValidPromo(code: String): Boolean {
        return code.equals("Felices50", ignoreCase = true)
    }

    fun refreshSelections() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingSelections = true) }
            val addresses = withContext(Dispatchers.Default) { addressRepository.getAddresses() }
            val payments = withContext(Dispatchers.Default) { paymentRepository.getPaymentMethods() }

            _uiState.update { state ->
                state.copy(
                    selectedAddress = addresses.firstOrNull { it.isDefault } ?: addresses.firstOrNull(),
                    selectedPayment = payments.firstOrNull { it.isDefault } ?: payments.firstOrNull(),
                    isLoadingSelections = false,
                    errorMessage = null
                )
            }
        }
    }

    fun confirmPurchase(onSuccess: (Order, Double) -> Unit) {
        val currentState = _uiState.value
        if (currentState.isProcessing) return

        if (currentState.items.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Tu carrito está vacío") }
            return
        }

        val address = currentState.selectedAddress
        if (address == null) {
            _uiState.update { it.copy(errorMessage = "Agrega una dirección de entrega") }
            return
        }

        val payment = currentState.selectedPayment
        if (payment == null) {
            _uiState.update { it.copy(errorMessage = "Agrega un método de pago") }
            return
        }

        val itemsSnapshot = currentState.items
        val shippingSnapshot = currentState.shippingCost
        val totalBeforeOrder = currentState.total

        viewModelScope.launch {
            _uiState.update { it.copy(isProcessing = true, errorMessage = null) }
            try {
                delay(PAYMENT_SIMULATION_DELAY_MS)
                val order = createOrderUseCase(
                    items = itemsSnapshot,
                    shippingCost = shippingSnapshot,
                    address = address,
                    paymentMethod = payment
                )
                // If a promo was applied for this purchase, consume it from the user's account.
                if (currentState.promoApplied && !currentState.promoCode.isNullOrBlank()) {
                    try {
                        userRepository.setPromoCodeForCurrentUser(null)
                        // update local uiState to reflect promo consumed
                        _uiState.update { it.copy(promoCode = null, promoApplied = false, isProcessing = false) }
                    } catch (_: Exception) {
                        // if clearing promo fails, still proceed with success but keep UI consistent
                        _uiState.update { it.copy(isProcessing = false) }
                    }
                } else {
                    _uiState.update { it.copy(isProcessing = false) }
                }

                onSuccess(order, totalBeforeOrder)
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        isProcessing = false,
                        errorMessage = error.message ?: "Error al procesar el pago"
                    )
                }
            }
        }
    }

    fun applyPromoCode(inputCode: String) {
        val code = inputCode.trim()
        if (!isValidPromo(code)) {
            _uiState.update { it.copy(errorMessage = "Código promocional inválido") }
            return
        }

        viewModelScope.launch {
            try {
                // persist on account (if logged in)
                userRepository.setPromoCodeForCurrentUser(code)
                _uiState.update { it.copy(promoCode = code, promoApplied = true) }
                // recalc totals
                updateTotals(_uiState.value.items)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "No fue posible aplicar el código") }
            }
        }
    }

    fun toggleUsePromo(apply: Boolean) {
        _uiState.update { it.copy(promoApplied = apply) }
        updateTotals(_uiState.value.items)
    }

    companion object {
        private const val SHIPPING_FEE_CLP = 5990.0
        private const val PAYMENT_SIMULATION_DELAY_MS = 1200L
        private const val PROMO_DISCOUNT_RATE = 0.10 // 10% discount
    }
}
