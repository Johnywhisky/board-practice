package net.scit.spring7.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.scit.spring7.entity.ReplyEntity;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReplyDto {
	private Long seqNo;
	private String writer;
	private String content;
	private LocalDateTime createdAt;
	private Long boardSeqNo;

	public static ReplyDto toDto(ReplyEntity entity) {
		return ReplyDto.builder()
			.seqNo(entity.getSeqNo())
			.writer(entity.getWriter())
			.content(entity.getContent())
			.createdAt(entity.getCreatedAt())
			.build();
	}
}
