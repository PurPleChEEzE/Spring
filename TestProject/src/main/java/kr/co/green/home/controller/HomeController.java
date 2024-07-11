package kr.co.green.home.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String home(HttpSession session, Model model) {
		
//		model.addAttribute("msg", session.getAttribute("msg"));
//		session.removeAttribute("msg");
		return "home";
	}
	
}
