package com.example.demo.Controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Enitities.Contact;
import com.example.demo.Enitities.User;
import com.example.demo.Repositries.ContactRepo;
import com.example.demo.Repositries.Urepo;

import com.example.demo.helper.Message;
import com.example.demo.helper.Upload;
import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/normal")
public class NormalController {
	@Autowired
	Urepo ur;
	@Autowired
	ContactRepo cr;
	@Autowired
	BCryptPasswordEncoder bc;
	static Contact oldc;
	static boolean flag = false;
	static int i = 0;

	@ModelAttribute
	public void data(Model m, Principal p) {
		try {
			String email = p.getName();
			User user = ur.findByEmail(email);
			m.addAttribute("user", "Hello " + user.getName());

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@GetMapping("/dashboard")
	public String normalDash(Model m, Principal p, HttpSession hs) {
		try {
			i++;
			m.addAttribute("title", "Dashboard");
			User user = ur.findByEmail(p.getName());
			if (user == null) {
				throw new Exception();
			}
			if (i > 2) {
				flag = false;
			}
			hs.setAttribute("msg", flag);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "normal/dashboard";
	}

	@GetMapping("/add-contact")
	public String addContact(Model m, Principal p) {
		try {
			m.addAttribute("title", "Add Contact");
			User user = ur.findByEmail(p.getName());
			if (user == null) {
				throw new Exception();
			}
			m.addAttribute("contact", new Contact());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return "normal/add-contact";
	}

	@PostMapping("/process")
	public String contactProcessing(@ModelAttribute Contact contact, Principal p, HttpSession hs,
			@RequestParam("profImage") MultipartFile mf) throws Exception {
		try {
			System.out.println(contact);
			String name = p.getName();
			Upload u = new Upload();
			String filename = u.upload(mf, name, "ctc" + contact.getEmail(), "contact");
			User user = ur.findByEmail(name);
			if (user == null) {
				throw new Exception();
			}
			System.out.println("Image   " + user.getImage());
			contact.setU(user);
			contact.setImage(filename);
			if (user == null || user.getImage() == null) {
				throw new Exception("error");
			}
			user.getContacts().add(contact);
			System.out.println("Imaeg   " + mf.getOriginalFilename());
			ur.save(user);
			hs.setAttribute("msg", new Message("Success", "alert-success"));

			hs.removeAttribute("msg");
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			hs.setAttribute("msg", new Message("Error", "alert alert-danger"));

		}
		return "normal/add-contact";
	}

	@GetMapping("/contacts/{pageno}")
	public String allContacts(Model m, @PathVariable int pageno, Principal p) {
		try {
			m.addAttribute("title", "Personal Contacts");
			String uname = p.getName();
			m.addAttribute("uname", uname);
			User user = ur.findByEmail(uname);
			if (user == null) {
				throw new Exception();
			}
			PageRequest pages = PageRequest.of(pageno, 5);

			Page<Contact> list = cr.findByU(user.getId(), pages);
			m.addAttribute("list", list);
			m.addAttribute("currentPage", pageno);
			m.addAttribute("totalPages", list.getTotalPages());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "normal/allContacts";
	}

	@GetMapping("/cuser/{cid}")
	public String oneContactDetail(@PathVariable int cid, Model m, Principal p) {
		try {
			Contact contact = cr.findById(cid).get();
			m.addAttribute("title", "Personal Contacts");
			String uname = p.getName();
			User user = ur.findByEmail(uname);
			if (user.getId() == contact.getU().getId()) {

				m.addAttribute("user", uname);
				m.addAttribute("cdetail", contact);
			}
		} catch (Exception e) {
			// TODO: handle exception

		}
		return "normal/particularContact";
	}

	@GetMapping("/delete/{cid}")
	@org.springframework.transaction.annotation.Transactional
	public String deleteContact(@PathVariable int cid, Principal p) throws IOException {
		try {
			Contact contact = cr.findById(cid).get();
			String name = p.getName();
			User loginuser = ur.findByEmail(name);

			if (loginuser == null) {
				throw new Exception();
			}
			File dltFile = new File("D:\\contactproj\\contacts\\src\\main\\resources\\static\\Images");
			String filePath = dltFile.getAbsolutePath() + File.separator + name + File.separator + "ctc"
					+ contact.getEmail() + File.separator + "contact" + File.separator + contact.getImage();
			System.out.println(filePath);
			Path oldImgPath = Paths.get(filePath);
			File contactFolder = new File(filePath);
			Files.walk(oldImgPath, FileVisitOption.FOLLOW_LINKS).sorted(Comparator.reverseOrder()).map(Path::toFile)
					.peek(System.out::println).forEach(File::delete);
			User findByEmail = ur.findByEmail(name);
			if (contact.getU().getId() == findByEmail.getId()) {
				contact.setU(null);
				cr.deleteById(cid);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}

		return "redirect:/normal/contacts/0";
	}

	@PostMapping("/update/{cid}")
	public String updateContact(Model m, @PathVariable int cid) {
		try {
			Contact contact = cr.findById(cid).get();
			m.addAttribute("contact", contact);
			oldc = contact;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "normal/updateContact";
	}

	@PostMapping("/updateContact")
	public String processUpdateContact(@ModelAttribute("contact") Contact c, Principal p,
			@RequestParam("profImage") MultipartFile mf) throws IOException {
		try {
			String name = p.getName();
			User user = ur.findByEmail(name);
			File dltFile = new File("D:\\contactproj\\contacts\\src\\main\\resources\\static\\Images");
			String filePath = dltFile.getAbsolutePath() + File.separator + name + File.separator + "ctc" + c.getEmail()
					+ File.separator + "contact" + File.separator + oldc.getImage();
			System.out.println(filePath);
			Path oldImgPath = Paths.get(filePath);
			Files.delete(oldImgPath);
			Upload u = new Upload();
			String filename = u.upload(mf, name, "ctc" + c.getEmail(), "contact");
			c.setU(user);
			c.setImage(filename);
			cr.save(c);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return "redirect:/normal/contacts/0";

	}

	@GetMapping("/user")
	public String userProfile(Model m, Principal p) {
		String name = p.getName();
		User user = ur.findByEmail(name);
		m.addAttribute("cdetail", user);
		m.addAttribute("title", "Hello " + user.getName());
		return "normal/userProfile";
	}

	@GetMapping("/changePassword")
	public String changePassword(HttpSession hs, Model m) {
		if (hs.getAttribute("errrmsg") != null) {
			hs.removeAttribute("errrmsg");
		}
		m.addAttribute("title", "Change Password");
		return "normal/changePassword";
	}

	@PostMapping("/cpass")
	public String processCpass(@RequestParam("oldpass") String oldpass, HttpSession hs,
			@RequestParam("newpass") String newpass, Principal p, Model m) {
		try {
			i = 1;
			String name = p.getName();
			User user = ur.findByEmail(name);
			if (bc.matches(oldpass, user.getPassword())) {
				user.setPassword(bc.encode(newpass));
				ur.save(user);
				flag = true;
				hs.setAttribute("msg", flag);
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			hs.setAttribute("errrmsg", new Message("Error", "alert-danger"));
			return "redirect:/normal/changePassword";
		}
		return "redirect:/normal/dashboard";
	}

	@PostMapping("/create_order")
	@ResponseBody
	public String cretaeOrder(@RequestBody Map<String, Object> map, HttpSession hs) throws RazorpayException {
		System.out.println(map);

		int amt = Integer.parseInt(map.get("amount").toString());
		RazorpayClient client = new RazorpayClient("rzp_live_YPC0AaNW3mzppv", "jj2twuac0Hm9AhGZA2qxylqD");
		JSONObject options = new JSONObject();
		options.put("amount", amt * 100);
		options.put("currency", "INR");
		options.put("receipt", "order_rcptid_11");
		Order order = client.orders.create(options);
		hs.setAttribute("id", order.get("id"));
		System.out.println(order);
		return order.toString();

	}
}
