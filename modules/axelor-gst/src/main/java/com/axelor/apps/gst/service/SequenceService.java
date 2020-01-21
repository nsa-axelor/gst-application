package com.axelor.apps.gst.service;

import com.axelor.apps.gst.db.Sequence;
import com.axelor.meta.db.MetaModel;

public interface SequenceService {
  public String generateSequence(Sequence sequence);

  public Sequence findSequenceByModel(MetaModel model);
}
