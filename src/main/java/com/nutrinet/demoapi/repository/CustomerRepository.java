package com.nutrinet.demoapi.repository;

import com.nutrinet.demoapi.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CustomerRepository extends MongoRepository<Customer, String> {
    Customer findCustomerById(String customerId);
    Customer findCustomerByEmail(String email);
    Customer findCustomerByNombreUsuario(String nombreUsuario);

}
