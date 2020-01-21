package com.axelor.apps.gst.db.repo;

import com.axelor.apps.gst.db.Party;
import com.axelor.apps.gst.db.Sequence;
import com.axelor.apps.gst.service.SequenceService;
import com.axelor.inject.Beans;
import com.axelor.meta.db.MetaModel;
import com.axelor.meta.db.repo.MetaModelRepository;
import com.google.inject.Inject;

public class PartyGstRepository extends PartyRepository {

  @Inject SequenceService sequenceService;

  @Override
  public Party save(Party entity) {
    MetaModelRepository metaModelRepository = Beans.get(MetaModelRepository.class);
    MetaModel model = metaModelRepository.findByName(entity.getClass().getSimpleName());
    Sequence sequence = sequenceService.findSequenceByModel(model);
    entity.setReference(sequenceService.generateSequence(sequence));
    return super.save(entity);
  }
}
