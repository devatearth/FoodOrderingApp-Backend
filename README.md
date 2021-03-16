# Capstone Project - Food Order App (Backend)
Made with Java Spring Boot.

### Team Members:
1.  Sricharan Krishnan
2.  Devanathan Babu
3.  Navven Raj
4.  Vetrivel Muthusamy

### Building and running this project on local instance:
1.  https://github.com/sricharankrishnan/FoodOrderingApp-Backend is the project url for the backend. You must be a part of the 
    group to have access to this repo or from Upgrad
2.  Perform cloning process with the help of git
3.  Login to your local instance of postgresql and create a db called ```restaurantdb```
4.  Make the necessary changes at application.properties and localhost.properties for your postgres login
5.  Add necessary user privileges to this particular db as required for your local instance
6.  You would need to install the necessary dependencices for this project now. Place yourself at the root of the project
7.  Run command ```mvn clean install -Psetup -Dmaven.test.skip```
8.  Ideally there should be no errors at this stage
9.  You can run the server with the command ```mvn spring-boot:run -Dmaven.test.skip```
10. Go to the browser and head to http://localhost:8080/api/swagger-ui.html

### Git Configurations
1.  If you’re programming on Windows and working with people who are not (or
vice-versa), you’ll probably run into line-ending issues at some point. This is be-
cause Windows uses both a carriage-return character and a linefeed character
for newlines in its files, whereas Mac and Linux systems use only the linefeed
character. This is a subtle but incredibly annoying fact of cross-platform work;
many editors on Windows silently replace existing LF-style line endings with
CRLF, or insert both line-ending characters when the user hits the enter key.
Git can handle this by auto-converting CRLF line endings into LF when you
add a file to the index, and vice versa when it checks out code onto your filesys-
tem. You can turn on this functionality with the core.autocrlf setting. If
you’re on a Windows machine, set it to true – this converts LF endings into
CRLF when you check out code

```$ git config --global core.autocrlf true```

2.  If you’re on a Linux or Mac system that uses LF line endings, then you don’t
want Git to automatically convert them when you check out files; however, if a
file with CRLF endings accidentally gets introduced, then you may want Git to
fix it. You can tell Git to convert CRLF to LF on commit but not the other way
around by setting core.autocrlf to input:

```$ git config --global core.autocrlf input```


