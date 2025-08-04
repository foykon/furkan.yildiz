package com.example.July4.calculator.school.impl;

import com.example.July4.calculator.calculator.ICalculator;
import com.example.July4.calculator.school.ISchoolCalcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class PrimarySchoolCalcService implements ISchoolCalcService {
    private final ICalculator calculator;

    @Autowired
    public PrimarySchoolCalcService(@Qualifier("simple") ICalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public double add(double num1, double num2) {
        System.out.println("scientific calc add send to simple");

        return calculator.add(num1, num2);
    }

    @Override
    public double subtract(double num1, double num2) {
        System.out.println("scientific calc subtract send to simple");

        return calculator.subtract(num1, num2);
    }

    @Override
    public double multiply(double num1, double num2) {
        System.out.println("scientific calc multiply send to simple");

        return calculator.multiply(num1, num2);
    }
}
