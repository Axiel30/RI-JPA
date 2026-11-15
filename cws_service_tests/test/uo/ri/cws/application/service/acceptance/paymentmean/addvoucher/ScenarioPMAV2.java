package uo.ri.cws.application.service.acceptance.paymentmean.addvoucher;

import java.util.UUID;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.ExceptionBox;
import uo.ri.cws.application.service.acceptance.util.dtobuilders.VoucherDtoBuilder;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;

public class ScenarioPMAV2 {
	private ExceptionBox context = new ExceptionBox();
	private PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();

	@When("[PM.AV.2] I try to add a voucher for a non existing client")
	public void whenITryToAddAVoucherForANonExistingClient() {
		VoucherDto voucher = new VoucherDtoBuilder()
				.withClientId(UUID.randomUUID().toString())
				.build();
		context.tryAndKeep(() -> service.addVoucher(voucher));
	}

	@Then("[PM.AV.2] a business error happens with an explaining message")
	public void thenABusinessErrorHappensWithAnExplainingMessage() {
		context.assertBusinessExceptionWithMessage();
	}
}
