package ru.basejava;

import ru.basejava.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {

    public static void main(String[] arg) throws IllegalAccessException {
        Resume resume = new Resume();
        Field field = resume.getClass().getDeclaredFields()[0];
        System.out.println("Fields:");
        field.setAccessible(true);
        System.out.println(field.getName() + " : " + field.get(resume));
        field.set(resume, "new_uuid");
        System.out.print(field.getName() + " : ");
        invokeToStringMethod(resume);
        field.setAccessible(false);
    }

    public static void invokeToStringMethod(Object object){
        try {
            Method method =  object.getClass().getDeclaredMethod("toString");
            method.setAccessible(true);
            System.out.println(method.invoke(object));
            method.setAccessible(false);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e){
            e.printStackTrace();
        }

    }
}
