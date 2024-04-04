import java.util.Random;
import java.util.Scanner;

public class RandomExample {
    public static void main(String... args)  {
        Random rand = new Random();
        Scanner in = new Scanner(System.in);
        int size = in.nextInt();
        int numberOfPlayers = in.nextInt();

        while (true) {
            for (int i = 0; i < size; ++i) {
                String nextLine = in.nextLine();
            }
            System.out.println(rand.nextInt(10));
        }
    }
}
