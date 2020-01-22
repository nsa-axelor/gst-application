package com.axelor.apps.gst.web;

import com.axelor.apps.gst.db.Address;
import com.axelor.apps.gst.db.Contact;
import com.axelor.apps.gst.db.Invoice;
import com.axelor.apps.gst.db.Party;
import com.axelor.apps.gst.db.Sequence;
import com.axelor.apps.gst.service.SequenceService;
import com.axelor.db.Query;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class InvoiceController {

  @Inject SequenceService sequenceService;

  @Transactional
  public void generateNewSequence(ActionRequest request, ActionResponse response) {
    MetaModelRepository metaModelRepository = Beans.get(MetaModelRepository.class);
    MetaModel model = metaModelRepository.findByName("Invoice");
    Sequence sequence = sequenceService.findSequenceByModel(model);
    response.setValue("reference", sequenceService.generateSequence(sequence));
  }

  public void setPartyPrimaryContact(ActionRequest request, ActionResponse response) {
    System.err.println("set partyContact");
    Invoice invoice = request.getContext().asType(Invoice.class);
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
    //    List<Contact> contacts = party.getContactList();
    //    Contact contact = Query.of(Contact.class).filter("self.type = 'primary' AND party.contact
    // = :self").bind("party",party).fetchOne();
    /*
     * List<Contact> primaryContacts =
     * Query.of(Contact.class).filter("self.type = 'primary'").bind("party",party).
     * fetch(); int n = 0; primaryContacts.forEach(primaryContact->{
     * if(contacts.get(n).getType()) });
     *
     * invoice.setPartyContact(contact); System.err.println("Contact : " +
     * contact.getName());
     */
    System.err.println("Contact: " + contact.getName());
    invoice.setPartyContact(contact);
    invoice.setInvoiceAddress(address);
    invoice.setShippingAddress(address);
    response.setValues(invoice);
  }

  public void changeShippingAddress(ActionRequest request, ActionResponse response) {
    Invoice invoice = request.getContext().asType(Invoice.class);
    Party party = invoice.getParty();
    System.err.println("change Shipping address");
    Address address;
    if (!invoice.getIsUseInvoiceAddressAsShipping()) {
      address =
          Query.of(Address.class)
              .filter("self.type = 'shipping' AND self.party = :party")
              .bind("party", party)
              .fetchOne();
    } else {
      address =
          Query.of(Address.class)
              .filter("self.type = 'invoice' AND self.party = :party")
              .bind("party", party)
              .fetchOne();
    }
    invoice.setShippingAddress(address);
    response.setValues(invoice);
  }
}
