package net.scit.spring7.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.scit.spring7.dto.BoardDto;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="boards")
//@EntityListeners(AuditingEntityListener.class)
public class BoardEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="seq_no")
	private Long seqNo;

	@Column(nullable=false)
	private String writer;

	@Column(nullable=false)
	private String title;

	@Column(nullable=false)
	private String content;

	@Column(name="hit_count")
	private Integer hitCount;

	@Column(name="created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column(name="updated_at")
	private LocalDateTime updatedAt;

	@Column(name="original_file_name")
	private String originalFileName;

	@Column(name="saved_file_name")
	private String savedFileName;

	@Formula("(SELECT count(*) FROM replys r WHERE seq_no = r.board_seq_no)")
	private Integer replyCount;

	@PrePersist
	public void prePersist() {
		this.hitCount = 0;
	}

	public static BoardEntity toEntity(BoardDto dto) {
		return BoardEntity.builder()
			.seqNo(dto.getSeqNo())
			.writer(dto.getWriter())
			.title(dto.getTitle())
			.content(dto.getContent())
			.hitCount(dto.getHitCount())
			.originalFileName(dto.getOriginalFileName())
			.savedFileName(dto.getSavedFileName())
			.build();
	}
}
