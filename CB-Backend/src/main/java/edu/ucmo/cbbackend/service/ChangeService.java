package edu.ucmo.cbbackend.service;

import edu.ucmo.cbbackend.dto.response.ChangeRequestHttpResponse;
import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.ChangeRepository;
import edu.ucmo.cbbackend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ChangeService {

    ChangeRepository changeRepository;
    private final UserRepository userRepository;
    public ChangeService(ChangeRepository changeRepository,
                         UserRepository userRepository) {
        this.changeRepository = changeRepository;
        this.userRepository = userRepository;
    }

    public ChangeRequest findById(Long id) {
        return changeRepository.findById(id).orElseThrow(() -> new RuntimeException("Change Request not found"));

    }

    public void deleteById(Long id) {
        changeRepository.deleteById(id);
    }

    public void save(ChangeRequest changeRequest) {
        changeRepository.save(changeRequest);
    }

    public Page<ChangeRequestHttpResponse> findAllSortByDate(int page, int size) {
        Page<ChangeRequestHttpResponse> changeRequestHttpResponses = changeRepository.findAll(PageRequest.of(page, size, Sort.by("dateUpdated").descending())).map(ChangeRequestHttpResponse::new);
        return changeRequestHttpResponses;
    }

    public Page<ChangeRequestHttpResponse> findAllByUserIdAndSortByDate(int page, int size, String username) {
        User user = userRepository.findByUsername(username);

        Page<ChangeRequest> changeRequestHttpResponses = changeRepository.findAllByAuthor(user, PageRequest.of(page, size, Sort.by("dateUpdated").descending()));
        return changeRequestHttpResponses.map(ChangeRequestHttpResponse::new);
    }


}