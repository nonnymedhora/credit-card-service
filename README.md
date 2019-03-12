# BaWaZ credit-card-service

A Secure Credit-Card service providing Validation, Encryption & Storage [MASKED] 

A secure RESTful service application interacting via VPN with Salesforce

The Credit Card Validation is handled via Luhn's Algorithm
Credit Card Encryption / Decryption is handled via BouncyCastle JCE using 256-bit cypher-block size

Persistence is handled by JPA and Hibernate to a secure MySQL database
Database connection pooling is handled by the C3P0 library

The Application Server used is Apache CXF

The Application Domain objects referred within are from an external project
where these domain objects are generated using Dozer JAXB Mapping

LiquiBase version- controls the database

Application and Server configuration is externalized in properties files

