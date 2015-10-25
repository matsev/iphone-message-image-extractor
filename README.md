## iPhone Message Image Extractor

This is a small utility program for extracting images from iPhone messages based on an iTunes backup.


## Usage

1. Create a backup of your iPhone using iTunes.
2. Copy the iPhone backup directory (a subdirectory of the `~/Library/Application Support/MobileSync` on OS X) to a known directory.   
3. Clone and build this project (using `$ gradle clean bootRepackage`).
4. Copy the resulting .jar file from this projects `build/libs/` directory to the copy of the iPhone backup directory.
5. Execute the program `$ java -jar iphone-message-image-extractor-0.0.1-SNAPSHOT.jar`.
6. All JPEG images are extracted in the same folder.


## Disclaimer

Only verified on OS X 10.11 "El Capitan" and iOS 9.1.