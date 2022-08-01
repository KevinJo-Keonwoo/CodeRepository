package com.my.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReturnTest {
	@GetMapping("a1")
	public ModelAndView a()	{ //가장 기본 방법
		ModelAndView mnv = new ModelAndView();
		mnv.addObject("greeting", "HELLO");
		mnv.setViewName("/WEB-INF/jsp/a.jsp");
		return mnv;
	}
	@GetMapping("b1")
	public String b(Model model) {  //view만 결정하고 싶을때
		//mnv.addObject를 대신할 매개변수
		model.addAttribute("greeting", "안녕하세요");
		return "/WEB-INF/jsp/a.jsp";
	}
	@GetMapping("c1") // /WEB-INF/jsp/ 가 prefix되고 .jsp가 surfix되어서 "/WEB-INF/jsp/c1.jsp"가 됨 
	public void c() { // viewresolver가 있는경우 정확히 찾아감 
		//void로 선언되는 경우에는 viewname을 자동으로 찾아서 return함 
	}
	@GetMapping(value = "d1", produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String d() {
		String responseData = "응답내용입니다";
		return responseData; // 뷰이름으로 응답내용입니다를 반환함 -> 응답내용입니다.jsp를 찾음 
							 // @ResponseBody를 추가하면 "응답내용입니다"를 반환함 
							 // 클라이언트가 @RequestBody를 추가하면 JSON형태로 옴
	}
}
