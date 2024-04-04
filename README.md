# Spring boot micro-service project 
This micro-service is designed to be developed using spring reactive framework webflux. 
Do not write any non-reactive or blocking code. 

# Enable Token Validation
Set `jwt.verify.token` to `true` to enable JWT token verification in incoming requests. 

# Enable api security
Please refer to `https://github.com/philips-internal/itaap-ms-dev-base#security-in-micro-service-projects` 


# Enable/disable json logging 
Set `sprng.applicatino.profile` to `dev` for non json logging

# Push auditing data 
Please refer to `https://github.com/philips-internal/itaap-audit-client#readme`

# Exception handling 
Update `ApplicationServiceErrors` for managing all exceptions. 
Please refer to `https://dev.azure.com/PhilipsAgile/55.0%20Enterprise%20Architecture/_wiki/wikis/55.0-Enterprise-Architecture.wiki/5964/Micro-services-Guidelines-(WIP)`

# Add any dependencies 
Please refer to `https://dev.azure.com/PhilipsAgile/55.0%20Enterprise%20Architecture/_wiki/wikis/55.0-Enterprise-Architecture.wiki/5964/Micro-services-Guidelines-(WIP)?anchor=dependency-management` 

# Open api documentation 
/api-docs - Yaml definition 
/swagger-ui.html - Swagger UI for API documentation

# Actuator Endpoint (Basic auth enabled)
/actuator

# Verification post-deployment (Newman tests)
* Newman test suite has been added for integration testing.
* The integration test will be executed post deployment and report will be generated.
* File 'newman-report-template.yaml' contains required tasks to setup, run and publish newman tests.
* Postman collection (collection and environment) comes with testcases for actuator endpoints (health, metrics, prometheus).
* Any new testcases can be added in the aforementioned postman collection.