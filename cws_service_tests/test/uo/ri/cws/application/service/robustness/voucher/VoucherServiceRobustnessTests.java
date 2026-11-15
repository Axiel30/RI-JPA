package uo.ri.cws.application.service.robustness.voucher;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService;

/**
 * Scenarios:
 *   - Trying to find vouchers with a null, empty or blank client NIF
 */
class VoucherServiceRobustnessTests {

	private final VoucherGenerationService service = Factories.service.forVoucherService();

	/**
	 * Given a null, empty or blank client NIF
	 * When trying to find the client's vouchers
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void findVouchersByClientNifRejectsNullOrBlankNif(String nif) {
		assertRejected(() -> service.findVouchersByClientNif(nif));
	}

	private static void assertRejected(Executable action) {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);

		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
