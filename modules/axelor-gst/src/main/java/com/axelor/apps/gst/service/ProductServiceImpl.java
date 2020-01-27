package com.axelor.apps.gst.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.db.Product;
import com.axelor.apps.gst.db.repo.ProductRepository;
import com.axelor.inject.Beans;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;

public class ProductServiceImpl implements ProductService{
	
	List<InvoiceLine> invoiceLineList;
	@Inject
	InvoiceLineService invoiceLineService;

	@Transactional
	@Override
	public List<InvoiceLine> getInvoiceLinesByIds(List<Long> ids) {
		invoiceLineList = new ArrayList<>();
		ids.forEach(id -> {
			InvoiceLine line = new InvoiceLine();
			Product product = Beans.get(ProductRepository.class).find(id);
//			Product product = Query.of(Product.class).filter("self.id = :id").bind("id", Long.valueOf(id)).fetchOne();
			line.setPrice(product.getSalePrice());
			line.setQty(new Integer(1));
			line.setNetAmount(line.getPrice().multiply(new BigDecimal(line.getQty())));
			line.setProduct(product);
			String itemName = invoiceLineService.generateItemName(product);
			line.setItem(itemName);
			line.setGstRate(product.getGstRate());
			line.setHsbn(product.getHsbn());
			invoiceLineList.add(line);
		});
		return invoiceLineList;
	}
	
}
