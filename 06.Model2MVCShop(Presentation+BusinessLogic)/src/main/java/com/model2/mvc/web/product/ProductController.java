package com.model2.mvc.web.product;

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

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.user.UserService;


//==> 상품관리 Controller
@Controller
public class ProductController {
	
	///Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//setter Method 구현 않음
		
	public ProductController(){
		System.out.println(this.getClass());
	}
	
	//==> classpath:config/common.properties  ,  classpath:config/commonservice.xml 참조 할것
	//==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	//@Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;
	
	@Value("#{commonProperties['pageSize']}")
	//@Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	
	@RequestMapping("/addProductView.do")
	public String addProductView() throws Exception {

		System.out.println("/addProductView.do");
		
		return "redirect:/product/addProductView.jsp";
	}
	
	@RequestMapping("/addProduct.do")
	public String addProduct( @ModelAttribute("product") Product product ) throws Exception {

		System.out.println("/addUser.do");
		System.out.println("바뀌기 전 product manudate : "+product.getManuDate());
		String date = product.getManuDate().replaceAll("-", "");
		product.setManuDate(date);
		System.out.println(product.toString());
		//Business Logic
		productService.addProduct(product);
		
		
		return "forward:/product/addProduct.jsp";
	}
	
	@RequestMapping("/getProduct.do")
	public String getProduct( @RequestParam(value="menu", required=false) String menu,
								 	@RequestParam("prodNo") int prodNo,
									Model model ) throws Exception {
		
		System.out.println("/getProduct.do");
		
		System.out.println("prodNo 넘어오는지 : "+prodNo);
		System.out.println("menu 넘어오는지 : "+menu);
		
		if(menu==null) {
			menu="";
		}
		
		//Business Logic
		Product product = productService.getProduct(prodNo);
		System.out.println("select 결과 잘 넘어오는지 : "+product.toString());
		
		// Model 과 View 연결
		model.addAttribute("product", product);
		//model.addAttribute("menu", menu);
		
		
		
		// if문으로 manage type마다 결과값다르게 보내기
		if(menu.equals("manage")) {
			return "redirect:/updateProductView.do?prodNo="+prodNo+"&menu="+menu;
		} else {
			return  "forward:/product/getProduct.jsp";
		}
	}
	
	@RequestMapping("/updateProductView.do")
	public String updateProductView( @RequestParam("prodNo") int prodNo,
											@RequestParam("menu") String menu ,
											Model model ) throws Exception{

		System.out.println("/updateProductView.do");
		System.out.println("prodNo 넘어오는지 : "+prodNo);
		System.out.println("menu 넘어오는지 : "+menu);
		
		//Business Logic
		Product product  = productService.getProduct(prodNo);
		// Model 과 View 연결
		model.addAttribute("product", product);
		model.addAttribute("menu", menu);
		
		return "forward:/product/updateProduct.jsp";
	}
	
	@RequestMapping("/updateProduct.do")
	public String updateProduct( @ModelAttribute("product") Product product,
										@RequestParam("menu") String menu) throws Exception{

		System.out.println("/updateProduct.do 실행");
		System.out.println("updateProduct()에서 menu type : "+menu);
		System.out.println("updateProduct()에서 prodNo : "+product.getProdNo());
		//Business Logic
		System.out.println("product에 binding된 내용 : "+product.toString());
		productService.updateProduct(product);
		
		return "redirect:/getProduct.do?prodNo="+product.getProdNo();//+"&menu="+menu;
	}
	
	
	@RequestMapping("/listProduct.do")
	public String listProduct( @ModelAttribute("search") Search search,
									@RequestParam("menu") String menu,
									Model model 
									) throws Exception{
		
		System.out.println("/listProduct.do");
		
		System.out.println("menu type : "+menu);
		
		if(search.getCurrentPage() ==0 ){
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		// Business logic 수행
		Map<String , Object> map=productService.getProductList(search);
		
		Page resultPage = new Page( search.getCurrentPage(), ((Integer)map.get("totalCount")).intValue(), pageUnit, pageSize);
		System.out.println(resultPage);
		
		// Model 과 View 연결
		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);
		model.addAttribute("menu", menu);
		List<Product> product =(List)map.get("list");
		
		for(Product product1 : product ) {
			System.out.println("상품목록 :: "+product1);
		}
		
		return "forward:/product/listProduct.jsp";
	}
}