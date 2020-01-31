package com.axelor.apps.gst.service;

import com.axelor.apps.gst.db.Address;
import com.axelor.apps.gst.db.Company;
import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.db.Party;
import com.axelor.db.Query;
import com.google.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InvoiceServiceImpl implements InvoiceService {

	@Inject
	InvoiceLineService invoiceLineService;

	@Override
	public Invoice getCalculatedInvoice(Invoice invoice, List<InvoiceLine> lineList) {
		BigDecimal totalNetAmount = new BigDecimal(0);
		BigDecimal netIgst = new BigDecimal(0);
		BigDecimal netCgst = new BigDecimal(0);
		BigDecimal netSgst = new BigDecimal(0);
		BigDecimal netGrossAmount = new BigDecimal(0);
		for (InvoiceLine line : lineList) {
			BigDecimal netAmount = line.getNetAmount();
			totalNetAmount = totalNetAmount.add(netAmount);
			netIgst = netIgst.add(line.getIgst());
			netSgst = netSgst.add(line.getSgst());
			netCgst = netCgst.add(line.getCsgt());
			netGrossAmount = netGrossAmount.add(line.getGrossAmount());
		}

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
			return Query.of(Address.class)
					.filter("(self.type = 'shipping' OR self.type = 'default') AND self.party = :party")
					.bind("party", party).fetchOne();
		}

		return Query.of(Address.class)
				.filter("(self.type = 'invoice' OR self.type = 'default') AND self.party = :party").bind("party", party)
				.fetchOne();
	}

	@Override
	public List<InvoiceLine> regenerateInvoiceLine(Invoice invoice) {
		List<InvoiceLine> lines = invoice.getInvoiceItemList();
		List<InvoiceLine> invoiceItemList = new ArrayList<>();
		lines.forEach(line -> invoiceItemList.add(invoiceLineService.calculateInvoiceLine(line, invoice)));
		return invoiceItemList;
	}
}
