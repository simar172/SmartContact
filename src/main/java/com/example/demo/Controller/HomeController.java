package com.example.demo.Controller;

import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Enitities.User;
import com.example.demo.Repositries.Urepo;
import com.example.demo.helper.Upload;
import com.mongodb.internal.connection.Authenticator;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	Urepo ur;
	@Autowired
	BCryptPasswordEncoder bc;

	@Autowired
	Upload u;
	static boolean flag = true;
	static String otpCheck = "";

	

	@GetMapping("/")
	public String home(HttpSession hs, Model m) {
		if (hs.getAttribute("msg") != null) {
			hs.removeAttribute("msg");
		}
		m.addAttribute("title", "Home");
		flag = true;
		return "home";
	}

	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("user", new User());
		m.addAttribute("title", "Signup");
		return "signup";
	}

	@PostMapping("/data")
	public String regUser(@ModelAttribute("user") User u, Model m, @RequestParam("imageProf") MultipartFile mf,
			HttpSession session) {
		try {
			System.out.println("use is  " + u);
			if (u.getEmail() == "") {
				throw new Exception("error");
			}
			Upload uplservice = new Upload();
			String uploadProfile = uplservice.uploadProfile(mf, u.getEmail());
			u.setEnabled(true);
			u.setImage(uploadProfile);
			u.setRole("ROLE_NORMAL");
			u.setPassword(bc.encode(u.getPassword()));
			ur.save(u);
			session.setAttribute("msg", new com.example.demo.helper.Message("Success!!", "alert-success"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			session.setAttribute("msg", new com.example.demo.helper.Message("Error", "alert-danger"));

		}
		return "redirect:/";
	}

	@GetMapping("/forgot/OTP")
	public String otpSend(HttpSession hs, Model m) throws InterruptedException {

		if (hs.getAttribute("msg") != null) {
			hs.removeAttribute("msg");
		}
		m.addAttribute("title", "Forgot Password");
		return "forgot";
	}

	@PostMapping("/email/send")
	public String emailSend(@RequestParam("email") String email, HttpSession hs) {
		try {
			hs.setAttribute("email", email);
			if (flag == false) {
				throw new UnknownServiceException("");

			}
			List<Integer> numbers = new ArrayList<>();
			for (int j = 0; j < 10; j++) {
				numbers.add(j);
			}

			java.util.Collections.shuffle(numbers);

			String result = "";
			for (int k = 0; k < 4; k++) {
				result += numbers.get(k).toString();
			}
			User findByEmail = ur.findByEmail(email);
			if (findByEmail == null) {
				throw new UsernameNotFoundException("");
			}
			
		} catch (UnknownServiceException e) {
			// TODO: handle exception

			return "redirect:/forgot/OTP";
		} catch (UsernameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hs.setAttribute("umsg", "User does'nt exist!!");
			return "redirect:/forgot/OTP";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "enterOTP";
	}

	@PostMapping("/proccessing/OTP")
	public String verifyOtp(@RequestParam("otp") String otp, Model m, HttpSession hs) {
		try {
			hs.removeAttribute("msg");
			if (!otpCheck.equals(otp)) {
				throw new Exception("");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			flag = true;
			m.addAttribute("flag", flag);
			return "enterOTP";
		}
		return "changePass";
	}

	@PostMapping("/check/pass")
	public String processPasswords(@RequestParam("newpass") String newpass, @RequestParam("confpass") String confpass,
			HttpSession hs) {
		String email = hs.getAttribute("email").toString();
		User user = ur.findByEmail(email);
		user.setPassword(bc.encode(newpass));
		ur.save(user);
		return "redirect:/signin";
	}

}
