package yotkaz.arduino.sm.service;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

/**
 * @author yotkaz
 *
 */
public class SerialMonitor {

	private String portName;
	private int dataRate;
	private int connectionTimeout;
	private String processName;
	private MessageHandler handler;

	private SerialPort serialPort;
	private SerialReader reader;
	private SerialWriter writer;

	public SerialMonitor(String portName, int dataRate, int connectionTimeout, String processName,
			MessageHandler handler) {
		this.portName = portName;
		this.dataRate = dataRate;
		this.connectionTimeout = connectionTimeout;
		this.processName = processName;
		this.handler = handler;
	}

	public void init()
			throws Exception {
		CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);
		if (portId.isCurrentlyOwned()) {
			throw new IllegalStateException("Port [" + portName + "] is currently in use");
		} else {
			CommPort port = portId.open(processName, connectionTimeout);
			if (port instanceof SerialPort) {
				serialPort = (SerialPort) port;
				serialPort.setSerialPortParams(dataRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
				reader = new SerialReader(serialPort.getInputStream(), handler);
				writer = new SerialWriter(serialPort.getOutputStream(), handler);

				new Thread(writer).start();
				serialPort.addEventListener(reader);
				serialPort.notifyOnDataAvailable(true);
			} else {
				throw new IllegalStateException("Only serial ports are supported!");
			}
		}
	}
	
	public SerialWriter getWriter() {
		return writer;
	}
	
	public SerialReader getReader() {
		return reader;
	}
}
