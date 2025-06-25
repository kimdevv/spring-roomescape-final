package roomescape.payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"productType", "productId"}))
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String paymentKey;

    @Column(nullable = false, unique = true)
    private String orderId;

    @Column(nullable = false)
    private Long amount;

    @Column(nullable = false)
    private ProductType productType;

    @Column(nullable = false)
    private Long productId;

    protected Payment() {}

    public Payment(String paymentKey, String orderId, Long amount, ProductType productType, Long productId) {
        this.id = null;
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
        this.productType = productType;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public Long getAmount() {
        return amount;
    }

    public ProductType getProductType() {
        return productType;
    }

    public Long getProductId() {
        return productId;
    }
}
