package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.domain.Product;


@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {
	
	// field
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDao dao;     // == ProductDao dao = new ProductDao();
	
	// for setter Injection
	public void setProductDao(ProductDao productdao) {
		this.dao = productdao;
	}

	@Override
	public void addProduct(Product product) throws Exception {
		this.dao.addProduct(product);;
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
		return dao.getProduct(prodNo);
	}

	@Override
	public Map<String, Object> getProductList(Search search) throws Exception {
		List<Product> list = dao.getProductList(search);
		int totalCount = dao.getTotalCount(search);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", new Integer(totalCount));
		
		return map;
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		dao.updateProduct(product);
	}

}
