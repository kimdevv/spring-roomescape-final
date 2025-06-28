package roomescape.payment.database;

import org.springframework.data.repository.CrudRepository;
import roomescape.payment.model.Payment;
import roomescape.payment.model.ProductType;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    List<Payment> findAll();

    Optional<Payment> findByProductTypeAndProductId(ProductType productType, Long productId);
}
