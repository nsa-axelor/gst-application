package com.axelor.apps.gst.service;

import com.axelor.apps.gst.db.Sequence;
import com.axelor.apps.gst.db.repo.SequenceRepository;
import com.axelor.meta.db.MetaModel;
import com.google.inject.persist.Transactional;

public class SequenceServiceImpl implements SequenceService {

  @Override
  public String generateSequence(Sequence sequence) {
    String seq = sequence.getPrefix() + generatePadding(sequence) + sequence.getSuffix();
    return seq;
  }

  public static String generatePadding(Sequence sequence) {

    StringBuilder p = new StringBuilder();

    int padding = sequence.getPadding();
    String currentNumber = sequence.getNextNumber();
    int currentNumberCount = currentNumber.length();

    for (int i = 0; i < (padding - currentNumberCount); i++) {
      p.append("0");
    }
    p.append(currentNumber);
    changeToNextNumber(sequence);
    return p.toString();
  }

  @Transactional
  private static void changeToNextNumber(Sequence sequence) {
    int nextNumer = Integer.parseInt(sequence.getNextNumber()) + 1;
    sequence.setNextNumber(String.valueOf(nextNumer));
  }

  @Override
  public Sequence findSequenceByModel(MetaModel model) {
    SequenceRepository repository = new SequenceRepository();
    Sequence sequence = repository.findByModel(model);
    return sequence;
  }
}
