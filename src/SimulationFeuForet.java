import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class SimulationFeuForet {
    private int hauteur;
    private int largeur;
    private char[][] grille;
    private char[][] etatPrecedent;
    private double probabilitePropagation;
    private Random random;

    public SimulationFeuForet(String configFile) {
        random = new Random();
        chargerConfiguration(configFile);
        initialiserGrille();
        propagerFeu();
    }

    private void chargerConfiguration(String configFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(configFile))) {
            hauteur = Integer.parseInt(br.readLine().split(":")[1].trim());
            largeur = Integer.parseInt(br.readLine().split(":")[1].trim());
            probabilitePropagation = Double.parseDouble(br.readLine().split(":")[1].trim());

            grille = new char[hauteur][largeur];
            etatPrecedent = new char[hauteur][largeur];

            String[] positionsFeu = br.readLine().split(":")[1].trim().split(",");
            for (String position : positionsFeu) {
                String[] coords = position.split("-");
                int x = Integer.parseInt(coords[0].trim());
                int y = Integer.parseInt(coords[1].trim());
                grille[x][y] = 'F';
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initialiserGrille() {
        System.out.println("État initial de la grille:");
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                System.out.print((grille[i][j] == 'F') ? "F " : ". ");
                etatPrecedent[i][j] = grille[i][j];
            }
            System.out.println();
        }
        System.out.println();
    }

    private void propagerFeu() {
        int etape = 0;

        while (SiResteFeu()) {
            System.out.println("Étape " + etape + ":");
            for (int i = 0; i < hauteur; i++) {
                for (int j = 0; j < largeur; j++) {
                    if (etatPrecedent[i][j] == 'F') {
                        grille[i][j] = 'X';  // La case s'éteint à l'étape t+1

                        // Propagation du feu aux cases adjacentes à l'étape t+1
                        propagerAuVoisinage(i - 1, j);
                        propagerAuVoisinage(i + 1, j);
                        propagerAuVoisinage(i, j - 1);
                        propagerAuVoisinage(i, j + 1);
                    }
                }
            }
            afficherGrille();
            copierGrille(grille, etatPrecedent);  // Copier l'état actuel dans l'état précédent
            etape++;
        }

        System.out.println("La simulation est terminée après " + etape + " étapes.");
    }

    private void afficherGrille() {
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                System.out.print((grille[i][j] == 'F') ? "F " : (grille[i][j] == 'X') ? "X " : ". ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void propagerAuVoisinage(int x, int y) {
        if (x >= 0 && x < hauteur && y >= 0 && y < largeur && etatPrecedent[x][y] != 'F' && grille[x][y] != 'X' && etatPrecedent[x][y] != 'X' && random.nextDouble() < probabilitePropagation) {
            grille[x][y] = 'F';  // La case devient en feu à l'étape t+1
        }
    }

    private boolean SiResteFeu() {
        for (int i = 0; i < hauteur; i++) {
            for (int j = 0; j < largeur; j++) {
                if (grille[i][j] == 'F') {
                    return true;
                }
            }
        }
        return false;
    }

    private void copierGrille(char[][] source, char[][] destination) {
        for (int i = 0; i < hauteur; i++) {
            System.arraycopy(source[i], 0, destination[i], 0, largeur);
        }
    }

    public static void main(String[] args) {
        File f=new File("guesmi.txt");
        f.getAbsolutePath();
        SimulationFeuForet simulation = new SimulationFeuForet("config.txt");
    }
}