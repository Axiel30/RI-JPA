package uo.ri.cws.domain.types;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

import uo.ri.util.assertion.ArgumentChecks;

/**
 * A monetary amount without an explicit currency.
 * 
 * Input amounts preserve their original BigDecimal resolution.
 * Addition, subtraction and multiplication are exact. 
 * Division is exact when its decimal expansion terminates and uses MathContext#DECIMAL128 otherwise.
 * 
 * Arithmetic is performed through the nested fluent MoneyCalculator, obtained via calc() or calc(Money).
 *
 * Factory methods : of(String), of(BigDecimal)
 * Comparison      : compareTo, greaterThan, greaterOrEqual
 * Equality        : value-based, scale-independent
 * Terminal methods: toMoneyExact(), toMoneyRoundedToCents()
 * Lossless split  : allocate(n) — e.g. €10.00 into 3 -> [€3.34, €3.33, €3.33]
 */
public record Money(BigDecimal amount) implements Comparable<Money> {

	private static final MathContext NON_TERMINATING_DIVISION_CONTEXT = MathContext.DECIMAL128;
	private static final RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_EVEN;
	private static final int SCALE_FOR_CENTS = 2; // Rounding to cents (when toMoneyRoundedToCents() or allocate())
	
	public static final Money ZERO = new Money(BigDecimal.ZERO);
	public static final Money ONE = new Money(new BigDecimal("1"));
	public static final Money TEN = new Money(new BigDecimal("10"));

	// ---- Constructors ----
	public Money { 
	    ArgumentChecks.isTrue(amount != null, "Amount is required");
	}

	// ---- Factory methods ----
	public static Money of(String amount) {
		return new Money(new BigDecimal(amount));
	}

	public static Money of(BigDecimal amount) {
		return new Money(amount);
	}

	// ---- Fluent Calculation API ----
	public static MoneyCalculator calc(Money money) {
		return new MoneyCalculator(money);
	}

	public static MoneyCalculator calc() {
		return new MoneyCalculator();
	}
	
	// ---- Other methods ----
	
	public Money roundToCents() {
		return new Money(amount.setScale(SCALE_FOR_CENTS, DEFAULT_ROUNDING_MODE));
	}
	
	// ---- Comparison ----
	@Override
	public int compareTo(Money that) {
		return this.amount.compareTo(that.amount);
	}

	public boolean greaterThan(Money other) {
		return compareTo(other) > 0;
	}

	public boolean greaterOrEqual(Money other) {
		return compareTo(other) >= 0;
	}
	
	public boolean isNonNegative() {
		return greaterOrEqual(ZERO);
	}

	// ---- Equality ----
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Money m)) {
			return false;
		}
		return amount.compareTo(m.amount) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount.stripTrailingZeros());
	}

	@Override
	public String toString() {
		return amount.toString();
	}

	// ---- Fluent Calculation API ----
	
	/**
	 * Computes monetary calculations without intermediate rounding.
	 * Fluent API: 
	 * 		Money.calc(€100).add(€50).times(1.21).toMoneyExact() -> €181.50
	 * 		Money.calc(€100).add(€50).times(1.21).toMoneyRoundedToCents() -> €181.50
	 * 	but
	 * 		Money.calc(€100).div(3.0).toMoneyRoundedToCents() -> €33.33
	 * 		Money.calc(€100).div(3.0).toMoneyExact() -> €33.333333... (max precision of DECIMAL128, but finite)
	 */
	public static final class MoneyCalculator {
		private BigDecimal accumulator;

		private MoneyCalculator(Money money) {
			accumulator = money.amount;
		}
		
		private MoneyCalculator() {
			accumulator = BigDecimal.ZERO;
		}

		/**
		 * €100 + €50 -> €150
		 */
		public MoneyCalculator add(Money other) {
			accumulator = accumulator.add(other.amount);
			return this;
		}

		/**
		 * €100 - €50 -> €50
		 */
		public MoneyCalculator subtract(Money other) {
			accumulator = accumulator.subtract(other.amount);
			return this;
		}

		/**
		 * €100 * 1.21 = €121
		 */
		public MoneyCalculator times(BigDecimal factor) {
			accumulator = accumulator.multiply(factor);
			return this;
		}

		/**
		 * €100 * 1.21 = €121
		 */
		public MoneyCalculator times(double factor) {
			return times( BigDecimal.valueOf(factor) );
		}

		/**
		 * €100 * 1.21 = €121
		 */
		public MoneyCalculator times(long factor) {
			return times( BigDecimal.valueOf(factor) );
		}

		/**
		 * €100 * "1.21" = €121
		 */
		public MoneyCalculator times(String factor) {
			return times(new BigDecimal(factor));
		}

		/**
		 * €100 * 21% -> €21
		 */
		public MoneyCalculator times(Percentage percentage) {
			return times( percentage.asRatio() );
		}

		/**
		 * €100.00 / 5.00 = €20.00
		 * €100.00 / 3 = €33.333333... (max precision of DECIMAL128, 34 digits limited)
		 * Uses exact division for terminating decimal expansions and DECIMAL128 for non-terminating ones.
		 */
		public MoneyCalculator div(BigDecimal divisor) {
			try {
				accumulator = accumulator.divide(divisor);
			} catch (ArithmeticException exception) {
				if (divisor.signum() == 0) {
					throw exception;
				}
				accumulator = accumulator.divide(divisor, NON_TERMINATING_DIVISION_CONTEXT);
			}
			return this;
		}

		/**
		 * €100 / "1.21" = €82.64
		 */
		public MoneyCalculator div(String divisor) {
			return div(new BigDecimal(divisor));
		}

		/**
		 * €100 / 1.21 = €82.64
		 */
		public MoneyCalculator div(double divisor) {
			return div( BigDecimal.valueOf(divisor) );
		}

		/**
		 * €100 / 1.21 = €82.64
		 */
		public MoneyCalculator div(long divisor) {
			return div( new BigDecimal(divisor) );
		}

		/**
		 *  -(€100) = €-100
		 */
		public MoneyCalculator negate() {
			accumulator = accumulator.negate();
			return this;
		}

		/**
		 * €100 with 21% -> €121
		 */
		public MoneyCalculator with(Percentage percentage) {
			return times( percentage.asMultiplier() );
		}

		/**
		 * €121 without 21% -> €100
		 */
		public MoneyCalculator without(Percentage percentage) {
			return div( percentage.asMultiplier() );
		}

		/**
		 * €100 min €50 -> €50
		 * €50 min €100 -> €50
		 */
		public MoneyCalculator min(Money other) {
			accumulator = accumulator.min(other.amount);
			return this;
		}

		/**
		 * €100 max €50 -> €100
		 * €50 max €100 -> €100
		 */
		public MoneyCalculator max(Money other) {
			accumulator = accumulator.max(other.amount);
			return this;
		}

		// ---- Allocation ----
		/**
		 * Splits an amount without loss into the given number of targets as evenly as
		 * possible with SCALE precision.
		 *  
		 * €10.00 allocate 3 -> [3.34, 3.33, 3.33]
		 * 		with sum([3.34, 3.33, 3.33]) = 10.00, no loss of money.
		 * 
		 * @return an array of rounded-to-cents Money
		 */
		public Money[] allocate(int targets) {
			Money[] results = new Money[targets];

			// Work in smallest units (cents) to distribute the remainder cent by cent
			BigDecimal scaleFactor = BigDecimal.TEN.pow(SCALE_FOR_CENTS);
			BigDecimal totalCents = accumulator
					.setScale(SCALE_FOR_CENTS, DEFAULT_ROUNDING_MODE)
			        .multiply(scaleFactor);

			BigDecimal[] parts = totalCents.divideAndRemainder(BigDecimal.valueOf(targets));
			BigDecimal baseCents = parts[0];
			int remainderTargets = parts[1].abs().intValue();
			BigDecimal centAdjustment = BigDecimal.valueOf(parts[1].signum());

			for (int i = 0; i < targets; i++) {
				BigDecimal shareCents = (i < remainderTargets)
				        ? baseCents.add(centAdjustment)
				        : baseCents;
				results[i] = new Money(shareCents.divide(scaleFactor));
			}

			return results;
		}

		/**
		 * Materializes the calculation preserving its full resolution.
		 */
		public Money toMoneyExact() {
			return new Money(accumulator);
		}

		/**
		 * Materializes the calculation rounded to cents using HALF_EVEN.
		 */
		public Money toMoneyRoundedToCents() {
			return new Money(accumulator.setScale(SCALE_FOR_CENTS, DEFAULT_ROUNDING_MODE));
		}

		public BigDecimal raw() {
			return accumulator;
		}
	}

}
