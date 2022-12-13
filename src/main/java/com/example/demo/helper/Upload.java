package com.example.demo.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Properties;

import java.util.Properties;


import org.springframework.stereotype.Service;

@Service
public class Upload {

	public String upload(MultipartFile mf, String uname, String contactName, String type) throws IOException {
		File mainpath = new File("D:\\contactproj\\contacts\\src\\main\\resources\\static\\Images");

		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		if (!mainpath.exists()) {
			mainpath.mkdir();
		}
		File unamefolder = new File(mainpath.getAbsolutePath() + File.separator + uname);
		if (!unamefolder.exists()) {
			unamefolder.mkdir();
		}
		File typeImg = new File(unamefolder.getAbsolutePath() + File.separator + contactName);
		if (!typeImg.exists()) {
			typeImg.mkdir();
		}
		File cname = new File(typeImg.getAbsolutePath() + File.separator + "contact");
		if (!cname.exists()) {
			cname.mkdir();
		}

		Files.copy(mf.getInputStream(), Path.of(cname + File.separator + mf.getOriginalFilename()),
				StandardCopyOption.REPLACE_EXISTING);
		return mf.getOriginalFilename();
	}

	public String uploadProfile(MultipartFile mf, String uname) throws IOException {
		File mainpath = new File("D:\\contactproj\\contacts\\src\\main\\resources\\static\\Profile");

		BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
		if (!mainpath.exists()) {
			mainpath.mkdir();
		}
		File unamefolder = new File(mainpath.getAbsolutePath() + File.separator + uname);
		if (!unamefolder.exists()) {
			unamefolder.mkdir();
		}

		Files.copy(mf.getInputStream(), Path.of(unamefolder + File.separator + mf.getOriginalFilename()),
				StandardCopyOption.REPLACE_EXISTING);
		return mf.getOriginalFilename();
	}

	public InputStream serveImage(String filename, String uname) throws FileNotFoundException {
		// TODO Auto-generated method stub
		InputStream isInputStream = new FileInputStream(
				"Images" + File.separator + uname + File.separator + "contact" + File.separator + filename);
		return isInputStream;
	}



	

	
	  

	   

	}

