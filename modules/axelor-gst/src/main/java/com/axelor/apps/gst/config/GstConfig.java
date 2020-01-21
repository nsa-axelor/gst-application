package com.axelor.apps.gst.config;

import com.axelor.app.AxelorModule;
import com.axelor.apps.gst.db.repo.PartyGstRepository;
import com.axelor.apps.gst.db.repo.PartyRepository;
import com.axelor.apps.gst.service.SequenceService;
import com.axelor.apps.gst.service.SequenceServiceImpl;
import com.axelor.apps.gst.web.InvoiceController;

public class GstConfig extends AxelorModule {
  @Override
  protected void configure() {
    bind(SequenceService.class).to(SequenceServiceImpl.class);
    bind(PartyRepository.class).to(PartyGstRepository.class);
    bind(InvoiceController.class);
    //    super.configure();
  }
}
