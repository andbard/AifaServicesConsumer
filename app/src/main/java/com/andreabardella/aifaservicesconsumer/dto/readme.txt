The usage of DTOs is here adopted only because the backend is developed by others.
Thus the backend services can be changed along with the web service response format.
Even if the format should change, it is likely that the delivered information won't change so much.
Leveraging on DTOs in the network data layer helps with separation of concerns.

References:
[.] http://stackoverflow.com/questions/21554977/should-services-always-return-dtos-or-can-they-also-return-domain-models
[.] http://guntherpopp.blogspot.it/2010/09/to-dto-or-not-to-dto.html
[.] http://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
