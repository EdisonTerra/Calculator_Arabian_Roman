import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) {
        System.out.println("Внимание! Калькулятор принимает цифры от 1 до 10 включительно ");
        System.out.println("выполняет операции сложения, вычитания, умножения и деления с двумя числами: ");
        System.out.print("Введите выражение: ");
        Scanner scn = new Scanner(System.in);
        String exp = scn.nextLine();
        char[] chars = exp.toCharArray();
        int plusCount = 0, minusCount = 0, asteriskCount = 0, slashCount = 0;
        for (char c : chars) {
            if (c == '+') {
                plusCount++;
            } else if (c == '-') {
                minusCount++;
            } else if (c == '*') {
                asteriskCount++;
            } else if (c == '/') {
                slashCount++;
            }
        }
        //Проверка количеситва  арифметического знака
        if (plusCount >= 2 || minusCount >= 2 || asteriskCount >= 2 || slashCount >= 2) {
            System.out.println("Некорректное выражение,формат математической операции не удовлетворяет заданию -  допускается один оператор (+, -, /, *)");
            return;
        }
        String sign = null;
        if (plusCount == 1) {
            sign = "+";
        } else if (minusCount == 1) {
            sign = "-";
        } else if (asteriskCount == 1) {
            sign = "*";
        } else if (slashCount == 1) {
            sign = "/";
        }
        String[] actions = {"+", "-", "/", "*"};
        int actionIndex = -1;
        for (int i = 0; i < actions.length; i++) {
            if (exp.contains(actions[i])) {
                actionIndex = i;
                break;
            }
        }
        if (actionIndex == -1) {
            System.out.println("Некорректное выражение, строка не является математической операцией");
            return;
        }
        String[] regexActions = {"\\+", "-", "/", "\\*"};
        assert sign != null;
        String[] data = exp.split(regexActions[actionIndex]);
        Converter converter = new Converter();
        //Определяем, находятся ли числа в одном формате (оба римские или оба арабские)
        if (converter.isRoman(data[0]) == converter.isRoman(data[1])) {
            int a, b;
            //Определяем, римские ли это числа
            boolean isRoman = converter.isRoman(data[0]);
            if (isRoman) {
                //если римские, то конвертируем их в арабские
                a = converter.romanToInt(data[0]);
                b = converter.romanToInt(data[1]);
            } else {
                //если арабские, конвертируем их из строки в число
                a = Integer.parseInt(data[0]);
                b = Integer.parseInt(data[1]);
            }
            if ((a <= 10) && (b <= 10)) {
                int result = switch (sign) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    default -> a / b;
                };
                if (isRoman) {
                    if (result <= 0) {
                        System.out.println("В римской системе нет отрицательных чисел");
                    } else{
                        System.out.println(converter.intToRoman(result));
                    }
                } else {
                    System.out.println(result);
                }
            } else {
                System.out.println("Некорректный ввод. Введите число от 1 до 10.");
            }
        } else {
            System.out.println("Используются одновременно разные системы счисления");
        }
    }
}
class Converter {
    TreeMap<Character, Integer> romanKeyMap = new TreeMap<>();
    TreeMap<Integer, String> arabianKeyMap = new TreeMap<>();
    Converter() {
        romanKeyMap.put('I', 1);
        romanKeyMap.put('V', 5);
        romanKeyMap.put('X', 10);
        arabianKeyMap.put(10, "X");
        arabianKeyMap.put(9, "IX");
        arabianKeyMap.put(5, "V");
        arabianKeyMap.put(4, "IV");
        arabianKeyMap.put(1, "I");
    }
    boolean isRoman(String number){return romanKeyMap.containsKey(number.charAt(0));
    }
    String intToRoman(int number) {
        StringBuilder roman = new StringBuilder();
        int arabianKey;
        do {
            arabianKey = arabianKeyMap.floorKey(number);
            roman.append(arabianKeyMap.get(arabianKey));
            number -= arabianKey;
        } while (number != 0);
        return roman.toString();
    }
    int romanToInt(String s) {
        int end = s.length() - 1;
        char[] arr = s.toCharArray();
        int arabian;
        int result = romanKeyMap.get(arr[end]);
        for (int i = end - 1; i >= 0; i--) {
            arabian = romanKeyMap.get(arr[i]);
            if (arabian < romanKeyMap.get(arr[i + 1])) {
                result -= arabian;
            } else {
                result += arabian;
            }
        }
        return result;
    }
}