package ru.basejava;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BaseJavaHW12 {

    private static int minValue(int[] arrayInput) {
        return Arrays.stream(arrayInput)
                .distinct()                     //возвращаем поток уникальных элементов массива
                .sorted()                       //возвращаем отсортированный поток
                .reduce((l, r) -> l * 10 + r)   //составляем число из полученных уникальных цифр исходного массива
                .orElseThrow();                 //возвращаем результат, если он существует, в противном случае кидаем NoSuchElementException
    }

    private static List<Integer> oddOrEven(List<Integer> integers) {
        int mod = integers.stream()
                .mapToInt(Integer::intValue)
                .sum() % 2;                     //определяем сумму всех чисел массива

        return integers.stream()
                .filter(n -> n % 2 != mod)
                .collect(Collectors.toList());  // возвращаем рузультат, отфильтровав массив в зависисомти от четности суммы всех чисел
    }

    public static void main(String[] args) {
        int[] arrayNumber = {1, 2, 4, 3, 8, 5, 3, 7, 4, 3, 9, 6};
        System.out.println(minValue(arrayNumber));

        List<Integer> list = new ArrayList<>();
        Collections.addAll(list, 3, 7, 6, 1, 1, 2, 7, 1);
        System.out.println(oddOrEven(list));
    }
}
