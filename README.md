BRIEF DESCRIPTION

Command line application built in Java only with libraries provided by Java JDK 8 

This application, receive a user name as input, and do a HTTPS Request to GitHub API to query the set of repositories. 

The application process the response of the service as String with Regular Expression to identify the programming language in each repository. 

Finally, with the result obtained, the application decide which languages are the favourite for the user (may have several favourites). 
  
TASKS TO RUN IT 

There is two folders. First one, called src, has the source code of the application. The other one, called bin, has a jar executable file with the application. 

To run the application, you only need Java JDK 8 and run the jar file with "java -jar GithubUsers.jar <userName>". For example: 
 java -jar GithubUsers.jar jjmena80