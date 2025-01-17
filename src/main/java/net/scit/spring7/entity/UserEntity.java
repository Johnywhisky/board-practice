package net.scit.spring7.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.scit.spring7.dto.LoginUserDetailsDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name="users")
public class UserEntity {
	@Id
	@Column(name="user_id")
	private String userId;

	@Column(name="user_pwd", nullable=false)
	private String userPwd;

	@Column(name="name", nullable=false)
	private String name;

	@Column(unique=true)
	private String email;

	@Builder.Default
	private String roles = "ROLE_USER";

	@Builder.Default
	private Boolean enabled = true;

	public static UserEntity toEntity(LoginUserDetailsDto dto) {

		return UserEntity.builder()
			.userId(dto.getUserId())
			.userPwd(dto.getUserPwd())
			.name(dto.getName())
			.email(dto.getEmail())
			.build();
	}
}
