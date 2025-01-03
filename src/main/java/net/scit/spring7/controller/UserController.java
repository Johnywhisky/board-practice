package net.scit.spring7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class UserController {
	@GetMapping("/regist")
	public String regist() {
		
		return "/user/registView";
	}
}
