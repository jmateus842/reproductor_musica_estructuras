@echo off
echo Limpiando y construyendo el JAR...
call mvn clean package
echo JAR creado en target/proyecto_reproductor_music-1.0-SNAPSHOT.jar 