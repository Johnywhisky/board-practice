package net.scit.spring7.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.scit.spring7.dto.LoginUserDetailsDto;
import net.scit.spring7.service.UserService;


@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
	private final UserService service;

	@GetMapping("/regist")
	public String registView() {

		return "/user/registView";
	}

	@PostMapping("/regist")
	public String regist(@ModelAttribute LoginUserDetailsDto userDto) {
		service.save(userDto);

		return "redirect:/";
	}

	@PostMapping("/idDupCheck")
	@ResponseBody
	public Boolean idDupCheck(@RequestParam(name="userId") String userId) {
		Boolean result = service.idDupCheck(userId);

		return result;
	}

	@GetMapping("/login")
	public String loginView(@RequestParam(name="error", required=false) Boolean error, Model model) {
		model.addAttribute("error", error);
		model.addAttribute("errMsg", "잘못된 아이디 또는 비밀번호 입니다.");

		return "/user/login";
	}
	
	@GetMapping("/withdraw")
	public String withdrawUser(@RequestParam(name="userId") String userId) {
		service.withdraw(userId);

		return "redirect:/user/logout";
	}
}
