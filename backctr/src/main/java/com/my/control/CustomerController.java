package com.my.control;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.dto.Customer;
import com.my.exception.AddException;
import com.my.exception.FindException;
import com.my.service.CustomerService;
@Controller
public class CustomerController {
	@Autowired
	private CustomerService service;
	
	@PostMapping("login")
	@ResponseBody
	public Map login(String id, String pwd, HttpSession session) {
		Map<String, Object>map = new HashMap<>();
		map.put("status", 0);
		String result = null; //"{\"status\": 0}"; //Json에서 Map형사용으로 변경  //실패 
		session.removeAttribute("loginInfo"); //loginInfo라는 값을 초기화(제거)한다 
		try {
			Customer c = service.login(id, pwd);
			map.put("status", 1); //정상 처리됐을 경우 1설정 
			session.setAttribute("loginInfo", id);
		} catch (FindException e) {
		}
		return map;
	}
	@PostMapping("signup")
	@ResponseBody
	public Map signup(Customer c) {
		
		Map<String, Object> map = new HashMap<>();
		map.put("status", 0);
		map.put("msg", "가입실패");
		try {	
			service.signup(c);
			map.put("status",1);
			map.put("msg", "가입성공");
		}catch(AddException e) {
		}
		return map;
	}
	@PostMapping("iddupchk")
	@ResponseBody
	private Map iddupchk(String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("status",0);
		map.put("msg","이미 사용중인 아이디입니다");
		try {
			Customer c = service.iddupchk(id);
		}catch (FindException e) { //사용가능한 아이디인 경우 
			map.put("status",1);
			map.put("msg","사용가능한 아이디입니다");
		}
		return map;
	}
	@PostMapping("loginstatus")
	@ResponseBody
	private Map loginstatus(HttpSession session){
		String loginedId = (String)session.getAttribute("loginInfo");
		Map<String, Object> map = new HashMap<>();
		if(loginedId == null) {
			map.put("status", 0);			
		}else {
			map.put("status", 1);			
		}
		return map; 
	}
	@PostMapping("logout")
	@ResponseBody
	private String logout(HttpSession session){
		session.removeAttribute("loginInfo");
		return null; 
	}
}
