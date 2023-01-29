package io.portx.service.impl;

import io.portx.domain.Payment;
import io.portx.domain.PaymentStatusEnum;
import io.portx.repository.PaymentRepository;
import io.portx.service.PaymentService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.logging.Logger;

@Component
public class PaymentServiceImpl implements PaymentService {

    Logger logger = Logger.getLogger(PaymentServiceImpl.class.getName());

    private final ConcurrentNavigableMap<String , Payment> paymentIdempotencyCache = new ConcurrentSkipListMap<>();

    private final KafkaTemplate<String, Payment> kafkaTemplate;

    private final PaymentRepository repository;

    private final static String TOPIC_PAYMENT = "payments";

    public PaymentServiceImpl(
            final KafkaTemplate<String, Payment> kafkaTemplate,
            final PaymentRepository repository) {
        this.kafkaTemplate = kafkaTemplate;
        this.repository = repository;
    }

    @Override
    public Payment save(final Payment payment, final String idempotencyKey){
        if(paymentIdempotencyCache.containsKey(idempotencyKey)) return paymentIdempotencyCache.get(idempotencyKey);
        payment.setStatus(PaymentStatusEnum.CREATED);
        Payment saved = this.repository.save(payment);
        paymentIdempotencyCache.put(idempotencyKey, payment);
        this.sendMessage(saved);
        return saved;
    }

    @Override
    public Payment findById(final long id){
       return this.repository.findById(id).orElse(null);
    }

    @Override
    public List<Payment> findAllByStatus(final PaymentStatusEnum status){
        return this.repository.findAllByStatus(status);
    }

    @Override
    public Payment update(final Payment payment){
        return this.repository.save(payment);
    }

    @Override
    public void delete(final long id){
        this.repository.deleteById(id);
    }


    private void sendMessage(final Payment payment){
        kafkaTemplate.send(TOPIC_PAYMENT, payment).whenComplete((result, ex) -> {
            if (ex != null) {
                logger.info("Message put on kafka successfully");
            }else {
              throw new RuntimeException("Cannot put a message on Kafka");
            }
        });
    }


}
