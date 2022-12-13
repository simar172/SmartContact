package com.example.demo.Enitities;

import org.springframework.data.mongodb.core.mapping.Document;


@Document("contact")
public class Contact {
	@org.springframework.data.annotation.Id
	int id;

	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	String name;
	
	User u;

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", u=" + u + ", secondname=" + secondname + ", work=" + work
				+ ", email=" + email + ", phone=" + phone + ", image=" + profImage + ", description=" + description
				+ "]";
	}

	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Contact(int id, String name, String secondname, String work, String email, String phone, String profImage,
			String description) {
		super();
		this.id = id;
		this.name = name;
		this.secondname = secondname;
		this.work = work;
		this.email = email;
		this.phone = phone;
		this.profImage = profImage;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSecondname() {
		return secondname;
	}

	public void setSecondname(String secondname) {
		this.secondname = secondname;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getImage() {
		return profImage;
	}

	public void setImage(String image) {
		this.profImage = image;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	String secondname;
	String work;
	String email;
	String phone;
	String profImage;
	String description;

}
