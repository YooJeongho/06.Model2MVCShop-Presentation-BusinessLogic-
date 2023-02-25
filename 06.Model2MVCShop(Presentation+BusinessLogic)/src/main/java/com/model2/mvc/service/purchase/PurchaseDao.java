package com.model2.mvc.service.purchase;

import java.util.List;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;

public interface PurchaseDao {
	
	//insert
	public void addPurchase(Purchase purchase) throws Exception;
	
	//select
	public Purchase getPurchase(int tranNo) throws Exception;
	
	//select Purchase List
	public List<Purchase> getPurchaseList(Search search, String userId ) throws Exception;
	
	//update Purchase Information
	public void updatePurchase(Purchase purchase) throws Exception;
	
	//update TranCode at product list
	public void updateTranCodeByProd(Purchase purchase) throws Exception;
	
	//update TranCode at purchase list
	public void updateTranCode(Purchase purchase) throws Exception;
	
	//page 처리를 위한 method
	public int getTotalCount(String userId) throws Exception;
}
