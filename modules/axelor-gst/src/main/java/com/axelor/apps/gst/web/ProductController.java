package com.axelor.apps.gst.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.axelor.apps.gst.db.InvoiceLine;
import com.axelor.apps.gst.global.Constants;
import com.axelor.apps.gst.service.ProductService;
import com.axelor.data.Importer;
import com.axelor.data.csv.CSVImporter;
import com.axelor.meta.schema.actions.ActionView;
import com.axelor.rpc.ActionRequest;
import com.axelor.rpc.ActionResponse;
import com.google.inject.Inject;

public class ProductController {

	@Inject
	ProductService productService;

	public void onClickSelectProductsIntoInvoiceLine(ActionRequest request, ActionResponse response) {
		try {
			@SuppressWarnings("unchecked")
			List<Integer> _ids = (ArrayList<Integer>) request.getContext().get("_ids");
			List<Long> ids = new ArrayList<>();
			try {
				for (Integer id : _ids) {
					ids.add(new Long(Long.valueOf(id)));
				}
			} catch (NullPointerException e) {
				response.setError("Please select at least one product");
			}
			List<InvoiceLine> lines = productService.getInvoiceLinesByIds(ids);
			response.setView(ActionView.define("Invoice Form").model("com.axelor.apps.gst.db.Invoice")
					.add("form", "invoice-form").param("forceEdit", "true").context("invoiceItemList", lines).map());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void importSomeProducts(ActionRequest request, ActionResponse response) throws IOException{
		try {
			Constants constant = new Constants();
			File configFile = constant.getConfigFile();
			Importer importer = new CSVImporter(configFile.getAbsolutePath(), constant.getDataCsvFile());
			importer.run();
			response.setReload(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
