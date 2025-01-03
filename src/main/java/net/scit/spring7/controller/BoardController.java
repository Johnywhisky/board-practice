package net.scit.spring7.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.scit.spring7.dto.BoardDto;
import net.scit.spring7.service.BoardService;


@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
	private final BoardService service;

	@GetMapping("/list")
	public String listView(
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		Model model
	) {
		List<BoardDto> boardDtoList = service.findAll(searchTheme, searchWord);
		model.addAttribute("boardList", boardDtoList);
		model.addAttribute("searchTheme", searchWord.equals("") ? "boardTitle" : searchTheme);
		model.addAttribute("searchWord", searchWord);

		return "/board/listView";
	}

	@GetMapping("/write")
	public String writeView() {

		return "/board/writeView";
	}

	@GetMapping("/detail/{seqNo}")
	public String detailView(
		@PathVariable("seqNo") Long seqNo,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		Model model
	) {
		BoardDto dto = service.findById(seqNo);
		service.incrementHitCount(seqNo);
		model.addAttribute("searchTheme", searchWord.equals("") ? "boardTitle" : searchTheme);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("board", dto);

		return "/board/detailView";
	}

	@PostMapping("/write")
	public String writeBoard(@ModelAttribute BoardDto dto) {
		service.save(dto);

		return "redirect:/board/list";
	}

	@GetMapping("/update/{seqNo}")
	public String updateView(
		@PathVariable("seqNo") Long seqNo,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		Model model
	) {
		model.addAttribute("board", service.findById(seqNo));
		model.addAttribute("searchTheme", searchWord.equals("") ? "boardTitle" : searchTheme);
		model.addAttribute("searchWord", searchWord);

		return "/board/updateView";
	}

	@PostMapping("/update/{seqNo}")
	public String updateBoard(
		@PathVariable("seqNo") Long seqNo,
		@RequestParam(name="searchTheme", defaultValue="boardTitle") String searchTheme,
		@RequestParam(name="searchWord", defaultValue="") String searchWord,
		@ModelAttribute BoardDto dto,
		RedirectAttributes redirectAttributes
	) {
		service.update(seqNo, dto);
		redirectAttributes.addAttribute("seqNo", seqNo);
		redirectAttributes.addAttribute("searchTheme", searchWord.equals("") ? "boardTitle" : searchTheme);
		redirectAttributes.addAttribute("searchWord", searchWord);

		return "redirect:/board/detail/{seqNo}";
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

}
