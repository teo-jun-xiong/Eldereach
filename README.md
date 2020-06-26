# Eldereach 
<p align="center"><img src="https://i.imgur.com/XBeEbLM.png" width="250px" height="250px"></p>

An Android application developed with the welfare of Singapore's elderly in mind. Eldereach aims to connect welfare organizations and caregivers and make the provision and requesting of food aid, transportation, and visits much more simpler. Read more about Eldereach [here](ABOUT.asciidoc). 

## Installation
Clone this repository and import into **Android Studio**
```bash
git@github.com:teo-jun-xiong/eldereach.git
```

## Configuration
Replace the string resource `map_api_key` with your Google Maps API key to enable Google Maps in `TransportClientActivity`. 

## Maintainers
This project is mantained by:
* [Teo Jun Xiong](http://github.com/teo-jun-xiong)

## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
5. Push your branch (git push origin my-new-feature)
6. Create a new Pull Request

## Try it out
Download the `.apk` for v1.0 [here](https://github.com/teo-jun-xiong/eldereach/releases/tag/v1.0)! 

Please note that the app will work, however, the client will be unable to create a transport request, as the Maps API key is a placeholder. Opening that activity will cause the app to crash. In order to try out the full app, please follow the installation and configuration instructions, and then create your own `.apk` by navigating the menu: Build > Build bundle(s) / APK(s) > Build APK(s). The APK can then be installed on your Android device or emulator. Additionally, Eldereach is optimized for a large screen real estate 
~ 2960 x 1440 pixels. 

## Credits
- Developed on Android Studio, using Firebase for user authentication and Firestore for database solutions.
- Multi-selection spinner from [Preet Sidhu](https://github.com/prsidhu/MultiSelectSpinner)
- Icons made by [Payungkead](https://www.flaticon.com/authors/payungkead) from [Flaticon](https://www.flaticon.com/Flaticon)
- Icons made by [Freepik](https://www.flaticon.com/authors/freepik) from [Flaticon](https://www.flaticon.com/Flaticon)

## License
[MIT](https://choosealicense.com/licenses/mit/)
