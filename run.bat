@echo off
setlocal

REM Set the path to your JavaFX SDK
set PATH_TO_FX="C:\javafx-sdk-21.0.7\lib"

REM Set the name of your JAR file
set JAR_FILE=target\proyecto_reproductor_music-1.0-SNAPSHOT.jar

REM Run the application
java --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml,javafx.media -jar %JAR_FILE% %*

endlocal
