package com.axelor.apps.gst.service;

import com.axelor.apps.gst.db.InvoiceLine;
import java.util.List;

public interface ProductService {
  public List<InvoiceLine> getInvoiceLinesByIds(List<Long> ids);
}
