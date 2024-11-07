package in.arjun.service;


import in.arjun.model.entity.Customer;
import in.arjun.model.request.CustomerRequest;
import in.arjun.model.request.ResetPasswordRequest;
import in.arjun.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(CustomerRequest customerRequest){

        Optional<Customer> optionalCustomer = customerRepository.findByEmail(customerRequest.getEmail());
        if (optionalCustomer.isPresent()){
            return optionalCustomer.get();
        }

        Customer customer=Customer.builder()
                .name(customerRequest.getName())
                .email(customerRequest.getEmail())
                .phoneNumber(customerRequest.getPhoneNumber())
                .build();
        return customerRepository.save(customer);
    }

    public Optional<Customer> getCustomerByEmailAndPassword(String email,String password){
        return customerRepository.findByEmailAndPassword(email, password);
    }

    public Optional<Customer> findCustomerByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public boolean findCustomerByToken(ResetPasswordRequest resetPasswordRequest){
        Optional<Customer> customerByToken = customerRepository.findByToken(resetPasswordRequest.getToken());
        if (customerByToken.isPresent()){
            Customer customer = customerByToken.get();
            customer.setPassword(resetPasswordRequest.getNewPassword());
            customer.setToken(resetPasswordRequest.getToken());
            Customer savedCustomer = customerRepository.save(customer);
            if (savedCustomer.getId() != null){
                return true;
            }
            return false;
        }
        return false;
    }

    public Customer getCustomerById(Long id){
        Optional<Customer> customerById = customerRepository.findById(id);
        if (customerById.isPresent())
            return customerById.get();
        return null;
    }
}
