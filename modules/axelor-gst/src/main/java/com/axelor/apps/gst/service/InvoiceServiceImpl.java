package com.axelor.apps.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.apps.gst.db.Address;
import com.axelor.apps.gst.db.Company;
import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.db.Party;
import com.axelor.db.Query;
import com.google.inject.Inject;

public class InvoiceServiceImpl implements InvoiceService {

	private BigDecimal totalNetAmount;
	private BigDecimal netIgst;
	private BigDecimal netCgst;
	private BigDecimal netSgst;
	private BigDecimal netGrossAmount;
	private List<InvoiceLine> invoiceItemList;
	@Inject
	InvoiceLineService invoiceLineService;

	@Override
	public Invoice getCalculatedInvoice(Invoice invoice, List<InvoiceLine> lineList) {
		totalNetAmount = new BigDecimal(0);
		netIgst = new BigDecimal(0);
		netCgst = new BigDecimal(0);
		netSgst = new BigDecimal(0);
		netGrossAmount = new BigDecimal(0);
		lineList.forEach(line -> {
			BigDecimal netAmount = line.getNetAmount();
			totalNetAmount = totalNetAmount.add(netAmount);
			netIgst = netIgst.add(line.getIgst());
			netSgst = netSgst.add(line.getSgst());
			netCgst = netCgst.add(line.getCsgt());
			netGrossAmount = netGrossAmount.add(line.getGrossAmount());
		});
		invoice.setNetAmount(totalNetAmount);
		invoice.setNetCsgt(netCgst);
		invoice.setNetIgst(netIgst);
		invoice.setNetSgst(netSgst);
		invoice.setGrossAmount(netGrossAmount);
		return invoice;
	}

	@Override
	public Address getAddressForShipping(Invoice invoice, Party party, Company company) {
		if (!invoice.getIsUseInvoiceAddressAsShipping()) {
			return Query.of(Address.class).filter("self.type = 'shipping' AND self.party = :party").bind("party", party)
					.fetchOne();
		}
//		return Query.of(Address.class).filter("self.type = 'invoice' AND self.company = :company")
//				.bind("company", company).fetchOne();
		return company.getAddress();
	}

	@Override
	public List<InvoiceLine> regenerateInvoiceLine(Invoice invoice) {
		List<InvoiceLine> lines = invoice.getInvoiceItemList();
		invoiceItemList = new ArrayList<>();
		lines.forEach(line -> {
			invoiceItemList.add(invoiceLineService.calculateInvoiceLine(line, invoice));
		});
		return invoiceItemList;
	}
}
