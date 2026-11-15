package uo.ri.ui.util;

import java.math.BigDecimal;

import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherDto;
import uo.ri.cws.application.service.paymentmean.VoucherGenerationService.VoucherSummaryDto;
import uo.ri.cws.application.service.vehicletype.VehicleTypeCrudService.VehicleTypeDto;
import uo.ri.util.console.Console;

public class Printer {

	public static void printMechanic(MechanicDto m) {

		Console.printf("\t%s %-10.10s %-15.15s %-25.25s\n"
				, m.id
				, m.nif
				, m.name
				, m.surname
			);
	}

	public static void printVoucher(VoucherDto voucher) {

		Console.printf("\t%s %-12.12s %-35.35s %10.2f %10.2f\n",
				voucher.id,
				voucher.code,
				voucher.description,
				voucher.accumulatedAmount,
				voucher.availableAmount
			);
	}

	public static void printVoucherTotals(int quantity, BigDecimal total,
			BigDecimal consumed, BigDecimal available) {

		Console.printf("\n\tQuantity: %d\n", quantity);
		Console.printf("\tTotal amount: %.2f\n", total);
		Console.printf("\tConsumed amount: %.2f\n", consumed);
		Console.printf("\tAvailable amount: %.2f\n", available);
	}

	public static void printVoucherSummary(VoucherSummaryDto row) {

		Console.printf("\t%-10.10s %-15.15s %-25.25s %5d %10.2f %10.2f %10.2f\n",
				row.nif(),
				row.name(),
				row.surname(),
				row.issuedQty(),
				row.totalAmount(),
				row.consumedAmount(),
				row.availableAmount()
			);
	}

	public static void printVehicleType(VehicleTypeDto vt) {

		Console.printf("\t%s %-10.10s %5.2f €/hour %d\n"
				, vt.id
				, vt.name
				, vt.pricePerHour
				, vt.minTrainigHours
			);
	}

}
