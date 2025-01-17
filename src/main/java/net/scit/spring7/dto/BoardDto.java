package net.scit.spring7.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.scit.spring7.entity.BoardEntity;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BoardDto {
	private Long seqNo;
	private String writer;
	private String title;
	private String content;
	private Integer hitCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	private MultipartFile uploadFile;
	private String originalFileName;
	private String savedFileName;

	private Integer replyCount;

	public static BoardDto toDto(BoardEntity entity) {
		return BoardDto.builder()
			.seqNo(entity.getSeqNo())
			.writer(entity.getWriter())
			.title(entity.getTitle())
			.content(entity.getContent())
			.hitCount(entity.getHitCount())
			.createdAt(entity.getCreatedAt())
			.updatedAt(entity.getUpdatedAt())
			.originalFileName(entity.getOriginalFileName())
			.savedFileName(entity.getSavedFileName())
			.replyCount(entity.getReplyCount())
			.build();
	}
}
