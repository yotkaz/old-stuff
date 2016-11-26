# ArduinoSM-GUI
Arduino Serial Monitor - GUI (JAVA, Spring MVC)

###How to build and run:
1. Install rxtx libraries http://rxtx.qbang.org/wiki/index.php/Download
2. Go to ArduinoSM main directory and run: `mvn clean install`
3. Go to ArduinoSM-GUI main directory and run:

    `mvn clean install` or `mvn clean package`
4. Go to the `target` directory. A generated JAR will be there.
5. Run SerialMonitor (Arduino must be connected) with at least two arguments:
`--port` and `--dataRate`:

    `java -jar asm-gui.jar --port=COM13 --dataRate=9600 --timeout=2000 --name=SerialMonitor --server.port=6669`
7. Go to `localhost:6669/sm` and have fun

###WARNING
- On some Ubuntu systems (other may also) rxtx cannot find ports named like this: `/dev/ttyACM0`. You should create link to this port like that:

  `sudo ln -s /dev/ttyACM0 /dev/ttyS70` (this one rxtx can find)
- Sometimes there are issues with permissions to the port. You have to check it
