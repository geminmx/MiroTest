# MiroTest

A web service (HTTP REST API) to work with widgets.

The service allows performing CRUD operations on widgets.
Identifiers of widgets generate automatically, z-Indexes are unique.

It is a Spring boot application, Maven used as a build tool.

Widgets are stored in HashMap. To avoid collisions and to perform all changes atomically used ReentrantReadWriteLock.

Coverage html report attached.

## Complications

**1. Pagination.** 

Not implemented.

**2. Filtering**

Main problem in this complication is to find a data structure in which 
the asymptotic complexity of the algorithm is on average less than O(n).

This problem could be solved if we use Range-tree to store our widgets.
Asymptotic complexity of filtering rectangles would be O(log^2(n)+k).
Coding and testing such a complex data structure takes a lot of time.

**3. Rate limiting**

Rate limits implemented to service with Bucket4j library.
Covered with tests.

**4. SQL database**

Not implemented.
