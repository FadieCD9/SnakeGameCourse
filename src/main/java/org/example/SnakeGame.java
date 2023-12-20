package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Random;

// Импорт log4j
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Класс SnakeGame представляет собой игру "Змейка".
 */
public class SnakeGame extends JFrame {
    private LinkedList<Point> snakeParts = new LinkedList<>();
    private int speed = 10;
    private double angle;
    private JPanel panel;

    // Создание экземпляра Logger
    private static final Logger logger = LogManager.getLogger(SnakeGame.class);

    /**
     * Конструктор SnakeGame инициализирует игру.
     */
    public SnakeGame() {
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Генерируем произвольный угол для начального направления движения
        angle = new Random().nextDouble() * 2 * Math.PI;

        // Добавляем начальные сегменты змейки
        for (int i = 0; i < 5; i++) {
            snakeParts.add(new Point(200 + i * 10, 200));
        }

        panel = new JPanel() {
            /**
             * Метод paintComponent рисует змейку на панели.
             * @param g Графический контекст.
             */
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Point p : snakeParts) {
                    g.fillOval(p.x, p.y, 10, 10);
                }
            }
        };
        add(panel);

        Timer timer = new Timer(100, new ActionListener() {
            /**
             * Метод actionPerformed обновляет состояние змейки и перерисовывает панель.
             * @param e Событие действия.
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int x = snakeParts.getFirst().x + (int) (speed * Math.cos(angle));
                int y = snakeParts.getFirst().y + (int) (speed * Math.sin(angle));

                if (x + 10 > panel.getWidth() || x < 0) {
                    angle = Math.PI - angle;
                    logger.info("Змейка ударилась о вертикальную стену и изменила направление.");
                }

                if (y + 10 > panel.getHeight() || y < 0) {
                    angle = -angle;
                    logger.info("Змейка ударилась о горизонтальную стену и изменила направление.");
                }

                snakeParts.addFirst(new Point(x, y));
                snakeParts.removeLast();

                panel.repaint();
            }
        });

        try {
            timer.start();
            setVisible(true);
        } catch (Exception ex) {
            logger.error("Произошла ошибка при запуске игры.", ex);
        }
    }

    /**
     * Точка входа в программу.
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        new SnakeGame();
    }
}
