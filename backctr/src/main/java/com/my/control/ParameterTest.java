package com.my.control;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.dto.Product;

@Controller
public class ParameterTest {
	@GetMapping("a")
	public void a() {  //컨트롤러용 메서드 (매핑이 붙어있는)
		System.out.println("a메서드 호출");
	}
	@GetMapping("b")
	public void b(HttpServletRequest request) {
		System.out.println(request.getParameter("no"));
	}
	@GetMapping("c")
	public void c(HttpServletResponse response) throws IOException{
		response.sendRedirect("http://www.google.com");
	}
	@GetMapping("d")
	public void d(HttpSession session){
		System.out.println("세션 새로 생성여부:" + session.isNew());
	}
	@GetMapping("e") // http://localhost:8888/backctr/e?prodNo=C0001&prodName=아메리카노&prodPrice=1000
	public void e(String prodNo, String prodName, int prodPrice){ //요청전달데이터와 매개변수 이름이 일치해야만함 -> 자동대입
		System.out.println("prodNo=" + prodNo);						//request.getParameter를 대신하는 방법임 
		System.out.println("prodName=" + prodName);
		System.out.println("prodPrice=" + prodPrice);
	}
	@GetMapping("f") // http://localhost:8888/backctr/f?prod_no=C0001&prodPrice=1000
					 // http://localhost:8888/backctr/f?prod_no=C0001
	public void f( @RequestParam(name = "prod_no")String prodNo, 
				   @RequestParam(required = false)String prodName,  //null
				   @RequestParam(required = false, defaultValue = "0")int prodPrice){   //null //0이 아님 요청전달데이터는 무조건 String
		System.out.println("prodNo=" + prodNo);						
		System.out.println("prodName=" + prodName);
		System.out.println("prodPrice=" + prodPrice);
	}
	@GetMapping("g") // http://localhost:8888/backctr/g?prodNo=C0001&prodName=아메리카노&prodPrice=1000
	public void g(Product p) { //매개변수가 dto 형태
		System.out.println("prodNo=" + p.getProdNo());
		System.out.println("prodName=" + p.getProdName());
		System.out.println("prodPrice=" + p.getProdPrice());
	}
	
	//http://localhost:8888/backctr/h?arr=one&arr=two&arr=three
	@GetMapping("h")
	public void h(String[] arr ) {
		for(String str: arr) {
			System.out.println(str);
		}
	}
	
	//http://localhost:8888/backctr/i?prodNo=1&prodName=a&prodNo=2&prodName=b
//	@GetMapping("i")
//	public void i(String[] prodNo, String[] prodName ) {
////		for(String no: prodNo) {
////			System.out.println(no);
////		}
////		for(String name: prodName) {
////			System.out.println(name);
////		}
//		for(int i = 0; i < prodNo.length; i++) {
//			System.out.println(prodNo[i] + ":" + prodName[i]);
//		}
//	}
	@PostMapping("i")
	public void i(@RequestBody List<Product> list) {
		for(Product p : list) {
			System.out.println(p);
		}
	}
}

