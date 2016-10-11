mkdir bin
mkdir bin\Cursors
mkdir bin\Windows
javac -d bin/ src/*.java
cp -Rpf src/Windows/*.fxml bin/Windows
cp -Rpf src/Cursors/*.png bin/Cursors
