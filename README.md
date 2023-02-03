# BabyBuy

The project activity is essential; users’ access through log in & registration, Firebase authorisation is implemented, and only Gmail accounts can gain access. Once successfully logged in, the products page will appear, and users will have a list they can add, delete or edit products on their account; again, the firebase database has been programmed to record all item information to store for later use. In addition, the BabyBuy system comes with a text messaging feature to send products to others. This feature is helpful when a friend or family member wants to buy the user an item found to help with a new-born.

## Preference Manager

I have implemented Kotlin programming language for my splash screen opening page & preference manager, showing my diversity in program learning, not just Java. The splash screen shows the brand name with a delay of one second before heading to the Login page. The pref manager manages the auth state within the application context and is in Kotlin for compatibility purposes.

## Storage

Both the authentication service and backend database are built on Firebase. A client dependency connects the application to the Firebase API by sending HTTP requests with the information provided by the app. The Google system enables the communication between the Firebase API and the app by linking Firebase to the app externally.

Firebase handles authentication and user profile management. The app's UI collects user information, which is subsequently transferred to Firebase via the client dependency and saved in Firebase's online storage. The same procedure is used to authenticate and retrieve user information. In summary, Firebase is used for user data storage and authentication, and it is incorporated into the application via a client dependency that allows connection with the Firebase API. A Limitation of the Firebase storage is that only the image appears for the administration to see, and only the user’s product data are stored on the device used, limiting access to other devices. A database with more robust API storage would be required for a general application on the Google play store. No additional data needs to be seen by the admin account for this prototype. If the client wanted an E-commerce app, listing the full product details would be required to manage orders, something to reflect on for future projects.

The Google services page allows the project to be connected to my Firebase account; project information identifies the properties to connect the application and database.

To create a new account, this function calls Firebase authorisation linked to my Gmail account. Using the command mAuth relates the Firebase import to the project and, on registration, adds the user's details to the accounts database.

## Architecture

To build Android apps, developers need to follow a set of design and architectural principles. Coding patterns promote modularity, maintainability, and testability by organising code. MVP, MVVM, and MVC are examples of popular Android architecture patterns. Using these patterns, code can be structured in an easy-to-understand, easy-to-modify, and easy-to-test manner.

### Add

The add Function allows the user to input items into the application that appears on the dropdown list but is also connected to Firebase storage. Items are saved for later use; all attached will remain for future log-ins.

### Edit

Products can be edited on all features to change mistakes or update information, which is very helpful for spelling mistakes or items that vary in value.

### Delete

The delete function erases products from both the application & Firebase storage; useful when items have been purchased and can be checked off the list.

## Test Strategy

A test strategy is a method or plans for evaluating a software product or system. It describes the overall testing strategy, including the tests that will be run, the resources employed, and the testing timeline. It also specifies the objectives and goals of testing, as well as the criteria for deciding whether a test was successful and the method for reporting and recording test findings. A test plan is often created as part of the software development process. It guarantees that the product or system satisfies the defined criteria and is defect-free before being deployed to end customers. The technique is also used to detect possible hazards or problems with the product or system and to guarantee that the testing process is efficient and cost-effective. It also aids in aligning testing efforts with the development process and overall project goals. A test strategy is a document that specifies how testing will be carried out in a project to ensure that the software is of high quality and fits the demands of end users.

## Evaluation

In summary, the application is a success in terms of the project brief from the client, as the prototype application created has all the core requirements implemented. No desirable optional requirements were completed, and as a developer, I know the improvements I need to make to advance the work produced. In this timeframe, I spent many hours embedding the CRUD system and connecting it to the Firebase storage database. Lack of time management & gaining skills on the job slowed down the process, leaving the desirable features out. The project's design is fundamental and, at the bare minimum, how a professional-looking application should be published. Focusing on backend programming, I have left the appearance bland and recommend a more interactive interface to bring the project to life. Many issues occurred due to design planning needing to be completed at the right stage and diving straight into implementation with no structure to follow, which means less action. I found this the hard way with hours spent amending interface features to run with the backend coding. A unique logo must be included to symbolise the brand, represent BabyBuy, and stand out to the client. The project has been completed within the client's deadline showing I can produce application software in a developer setting and is evidence I can use these skills for the industry. Android application development has taught me more about debugging & how to produce software for Android devices, increasing my depth in Java programming and teaching me Kotlin language to progress as a developer.

## License

This software is open-sourced software licensed under the [AGPL-3.0](LICENSE.md).
