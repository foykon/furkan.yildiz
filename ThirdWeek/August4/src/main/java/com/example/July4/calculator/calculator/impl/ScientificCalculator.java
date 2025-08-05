package com.example.July4.calculator.calculator.impl;

import com.example.July4.calculator.calculator.ICalculator;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component("scientific")
public class ScientificCalculator implements ICalculator {
    @Override
    public double add(double num1, double num2) {
        System.out.println("scientific calc add");

        return num1+num2;
    }

    @Override
    public double subtract(double num1, double num2) {
        System.out.println("scientific calc subtract");
        return num1-num2;
    }

    @Override
    public double multiply(double num1, double num2) {
        System.out.println("scientific calc multiply");
        return num1*num2;
    }

    @PostConstruct
    public  void    init(){
        System.out.println("init çağrıldı");
    }

    @PreDestroy
    public void  destroy(){
        System.out.println("destroy çağrıldı");
    }
}
