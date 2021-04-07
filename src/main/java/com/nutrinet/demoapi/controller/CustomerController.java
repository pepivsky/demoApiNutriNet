package com.nutrinet.demoapi.controller;

import com.nutrinet.demoapi.model.Customer;
import com.nutrinet.demoapi.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CustomerController {

    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping(value = "/")
    public List<Customer> getAllCustomers() {
        logger.info("Getting all customers");
        return customerRepository.findAll();
    }

    @GetMapping(value = "/{customerId}")
    public Customer getCustomerById(@PathVariable String customerId) {
        logger.info("Getting movie with ID: {}", customerId);
        return customerRepository.findCustomerById(customerId);
    }

    @PostMapping(value = "/create")
    public Customer addCustomer(@RequestBody Customer customer) {
        if (checkifEmailOrUsernameExist(customer)) {
            //return status(HttpStatus.FORBIDDEN).body("usuario o email ya existe");
            return new Customer();
        } else {
            logger.info("Saving customer");
            customer.setImc(calcularIMC(customer));
            customer.setGeb(calcularGEB(customer));
            return customerRepository.save(customer);
        }
    }

    @PutMapping(value = "/update/{customerId}")
    public Customer updateMovie(@PathVariable String customerId, @RequestBody Customer customer) {
        logger.info("Updating customer with ID: {}", customerId);
        customer.setId(customerId);
        customer.setFechaActualizacion(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @DeleteMapping(value = "/delete/{customerId}")
    public String deleteCustomer(@PathVariable String customerId) {
        logger.info("Deleting customer with id: {}", customerId);

        if (customerRepository.existsById(customerId)) {
            try {
                customerRepository.deleteById(customerId);
                return 0 + "borrado exitosamente";
            } catch (Exception e) {
                return 1 + "Ha ocurrido un error";
            }
        } else {
            return 1 + " Este usuario no existe";
        }
        //logger.info(String.valueOf(customerRepository.existsById(customerId)));
    }

    public double calcularIMC(Customer customer) {
        Double imc = (customer.getPeso() / Math.pow(customer.getEstatura(), 2));
        BigDecimal bd = new BigDecimal(imc).setScale(2, RoundingMode.HALF_UP);
        double roundedIMC = bd.doubleValue();
        return roundedIMC;
    }

    public double calcularGEB(Customer customer) {
        double geb = 66.473 + (13.751 * customer.getPeso()) + (5.003 * (customer.getEstatura() * 100)) - (6.7550 * customer.getEdad());
        logger.info(geb + ""); //formula harris benedict
        return geb;
    }

    public boolean checkifEmailOrUsernameExist(Customer customer) {
        Customer customerWithEmail = customerRepository.findCustomerByEmail(customer.getEmail());
        Customer customerWithUsername = customerRepository.findCustomerByNombreUsuario(customer.getNombreUsuario());

        if (customerWithEmail != null && customerWithUsername != null) {
            if (customerWithEmail.getEmail().equals(customer.getEmail()) ||
                    customerWithUsername.getNombreUsuario().equals(customer.getNombreUsuario())) {
                logger.info("Ya existe este username o email");
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
