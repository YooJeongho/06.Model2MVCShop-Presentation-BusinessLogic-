package com.model2.mvc.service.product;

import java.util.List;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;

public interface ProductDao {

	//Insert
	public void addProduct(Product product) throws Exception;
	
	//SELECT
	public Product getProduct(int productNo) throws Exception;
	
	//Select product list
	public List<Product> getProductList(Search search) throws Exception;
	
	//Update product
	public void updateProduct(Product product) throws Exception;
	
	//page 처리를 위한 method
	public int getTotalCount(Search search) throws Exception;
	
	
}
