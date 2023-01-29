package io.portx.service;

import io.portx.domain.Payment;
import io.portx.domain.PaymentStatusEnum;

import java.util.List;

public interface PaymentService {
    Payment save(Payment payment, String idempotencyKey);

    Payment findById(long id);

    List<Payment> findAllByStatus(PaymentStatusEnum status);

    Payment update(Payment payment);

    void delete(long id);
}
