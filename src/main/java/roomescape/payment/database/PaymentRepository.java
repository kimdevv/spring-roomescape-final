package roomescape.payment.database;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.payment.model.Payment;
import roomescape.payment.model.ProductType;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByProductTypeAndProductId(ProductType productType, Long productId);
}
