package com.axelor.apps.gst.web;

import com.axelor.apps.gst.db.Sequence;
import com.axelor.apps.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class InvoiceController {

	@Inject
	SequenceService sequenceService;

	@Transactional
	public void generateNewSequence(ActionRequest request, ActionResponse response) {
		MetaModelRepository metaModelRepository = Beans.get(MetaModelRepository.class);
		MetaModel model = metaModelRepository.findByName("Invoice");
		Sequence sequence = sequenceService.findSequenceByModel(model);
		response.setValue("reference", sequenceService.generateSequence(sequence));
	}
}
