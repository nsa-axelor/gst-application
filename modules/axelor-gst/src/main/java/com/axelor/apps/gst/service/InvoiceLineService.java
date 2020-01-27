package com.axelor.apps.gst.service;

import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.db.Product;

public interface InvoiceLineService {

  public InvoiceLine calculateInvoiceLine(InvoiceLine line, Invoice parent);
  public String generateItemName(Product product);
}
