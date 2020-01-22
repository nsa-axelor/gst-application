package com.axelor.apps.gst.db.repo;

import com.axelor.apps.gst.db.Address;

public class AddressGstRepository extends AddressRepository {

  @Override
  public Address save(Address entity) {
    String fullAddress =
        entity.getType()
            + " - "
            + entity.getLine1()
            + ", "
            + entity.getLine2()
            + ", "
            + entity.getCity()
            + " "
            + entity.getPincode();
    System.err.println("Full Address: " + fullAddress);
    return super.save(entity);
  }
}
