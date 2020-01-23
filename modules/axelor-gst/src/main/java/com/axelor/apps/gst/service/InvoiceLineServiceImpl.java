package com.axelor.apps.gst.service;

import java.math.BigDecimal;
import java.util.Optional;

import com.axelor.apps.gst.db.Address;
import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;

public class InvoiceLineServiceImpl implements InvoiceLineService {

	@Override
	public InvoiceLine calculateInvoiceLine(InvoiceLine line, Invoice parent) {
		BigDecimal price = line.getPrice();
		BigDecimal qty = new BigDecimal(line.getQty());
		BigDecimal netAmount = price.multiply(qty);
		line.setNetAmount(netAmount);
		BigDecimal gstRate = line.getGstRate();
		Optional<Invoice> optionalInvoice = Optional.ofNullable(parent);
		
		if (optionalInvoice.isPresent()) {

			Address companyAddress = parent.getCompany().getAddress();
			Address invoiceAddress = parent.getInvoiceAddress();

			if (!invoiceAddress.getState().getName().equals(companyAddress.getState().getName())) {
				BigDecimal igst = gstRate.multiply(netAmount).divide(new BigDecimal(100));
				line.setIgst(igst);
				line.setSgst(new BigDecimal(0));
				line.setCsgt(new BigDecimal(0));
				BigDecimal grossAmount = netAmount.add(igst);
				line.setGrossAmount(grossAmount);
			} else {
				BigDecimal sgst = gstRate.multiply(netAmount).divide(new BigDecimal(100));
				sgst = sgst.divide(new BigDecimal(2));
				BigDecimal cgst = gstRate.multiply(netAmount).divide(new BigDecimal(100));
				cgst = cgst.divide(new BigDecimal(2));
				line.setCsgt(cgst);
				line.setSgst(sgst);
				line.setIgst(new BigDecimal(0));
				BigDecimal grossAmount = netAmount.add(cgst).add(sgst);
				line.setGrossAmount(grossAmount);
			}
		}

		return line;
	}

}
