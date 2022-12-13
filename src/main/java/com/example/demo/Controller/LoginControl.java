package com.example.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginControl {
	static boolean flag = false;

	@RequestMapping("/signin")
	public String login(Model m) {
		m.addAttribute("title", "Log In");
		return "login";
	}
}
