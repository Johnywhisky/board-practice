package net.scit.spring7.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.scit.spring7.dto.LoginUserDetailsDto;
import net.scit.spring7.entity.UserEntity;
import net.scit.spring7.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
	private final UserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		UserEntity entity = repo.findById(userId).orElseThrow(() -> {
			throw new UsernameNotFoundException("");
		});

		return LoginUserDetailsDto.toDto(entity);
	}

}
