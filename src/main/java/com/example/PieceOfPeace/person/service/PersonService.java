package com.example.PieceOfPeace.person.service;

import com.example.PieceOfPeace.file.service.FileService; // FileSystemStorageService -> FileService
import com.example.PieceOfPeace.person.dto.request.PersonRegisterRequest;
import com.example.PieceOfPeace.person.dto.request.PersonUpdateRequest;
import com.example.PieceOfPeace.person.dto.response.PersonResponse;
import com.example.PieceOfPeace.person.entity.Person;
import com.example.PieceOfPeace.person.repository.PersonRepository;
import com.example.PieceOfPeace.user.entity.User;
import com.example.PieceOfPeace.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    @Transactional
    public void registerPerson(PersonRegisterRequest request, MultipartFile profileImage, String writerEmail) throws IOException {
        personRepository.findByVectorId(request.vectorId()).ifPresent(p -> {
            throw new IllegalArgumentException("이미 등록된 인물입니다.");
        });

        User writer = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("작성자 정보를 찾을 수 없습니다."));

        String profileImageUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageUrl = fileService.uploadFile(profileImage);
        }

        Person person = Person.builder()
                .vectorId(request.vectorId())
                .name(request.name())
                .relationship(request.relationship())
                .profileImageUrl(profileImageUrl)
                .writer(writer)
                .build();

        personRepository.save(person);
    }

    public PersonResponse findPersonByVectorId(String vectorId) {
        Person person = personRepository.findByVectorId(vectorId)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물을 찾을 수 없습니다."));
        return PersonResponse.from(person);
    }

    public List<PersonResponse> findMyPeople(String writerEmail) {
        User writer = userRepository.findByEmail(writerEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        List<Person> people = personRepository.findAllByWriterId(writer.getId());

        return people.stream()
                .map(PersonResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePerson(Long personId, PersonUpdateRequest request, String userEmail) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물을 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        if (!Objects.equals(person.getWriter().getId(), user.getId())) {
            throw new SecurityException("인물 정보를 수정할 권한이 없습니다.");
        }

        person.update(request.name(), request.relationship());
    }

    @Transactional
    public void deletePerson(Long personId, String userEmail) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new IllegalArgumentException("해당 인물을 찾을 수 없습니다."));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("사용자 정보를 찾을 수 없습니다."));

        if (!Objects.equals(person.getWriter().getId(), user.getId())) {
            throw new SecurityException("인물 정보를 삭제할 권한이 없습니다.");
        }

        fileService.deleteFile(person.getProfileImageUrl());

        personRepository.delete(person);
    }
}
