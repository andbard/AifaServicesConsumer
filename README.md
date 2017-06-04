# AifaServicesConsumer
A consumer for accessing the AIFA drug archive services.

This application aims to adopt some of the best practices in Android development. 
In particular, exploiting <a href="https://github.com/google/dagger">Dagger 2</a>
and <a href="https://github.com/ReactiveX/RxJava">RxJava 2</a> 
this application implements the Model View Presenter pattern delegating the Activity to subscribe/unsubscribe to/from a 
cached version of an Observable, owned by the Presenter, emitting the requested data, thus surviving to configuration changes
and preserving the request state. The Presenter, in turns, has the ability to release, if required, the un-cached version of the Observable,
thus avoiding waste of resources.
