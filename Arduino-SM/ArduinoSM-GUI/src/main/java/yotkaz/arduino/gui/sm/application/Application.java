package yotkaz.arduino.gui.sm.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

/**
 * @author yotkaz
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = "yotkaz.arduino.gui")
public class Application {

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Invalid arguments! Usage:\n" + "java -jar asm-gui.jar --port=COM13"
					+ " --dataRate=9600 --timeout=2000 --name=SerialMonitor --server.port=6669");
			System.exit(1);
		}
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public TemplateResolver templateResolver() {
		ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("LEGACYHTML5");

		return templateResolver;
	}

}
