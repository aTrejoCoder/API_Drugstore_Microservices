package microservice.ecommerce_payment_service.Repository;

import microservice.ecommerce_payment_service.Model.Card;
import microservice.ecommerce_payment_service.Model.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p FROM Payment p WHERE p.clientId = :clientId AND p.status = 'SUCCESS'")
    Page<Payment> findCompletedPaymentsByClientId(Long clientId, Pageable pageable);
}