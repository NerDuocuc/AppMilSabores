package com.service.pasteleriamilsabores.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {
    @Id
    @Column(name = "id")
    private String id;

    @ManyToOne
    @JoinColumn(name = "usuario_run")
    private User user;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PedidoItem> items = new ArrayList<>();

    @Column(name = "monto_total")
    private BigDecimal totalAmount;

    @Column(name = "monto_subtotal")
    private BigDecimal subtotalAmount;

    @Column(name = "porcentaje_descuento_aplicado")
    private Integer discountAppliedPercent;

    @Column(name = "porcentaje_descuento_permanente_aplicado")
    private Integer lifetimeDiscountAppliedPercent;

    @Column(name = "torta_gratis_aplicada")
    private Boolean freeCakeApplied;

    @Column(name = "monto_torta_gratis")
    private java.math.BigDecimal freeCakeAmount;

    @Column(name = "monto_descuento")
    private java.math.BigDecimal discountAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "fecha_creacion")
    private java.time.LocalDateTime createdAt;

    @Column(name = "direccion_envio")
    private String deliveryAddress;

    public void addItem(PedidoItem item) {
        item.setPedido(this);
        this.items.add(item);
    }

    public void removeItem(PedidoItem item) {
        item.setPedido(null);
        this.items.remove(item);
    }

    public void setItems(List<PedidoItem> items) {
        this.items.clear();
        if (items != null) {
            for (PedidoItem it : items) {
                it.setPedido(this);
                this.items.add(it);
            }
        }
    }
}
