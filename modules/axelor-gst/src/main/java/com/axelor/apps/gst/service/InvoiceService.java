package com.axelor.apps.gst.service;

import com.axelor.apps.gst.db.Address;
import com.axelor.apps.gst.db.Company;
import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.db.Party;
import java.util.List;

public interface InvoiceService {
  public Invoice getCalculatedInvoice(Invoice invoice, List<InvoiceLine> lineList);

  public Address getAddressForShipping(Invoice invoice, Party party, Company company);

  public List<InvoiceLine> regenerateInvoiceLine(Invoice invoice);
}
