package net.scit.spring7.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.scit.spring7.dto.ReplyDto;
import net.scit.spring7.entity.ReplyEntity;
import net.scit.spring7.repository.BoardRepository;
import net.scit.spring7.repository.ReplyRepository;


@Service
@RequiredArgsConstructor
public class ReplyService extends AuthenticationService {
	private final ReplyRepository repo;
	private final BoardRepository boardRepo;

	public List<ReplyDto> findAllByBoardSeqNo(Long boardSeqNo) {

		return repo.findByBoard(boardRepo.findById(boardSeqNo), Sort.by(Sort.Direction.ASC, "createdAt"))
			.stream().map(ReplyDto::toDto).toList();
	}

	public ReplyDto save(ReplyDto dto) {
		ReplyEntity entity = ReplyEntity.toEntity(dto);
		entity.setBoard(
			boardRepo.findById(dto.getBoardSeqNo()).orElseThrow(RuntimeException::new)
		);
		entity.setWriter(this.getUserId());

		return ReplyDto.toDto(repo.save(entity));
	}

	@Transactional
	public void update(Long seqNo, ReplyDto dto) {
		ReplyEntity entity = repo.findById(seqNo).orElseThrow(RuntimeException::new);
		entity.setContent(dto.getContent());

		return ;
	}

	public void delete(Long seqNo) {
		if (repo.existsById(seqNo))
			repo.deleteById(seqNo);

		return ;
	}
}
