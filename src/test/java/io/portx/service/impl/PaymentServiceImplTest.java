package io.portx.service.impl;

import io.portx.domain.Beneficiary;
import io.portx.domain.Originator;
import io.portx.domain.Payment;
import io.portx.domain.PaymentStatusEnum;
import io.portx.domain.Receiver;
import io.portx.domain.Sender;
import io.portx.service.PaymentService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PaymentServiceImplTest {

    @Autowired PaymentService service;

    @Test
    void should_get_payments_by_create_status() {
        Payment payment = this.service.save(this.buildPayment(), UUID.randomUUID().toString());
        this.service.save(this.buildPayment(), UUID.randomUUID().toString());
        this.service.save(this.buildPayment(), UUID.randomUUID().toString());
        this.service.save(this.buildPayment(), UUID.randomUUID().toString());
        this.service.save(this.buildPayment(), UUID.randomUUID().toString());

        List<Payment> payments = this.service.findAllByStatus(PaymentStatusEnum.CREATED);

        assertEquals(payments.size(), 5);
        assertEquals(payment.getId(), payments.get(0).getId());
    }

    @Test
    void should_save_payment() {
        Payment payment = this.service.save(this.buildPayment(), UUID.randomUUID().toString());
        assertNotNull(payment);
    }

    @Test
    void should_try_save_payment_with_same_idempotencyKey(){
        String idempotencyKey = UUID.randomUUID().toString();
        Payment payment = this.service.save(this.buildPayment(), idempotencyKey);
        assertNotNull(payment);

        Payment payment2 = this.service.save(this.buildPayment(), idempotencyKey);
        assertNotNull(payment);

        assertEquals(payment.getId(), payment2.getId());
    }

    @Test
    void should_update_payment() {
        Payment mock = this.buildPayment();
        mock.setCurrency("USD");
        Payment payment = this.service.save(this.buildPayment(), UUID.randomUUID().toString());

        assertEquals(payment.getCurrency(), "USD");

        payment.setCurrency("EUR");
        Payment updated = this.service.update(payment);

        assertEquals(updated.getId(), payment.getId());
        assertEquals(updated.getCurrency(), "EUR");

    }

    @Test
    void should_delete_payment_by_id() {
        Payment payment = this.service.save(this.buildPayment(), UUID.randomUUID().toString());
        Payment payment2  = this.service.save(this.buildPayment(), UUID.randomUUID().toString());

        List<Payment> payments = this.service.findAllByStatus(PaymentStatusEnum.CREATED);
        assertEquals(payments.size(), 2);

        service.delete(payment.getId());

        payments = this.service.findAllByStatus(PaymentStatusEnum.CREATED);
        assertEquals(payments.size(), 1);

        service.delete(payment2.getId());

        payments = this.service.findAllByStatus(PaymentStatusEnum.CREATED);
        assertEquals(payments.size(), 0);

    }

    private Payment buildPayment(){
        Originator originator = new Originator();
        originator.setName("originator");

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName("beneficiary");

        Sender sender = new Sender();
        sender.setNumber(123L);
        sender.setType("payment");

        Receiver receiver = new Receiver();
        receiver.setNumber(123L);
        receiver.setType("payment");

        Payment payment = new Payment();
        payment.setCurrency("USD");
        payment.setOriginator(originator);
        payment.setBeneficiary(beneficiary);
        payment.setSender(sender);
        payment.setReceiver(receiver);
        payment.setAmount(999.00);
        return payment;
    }
}
