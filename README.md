# Android Job Candidate

### Task Description

Fix all of the TODOs and rewrite the project so it reflects your coding style and preferred way of displaying a list of items and a details page.
We expect that the assignment will be written in Kotlin

When clicking one of the items in the list, the details of that item should be shown.
When loading data from the Api, there should be a ProgressBar visible.
In the case of a connection timeout, there should be a fullscreen error message with a retry button.
Clicking the retry button should make a new request to the api.

Your solution should be something you would put into production.
This means that we expect that the app is stable and performs well in all possible use cases

*At the interview we expect you to walk us through the code and explain what you have done.*

# Solution

### Main Components

The following components has been used:

[Navigation Component](https://developer.android.com/guide/navigation): For navigation between fragments

[Databinding](https://developer.android.com/topic/libraries/data-binding): For binding UI components in layout to data sources

[LiveData](https://developer.android.com/topic/libraries/architecture/livedata): For displaying live observable data in recyclerview 

[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel): For storing and managing UI-related data

[Hilt](https://developer.android.com/training/dependency-injection/hilt-android): For dependency injection

[Picasso](https://square.github.io/picasso/): For image loading

[Room](https://developer.android.com/jetpack/androidx/releases/room): For local storing of data