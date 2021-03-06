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
    public ResponseEntity<?> getCustomerById(@PathVariable String customerId) {
        logger.info("Getting customer with ID: {}", customerId);
        return (customerRepository.findCustomerById(customerId) != null) ? new ResponseEntity(customerRepository.findCustomerById(customerId), HttpStatus.ACCEPTED) : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1 No existe este cliente");
    }

    @PostMapping(value = "/create")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        if (checkifEmailOrUsernameExist(customer)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1 Ya existe este username o email");

        } else {
            logger.info("Saving customer");
            customer.setImc(calcularIMC(customer));
            customer.setGeb(calcularGEB(customer));
            //return new ResponseEntity<Customer>(customerRepository.save(customer), HttpStatus.OK);
            return new ResponseEntity<Customer>(customerRepository.save(customer), HttpStatus.OK);

        }
    }

    @PutMapping(value = "/update/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        if (customerRepository.existsById(customerId)) {
            logger.info("Updating customer with ID: {}", customerId);
            customer.setId(customerId);
            customer.setFechaActualizacion(LocalDateTime.now());
            customer.setImc(calcularIMC(customer));
            customer.setGeb(calcularGEB(customer));
            return new ResponseEntity<Customer>(customerRepository.save(customer), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1 No existe un cliente con el id:" + customerId);
        }
    }

    @DeleteMapping(value = "/delete/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable String customerId) {
        logger.info("Deleting customer with id: {}", customerId);

        if (customerRepository.existsById(customerId)) {
            try {
                customerRepository.deleteById(customerId);
                return new ResponseEntity<>("0 borrado", HttpStatus.OK);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("-1 Ha ocurrido un error");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("-1 No existe el usuario con el id:" + customerId);
        }
    }

    public double calcularIMC(Customer customer) {
        Double imc = (customer.getPeso() / Math.pow(customer.getEstatura(), 2));
        BigDecimal bd = new BigDecimal(imc).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
        } else if (customerWithEmail != null) {
            return customerWithEmail.getEmail().equals(customer.getEmail());
        } else if (customerWithUsername != null) {
            return customerWithUsername.getNombreUsuario().equals(customer.getNombreUsuario());
        } else {
            return false;
        }
    }

}
