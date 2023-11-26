package ru.geekbrains.core.lesson2;

import java.util.Random;
import java.util.Scanner;

public class Program {
    private static final char DOT_HUMAH = 'X'; //обозначение хода игрока
    private static final char DOT_BOT = 'O'; //обозначение хода бота
    private static final char DOT_EMPTY = '•'; //пустые ячейки игрового поля
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random random = new Random();
    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;

    /**
     * инициализация игрового поля
     */
    private static void initField() {
        System.out.print("Формирование рабочего поля\n");
        do {
            System.out.println("Введите количество строк и количество столбцов через пробел:\n");
            fieldSizeX = SCANNER.nextInt();
            fieldSizeY = SCANNER.nextInt();
        } while (fieldSizeX < 0 || fieldSizeY < 0);
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }


    /**
     * вывод игрового поля на терминал
     */
    private static void printField() {
        System.out.print(" +");
        for (int i = 0; i < fieldSizeY * 2 + 1; i++) {
            System.out.print((i % 2 == 0) ? " – " : i / 2 + 1);
        }
        System.out.println();
        for (int i = 0; i < fieldSizeY * 2 + 2; i++) {
            System.out.print(" –");
        }
        System.out.println();
        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(" " + (x + 1) + " | ");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + " | ");

            }
            System.out.println();
        }
        for (int i = 0; i < fieldSizeY * 2 + 2; i++) {
            System.out.print(" –");
        }
        System.out.println();
    }

    /**
     * ход игрока, ввод с терминала
     */
    private static void humanTurn() {
        int x, y;
        do {
            System.out.printf("Введите номер строки (от 1 до %d) и номер столбца (от 1 до %d) через пробел:\n",
                    fieldSizeX,
                    fieldSizeY);
            x = SCANNER.nextInt() - 1;
            y = SCANNER.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = DOT_HUMAH;
    }

    /**
     * ход бота, случайный выбор
     */
    private static void botTurn() {
        int x, y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = DOT_BOT;
    }

    /**
     *
     * Проверка свободной ячейки для хода бота
     *
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == DOT_EMPTY;
    }

    /**
     *
     * Проверка валидности введенных значений
     *
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellValid(int x, int y) {
        return x >= 0 && y >= 0 && x < fieldSizeX && y < fieldSizeY;
    }

    /**
     * Проверка строк/столбцов/диагоналей
     *
     * @param player
     * @return
     */
    static boolean checkWin(char player) {
        //Проверка строк
        for (int x = 0; x < fieldSizeX; x++) {
            boolean win = true;
            for (int y = 0; y < fieldSizeY; y++) {
                if (field[x][y] != player) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return true;
            }
        }

        //Проверка столбцов
        for (int y = 0; y < fieldSizeY; y++) {
            boolean win = true;
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[x][y] != player) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return true;
            }
        }

        //Проверка диагонали
        boolean win;
        for (int diagonal = 0;
             diagonal < Math.max(fieldSizeX, fieldSizeY) - Math.min(fieldSizeX, fieldSizeY) + 1; diagonal++) {
            win = true;
            for (int i = 0; i < Math.min(fieldSizeX, fieldSizeY); i++) {
                if (field[i][i + diagonal] != player) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return true;
            }
        }

        //Проверка зеркальной диагонали
        for (int diagonal = 0;
             diagonal < Math.max(fieldSizeX, fieldSizeY) - Math.min(fieldSizeX, fieldSizeY) + 1; diagonal++) {
            win = true;
            for (int i = 0; i < Math.min(fieldSizeX, fieldSizeY); i++) {
                if (field[i][fieldSizeY - 1 - i - diagonal] != player) {
                    win = false;
                    break;
                }
            }
            if (win) {
                return true;
            }
        }
        return false;
    }


    /**
     * Проверка ничьей
     *
     * @return
     */
    static boolean checkDraw() {
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return true;
            }
        }
        System.out.println("Ничья. Играем дальше?");
        return false;
    }

    /**
     * Проверка: игрок или бот
     *
     * @param player
     * @param str
     * @return
     */
    static boolean gameCheck(char player, String str) {
        if (checkWin(player)) {
            System.out.println(str);
            return true;
        }
        return false;
    }


    /**
     *
     * Инициализация поля ввода для игрока
     * Запрос хода до тех пор, пока не будет зафиксирована ничья или победа
     *
     * @param args
     */
    public static void main(String[] args) {
        initField();
        printField();
        while (checkDraw()) {
            humanTurn();
            printField();
            if (gameCheck(DOT_HUMAH, "Поздравляем! Вы выиграли!")) {
                break;
            }
            botTurn();
            printField();
            if (gameCheck(DOT_BOT, "Увы! Вы проиграли!")) {
                break;
            }
        }
    }
}

