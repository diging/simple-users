# simple-users

[![Build Status](https://diging-dev.asu.edu/jenkins/buildStatus/icon?job=simple_users_test_on_push&style=plastic&subject=Tests)](https://diging-dev.asu.edu/jenkins/view/Simple%20Users/job/simple_users_test_on_push/) [![Build Status](https://diging-dev.asu.edu/jenkins/buildStatus/icon?job=simple-users_deploy_to_maven_central&style=plastic&subject=Published%20Maven%20Central&color=orange)](https://diging-dev.asu.edu/jenkins/view/Simple%20Users/job/simple-users_deploy_to_maven_central/)

Simple-users is a library for basic user management for Spring applications. It is set up to work with Spring Security and to use Spring Data for storage of user objects.

## Installation

If you use Maven, just add the following dependency:

```
<dependency>
    <groupId>edu.asu.diging</groupId>
    <artifactId>simple-users</artifactId>
    <version>[simple-users-version]</version>
</dependency>
```

where ```[simple-users-version]``` is the version you want to use.

## How to use simple-users

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
* ```edu.asu.diging.simpleusers.core.service.impl.EmailService```, returning ```edu.asu.diging.simpleusers.core.service.IEmailService```
* ```edu.asu.diging.simpleusers.core.service.impl.TokenService```, returning ```edu.asu.diging.simpleusers.core.service.ITokenService```
* an implementation of ```org.springframework.mail.javamail.JavaMailSender```

User objects are stored using Spring Data and the repository ```edu.asu.diging.simpleusers.core.data.UserRepository```. Tokens are stored via ```edu.asu.diging.simpleusers.core.data.TokenRepository```.

Also, this library provides several controllers: 
* ```edu.asu.diging.simpleusers.web.CreateAccountController```
* ```edu.asu.diging.simpleusers.web.RequestPasswordResetController```
* ```edu.asu.diging.simpleusers.web.ResetPasswordInitiatedController```
* ```edu.asu.diging.simpleusers.web.admin.RemoveRoleController```
* ```edu.asu.diging.simpleusers.web.admin.RemoveAdminRoleController```
* ```edu.asu.diging.simpleusers.web.admin.ListUsersController```
* ```edu.asu.diging.simpleusers.web.admin.DisableUserController```
* ```edu.asu.diging.simpleusers.web.admin.ApproveAccountController```
* ```edu.asu.diging.simpleusers.web.admin.AddRoleController```
* ```edu.asu.diging.simpleusers.web.admin.AddAdminRoleController```
* ```edu.asu.diging.simpleusers.web.password.ChangePasswordController```

## Storing data

The user class is annotated using JPA annotations. All you need to do is to configure a data source, an entity mananger factory, a transaction provider, and an persistence exception translation processor.

## Securing your application

Simply configure Spring Security as you normally would, and let it find the user details service provided by this library through component scanning or provide it as a bean (if you choose to instantiate it yourself, you also need to provide a user factory instance).

## Users for Setup

simple-users expects a ```user.properties``` file at the root of the classpath that contains a list of hard-coded users in the format:
```[username]=[bcrypt encrypted password],[ROLE],[enabled|disabled]```
(without the brackets)

These user accounts are not meant to be used as permanent user accounts, but rather to be used during the setup phase to be able to approve the first admin user account.

## Registering new users

To register a new user, make sure the CreateAccountController is available in a context, then send your users to /register. Provide a register view (by default the CreateAccountController assumes that there is a view for 'register'). After succesfully registering, users will be redirected to '/' by default. There will be a redirect flash attribute in the model called "accountRegistrationStatus" set to "success" if account registration was successful

## Managing users

To approve user accounts or deactivate users, provide a link to ```/admin/user/list```. Then provide a view that iterates over all users (```${users}```). For each user, the following endpoints are available:
* ```/admin/user/${user.username}/approve```: make a POST request to approve user
* ```/admin/user/${user.username}/admin```: make a POST request to give a user ADMIN role
* ```/admin/user/${user.username}/admin/remove```: make a POST request to remove ADMIN role from user
* ```/admin/user/${user.username}/disable```: make a POST request to disable a user account
* ```/admin/user/${user.username}/role/add?roles=role1,role2```: make a POST request to add the specified roles
* ```/admin/user/${user.username}/role/remove?roles=role1,role2```: make a POST request to remove the specified roles

The default prefix for simple user related endpoints is ```/admin/user```. See below for how to change it. By default, the controller serving ```/admin/user/list``` assumes that the view is ```admin/user/list```. This can also be changed as described below.

## Configuring simple-users

You can configure simple-users by adding a configuration class that implements ```edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration``` and that is annotated with ```@Configuration```. When you implement the ```configure``` method you can set the following configurations:
* ```userListView```: view name of the view that shows the user management
* ```registerView```: view name of the register view
* ```registerSuccessRedirect```: redirect URL after successful account registration
* ```usersEndpointPrefix```: set the prefix for all simple user related endpoints; setting this property make all endpoints available under the provided prefix (e.g. setting this property to ```/secured/path/users/``` will make the user list available at ```/secured/path/users/list```).

## Enabling password reset

Since v0.4, simple-users provides password reset functionality. To let users reset their password by email, do the following.

* In the simple user configuration class, set the following properties of ```SimpleUsers```:
  * ```emailUsername```: username to use the email server
  * ```emailPassword```: password to use the email server
  * ```emailServerHost```: host name of the email server
  * ```emailServerPort```: port of the email server
  * ```emailFrom```: email address to use in the "from" field
  * ```instanceUrl```: the url of the webapp using simple-users; this url is used to generate the reset link
  * ```appName```: the name of the web application using simple-users; the name is used in the reset emails; default is "Web Application"
* Optionally, you can also configure the following:
  * ```emailProtocol```: protocol to use (defaults to smtp)
  * ```emailAuthentication```: should authentication be used (defaults to true)
  * ```emailStartTlsEnable```: enable STARTTLS (defaults to true)
  * ```emailDebug```: enable email debugging in ```JavaMailSenderImpl```
  * ```emailSubject```: specify a subject for the reset emails (default is "Password Reset Request")
  * ```emailBody```: the email body used for reset emails; see below for more details
  * ```tokenExpirationPeriod```: the time in minutes after which a token expires, by default tokens expire after 24 hours
* For the password reset feature to work, certain urls have to be secured or allowed with Spring Security:
  * ```"/password/**"``` has to be allowed for users with the role ```SimpleUsersConstants.CHANGE_PASSWORD_ROLE```, e.g. by adding ```.antMatchers("/password/**").hasRole(SimpleUsersConstants.CHANGE_PASSWORD_ROLE)```
  * ```"/reset/**"``` has to be allowed for anyone, e.g. by adding ```.antMatchers("/reset/**").permitAll()```
* A couple of controllers and views also have to be implemented:
  * A controller and view for initiating the password reset have to be implemented, the page only needs to ask for the email address of the account to reset.
  * The page for initiating the password reset, needs to make a POST request to ```/reset/request``` providing the email address for the user requesting a password reset as ```email``` parameter. Simple-users will check if a user with that email address exist and if so send an email with password reset instructions. The URL the POST request is made to can be overwritten by configuring ```resetPasswordEndpoint```.
  * A controller and view have to be implement at ```/reset/request/sent``` that show a user that password reset has been initiated. The URL can be changed by configuring ```resetRequestSentEndpoint```.
  * A view with name ```password/change``` has to be implemented that shows the password reset form. It should have two input fields: one for the new password (name should be ```password```), one for the repeated new password (name should be ```passwordRepeat```). The new password should be send as POST request to ```/password/change```.
  * If the password field is empty or if the password and repeated password don't match, simple-users will redirect back to the ```password/change``` view, providing an error parameter with either the value ```noPassword``` or ```notMatching```.
  * If the password was successfully changed, simple-users redirects to ```/success=PasswordChanged```.
  
 ### Changing reset email text
 
 By default, simple-users will send the following email for resetting a password:
 
> Hi $user!
> 
> You've requested your password to be reset for $app. To reset your password, please follow this link:
> $url.
> 
> If you did not request a password reset, please ignore this email and let the system administrator know.
> Your $app Team
> 
> ---
> This email was automatically generated. Please do not reply.

where the following variables are used:
* ```$user```: name of the user
* ```$app```: name of the app
* ```$url```: reset url

You can use your own template (using the variables above). Just set it in simple-users configuration via ```emailBody```.
