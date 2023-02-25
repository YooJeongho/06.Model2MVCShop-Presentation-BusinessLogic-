package com.model2.mvc.service.purchase.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;

@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao {
	
	//field
	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	// for setter injection
	public void setSqlSession(SqlSession sqlsession) {
		this.sqlSession = sqlsession;
	}
	//default constructor
	public PurchaseDaoImpl() {
		System.out.println("PurchaseDaoImpl default constructor called");
	}

	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		sqlSession.insert("PurchaseMapper.addPurchase", purchase);

	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		Purchase purchase = sqlSession.selectOne("PurchaseMapper.getPurchase", tranNo);
		return purchase;
	}

	@Override
	public List<Purchase> getPurchaseList(Search search, String userId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("search", search);
		map.put("userId", userId);
		
		return sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
	}

	@Override
	public void updatePurchase(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updatePurchase", purchase);
	}

	@Override
	public void updateTranCodeByProd(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updateTranCodeByProd", purchase);

	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		sqlSession.update("PurchaseMapper.updatePurchaseTrancode", purchase);

	}

	@Override
	public int getTotalCount(String userId) throws Exception {
		
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", userId);
	}

}
