# FAQ

## Why use single activity architecture?

The cost of screen switching and data transfer between activities is high, Single-Activity architecture can reduce the cost and improve the performance, which is suitable for some small and medium-sized projects.
However, a single activity is not a silver bullet and may not be suitable for all projects.


## What are the advantages over Jetpack Navigation?

There are some advantages as follows:
* There is no need to configure `NavGraph`
* Lifecycle is more reasonably
* API is easier to use


## Is this library stable and will be will it be maintained continuously ?

Fragivity is implemented based on navigation, which ensures the stability of basic capabilities;
Before there is no better alternative, the project will be maintained continuously. Maybe `Jetpack Compose` will become a good alternative in the future


## Is it supported in Java ?

Fragivity will provide Java side compatibility, but kotlin is recommended


## Support for DI ?

Fragivity does not affect the use of fragment for any three-party libraries, and DI library such as Koin or hilt can be used normally
