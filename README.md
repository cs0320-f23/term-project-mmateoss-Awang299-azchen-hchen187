# term-project-mmateoss-Awang299-azchen-hchen187-

# Project Details

## Project Name

The name of the Project is SpotiDuo.

## Project Description

## Team members and Contributors

Team members for this project were Awang299 (Allen), hchen187 (Charlene), azchen (Andrew), and mmateoss (Marcel).


Links to outisde contributions used:

Used these to learn how to do things in the code

. https://github.com/deepgram-starters/deepgram-java-starters/blob/main/Starter-01/src/main/java/com/deepgram/App.java 
    . Used this to figure out how exactly the deepgram API wanted us to send a post request to their API, the documentation on their website was not the best for Java but this sample project they released worked well to show me how they wanted the Post API request to go.

. https://www.baeldung.com/java-base64-encode-and-decode
    . Used this to learn how to encode and decode objects in base64 so that they could be sent as parts of the body of some API requests.

. https://developer.spotify.com/documentation/web-api
    . Used the spotify documentation extensively in order to be able to

. https://mkyong.com/java/how-to-convert-file-into-an-array-of-bytes/
    . Used to learn how to convert a file into a byte[] in order to be able to send it as the body of an API.

. https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/HttpRequest.BodyPublishers.html 
    . Used this documentaion to learn the differnt BodyPublishers that could be used and chose the correct one for my specific needs.

. https://www.javatpoint.com/java-string-replaceall
    . Used this to learn about the documentation of the replaceall methdo in java


## Link to Repo

The link to our repo is: https://github.com/cs0320-f23/term-project-mmateoss-Awang299-azchen-hchen187

## Hours Worked

We spent an average of 16 hours per week each on this project. Resulting in a total of around 192 cumulative hours.

# Design Choices

## Relationship between classes/files

This project is split into two directories: the frontend and the backend. We
will describe both the frontend and the backend here:

### Front-End


### Back-End


## Specific Data Structures used and other High Level


# Errors/Bugs

    . There are no known bugs or errors. We tested pretty thoroughly and feel confident in our project.

# Tests
    . There are a lot of tests included in this project. They test that the functionality works both using mocked and real data and also integrating different sections overall. 

# How To

## How to Run Tests

### Front-End Tests:

    . To run the tests, make sure that you are CD into repl-mmateoos-thuang49.. You should have playwright installed
        but if you don't, in your terminal type:
                            npx playwright install
        once that is done, go back to your terminal and run the following commands to run the tests:
                            npx playwright test
                                - runs the tests
                            npx playwright test --ui
                                - opens a UI that allows for you to watch and run your tests as if in a live browser
    . To run the jest tests, make sure that you are CD into repl-mmateoos-thuang49.
    	Then run the command: npm test CommandRegistration.test.ts

        One thing to note is that for integration tests, if some fail, you will
        have to manually run them one-by-one, as they likely failed due to
        conflicting requests to the server. 

### Back-End Tests:

    . To run the tests in the backend, navigate individually to the testing files and press the green arrow next to the class declaration for the testing file. This will allow you to run the tests that you want for each individual testing class. Additionally, for some of the tests to run, the server requires for there to be certain API keys and Client Secrets/Ids to be included. There are
    places with TODO comments where these should be included. These are in some tests and if you want to run tests that actully call upon the spotify API, you must put the cleint id and secret to your Spotify API in the token generator class under the todo comments.

## How to Run Program

### SpotiDuo WebApp (full-stack web app)

To run the fron-end, navigate to the front-end directory and then run the command:

[insert command here]

This should start the front-end up. You can then follow the link that shows up in the terminal to open the front-end in your web browser. However, before using it, please follow the steps in the just the server section (underneath this) to start your back-end server so that everything functions as intended.


### Just the server

To start the server, navigate in the backend to the server file and in IntelliJ or your development IDE, run the server file. In IntelliJ this is pressing the green arrow at the top of the class, right above where the class declaration is or the green arrow to the left of the class declaration. Once the server is running, you can use the frontend as intended as it will be able to make API calls to the backend in order to get the needed information to function as intended. Additionally, please ensure that you enter the correct API client secret and id to your web-pp in the token generator class in order for certain parts to be able to connect with the Spotify API correctly.

    Endpoints that the server has:


## Accessibility 
