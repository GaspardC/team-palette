# team-palette


https://www.youtube.com/watch?v=MPLKD5c_vvc  

JavaCode is located in src/main/java/com/epfl/computational_photography/paletizer/  

**Activities :**   

*PaletteActivity.java* : main activity where palette search palette generation are applied and where we can visualize the result on an image.  
*FullScreenActivity.java* : activity to visualize the result full screen and eventually save the image.  
*MainActivity.java* : menu activity or home activity to chose between the 3 options implemented.  
*SplashActivity.java* : Only launched at first launched and display a background image for 3 seconds. It use to launch the background job (service) to prepare the loading of the database  
*SlideMenu/SlideMenuActivity.java*: creation of the menu  
*/fastTransfer/TransferActivity.java* : Simple Reinhard transfer either on a static image or on a live inputstream from camera  



**Classes :**  

*/ColorPicker/ColorPicker.java* : implement the Color Picker behavior   
*/ColorPicker/ColorPickerDialog.java* : custom Dialog View which holds the ColorPIcker  

*/fastTransfer/FastTransfer.java* : class used to compute the simple color transfer used in the TransferActivity.java  
*/fastTransfer/MySurfaceView.java* : The surfaceview is the only way in Android to have a live render of the camera input stream. This surfaceview is overridden to apply the color transfer algorithm at the same time  

*/palette/Extractor.java*  : embeds the algorithm to create a new palette from an image.  
*/palette/Transferer.java* : implementation of the main color transfer algorithm (global Reinhard)  

*/palette_database/* : Implements the palette database system and word queries. Includes : computing semantic similarity between words using Wordnet, loading the Kuler database, adding a palette to the database, retrieving palettes from Flickr image search, loading POS tags for palette names.  


*MyService.java* : a service is use to do some background jobs, independant from the UI, and accross all activities. It is launched in the first activity (Splash Screen) and dies when its task is completed (preparing the database).  
*PhotoManager.java* : Class used in many activities to create an interface which makes the loading of photos from library and from camera easier.  
*PaletteAdapterList.java* : Adapter used to display the palette obtained from the query.  
*PaletizerApplication.java* : An application is a class where you can set global static variable and settings.  
