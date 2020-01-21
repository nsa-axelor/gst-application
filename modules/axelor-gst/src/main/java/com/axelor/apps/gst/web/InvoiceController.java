package com.axelor.apps.gst.web;

import com.axelor.apps.gst.db.Invoice;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;

public class InvoiceController {

  public void generateNewSequence(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
  }
}
