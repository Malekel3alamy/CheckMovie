
**Movie App**

**Overview** : 
Movie App is an Android application built using Kotlin. It allows users to browse and search for movies, view detailed information about each movie, and manage their favorite movies. The app leverages the MVVM architecture pattern and integrates with The Movie Database (TMDB) API to fetch movie data.

**Features** : 
- Browse popular movies
- Search for movies by title
- View detailed information about each movie
- Add movies to favorites
- Responsive UI with support for different screen sizes
**Screenshots**: 

!Screenshot 1 
![WhatsApp Image 2024-08-27 at 9 01 04 AM (3)](https://github.com/user-attachments/assets/3da2796f-a088-4f49-a77c-8f2072a6b5bb)

!Screenshot 2
![WhatsApp Image 2024-08-27 at 9 01 04 AM (2)](https://github.com/user-attachments/assets/3df07a88-4d6a-4d13-be1e-eb97f9ad22c5)

!Screenshot 3
![WhatsApp Image 2024-08-27 at 9 01 04 AM (1)](https://github.com/user-attachments/assets/a004fd16-b883-46e9-b778-03276479dcc9)

!Screenshot 4
![WhatsApp Image 2024-08-27 at 9 01 04 AM](https://github.com/user-attachments/assets/f1108fa8-d194-40c6-aaf0-82c1ba420ef6)

**Architecture** : 
The app follows the MVVM (Model-View-ViewModel) architecture pattern:

- Model: Handles the data layer, including network requests and database operations.
- View: Displays the data and interacts with the user.
- ViewModel: Acts as a bridge between the Model and the View, managing UI-related data in a lifecycle-conscious way.
  
**Libraries and Tools** :
- Kotlin: Programming language used for development

- Retrofit: For network requests

- Room Database : 

- Work Manager : 

- Glide: For image loading

- LiveData: For data observation

- ViewModel: For managing UI-related data

- DataBinding: For binding UI components to data sources

- Navigation Component: For handling navigation


**Setup and Installation** : 
- Clone the repository:
- git clone https://github.com/yourusername/movie-app.git

- Open the project in Android Studio.
- Add your TMDB API key in the local.properties file:
 TMDB_API_KEY=your_api_key

- Build and run the project on an emulator or physical device.

 **Usage** : 
- Launch the app and browse the list of popular movies.

- Use the search feature to find specific movies.
- Tap on a movie to view detailed information.
- Add movies to your favorites for quick access.
  
**Contributing**
- Contributions are welcome! Please fork the repository and submit a pull request with your changes.

**License**
- This project is licensed under the MIT License - see the LICENSE file for details.

**Acknowledgements**
TMDB API for providing movie data








  
