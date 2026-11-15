package uo.ri.cws.application.service.robustness.paymentmean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.YearMonth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import uo.ri.conf.Factories;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.CardDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService.VoucherDto;

/**
 * Scenarios:
 *   - Trying to add a card with a null DTO
 *   - Trying to add a card with a null, empty or blank client id
 *   - Trying to add a card with a null, empty or blank card number
 *   - Trying to add a card with a null, empty or blank card type
 *   - Trying to add a card with a null expiration date
 *   - Trying to add a voucher with a null DTO
 *   - Trying to add a voucher with a null, empty or blank client id
 *   - Trying to add a voucher with a null, empty or blank code
 *   - Trying to add a voucher with a null, empty or blank description
 *   - Trying to add a voucher with a negative available amount
 *   - Trying to delete a payment mean with a null, empty or blank identifier
 *   - Trying to find a payment mean with a null, empty or blank identifier
 *   - Trying to find payment means with a null, empty or blank client NIF
 *   - Trying to find a voucher with a null, empty or blank code
 *   - Trying to find a credit card with a null, empty or blank number
 */
class PaymentMeanCrudServiceRobustnessTests {

	private final PaymentMeanCrudService service = Factories.service.forPaymentMeanCrudService();

	/**
	 * Given a null card DTO
	 * When trying to add a card
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void addCardRejectsNullDto() {
		assertRejected(() -> service.addCard(null));
	}

	/**
	 * Given a card DTO with a null, empty or blank client id
	 * When trying to add the card
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addCardRejectsNullOrBlankClientId(String clientId) {
		CardDto dto = validCard();
		dto.clientId = clientId;

		assertRejected(() -> service.addCard(dto));
	}

	/**
	 * Given a card DTO with a null, empty or blank card number
	 * When trying to add the card
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addCardRejectsNullOrBlankCardNumber(String cardNumber) {
		CardDto dto = validCard();
		dto.cardNumber = cardNumber;

		assertRejected(() -> service.addCard(dto));
	}

	/**
	 * Given a card DTO with a null, empty or blank card type
	 * When trying to add the card
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addCardRejectsNullOrBlankCardType(String cardType) {
		CardDto dto = validCard();
		dto.cardType = cardType;

		assertRejected(() -> service.addCard(dto));
	}

	/**
	 * Given a card DTO with a null expiration date
	 * When trying to add the card
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void addCardRejectsNullExpirationDate() {
		CardDto dto = validCard();
		dto.cardExpiration = null;

		assertRejected(() -> service.addCard(dto));
	}

	/**
	 * Given a null voucher DTO
	 * When trying to add a voucher
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void addVoucherRejectsNullDto() {
		assertRejected(() -> service.addVoucher(null));
	}

	/**
	 * Given a voucher DTO with a null, empty or blank client id
	 * When trying to add the voucher
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addVoucherRejectsNullOrBlankClientId(String clientId) {
		VoucherDto dto = validVoucher();
		dto.clientId = clientId;

		assertRejected(() -> service.addVoucher(dto));
	}

	/**
	 * Given a voucher DTO with a null, empty or blank code
	 * When trying to add the voucher
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addVoucherRejectsNullOrBlankCode(String code) {
		VoucherDto dto = validVoucher();
		dto.code = code;

		assertRejected(() -> service.addVoucher(dto));
	}

	/**
	 * Given a voucher DTO with a null, empty or blank description
	 * When trying to add the voucher
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void addVoucherRejectsNullOrBlankDescription(String description) {
		VoucherDto dto = validVoucher();
		dto.description = description;

		assertRejected(() -> service.addVoucher(dto));
	}

	/**
	 * Given a voucher DTO with a negative available amount
	 * When trying to add the voucher
	 * Then the argument is rejected with an explaining message
	 */
	@Test
	void addVoucherRejectsNegativeAvailableAmount() {
		VoucherDto dto = validVoucher();
		dto.availableAmount = BigDecimal.valueOf(-1);

		assertRejected(() -> service.addVoucher(dto));
	}

	/**
	 * Given a null, empty or blank payment mean identifier
	 * When trying to delete the payment mean
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void deleteRejectsNullOrBlankId(String id) {
		assertRejected(() -> service.deletePaymentMean(id));
	}

	/**
	 * Given a null, empty or blank payment mean identifier
	 * When trying to find the payment mean
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void findByIdRejectsNullOrBlankId(String id) {
		assertRejected(() -> service.findById(id));
	}

	/**
	 * Given a null, empty or blank client NIF
	 * When trying to find the client's payment means
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void findPaymentMeansByClientNifRejectsNullOrBlankNif(String nif) {
		assertRejected(() -> service.findPaymentMeansByClientNif(nif));
	}

	/**
	 * Given a null, empty or blank voucher code
	 * When trying to find the voucher
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void findVoucherByCodeRejectsNullOrBlankCode(String code) {
		assertRejected(() -> service.findVoucherByCode(code));
	}

	/**
	 * Given a null, empty or blank credit card number
	 * When trying to find the credit card
	 * Then the argument is rejected with an explaining message
	 */
	@ParameterizedTest
	@NullSource
	@ValueSource(strings = { "", "   " })
	void findCreditCardByNumberRejectsNullOrBlankNumber(String number) {
		assertRejected(() -> service.findCreditCardByNumber(number));
	}

	private static CardDto validCard() {
		CardDto dto = new CardDto();
		dto.clientId = "client-id";
		dto.accumulatedAmount = BigDecimal.ZERO;
		dto.cardNumber = "4111111111111111";
		dto.cardExpiration = YearMonth.now().plusYears(1);
		dto.cardType = "VISA";
		return dto;
	}

	private static VoucherDto validVoucher() {
		VoucherDto dto = new VoucherDto();
		dto.clientId = "client-id";
		dto.accumulatedAmount = BigDecimal.ZERO;
		dto.code = "V-ROBUSTNESS";
		dto.description = "Robustness voucher";
		dto.availableAmount = BigDecimal.TEN;
		return dto;
	}

	private static void assertRejected(Executable action) {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, action);

		assertNotNull(exception.getMessage());
		assertFalse(exception.getMessage().isBlank());
	}
}
