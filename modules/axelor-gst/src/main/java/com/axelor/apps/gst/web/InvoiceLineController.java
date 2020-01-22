package com.axelor.apps.gst.web;

import java.math.BigDecimal;

import com.axelor.apps.gst.db.Address;
import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.db.Product;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceLineController {

	public void generateItemName(ActionRequest request, ActionResponse response) {
		InvoiceLine line = request.getContext().asType(InvoiceLine.class);
		Product product = line.getProduct();
		String itemName = "[" + product.getCode() + "]" + product.getName();
		line.setItem(itemName);
		line.setGstRate(product.getGstRate());
		response.setValues(line);
	}

	public void calculateTaxAndAmount(ActionRequest request, ActionResponse response) {
		InvoiceLine line = request.getContext().asType(InvoiceLine.class);
		Invoice invoice = request.getContext().getParent().asType(Invoice.class);
		BigDecimal price = line.getPrice();
		BigDecimal qty = new BigDecimal(line.getQty());
		BigDecimal netAmount = price.multiply(qty);
		line.setNetAmount(netAmount);
		BigDecimal gstRate = line.getGstRate();
		
		Address companyAddress = invoice.getCompany().getAddress();
		Address invoiceAddress = invoice.getInvoiceAddress();
		
		if(!invoiceAddress.getState().getName().equals(companyAddress.getState().getName())) {
			BigDecimal igst = gstRate.multiply(netAmount);
			line.setIgst(igst);
			BigDecimal grossAmount = netAmount.add(igst);
			line.setGrossAmount(grossAmount);
		}else {
			BigDecimal sgst = gstRate.multiply(netAmount);
			sgst = sgst.divide(new BigDecimal(2));
			BigDecimal cgst = gstRate.multiply(netAmount);
			cgst = cgst.divide(new BigDecimal(2));
			line.setCsgt(cgst);
			line.setSgst(sgst);
			BigDecimal grossAmount = netAmount.add(cgst).add(sgst);
			line.setGrossAmount(grossAmount);
		}
		response.setValues(line);
	}
}
