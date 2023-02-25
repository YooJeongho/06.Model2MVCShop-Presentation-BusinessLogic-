package com.model2.mvc.service.purchase.Impl;

import java.awt.dnd.DragSourceAdapter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.impl.ProductDaoImpl;
import com.model2.mvc.service.purchase.PurchaseDao;
import com.model2.mvc.service.purchase.PurchaseService;


@Service("purchaseServiceImpl")
public class PurchaseServiceImpl implements PurchaseService {
	
	//field
	@Autowired
	@Qualifier("purchaseDaoImpl")
	private PurchaseDao purchaseDao;
	
	@Autowired
	@Qualifier("productDaoImpl")
	private ProductDao productDAO;
	
	// for setter Injection
	public void setPurchaseDao(PurchaseDao purchaseDao) {
		this.purchaseDao = purchaseDao;
	}
	
	public void setProductDao(ProductDao productDao) {
		this.productDAO = productDao;
	}
	
	@Override
	public void addPurchase(Purchase purchaseVO) throws Exception {
		System.out.println("PurchaseServiceImpl의 addPurchase 실행");
		purchaseDao.addPurchase(purchaseVO);
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		System.out.println("PurchaseServiceImpl의 getPurchase 실행");
		Purchase purchase = purchaseDao.getPurchase(tranNo);
		return purchase;
	}

	@Override
	public Map<String, Object> getPurchaseList(Search search, String userId) throws Exception {
		System.out.println("PurchaseServiceImpl의 getPurchaseList() 실행");
		List<Purchase> list = purchaseDao.getPurchaseList(search, userId);
		int totalCount = purchaseDao.getTotalCount(userId);
		
		Map <String, Object> map = new HashMap<String, Object> ();
		map.put("list", list);
		map.put("totalCount", new Integer(totalCount));
		
		return map;
	}

	@Override
	public HashMap<String, Object> getSaleList(Search search) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		purchaseDao.updatePurchase(purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		purchaseDao.updateTranCode(purchase);
		
	}

	@Override
	public void updateTranCodeByProd(Purchase purchase) throws Exception {
		purchaseDao.updateTranCodeByProd(purchase);
		
	}

}
