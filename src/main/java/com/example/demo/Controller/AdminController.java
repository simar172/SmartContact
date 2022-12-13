package com.example.demo.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.Enitities.User;
import com.example.demo.Repositries.Urepo;


@Controller
@RequestMapping("/admin")
public class AdminController {
	@Autowired
	Urepo ur;

	@GetMapping("/home")
	public String home(Model m) {
		List<User> findAll = ur.findAll();
		m.addAttribute("list", findAll);
		return "admin/home";
	}

	@GetMapping("/delete/{uid}")
	public String deleteUser(@PathVariable int uid) {

		ur.deleteById(uid);

		return "redirect:/admin/home";
	}
	@GetMapping("/update")
	public String update() {
		return "admin/update";
	}

}
