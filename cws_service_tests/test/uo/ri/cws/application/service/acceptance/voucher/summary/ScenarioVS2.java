package uo.ri.cws.application.service.acceptance.voucher.summary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import uo.ri.conf.Factories;
import uo.ri.cws.application.service.acceptance.util.dbfixture.DbFixtures;
import uo.ri.cws.application.service.acceptance.util.dbfixture.records.TClientsRecord;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.util.exception.BusinessException;

public class ScenarioVS2 {
	private final VoucherGenerationService service = Factories.service.forVoucherService();
	private TClientsRecord firstClient;
	private TClientsRecord secondClient;
	private TClientsRecord thirdClient;
	private List<VoucherSummaryDto> result;

	@Given("[V.S.2] several clients with consumed and available vouchers")
	public void givenSeveralClientsWithConsumedAndAvailableVouchers() {
		firstClient = DbFixtures.aClient();
		DbFixtures.aVoucherForClientOf(
				firstClient.id, 
				new BigDecimal("100.00"),
				new BigDecimal("60.00"), 
				"First voucher"
			);
		DbFixtures.aVoucherForClientOf(
				firstClient.id, 
				new BigDecimal("50.00"),
				new BigDecimal("20.00"), 
				"Second voucher"
			);

		secondClient = DbFixtures.aClient();
		DbFixtures.aVoucherForClientOf(
				secondClient.id, 
				new BigDecimal("200.00"),
				new BigDecimal("150.00"), 
				"Third voucher"
			);

		thirdClient = DbFixtures.aClient();
		DbFixtures.aVoucherForClientOf(
				thirdClient.id, 
				new BigDecimal("30.00"),
				new BigDecimal("10.00"), 
				"Fourth voucher"
			);

		DbFixtures.aClient(); // a client without vouchers
	}

	@When("[V.S.2] I ask for the voucher summary")
	public void whenIAskForTheVoucherSummary() throws BusinessException {
		result = service.getVoucherSummary();
	}

	@Then("[V.S.2] I get one aggregated row per client with vouchers")
	public void thenIGetOneAggregatedRowPerClientWithVouchers() {
		assertEquals(3, result.size()); // not 4 clients, because one has no vouchers
		assertClient(result.get(0), secondClient);
		assertClient(result.get(1), firstClient);
		assertClient(result.get(2), thirdClient);
	}

	@And("[V.S.2] the rows are sorted by available amount descending")
	public void andTheRowsAreSortedByAvailableAmountDescending() {
		assertEquals(0, new BigDecimal("150.00").compareTo(result.get(0).availableAmount()));
		assertEquals(0, new BigDecimal("80.00").compareTo(result.get(1).availableAmount()));
		assertEquals(0, new BigDecimal("10.00").compareTo(result.get(2).availableAmount()));
	}

	@And("[V.S.2] each row includes issued, total, consumed and available amounts")
	public void andEachRowIncludesIssuedTotalConsumedAndAvailableAmounts() {
		assertAmounts(result.get(0), 1, "200.00", "50.00", "150.00");
		assertAmounts(result.get(1), 2, "150.00", "70.00", "80.00");
		assertAmounts(result.get(2), 1, "30.00", "20.00", "10.00");
	}

	private void assertClient(VoucherSummaryDto row, TClientsRecord client) {
		assertEquals(client.nif, row.nif());
		assertEquals(client.name, row.name());
		assertEquals(client.surname, row.surname());
	}

	private void assertAmounts(VoucherSummaryDto row, long quantity, String total, String consumed, String available) {
		assertEquals(quantity, row.issuedQty());
		assertEquals(0, new BigDecimal(total).compareTo(row.totalAmount()));
		assertEquals(0, new BigDecimal(consumed).compareTo(row.consumedAmount()));
		assertEquals(0, new BigDecimal(available).compareTo(row.availableAmount()));
	}
}
