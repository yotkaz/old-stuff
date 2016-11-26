package yotkaz.arduino.gui.sm.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author yotkaz
 *
 */
@Configuration
public class SerialMonitorServiceConfiguration {

	@Value("${port}")
	private String port;
	@Value("${dataRate}")
	private int dataRate;
	@Value("${timeout:2000}")
	private int timeout;
	@Value("${name:SerialMonitor}")
	private String name;
	
	public String getPort() {
		return port;
	}
	
	public int getDataRate() {
		return dataRate;
	}
	
	public int getTimeout() {
		return timeout;
	}
	
	public String getName() {
		return name;
	}
}
