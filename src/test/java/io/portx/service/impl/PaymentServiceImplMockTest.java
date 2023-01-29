package io.portx.service.impl;

import io.portx.domain.Beneficiary;
import io.portx.domain.Originator;
import io.portx.domain.Payment;
import io.portx.domain.PaymentStatusEnum;
import io.portx.domain.Receiver;
import io.portx.domain.Sender;
import io.portx.repository.PaymentRepository;
import io.portx.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PaymentServiceImplMockTest {

    @Mock PaymentRepository paymentRepository;

    @Mock KafkaTemplate<String, Payment> kafkaTemplate;

    private PaymentService service;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        service = new PaymentServiceImpl(kafkaTemplate, paymentRepository);
    }

    @Test
    void should_throw_error_when_save_payment() {
        Payment saved = buildPayment();
        saved.setId(0);
        saved.setStatus(PaymentStatusEnum.CREATED);
        when(paymentRepository.save(any())).thenReturn(saved).thenThrow(RuntimeException.class);;
        assertThrows(RuntimeException.class,
                ()-> this.service.save(saved, UUID.randomUUID().toString()));
    }

    @Test
    void should_throw_error_when_put_message_on_kafka_save_payment() {
        Payment saved = buildPayment();
        saved.setId(0);
        saved.setStatus(PaymentStatusEnum.CREATED);
        when(paymentRepository.save(any())).thenReturn(saved);
        when(kafkaTemplate.send("payments", saved)).thenThrow(RuntimeException.class);
        assertThrows(RuntimeException.class,
                ()-> this.service.save(saved, UUID.randomUUID().toString()));
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
