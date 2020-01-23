package com.axelor.apps.gst.service;

import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;

public interface InvoiceLineService {
	
	public InvoiceLine calculateInvoiceLine(InvoiceLine line, Invoice parent);
}
