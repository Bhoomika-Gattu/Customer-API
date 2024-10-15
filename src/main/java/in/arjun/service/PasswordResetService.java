package in.arjun.service;

import in.arjun.model.entity.Customer;
import in.arjun.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private CustomerRepository customerRepository;

    public String createPasswordResetToken(String userEmail) {
        // Generate a random token (you can store this in your DB)
        String token = UUID.randomUUID().toString();



        // Create the reset URL (adjust to your frontend URL)
        String resetUrl = "http://localhost:4200/reset-password?token=" + token;

        Optional<Customer> customerByEmail = customerRepository.findByEmail(userEmail);
        if (customerByEmail.isPresent()) {
            Customer customer = customerByEmail.get();
            customer.setToken(token);
            customerRepository.save(customer);
            return resetUrl;
        }

        // TODO: Save the token with the user’s email in your database to verify later
        return null;

    }
}
