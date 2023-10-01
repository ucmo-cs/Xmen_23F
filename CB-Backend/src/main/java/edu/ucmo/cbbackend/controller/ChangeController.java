// package edu.ucmo.cbbackend.controller;

// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RestController;

// import edu.ucmo.cbbackend.model.ChangeRequest;
// import edu.ucmo.cbbackend.repository.ChangeRepository;


// @RestController
// @CrossOrigin
// public class ChangeController {
    
//     private final ChangeRepository changeRepository;
//     private final ChangeRepository changeDetailsService;

//     public ChangeController(ChangeRepository changeRepository, ChangeRepository changeDetailsService) {
//         this.changeRepository = changeRepository;
//         this.changeDetailsService = changeDetailsService;
//     }

//     @GetMapping
//     public ChangeRequest getChangeByID(@PathVariable Long id) {
//         System.out.println("id = " + id);
//         return changeRepository.findByID(id);
//     }



// }
