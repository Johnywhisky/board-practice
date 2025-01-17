package net.scit.spring7.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.scit.spring7.entity.UserEntity;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class LoginUserDetailsDto implements UserDetails {
	private static final long serialVersionUID = -8928674586812538655L;

	private String userId;
	private String userPwd;
	private String name;
	private String email;
	private String roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		return List.of(new SimpleGrantedAuthority(this.roles));
	}

	@Override
	public String getUsername() {return this.userId;}
	@Override
	public String getPassword() {return this.userPwd;}

	public static LoginUserDetailsDto toDto(UserEntity entity) {

		return LoginUserDetailsDto.builder()
			.userId(entity.getUserId())
			.userPwd(entity.getUserPwd())
			.name(entity.getName())
			.roles(entity.getRoles())
			.build();
	}
}
