package com.model2.mvc.web.purchase;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.user.UserService;


//==> ��ǰ���� Controller
@Controller
public class PurchaseController {
	
	///Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	//setter Method ���� ����
		
	public PurchaseController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml ���� �Ұ�
	//==> �Ʒ��� �ΰ��� �ּ��� Ǯ�� �ǹ̸� Ȯ�� �Ұ�
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addPurchaseView.do")
	public ModelAndView addProductView(@RequestParam("prodNo") int prodNo,
													 HttpSession session
													 ) throws Exception {
		
		Product product = productService.getProduct(prodNo);
		User user = (User)session.getAttribute("user");
		
		Purchase purchase = new Purchase();
		
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/addPurchaseView.jsp");
		modelAndView.addObject("purchase", purchase);
		
		System.out.println("/addProductView.do end");
		
		return modelAndView;
	}
	
	@RequestMapping("/addPurchase.do")
	public ModelAndView addPurchase( @ModelAttribute("purchase") Purchase purchase,
												@RequestParam("prodNo") int prodNo,
												HttpSession session) throws Exception {

		System.out.println("/addPurchase.do");
		System.out.println("/addPurchase���� ������ �̸� : "+purchase.getReceiverName());
		System.out.println("/addPurchase���� ������ �ּ� : "+purchase.getDivyAddr());
		System.out.println("/addPurchase���� ���ſ�û ���� : "+purchase.getDivyRequest());
		String userId = ((User)session.getAttribute("user")).getUserId();
		
		purchase.setTranCode("1");
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		purchase.setBuyer(userService.getUser(userId));
		purchaseService.addPurchase(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/purchase/addPurchase.jsp");
		modelAndView.addObject("purchase", purchase);
		
		System.out.println("/addPurchase.do end");
		
		return modelAndView;
	}
	
	@RequestMapping("/getPurchase.do")
	public ModelAndView getPurchase(@RequestParam("tranNo") int tranNo) throws Exception {
		
		System.out.println("/getPurchase.do start");
		
		System.out.println("prodNo �Ѿ������ : "+tranNo);
		
		// Buiseness Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		
		
		// Model �� View ����
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		
		System.out.println("/getPurchase.do End");
		
		return  modelAndView;
	}
	
	@RequestMapping("/updatePurchase.do")
	public ModelAndView updatePurchase(@ModelAttribute("purchase") Purchase purchase,
													@RequestParam("tranNo") int tranNo) throws Exception{

		System.out.println("/updatePurchase.do start");

		//Business Logic
		purchaseService.updatePurchase(purchase);
		
		purchase = purchaseService.getPurchase(tranNo	);
		
		// Model �� View ����
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/purchase/getPurchase.jsp");
		
		System.out.println("/updatePurchase.do end");
		
		return modelAndView;
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public ModelAndView updatePurchaseView(@RequestParam("tranNo") int tranNo) throws Exception{

		System.out.println("/updateProduct.do");
		//Business Logic
		Purchase purchase = purchaseService.getPurchase(tranNo);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("purchase", purchase);
		modelAndView.setViewName("forward:/purchase/updatePurchaseView.jsp");
		
		System.out.println("/updatePurchaseView.do end");
		return modelAndView;
	}
	
	
	@RequestMapping("/listPurchase.do")
	public ModelAndView listPurchase( @ModelAttribute("search") Search search,
												HttpSession session) throws Exception{
		
		System.out.println("/listPurchase.do start");
		
		User user = (User)session.getAttribute("user");
		String userId = user.getUserId();
		System.out.println("listPurchase");
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic ����
		Map<String , Object> map=purchaseService.getPurchaseList(search, userId);
		
		Page pageResult = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(pageResult);
		
		// Model �� View ����
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("pageresult", pageResult);
		modelAndView.addObject("search", search);
		modelAndView.setViewName("forward:/purchase/listPurchase.jsp");
		
		return modelAndView;
	}
	
	@RequestMapping("/updateTranCode.do")
	public ModelAndView updateTranCode(@RequestParam("tranCode") String tranCode,
													@RequestParam("page") String currentPage,
													@RequestParam("tranNo") int tranNo) throws Exception {
		
		System.out.println("/updateTranCode.do start");
		Purchase purchase = new Purchase();
		purchase.setTranCode(tranCode);
		purchase.setTranNo(tranNo);
		
		purchaseService.updateTranCode(purchase);
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName(currentPage);
		
		System.out.println("forward:/listPurchase.do?&currentPage="+currentPage);
		
		return modelAndView;
	}
	
	
	@RequestMapping("/updateTranCodeByProd.do")
	public ModelAndView updateTranCodeByProd (@RequestParam("prodNo") int prodNo, 
																@RequestParam("tranCode") String tranCode,
																@RequestParam("searchCondition") String searchCondition,
																@RequestParam("searchKeyword") String searchKeyword,
																@RequestParam("currentPage") String currentPage) throws Exception {
		
		Product product = productService.getProduct(prodNo);
		Purchase purchase = new Purchase();
		purchase.setPurchaseProd(product);
		purchase.setTranCode(tranCode);
		
		purchaseService.updateTranCodeByProd(purchase);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/listProduct.do?menu=manage&searchCondition="+searchCondition+"&searchKeyword="+searchKeyword+"&currentPage="+currentPage);
		return modelAndView;
	}
}