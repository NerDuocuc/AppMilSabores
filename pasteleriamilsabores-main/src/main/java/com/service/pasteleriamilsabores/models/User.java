package com.service.pasteleriamilsabores.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class User {
    @Id
    @Column(name = "run")
    private String run;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellidos")
    private String apellidos;
    @Column(name = "correo")
    private String correo;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    @Column(name = "tipo_usuario")
    @Convert(converter = com.service.pasteleriamilsabores.models.converters.UserTypeConverter.class)
    private UserType tipoUsuario;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Address> addresses = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Card> cards = new ArrayList<>();
    @Column(name = "password")
    private String password;

    @Column(name = "porcentaje_descuento")
    private Integer discountPercent;

    @Column(name = "porcentaje_descuento_permanente")
    private Integer lifetimeDiscountPercent;

    @Column(name = "torta_gratis_elegible")
    private Boolean freeCakeEligible;

    @Column(name = "codigo_registro")
    private String registrationCode;

    @Column(name = "fecha_registro")
    private LocalDate registrationDate;

    // maintain bidirectional relationship when setting collection
    public void setAddresses(List<Address> addresses) {
        this.addresses.clear();
        if (addresses != null) {
            for (Address a : addresses) {
                a.setUser(this);
                this.addresses.add(a);
            }
        }
    }
    public void addAddress(Address address) {
        address.setUser(this);
        this.addresses.add(address);
    }
    public void removeAddress(Address address) {
        address.setUser(null);
        this.addresses.remove(address);
    }
    public void setCards(List<Card> cards) {
        this.cards.clear();
        if (cards != null) {
            for (Card c : cards) {
                c.setUser(this);
                this.cards.add(c);
            }
        }
    }
    public void addCard(Card card) {
        card.setUser(this);
        this.cards.add(card);
    }
    public void removeCard(Card card) {
        card.setUser(null);
        this.cards.remove(card);
    }
}
