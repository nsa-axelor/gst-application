package com.axelor.apps.gst.web;

import com.axelor.apps.gst.db.Address;
import com.axelor.apps.gst.db.Company;
import com.axelor.apps.gst.db.Contact;
import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.db.Party;
import com.axelor.apps.gst.db.Sequence;
import com.axelor.apps.gst.service.InvoiceService;
import com.axelor.apps.gst.service.SequenceService;
import com.axelor.db.Query;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import java.util.List;

public class InvoiceController {

  @Inject SequenceService sequenceService;
  @Inject InvoiceService invoiceService;

  @Transactional
  public void generateNewSequence(ActionRequest request, ActionResponse response) {
    MetaModelRepository metaModelRepository = Beans.get(MetaModelRepository.class);
    MetaModel model = metaModelRepository.findByName("Invoice");
    Sequence sequence = sequenceService.findSequenceByModel(model);
    response.setValue("reference", sequenceService.generateSequence(sequence));
  }

  public void setPartyPrimaryContactAndCalculations(
      ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    try {
      Party party = invoice.getParty();
      Contact contact =
          Query.of(Contact.class)
              .filter("self.type = 'primary' AND self.party = :party")
              .bind("party", party)
              .fetchOne();
      Address address =
          Query.of(Address.class)
              .filter("self.type = 'invoice' AND self.party = :party")
              .bind("party", party)
              .fetchOne();
      Company company = invoice.getCompany();
      Address shippingAddress = invoiceService.getAddressForShipping(invoice, party, company);
      invoice.setPartyContact(contact);
      invoice.setInvoiceAddress(address);
      invoice.setShippingAddress(shippingAddress);
      List<InvoiceLine> lineList = invoice.getInvoiceItemList();
      invoice.setInvoiceItemList(invoiceService.regenerateInvoiceLine(invoice));
      invoice = invoiceService.getCalculatedInvoice(invoice, lineList);
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    response.setValues(invoice);
  }

  //	public void changeShippingAddress(ActionRequest request, ActionResponse response) {
  //		Invoice invoice = request.getContext().asType(Invoice.class);
  //		Party party = invoice.getParty();
  //		Company company = invoice.getCompany();
  //		Address address = invoiceService.getAddressForShipping(invoice, party,company);
  //		invoice.setShippingAddress(address);
  //		List<InvoiceLine> lineList = invoice.getInvoiceItemList();
  //		invoice.setInvoiceItemList(invoiceService.regenerateInvoiceLine(invoice));
  //		lineList.forEach(line -> System.out.println(line.getProduct().getName()));
  //
  //		invoice = invoiceService.getCalculatedInvoice(invoice, lineList);
  //		response.setValues(invoice);
  //	}

  public void calculateTotalTaxAndAmounts(ActionRequest request, ActionResponse response) {
    try {
      Invoice invoice = request.getContext().asType(Invoice.class);
      List<InvoiceLine> lineList = invoice.getInvoiceItemList();
      invoice.setInvoiceItemList(invoiceService.regenerateInvoiceLine(invoice));
      invoice = invoiceService.getCalculatedInvoice(invoice, lineList);
      response.setValues(invoice);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
