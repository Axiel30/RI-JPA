package uo.ri.cws.application.service.paymentmean.crud;

import java.util.List;
import java.util.Optional;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.crud.commands.AddCard;
import uo.ri.cws.application.service.paymentmean.crud.commands.AddVoucher;
import uo.ri.cws.application.service.paymentmean.crud.commands.DeletePaymentMean;
import uo.ri.cws.application.service.paymentmean.crud.commands.FindCreditCardByNumber;
import uo.ri.cws.application.service.paymentmean.crud.commands.FindPaymentMeanById;
import uo.ri.cws.application.service.paymentmean.crud.commands.FindPaymentMeansByClientNif;
import uo.ri.cws.application.service.paymentmean.crud.commands.FindVoucherByCode;
import uo.ri.cws.application.util.command.CommandExecutor;
import uo.ri.util.exception.BusinessException;

public class PaymentMeanCrudServiceImpl implements PaymentMeanCrudService {

    private CommandExecutor executor = Factories.executor.forExecutor();

    @Override
    public CardDto addCard(CardDto card) throws BusinessException {
        return executor.execute(new AddCard(card));
    }

    @Override
    public VoucherDto addVoucher(VoucherDto voucher) throws BusinessException {
        return executor.execute(new AddVoucher(voucher));
    }

    @Override
    public void deletePaymentMean(String id) throws BusinessException {
        executor.execute(new DeletePaymentMean(id));
    }

    @Override
    public Optional<PaymentMeanDto> findById(String id)
            throws BusinessException {
        return executor.execute(new FindPaymentMeanById(id));
    }

    @Override
    public List<PaymentMeanDto> findPaymentMeansByClientNif(String nif)
            throws BusinessException {
        return executor.execute(new FindPaymentMeansByClientNif(nif));
    }

    @Override
    public Optional<VoucherDto> findVoucherByCode(String code)
            throws BusinessException {
        return executor.execute(new FindVoucherByCode(code));
    }

    @Override
    public Optional<CardDto> findCreditCardByNumber(String cardNumber)
            throws BusinessException {
        return executor.execute(new FindCreditCardByNumber(cardNumber));
    }

}
