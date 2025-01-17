package net.scit.spring7.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.scit.spring7.dto.ReplyDto;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="replys")
public class ReplyEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="seq_no")
	private Long seqNo;

	@Column(nullable=false)
	private String writer;

	@Column(nullable=false)
	private String content;

	@Column(name="created_at")
	@CreationTimestamp
	private LocalDateTime createdAt;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="board_seq_no", referencedColumnName="seq_no")
	private BoardEntity board;

	public static ReplyEntity toEntity(ReplyDto dto) {
		return ReplyEntity.builder()
			.seqNo(dto.getSeqNo())
			.writer(dto.getWriter())
			.content(dto.getContent())
			.build();
	}
}
