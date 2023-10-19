package edu.ucmo.cbbackend.service;

import edu.ucmo.cbbackend.model.ChangeRequest;
import edu.ucmo.cbbackend.model.User;
import edu.ucmo.cbbackend.repository.ChangeRepository;
import org.springframework.stereotype.Service;

@Service
public class ChangeService {

    ChangeRepository changeRepository;


    public ChangeService(ChangeRepository changeRepository) {
        this.changeRepository = changeRepository;
    }

    public ChangeRequest findById(Long id){
        return changeRepository.findById(id).orElseThrow(() -> new RuntimeException("Change Request not found"));

    }

    public void deleteById(Long id){
        changeRepository.deleteById(id);
    }

    public void save(ChangeRequest changeRequest){
        changeRepository.save(changeRequest);
    }


}