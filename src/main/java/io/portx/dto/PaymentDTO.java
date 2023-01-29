package io.portx.dto;

import io.portx.domain.PaymentStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private long id;

    private String currency;

    private OriginatorDTO originator;

    private BeneficiaryDTO beneficiary;

    private AccountDTO sender;

    private AccountDTO receiver;

    private Double amount;

}
