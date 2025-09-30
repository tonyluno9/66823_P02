import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import javax.swing.*;

public class MonteCarloPi extends JPanel {
    private final int n;
    private final double[] aproximaciones;
    private double piFinal;
    private final double[] xDentro, yDentro, xFuera, yFuera;
    private int dentroCirculo, fueraCirculo;

    public MonteCarloPi(int n) {
        this.n = n;
        xDentro = new double[n];
        yDentro = new double[n];
        xFuera = new double[n];
        yFuera = new double[n];
        aproximaciones = new double[n];
        setPreferredSize(new Dimension(600, 600));
        simular();
    }

    private void simular() {
        Random rand = new Random();
        int dentro = 0;
        int idxIn = 0, idxOut = 0;

        for (int i = 1; i <= n; i++) {
            double x = rand.nextDouble() * 2 - 1; // [-1, 1]
            double y = rand.nextDouble() * 2 - 1; // [-1, 1]

            if (x * x + y * y <= 1.0) {
                if (idxIn < n) {
                    xDentro[idxIn] = x;
                    yDentro[idxIn] = y;
                    idxIn++;
                }
                dentro++;
            } else {
                if (idxOut < n) {
                    xFuera[idxOut] = x;
                    yFuera[idxOut] = y;
                    idxOut++;
                }
            }

            aproximaciones[i - 1] = 4.0 * dentro / i;
        }

        dentroCirculo = idxIn;
        fueraCirculo = idxOut;
        piFinal = aproximaciones[n - 1];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height) - 40; // margen
        int cx = width / 2;
        int cy = height / 2;
        int left = cx - size / 2;
        int top = cy - size / 2;

        // Cuadrado guía (opcional)
        g2.setColor(new Color(230, 230, 230));
        g2.drawRect(left, top, size, size);

        // Círculo unitario dentro del cuadrado
        g2.setColor(Color.LIGHT_GRAY);
        g2.draw(new Ellipse2D.Double(left, top, size, size));

        // Puntos dentro (azul)
        g2.setColor(Color.BLUE);
        for (int i = 0; i < dentroCirculo; i++) {
            int px = (int) (cx + xDentro[i] * (size / 2.0));
            int py = (int) (cy - yDentro[i] * (size / 2.0));
            g2.fillOval(px, py, 3, 3);
        }

        // Puntos fuera (rojo)
        g2.setColor(Color.RED);
        for (int i = 0; i < fueraCirculo; i++) {
            int px = (int) (cx + xFuera[i] * (size / 2.0));
            int py = (int) (cy - yFuera[i] * (size / 2.0));
            g2.fillOval(px, py, 3, 3);
        }

        // Texto PI
        g2.setColor(Color.BLACK);
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
        String texto = String.format("Aproximación de Pi: %.6f (n=%d)", piFinal, n);
        g2.drawString(texto, 20, 30);

        g2.dispose();
    }

    public double getPiFinal() {
        return piFinal;
    }

    public void repetirSimulacion() {
        simular();
        repaint();
    }

    private static JPanel buildUI() {
        MonteCarloPi panel = new MonteCarloPi(10_000);

        JButton botonRepetir = new JButton("Repetir simulación");
        botonRepetir.addActionListener((ActionEvent e) -> panel.repetirSimulacion());

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(panel, BorderLayout.CENTER);
        contenedor.add(botonRepetir, BorderLayout.SOUTH);
        return contenedor;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Monte Carlo Pi");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(buildUI());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
