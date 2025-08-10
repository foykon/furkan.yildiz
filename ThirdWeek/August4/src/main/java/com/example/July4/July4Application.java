package com.example.July4;

import com.example.July4.calculator.school.ISchoolCalcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class July4Application {

	public static void main(String[] args) {
		calcExample(args);


	}



	private static void calcExample(String[] args){
		ConfigurableApplicationContext context = SpringApplication.run(July4Application.class, args);

		ISchoolCalcService academicCalcService = context.getBean("academicCalcCalcService", ISchoolCalcService.class);
		ISchoolCalcService primarySchoolCalcService = context.getBean("primarySchoolCalcService", ISchoolCalcService.class);

		academicCalcService.add(5, 3);
		primarySchoolCalcService.add(5, 3);
	}

}
