package uo.ri.cws.application.service.paymentmean.crud.commands;

import java.math.BigDecimal;

import uo.ri.conf.Factories;
import uo.ri.cws.application.repository.ClientRepository;
import uo.ri.cws.application.repository.PaymentMeanRepository;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.crud.DtoAssembler;
import uo.ri.cws.application.util.command.Command;
import uo.ri.cws.domain.Associations;
import uo.ri.cws.domain.Client;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.types.Money;
import uo.ri.cws.domain.types.VoucherCode;
import uo.ri.util.assertion.ArgumentChecks;
import uo.ri.util.exception.BusinessChecks;
import uo.ri.util.exception.BusinessException;

public class AddVoucher implements Command<VoucherDto> {

    private VoucherDto dto;
    private ClientRepository clientRepo = Factories.repository.forClient();
    private PaymentMeanRepository pmRepo =
            Factories.repository.forPaymentMean();

    public AddVoucher(VoucherDto dto) {
        ArgumentChecks.isNotNull(dto, "Voucher dto cannot be null");
        ArgumentChecks.isNotBlank(dto.clientId,
                "Client id cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.code,
                "Voucher code cannot be null or blank");
        ArgumentChecks.isNotBlank(dto.description,
                "Voucher description cannot be null or blank");
        ArgumentChecks.isNotNull(dto.availableAmount,
                "Available amount cannot be null");
        ArgumentChecks.isTrue(
                dto.availableAmount.compareTo(BigDecimal.ZERO) >= 0,
                "Available amount cannot be negative");
        this.dto = dto;
    }

    @Override
    public VoucherDto execute() throws BusinessException {
        Client client = clientRepo.findById(dto.clientId)
                .orElseThrow(() ->
                        new BusinessException("Client does not exist"));

        BusinessChecks.isFalse(
                pmRepo.findVoucherByCode(new VoucherCode(dto.code)).isPresent(),
                "Voucher already exists with this code"
        );

        Voucher voucher = new Voucher(
                new VoucherCode(dto.code),
                dto.description,
                Money.of(dto.availableAmount)
        );
        Associations.Holds.link(client, voucher);
        pmRepo.add(voucher);

        return DtoAssembler.toDto(voucher);
    }

}
