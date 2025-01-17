package net.scit.spring7.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.scit.spring7.dto.BoardDto;
import net.scit.spring7.dto.LoginUserDetailsDto;
import net.scit.spring7.service.BoardService;
import net.scit.spring7.utility.Pagination;


@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService service;

	@Value("${spring.servlet.multipart.location}")
	private String uploadFilePath;

	@GetMapping("/list")
	public String listView(
		@AuthenticationPrincipal LoginUserDetailsDto loginUser,
		@PageableDefault(page=1) Pageable pageable,
		@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		Model model
	) {
		Page<BoardDto> boardDtoList = service.findAll(searchTheme, searchWord, pageable, pageSize);
		Pagination pagination = new Pagination(pageSize, pageable.getPageNumber(), boardDtoList.getTotalPages());

		model.addAttribute("boardList", boardDtoList);
		model.addAttribute("searchTheme", searchWord.isEmpty() ? "boardTitle" : searchTheme);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("pagination", pagination);
		if (loginUser != null)
			model.addAttribute("loginName", loginUser.getName());

		return "/board/listView";
	}

	@GetMapping("/detail/{seqNo}")
	public String detailView(
		@AuthenticationPrincipal LoginUserDetailsDto loginUser,
		@PathVariable("seqNo") Long seqNo,
		@PageableDefault(page=1) Pageable pageable,
		@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		Model model
	) {
		BoardDto dto = service.findById(seqNo);
		service.incrementHitCount(seqNo);
		Pagination pagination = new Pagination(pageSize, pageable.getPageNumber(), 0);

		model.addAttribute("board", dto);
		model.addAttribute("searchTheme", searchWord.isEmpty() ? "boardTitle" : searchTheme);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("pagination", pagination);
		if (loginUser != null)
			model.addAttribute("loginName", loginUser.getName());

		return "/board/detailView";
	}
	
	@GetMapping("/write")
	public String writeView(@AuthenticationPrincipal LoginUserDetailsDto loginUser, Model model) {
		if (loginUser != null)
			model.addAttribute("loginName", loginUser.getName());

		return "/board/writeView";
	}

	@PostMapping("/write")
	public String writeBoard(@ModelAttribute BoardDto dto){
		service.save(dto);

		return "redirect:/board/list";
	}

	@GetMapping("/update/{seqNo}")
	public String updateView(
		@AuthenticationPrincipal LoginUserDetailsDto loginUser,
		@PathVariable("seqNo") Long seqNo,
		@PageableDefault(page=1) Pageable pageable,
		@RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		Model model
	) {
		BoardDto dto = service.findById(seqNo);

		model.addAttribute("board", dto);
		model.addAttribute("searchTheme", searchWord.equals("") ? "boardTitle" : searchTheme);
		model.addAttribute("searchWord", searchWord);
		if (loginUser != null)
			model.addAttribute("loginName", loginUser.getName());

		return "/board/updateView";
	}


	@PostMapping("/update/{seqNo}")
	public String updateBoard(
		@PathVariable("seqNo") Long seqNo,
		@ModelAttribute BoardDto dto,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		RedirectAttributes redirectAttributes
	) {
		service.update(dto);
		redirectAttributes.addAttribute("seqNo", seqNo);
		redirectAttributes.addAttribute("searchTheme", searchWord.equals("") ? "boardTitle" : searchTheme);
		redirectAttributes.addAttribute("searchWord", searchWord);

		return "redirect:/board/detail/{seqNo}";
	}

	@GetMapping("/download")
	public void downLoadFile(@RequestParam(name="seqNo") Long seqNo, HttpServletResponse resp) throws IOException {
		service.download(seqNo, resp);

		return ;
	}

	@GetMapping("/delete/{seqNo}")
	public String deleteBoard(
		@PathVariable("seqNo") Long seqNo,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		RedirectAttributes redirectAttributes
	) {
		service.deleteById(seqNo);
		redirectAttributes.addAttribute("searchTheme", searchWord.equals("") ? "boardTitle" : searchTheme);
		redirectAttributes.addAttribute("searchWord", searchWord);

		return "redirect:/board/list";
	}

	@GetMapping("/file/delete")
	public String deleteFile(
		@RequestParam(name="seqNo") Long seqNo,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		RedirectAttributes redirectAttributes
	) {
		service.deleteFile(seqNo);

		redirectAttributes.addAttribute("seqNo", seqNo);
		redirectAttributes.addAttribute("searchTheme", searchWord.equals("") ? "boardTitle" : searchTheme);
		redirectAttributes.addAttribute("searchWord", searchWord);

		return "redirect:/board/detail/{seqNo}";
	}
}
