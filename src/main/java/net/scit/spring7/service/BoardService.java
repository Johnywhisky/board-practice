package net.scit.spring7.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.scit.spring7.dto.BoardDto;
import net.scit.spring7.entity.BoardEntity;
import net.scit.spring7.repository.BoardRepository;


@Service
@RequiredArgsConstructor
public class BoardService extends AuthenticationConfig {
	private final BoardRepository repo;

	public List<BoardDto> findAll(String searchTheme, String searchWord) {
		List<BoardEntity> entities;

		switch (searchTheme) {
			case "boardTitle":
				entities = repo.findByTitleContains(searchWord, Sort.by(Sort.Direction.DESC, "createdAt")); break;
			case "boardWriter":
				entities = repo.findByWriterContains(searchWord, Sort.by(Sort.Direction.DESC, "createdAt")); break;
			case "boardContent":
				entities = repo.findByContentContains(searchWord, Sort.by(Sort.Direction.DESC, "createdAt")); break;
			default:
				entities = repo.findAll(Sort.by(Sort.Direction.DESC, "createdAt")); break;
		}

		return entities.stream().map(BoardDto::toDto).toList();
	}

	public BoardDto findById(Long seqNo) {
		Optional<BoardEntity> optEntity = repo.findById(seqNo);

		if (optEntity.isEmpty()) return null;

		return BoardDto.toDto(optEntity.get()); 
	}

	public void save(BoardDto dto) {
		repo.save(BoardEntity.toEntity(dto));

		return ;
	}

	@Transactional
	public void incrementHitCount(Long seqNo) {
		Optional<BoardEntity> optEntity = repo.findById(seqNo);
		if (optEntity.isEmpty()) return ;

		BoardEntity entity = optEntity.get();
		if (entity.getWriter().equals(this.getUserId())) return ;

		entity.setHitCount(entity.getHitCount() + 1);

		return ;
	}

	@Transactional
	public void update(Long seqNo, BoardDto dto) {
		LocalDateTime now = LocalDateTime.now();

		Optional<BoardEntity> optEntity = repo.findById(seqNo);
		if (optEntity.isEmpty()) return ;

		BoardEntity entity = optEntity.get();
		String userId = this.getUserId();
		if (entity.getWriter().equals(userId)) return ;

		entity.setTitle(dto.getTitle());
		entity.setContent(dto.getContent());
		entity.setUpdatedAt(now);

		return ;
	}

	@Transactional
	public void deleteById(Long seqNo) {
		Optional<BoardEntity> optEntity = repo.findById(seqNo);
		if (optEntity.isEmpty()) return ;

		repo.deleteById(seqNo);

		return ;
	}
}
