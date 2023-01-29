package io.portx.repository;

import io.portx.domain.Payment;
import io.portx.domain.PaymentStatusEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment, Long> {

    List<Payment> findAllByStatus(PaymentStatusEnum status);

}
