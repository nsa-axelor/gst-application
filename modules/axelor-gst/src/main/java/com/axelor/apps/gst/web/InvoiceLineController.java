package com.axelor.apps.gst.web;

import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.db.Product;
import com.axelor.apps.gst.service.InvoiceLineService;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class InvoiceLineController {

  @Inject InvoiceLineService service;

  public void generateItemName(ActionRequest request, ActionResponse response) {
    InvoiceLine line = request.getContext().asType(InvoiceLine.class);
    Product product = line.getProduct();
    String itemName = "[" + product.getCode() + "]" + product.getName();
    line.setItem(itemName);
    line.setGstRate(product.getGstRate());
    response.setValues(line);
  }

  public void calculateTaxAndAmount(ActionRequest request, ActionResponse response) {
    try {
      InvoiceLine line = request.getContext().asType(InvoiceLine.class);
      Invoice parent = request.getContext().getParent().asType(Invoice.class);
      line = service.calculateInvoiceLine(line, parent);
      response.setValues(line);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setProductPrice(ActionRequest request, ActionResponse response) {
    try {
      InvoiceLine line = request.getContext().asType(InvoiceLine.class);
      Invoice parent = request.getContext().getParent().asType(Invoice.class);
      Product product = line.getProduct();
      line.setPrice(product.getSalePrice());
      line = service.calculateInvoiceLine(line, parent);
      response.setValues(line);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
