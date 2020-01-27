package com.axelor.apps.gst.service;

import java.util.List;

import com.axelor.apps.gst.db.InvoiceLine;

public interface ProductService {
	public List<InvoiceLine> getInvoiceLinesByIds(List<Long> ids);
}
