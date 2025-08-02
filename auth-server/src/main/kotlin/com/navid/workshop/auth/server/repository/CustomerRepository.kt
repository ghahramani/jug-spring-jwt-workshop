package com.navid.workshop.auth.server.repository

import com.navid.workshop.auth.server.domain.Customer
import org.springframework.data.repository.ListCrudRepository
import java.util.*

interface CustomerRepository : ListCrudRepository<Customer, UUID>