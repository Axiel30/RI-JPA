package uo.ri.ui.util;

import java.math.BigDecimal;
import java.util.List;

import uo.ri.cws.application.service.invoice.InvoicingService;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.mechanic.MechanicCrudService.MechanicDto;
import uo.ri.cws.application.service.paymentmean.PaymentMeanCrudService;
import uo.ri.util.console.Console;

public class Printer {

	public static void printInvoice(InvoiceDto invoice) {
		Console.printf("Invoice number: %d\n", 			invoice.number );
		Console.printf("\tDate: %1$td/%1$tm/%1$tY\n", 	invoice.date);
		Console.printf("\tSubtotal: %.2f EUR\n", 		invoice.subtotal );
		Console.printf("\tVAT amount: %.2f EUR\n", 		invoice.vatAmount );
		Console.printf("\tVAT percentage: %.1f %% \n", 	invoice.vatPercentage );
		Console.printf("\tTotal: %.2f EUR\n", 			invoice.total );
		Console.printf("\tState: %s\n", 				invoice.state );
	}

	public static void printPaymentMeans(List<?> means) {
		Console.println();
		Console.println("Available payment means");

		Console.printf("\t%s \t%-15.15s \t%s \t%s\n",
				"ID", "Type", "Accumulated", "Details");
		for (Object mean : means) {
			printPaymentMean(mean);
		}
	}

	public static void printPaymentMean(PaymentMeanCrudService.PaymentMeanDto mean) {
		printPaymentMean((Object) mean);
	}

	private static void printPaymentMean(Object mean) {
		Console.printf("\t%s \t%-15.15s \t%.2f \t%s\n",
				id(mean),
				type(mean),
				accumulatedAmount(mean),
				details(mean)
		);
	}

	private static String id(Object mean) {
		if (mean instanceof InvoicingService.PaymentMeanDto dto) {
			return dto.id;
		}
		if (mean instanceof PaymentMeanCrudService.PaymentMeanDto dto) {
			return dto.id;
		}
		throw new IllegalArgumentException("Unknown payment mean type");
	}

	private static BigDecimal accumulatedAmount(Object mean) {
		if (mean instanceof InvoicingService.PaymentMeanDto dto) {
			return dto.accumulated;
		}
		if (mean instanceof PaymentMeanCrudService.PaymentMeanDto dto) {
			return dto.accumulatedAmount;
		}
		throw new IllegalArgumentException("Unknown payment mean type");
	}

	private static String type(Object mean) {
		if (mean instanceof InvoicingService.CashDto
				|| mean instanceof PaymentMeanCrudService.CashDto) {
			return "Cash";
		}
		if (mean instanceof InvoicingService.CardDto
				|| mean instanceof PaymentMeanCrudService.CardDto) {
			return "Credit card";
		}
		if (mean instanceof InvoicingService.VoucherDto
				|| mean instanceof PaymentMeanCrudService.VoucherDto) {
			return "Voucher";
		}
		return mean.getClass().getSimpleName();
	}

	private static String details(Object mean) {
		if (mean instanceof InvoicingService.VoucherDto dto) {
			return "available " + dto.availableAmount;
		}
		if (mean instanceof PaymentMeanCrudService.VoucherDto dto) {
			return "available " + dto.availableAmount;
		}
		if (mean instanceof InvoicingService.CardDto dto) {
			return "expiration " + dto.cardExpiration;
		}
		if (mean instanceof PaymentMeanCrudService.CardDto dto) {
			return "expiration " + dto.cardExpiration;
		}
		return "";
	}

	public static void printWorkOrder(InvoicingWorkOrderDto rep) {

		Console.printf("\t%s \t%-40.40s \t%td/%<tm/%<tY \t%-12.12s \t%.2f\n",
				rep.id
				, rep.description
				, rep.date
				, rep.state
				, rep.amount
		);
	}

	public static void printMechanic(MechanicDto m) {

		Console.printf("\t%s %-10.10s %-25.25s %-25.25s\n",
				m.id
				, m.nif
				, m.name
				, m.surname
			);
	}

}
