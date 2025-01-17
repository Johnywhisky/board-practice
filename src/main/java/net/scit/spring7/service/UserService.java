package net.scit.spring7.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.scit.spring7.dto.LoginUserDetailsDto;
import net.scit.spring7.entity.UserEntity;
import net.scit.spring7.repository.UserRepository;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository repo;
	private final BCryptPasswordEncoder bcryptPwdEncoder;

	@Transactional
	public Boolean save(LoginUserDetailsDto userDto) {
		UserEntity entity = UserEntity.toEntity(userDto);
		entity.setUserPwd(bcryptPwdEncoder.encode(entity.getUserPwd()));

		repo.save(entity);

		return repo.existsById(entity.getUserId());
	}

	public Boolean idDupCheck(String userId) {
		Boolean result = repo.existsById(userId);

		return result;
	}

	public void withdraw(String userId) {
		repo.deleteById(userId);

		return ;
	}
}
