package edu.ucmo.cbbackend.service;

import edu.ucmo.cbbackend.dto.request.ChangeRequestBodyDTO;
import edu.ucmo.cbbackend.dto.response.ChangeRequestHttpResponseDTO;
import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.ChangeRepository;
import edu.ucmo.cbbackend.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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

 public Page<ChangeRequestHttpResponseDTO> findAllSortByDate(int page, int size, boolean showUsernames) {
    Page<ChangeRequest> changeRequests = changeRepository.findAll(PageRequest.of(page, size, Sort.by("dateUpdated").descending()));
    return changeRequests.map(changeRequest -> toDto(changeRequest, showUsernames));
}

    public Page<ChangeRequestHttpResponseDTO> findAllByUserIdAndSortByDate(int page, int size, String username, boolean showUsernames) {
        User user = userRepository.findByUsername(username);

        Page<ChangeRequest> changeRequestHttpResponses = changeRepository.findAllByAuthor(user, PageRequest.of(page, size, Sort.by("dateUpdated").descending()));
         return changeRequestHttpResponses.map(changeRequest -> toDto(changeRequest, showUsernames));
    }

    public ChangeRequestHttpResponseDTO toDto (ChangeRequest changeRequest, boolean showUsername){
        return new ChangeRequestHttpResponseDTO(changeRequest.getId(),
                changeRequest.getAuthor().getId(),
                changeRequest.getChangeType(),
                changeRequest.getApplicationId(),
                changeRequest.getDescription(),
                changeRequest.getReason(),
                changeRequest.getDateCreated(),
                changeRequest.getDateUpdated(),
                changeRequest.getTimeWindowStart(),
                changeRequest.getTimeWindowEnd(),
                changeRequest.getTimeToRevert(),
                changeRequest.getApproveOrDeny(),
                changeRequest.getState(),
                changeRequest.getImplementer(),
                showUsername ? Optional.of(changeRequest.getAuthor().getUsername()) : Optional.empty(),
                changeRequest.getRiskLevel(),
                changeRequest.getRoles()
        );

    }

    public boolean changeRequestDateValidation(ChangeRequestBodyDTO changeRequestBodyDTO){
        return  changeRequestBodyDTO.getTimeWindowStart().after(changeRequestBodyDTO.getTimeWindowEnd());
    }

    }

