# simple-users

Simple-users is a library for basic user management for Spring applications. It is set up to work with Spring Security and to use Spring Data for storage of user objects.

## How to

The easiest way to use it, is to include its dependency and then let your Spring app scan its packages, by adding 
```
@ComponentScan({"edu.asu.diging.simpleusers"})
```
to your Java configuration class (or the equivalent XML component scan tag).

You can also separate controllers and business logic by separately scanning ```edu.asu.diging.simpleusers.core``` and ```edu.asu.diging.simpleusers.web```.

If you want to create required beans individually, you need to instantiate the following classes (or replace them with appropriate implementations):

* ```edu.asu.diging.simpleusers.core.service.impl.UserService```, returning its interface ```edu.asu.diging.simpleusers.core.service.IUserManager```
* ```edu.asu.diging.simpleusers.core.factory.impl.UserFactory```, returning ```edu.asu.diging.simpleusers.core.factory.IUserFactory```
* ```edu.asu.diging.simpleusers.core.config.impl.ConfigurationProviderImpl```, returning ```edu.asu.diging.simpleusers.core.config.ConfigurationProvider```

User objects are stored using Spring Data and the repository ```edu.asu.diging.simpleusers.core.data.UserRepository```.

Also, this library provides one controller to create new user accounts: ```edu.asu.diging.simpleusers.web.CreateAccountController```.

## Storing data

The user class is annotated using JPA annotations. All you need to do is to configure a data source, an entity mananger factory, a transaction provider, and an persistence exception translation processor.

## Securing your application

Simply configure Spring Security as you normally would, and let it find the user details service provided by this library through component scanning or provide it as a bean (if you choose to instantiate it yourself, you also need to provide a user factory instance).

## Registering new users

To register a new user, make sure the CreateAccountController is available in a context, then send your users to /register. Provide a register view (by default the CreateAccountController assumes that there is a view for 'register'). After succesfully registering, users will be redirected to '/' by default. There will be a redirect flash attribute in the model called "accountRegistrationStatus" set to "success" if account registration was successful

## Managing users

To approve user accounts or deactivate users, provide a link to ```/admin/user/list```. Then provide a view that iterates over all users (```${users}```). For each user, the following endpoints are available:
* ```/admin/user/${user.username}/approve```: make a POST request to approve user
* ```/admin/user/${user.username}/admin```: make a POST request to give a user ADMIN role
* ```/admin/user/${user.username}/admin/remove```: make a POST request to remove ADMIN role from user
* ```/admin/user/${user.username}/disable```: make a POST request to disable a user account

By default, the controller serving ```/admin/user/list``` assumes that the view is ```admin/user/list```.

## Configuring simple-users

You can configure simple-users by adding a configuration class that implements ```edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration``` and that is annotated with ```@Configuration```. When you implement the ```configure``` method you can set the following configurations:
* ```userListView```: view name of the view that shows the user management
* ```registerView```: view name of the register view
* ```registerSuccessRedirect```: redirect URL after successful account registration.



