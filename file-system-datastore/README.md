# FileSystemDataStore

You can use the FileSystemDataStore to store and load data in a file system. You can use it in a multi-user system, where multiple users access the data concurrently.

* There is only one read or write operation at any given moment. No two threads are allowed to writer/read within the same location at the same time.
* Can read or write multiple files as part of the same operation
* There can be multiple locations within a base directory. There is exactly one concrete FileSystemDataStore for each location.
* You can get the FileSystemDataStore for a certain location with FileSystemAccess#dataStoreFor(Location)

## TODO / Open points

* When performing multiple operations, what should we do when one throws an exception?
