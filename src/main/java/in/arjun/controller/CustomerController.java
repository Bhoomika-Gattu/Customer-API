package in.arjun.controller;

import in.arjun.model.entity.Customer;
import in.arjun.model.request.CustomerRequest;
import in.arjun.model.request.LoginRequest;
import in.arjun.model.request.ResetPasswordRequest;
import in.arjun.service.CustomerService;
import in.arjun.service.EmailService;
import in.arjun.service.PasswordResetService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/save")
    public Customer addCustomer(@RequestBody CustomerRequest customerRequest){
        return customerService.saveCustomer(customerRequest);
    }

    @PostMapping("/find-customer")
    public Customer findCustomerByEmailAndPassword(@RequestBody LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        Optional<Customer> customerByEmailAndPassword = customerService.getCustomerByEmailAndPassword(email, password);
        if (customerByEmailAndPassword.isPresent()){
            return customerByEmailAndPassword.get();
        }
        return null;
    }

    @PostMapping("/find-email/{email}")
    public boolean findCustomerByEmail(@PathVariable String email){
        Optional<Customer> customerByEmail = customerService.findCustomerByEmail(email);
        String resetUrl = passwordResetService.createPasswordResetToken(email);
        if (customerByEmail.isPresent()){
        try {
            emailService.sendResetPasswordEmail(email, resetUrl);
            return true;
        } catch (MessagingException e) {

            return false;
        }
    } else {
        return false;
    }
    }

    @PostMapping("/reset-password")
    public boolean passwordReset(@RequestBody ResetPasswordRequest resetPasswordRequest){

        boolean customerByToken = customerService.findCustomerByToken(resetPasswordRequest);
        if (customerByToken)
            return true;
        return false;

    }
}
