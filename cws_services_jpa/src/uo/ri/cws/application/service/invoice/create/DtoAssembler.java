package uo.ri.cws.application.service.invoice.create;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uo.ri.cws.application.service.invoice.InvoicingService.CardDto;
import uo.ri.cws.application.service.invoice.InvoicingService.CashDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoiceDto;
import uo.ri.cws.application.service.invoice.InvoicingService.InvoicingWorkOrderDto;
import uo.ri.cws.application.service.invoice.InvoicingService.PaymentMeanDto;
import uo.ri.cws.application.service.invoice.InvoicingService.VoucherDto;
import uo.ri.cws.domain.Cash;
import uo.ri.cws.domain.CreditCard;
import uo.ri.cws.domain.Invoice;
import uo.ri.cws.domain.PaymentMean;
import uo.ri.cws.domain.Voucher;
import uo.ri.cws.domain.WorkOrder;

public class DtoAssembler {

	public static List<VoucherDto> toVoucherDtoList(List<Voucher> list) {
		List<VoucherDto> res = new ArrayList<>();
		for(Voucher b: list) {
			res.add( toDto( b ) );
		}
		return res;
	}

	public static VoucherDto toDto(Voucher v) {
		VoucherDto dto = new VoucherDto();
		dto.id = v.getId();
		dto.version = v.getVersion();

		dto.clientId = v.getClient().getId();
		dto.accumulated = v.getAccumulated().amount();
		dto.code = v.getCode().code();
		dto.description = v.getDescription();
		dto.availableAmount = v.getAvailableAmount().amount();

		return dto;
	}

	public static CardDto toDto(CreditCard cc) {
		CardDto dto = new CardDto();
		dto.id = cc.getId();
		dto.version = cc.getVersion();

		dto.clientId = cc.getClient().getId();
		dto.accumulated = cc.getAccumulated().amount();

		dto.cardNumber = cc.getNumber().number();
		dto.cardExpiration = cc.getValidThru();
		dto.cardType = cc.getType();
		return dto;
	}

	public static CashDto toDto(Cash m) {
		CashDto dto = new CashDto();
		dto.id = m.getId();
		dto.version = m.getVersion();

		dto.clientId = m.getClient().getId();
		dto.accumulated = m.getAccumulated().amount();
		return dto;
	}

	public static InvoiceDto toDto(Invoice invoice) {
	    InvoiceDto dto = new InvoiceDto();

	    dto.id = invoice.getId();
	    dto.version = invoice.getVersion();

	    dto.number = invoice.getNumber();
	    dto.date = invoice.getDate();

	    dto.total = invoice.getTotal().amount();
	    dto.subtotal = invoice.getSubtotal().amount();
	    dto.vatAmount = invoice.getVatAmount().amount();
	    dto.vatPercentage = invoice.getVatRate().value();

	    dto.state = invoice.getState().toString();

	    return dto;
	}
	
	public static List<PaymentMeanDto> toPaymentMeanDtoList(List<PaymentMean> list) {
		return list.stream()
				.map( mp -> toDto( mp ) )
				.collect( Collectors.toList() );
	}

	private static PaymentMeanDto toDto(PaymentMean mp) {
		if (mp instanceof Voucher) {
			return toDto( (Voucher) mp );
		}
		else if (mp instanceof CreditCard) {
			return toDto( (CreditCard) mp );
		}
		else if (mp instanceof Cash) {
			return toDto( (Cash) mp);
		}
		else {
			throw new RuntimeException("Unexpected type of payment mean");
		}
	}

	public static List<InvoicingWorkOrderDto> toInvoicingWorkOrderDtoList(
			List<WorkOrder> list) {
		return list.stream()
				.map( a -> toInvoicingWorkOrderDto( a ) )
				.collect( Collectors.toList() );
	}

	private static InvoicingWorkOrderDto toInvoicingWorkOrderDto(WorkOrder w) {
		InvoicingWorkOrderDto dto = new InvoicingWorkOrderDto();

		dto.id = w.getId();
		dto.description = w.getDescription();
		dto.date = w.getDate();
		dto.state = w.getState().toString();
		dto.amount = w.getAmount().amount();

		return dto;
	}

}
