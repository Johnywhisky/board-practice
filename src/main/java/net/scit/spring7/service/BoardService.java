package net.scit.spring7.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.scit.spring7.dto.BoardDto;
import net.scit.spring7.entity.BoardEntity;
import net.scit.spring7.repository.BoardRepository;
import net.scit.spring7.utility.FileService;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService extends AuthenticationService {
	private final BoardRepository repo;
	private final int PAGEOFFSET = 1;

	@Value("${spring.servlet.multipart.location}")
	private String uploadFilePath;

	public Page<BoardDto> findAll(String searchTheme, String searchWord, Pageable pageable, Integer pageSize) {
		int pageNumber = pageable.getPageNumber() - PAGEOFFSET;
		System.out.println(pageNumber);
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		PageRequest pageReq = PageRequest.of(pageNumber, pageSize, sort);

		Page<BoardEntity> paginatedEntities = switch (searchTheme) {
			case "boardTitle" -> repo.findByTitleContains(searchWord, pageReq);
			case "boardWriter" -> repo.findByWriterContains(searchWord, pageReq);
			case "boardContent" -> repo.findByContentContains(searchWord, pageReq);
			default -> repo.findAll(pageReq);
		};

		return paginatedEntities.map(BoardDto::toDto);
	}

	public BoardDto findById(Long seqNo) {

		return repo.findById(seqNo).map(BoardDto::toDto).orElse(null); 
	}

	@Transactional
	public void save(BoardDto dto) {
		if (!dto.getUploadFile().isEmpty()) {
			String savedFileName = FileService.saveFileAndgetSavedFileName(dto.getUploadFile(), uploadFilePath);
			dto.setSavedFileName(savedFileName);
			dto.setOriginalFileName(dto.getUploadFile().getOriginalFilename());
		}
		repo.save(BoardEntity.toEntity(dto));

		return ;
	}

	@Transactional
	public void update(BoardDto dto) {
		LocalDateTime now = LocalDateTime.now();
		BoardEntity entity = repo.findById(dto.getSeqNo()).orElseThrow(RuntimeException::new);
		if (!entity.getWriter().equals(this.getUserId())) return ;

		String newFileName = dto.getUploadFile().isEmpty() ? null : dto.getUploadFile().getOriginalFilename();
		log.info("newFileName: {}", newFileName);
		String savedFileName = null;
		String oldFileName = entity.getSavedFileName();
		log.info("oldFileName: {}", oldFileName);

		if (newFileName != null) {
			savedFileName = FileService.saveFileAndgetSavedFileName(dto.getUploadFile(), uploadFilePath);
			entity.setOriginalFileName(dto.getUploadFile().getOriginalFilename());
			entity.setSavedFileName(savedFileName);

			if (oldFileName != null) {
				String fullPath = uploadFilePath + "/" + oldFileName; 
				FileService.deleteFile(fullPath);
			}
		}
		entity.setTitle(dto.getTitle());
		entity.setContent(dto.getContent());
		entity.setUpdatedAt(now);

		return ;
	}

	@Transactional
	public void incrementHitCount(Long seqNo) {
		BoardEntity entity = repo.findById(seqNo).orElseThrow(RuntimeException::new);

		if (entity.getWriter().equals(this.getUserId())) return ;

		entity.setHitCount(entity.getHitCount() + 1);				

		return ;
	}

	@Transactional
	public void deleteById(Long seqNo) {
		BoardEntity entity = repo.findById(seqNo).orElseThrow(RuntimeException::new);

		if (entity.getSavedFileName() != null) {
			String fullPath = uploadFilePath + "/" + entity.getSavedFileName(); 
			FileService.deleteFile(fullPath);
		}
		repo.deleteById(seqNo);

		return ;
	}

	@Transactional
	public void deleteFile(Long seqNo) {
		BoardEntity entity = repo.findById(seqNo).orElseThrow(RuntimeException::new);
		if (entity.getSavedFileName() == null) {return ;}

		String fullPath = uploadFilePath + "/" + entity.getSavedFileName(); 
		if (FileService.deleteFile(fullPath)) {
			entity.setOriginalFileName(null);
			entity.setSavedFileName(null);
		}
		return ;
	}

	public void download(Long seqNo, HttpServletResponse resp) throws IOException{
		BoardEntity entity = repo.findById(seqNo).orElseThrow(RuntimeException::new);

		String dispositionFileName = URLEncoder.encode(entity.getOriginalFileName(), StandardCharsets.UTF_8.toString());
		FileInputStream inboundFile = new FileInputStream(uploadFilePath + "/" + entity.getSavedFileName());

		ServletOutputStream outboundFile = resp.getOutputStream();
		resp.setHeader("Content-Disposition", "attachment;filename=" + dispositionFileName);

		FileCopyUtils.copy(inboundFile, outboundFile);

		outboundFile.close();

		return ;
	}
}
