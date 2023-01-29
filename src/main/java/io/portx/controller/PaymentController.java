package io.portx.controller;

import io.portx.controller.mapper.PaymentMapper;
import io.portx.domain.Payment;
import io.portx.domain.PaymentStatusEnum;
import io.portx.dto.PaymentDTO;
import io.portx.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    private final PaymentMapper mapper;

    public PaymentController(final PaymentService service, final PaymentMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<PaymentDTO> create(@RequestBody PaymentDTO paymentDto, @RequestHeader("idempotency-Key") String idempotencyKey) {
        Payment payment = this.mapper.convertToPayment(paymentDto);
        return ok(this.mapper.convertToPaymentDTO(this.service.save(payment, idempotencyKey)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> update(@PathVariable long id, @RequestBody PaymentDTO dto) {

        Payment foundEntity = service.findById(id);

        if (foundEntity == null) {
            return noContent().build();
        }

        Payment entity = this.mapper.convertToPayment(dto);

        return ok(this.mapper.convertToPaymentDTO(service.update(entity)));
    }

    @GetMapping("/search")
    public List<PaymentDTO> findAllByStatus(@RequestParam PaymentStatusEnum status) {
        return mapper.convertToPaymentDTOList(service.findAllByStatus(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  deletePayment(@PathVariable long id) {
         service.delete(id);
         return ResponseEntity.status(NO_CONTENT).body(null);
    }
}
