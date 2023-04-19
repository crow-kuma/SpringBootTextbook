package com.example.demo;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RegistrationController {
	@PostMapping("/register")
	public ModelAndView regiter(@RequestParam("name") String name,
								@RequestParam("password") String password,
								@RequestParam("gender") int gender,
								@RequestParam("area") int area,
								@RequestParam("interest") int[] interests,
								@RequestParam("remarks") String remarks,
								ModelAndView mv) {
		StringBuilder sb = new StringBuilder();
		sb.append("名前：" + name);
		sb.append(", パスワード：" + password);
		sb.append(", 性別：" + gender);
		sb.append(", 地域：" + area);
		sb.append(",  興味のある分野：" + Arrays.toString(interests));
		sb.append(", 備考：" + remarks.replaceAll("¥n", ""));

		mv.setViewName("result");
		mv.addObject("registData", sb.toString());
		return mv;
	}
}