package io.portx.controller.mapper;

import io.portx.domain.Payment;
import io.portx.dto.PaymentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PaymentMapper {

    private final ModelMapper modelMapper;

    public PaymentMapper(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Payment convertToPayment(PaymentDTO paymentDTO) {
        return modelMapper.map(paymentDTO, Payment.class);
    }

    public PaymentDTO convertToPaymentDTO(Payment payment) {
        return modelMapper.map(payment, PaymentDTO.class);
    }

    public List<Payment> convertToVehicleList(List<PaymentDTO> vehiclesDTO) {
        return vehiclesDTO
                .stream()
                .map(this::convertToPayment)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> convertToPaymentDTOList(List<Payment> payments) {
        return payments
                .stream()
                .map(this::convertToPaymentDTO)
                .collect(Collectors.toList());
    }

}
