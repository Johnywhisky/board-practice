package net.scit.spring7.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.scit.spring7.dto.ReplyDto;
import net.scit.spring7.service.ReplyService;


@RequestMapping("/reply")
@RestController
@RequiredArgsConstructor
public class ReplyController {
	private final ReplyService service;

	@GetMapping("/list")
	public List<ReplyDto> list(@RequestParam(name="boardSeqNo") Long boardSeqNo) {

		return service.findAllByBoardSeqNo(boardSeqNo);
	}

	@PostMapping("/write")
	public ReplyDto writeReply(@ModelAttribute ReplyDto dto) {

		return service.save(dto);
	}
	
	@PostMapping("/{seqNo}/update")
	public void updateReply(@PathVariable("seqNo") Long seqNo, ReplyDto dto) {
		System.out.println(dto);
		service.update(seqNo, dto);

		return ;
	}
	@GetMapping("/{seqNo}/delete")
	public void delReply(@PathVariable("seqNo") Long seqNo) {
		service.delete(seqNo);

		return ;
	}
}
