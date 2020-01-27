package com.axelor.apps.gst.config;

import com.axelor.app.AxelorModule;
import com.axelor.apps.gst.db.repo.PartyGstRepository;
import com.axelor.apps.gst.db.repo.PartyRepository;
import com.axelor.apps.gst.service.InvoiceLineService;
import com.axelor.apps.gst.service.InvoiceLineServiceImpl;
import com.axelor.apps.gst.service.InvoiceService;
import com.axelor.apps.gst.service.InvoiceServiceImpl;
import com.axelor.apps.gst.service.ProductService;
import com.axelor.apps.gst.service.ProductServiceImpl;
import com.axelor.apps.gst.service.SequenceService;
import com.axelor.apps.gst.service.SequenceServiceImpl;

public class GstConfig extends AxelorModule {
  @Override
  protected void configure() {
    bind(SequenceService.class).to(SequenceServiceImpl.class);
    bind(PartyRepository.class).to(PartyGstRepository.class);
    bind(InvoiceService.class).to(InvoiceServiceImpl.class);
    bind(InvoiceLineService.class).to(InvoiceLineServiceImpl.class);
    bind(ProductService.class).to(ProductServiceImpl.class);
  }
}
