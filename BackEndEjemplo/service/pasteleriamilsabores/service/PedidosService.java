package com.service.pasteleriamilsabores.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.pasteleriamilsabores.dto.OrderItemRequest;
import com.service.pasteleriamilsabores.dto.OrderRequest;
import com.service.pasteleriamilsabores.dto.OrderResponse;
import com.service.pasteleriamilsabores.dto.OrderItemSummaryDto;
import com.service.pasteleriamilsabores.dto.OrderSummaryDto;
import com.service.pasteleriamilsabores.models.Pedido;
import com.service.pasteleriamilsabores.models.PedidoItem;
import com.service.pasteleriamilsabores.models.Producto;
import com.service.pasteleriamilsabores.models.User;
import com.service.pasteleriamilsabores.repository.PedidoRepository;
import com.service.pasteleriamilsabores.repository.ProductoRepository;
import com.service.pasteleriamilsabores.repository.UserRepository;

@Service
public class PedidosService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest req) {
        if (req == null) throw new IllegalArgumentException("OrderRequest required");
        User user = userRepository.findById(req.getUserRun()).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        String pedidoId = UUID.randomUUID().toString();
        pedido.setId(pedidoId);
        pedido.setUser(user);
        pedido.setCreatedAt(LocalDateTime.now());
        pedido.setStatus("CREATED");
        pedido.setDeliveryAddress(req.getDeliveryAddress());

        BigDecimal subtotal = BigDecimal.ZERO;
        List<Object> itemsEcho = new ArrayList<>();

        // build items
        List<PedidoItem> items = new ArrayList<>();
        for (OrderItemRequest ir : req.getItems()) {
            Producto prod = productoRepository.findById(ir.getProductoCodigo()).orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + ir.getProductoCodigo()));
            BigDecimal unit = prod.getPrecioProducto() == null ? BigDecimal.ZERO : BigDecimal.valueOf(prod.getPrecioProducto());
            Integer qtyInteger = ir.getCantidad();
            int qty = qtyInteger == null ? 0 : qtyInteger;
            // validate stock
            Integer stock = prod.getStock();
            if (stock == null) stock = 0;
            if (qty > stock) {
                throw new IllegalArgumentException("Stock insuficiente para producto: " + prod.getCodigoProducto());
            }
            // reserve/decrement stock
            prod.setStock(stock - qty);
            productoRepository.save(prod);
            BigDecimal lineTotal = unit.multiply(BigDecimal.valueOf(qty));

            PedidoItem pi = new PedidoItem();
            pi.setId(UUID.randomUUID().toString());
            pi.setProducto(prod);
            pi.setCantidad(qty);
            pi.setPrecioUnitario(unit);
            pi.setTotalPrice(lineTotal);
            pi.setPedido(pedido);
            items.add(pi);

            subtotal = subtotal.add(lineTotal);

            // simple echo
            itemsEcho.add(java.util.Map.of("codigo", prod.getCodigoProducto(), "nombre", prod.getNombreProducto(), "cantidad", qty, "unit", unit, "lineTotal", lineTotal));
        }

        pedido.setItems(items);

        // preserve original subtotal (before free cake / discounts)
        BigDecimal originalSubtotal = subtotal;

        // separate cake vs non-cake subtotals
        BigDecimal cakeSubtotal = BigDecimal.ZERO;
        for (PedidoItem pi : items) {
            String name = pi.getProducto() == null ? "" : pi.getProducto().getNombreProducto();
            if (name != null && name.toLowerCase().contains("torta")) {
                cakeSubtotal = cakeSubtotal.add(pi.getTotalPrice());
            }
        }
        BigDecimal nonCakeSubtotal = originalSubtotal.subtract(cakeSubtotal);

        // 1) apply free cake: if user has freeCakeEligible true, find a cake in items and make one unit free
        BigDecimal freeCakeAmount = BigDecimal.ZERO;
        boolean freeCakeApplied = false;
        if (Boolean.TRUE.equals(user.getFreeCakeEligible())) {
            for (PedidoItem pi : items) {
                String name = pi.getProducto() == null ? "" : pi.getProducto().getNombreProducto();
                if (name != null && name.toLowerCase().contains("torta") && pi.getCantidad() != null && pi.getCantidad() > 0 && !freeCakeApplied) {
                    BigDecimal unitPrice = pi.getPrecioUnitario() == null ? BigDecimal.ZERO : pi.getPrecioUnitario();
                    freeCakeAmount = freeCakeAmount.add(unitPrice);
                    // reduce line total by unitPrice
                    pi.setTotalPrice(pi.getTotalPrice().subtract(unitPrice));
                    cakeSubtotal = cakeSubtotal.subtract(unitPrice);
                    freeCakeApplied = true;
                    break;
                }
            }
        }

        // 2) apply discounts only to cakes, and only if frontend enabled the flag
        Integer ageDiscountInteger = user.getDiscountPercent();
        Integer lifetimeInteger = user.getLifetimeDiscountPercent();
        int ageDiscount = ageDiscountInteger == null ? 0 : ageDiscountInteger;
        int lifetime = lifetimeInteger == null ? 0 : lifetimeInteger;
        int totalPercent = ageDiscount + lifetime; // cumulative as requested

        BigDecimal discountAmount = BigDecimal.ZERO;
        boolean applyDiscounts = Boolean.TRUE.equals(req.getApplyDiscounts());
        if (applyDiscounts && totalPercent > 0 && cakeSubtotal.compareTo(BigDecimal.ZERO) > 0) {
            discountAmount = cakeSubtotal.multiply(BigDecimal.valueOf(totalPercent)).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        }

        BigDecimal total = cakeSubtotal.subtract(discountAmount).add(nonCakeSubtotal).setScale(2, RoundingMode.HALF_UP);

        // persist snapshot fields
        pedido.setFreeCakeApplied(freeCakeApplied);
        pedido.setFreeCakeAmount(freeCakeAmount.setScale(2, RoundingMode.HALF_UP));
        pedido.setDiscountAppliedPercent(ageDiscount > 0 ? ageDiscount : null);
        pedido.setLifetimeDiscountAppliedPercent(lifetime > 0 ? lifetime : null);
        pedido.setTotalAmount(total);
        pedido.setSubtotalAmount(subtotal.setScale(2, RoundingMode.HALF_UP));
        pedido.setDiscountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP));

        // save pedido
        pedidoRepository.save(pedido);

        OrderResponse resp = new OrderResponse();
        resp.setPedidoId(pedidoId);
        resp.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        resp.setFreeCakeAmount(freeCakeAmount.setScale(2, RoundingMode.HALF_UP));
        resp.setDiscountPercentApplied(ageDiscount > 0 ? ageDiscount : null);
        resp.setLifetimeDiscountPercentApplied(lifetime > 0 ? lifetime : null);
        resp.setDiscountAmount(discountAmount.setScale(2, RoundingMode.HALF_UP));
        resp.setTotal(total);
        resp.setItems(itemsEcho);

        // suggest available tortas (products with 'torta' in name and stock>0)
        List<String> cakeCandidates = new ArrayList<>();
        for (Producto p : productoRepository.findAll()) {
            String name = p.getNombreProducto() == null ? "" : p.getNombreProducto();
            Integer s = p.getStock();
            if (name.toLowerCase().contains("torta") && s != null && s > 0) {
                cakeCandidates.add(p.getCodigoProducto());
            }
        }
        boolean freeCakeAvailable = Boolean.TRUE.equals(user.getFreeCakeEligible()) && !cakeCandidates.isEmpty();
        resp.setFreeCakeAvailable(freeCakeAvailable);
        resp.setFreeCakeSuggestedProducts(cakeCandidates);

        return resp;
    }

    public List<OrderSummaryDto> listOrdersForUser(String run) {
        if (run == null || run.isBlank()) {
            return List.of();
        }
        return pedidoRepository.findByUserRunOrderByCreatedAtDesc(run)
                .stream()
                .map(this::toSummaryDto)
                .toList();
    }

    public List<OrderSummaryDto> listAllOrders() {
        return pedidoRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toSummaryDto)
                .toList();
    }

    private OrderSummaryDto toSummaryDto(Pedido pedido) {
        OrderSummaryDto dto = new OrderSummaryDto();
        dto.setPedidoId(pedido.getId());
        dto.setStatus(pedido.getStatus());
        dto.setCreatedAt(pedido.getCreatedAt());
        dto.setTotal(pedido.getTotalAmount());
        dto.setDeliveryAddress(pedido.getDeliveryAddress());
        dto.setFreeCakeApplied(pedido.getFreeCakeApplied());
        dto.setFreeCakeAmount(pedido.getFreeCakeAmount());
        dto.setDiscountAppliedPercent(pedido.getDiscountAppliedPercent());
        dto.setLifetimeDiscountAppliedPercent(pedido.getLifetimeDiscountAppliedPercent());
        dto.setSubtotal(pedido.getSubtotalAmount());
        dto.setDiscountAmount(pedido.getDiscountAmount());
        dto.setTotalSinDescuento(pedido.getSubtotalAmount());
        dto.setTotalConDescuento(pedido.getTotalAmount());
        if (pedido.getUser() != null) {
            dto.setPurchaserRun(pedido.getUser().getRun());
            dto.setPurchaserNombre(pedido.getUser().getNombre());
            dto.setPurchaserApellidos(pedido.getUser().getApellidos());
            dto.setPurchaserCorreo(pedido.getUser().getCorreo());
            dto.setPurchaserTelefono(pedido.getUser().getTelefono());
        }
        List<OrderItemSummaryDto> items = pedido.getItems().stream().map(pi -> {
            OrderItemSummaryDto item = new OrderItemSummaryDto();
            if (pi.getProducto() != null) {
                item.setProductoCodigo(pi.getProducto().getCodigoProducto());
                item.setProductoNombre(pi.getProducto().getNombreProducto());
            }
            item.setCantidad(pi.getCantidad());
            item.setPrecioUnitario(pi.getPrecioUnitario());
            item.setTotalPrice(pi.getTotalPrice());
            return item;
        }).toList();
        dto.setItems(items);
        return dto;
    }
}
