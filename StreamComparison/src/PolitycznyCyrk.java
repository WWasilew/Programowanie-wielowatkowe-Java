import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PolitycznyCyrk {

    public static void main(String[] args) {
        List<String> candidates = List.of(
                "Karol Nawrocki",
                "Rafał Trzaskowski",
                "Szymon Hołownia",
                "Sławomir Mentzen",
                "Magdalena Biejat",
                "Adrian Zandberg",
                "Grzegorz Braun"
        );

        for(int j = 1; j <= 1; ++j) {       // ta pętla była po to by zapisać do pliku results.txt wyniki czasu wykonywania i tak samo linijka jedna pod spodem
            int totalVotes = j * 20_000_000; //
            System.out.println("\n obecna liczba głosów: " + totalVotes);
            List<String> votes = new ArrayList<>(totalVotes);
            Random rand = ThreadLocalRandom.current();

            for (int i = 0; i < totalVotes; i++) {
                // nie wszyscy głosują
                if (i % 2137 == 0) {
                    votes.add("Nie głosuję");
                } else {
                    votes.add(candidates.get(rand.nextInt(candidates.size())));
                }
            }

            System.out.println("\n stream");
            long startStream = System.nanoTime();

            Map<String, Long> results1 = votes.stream()
                    .filter(Objects::nonNull)                                       // nie chemy nulli
                    .filter(name -> !name.equals("Nie głosuję"))                      // filtrujemy osoby ktore nie chcą głosowac
                    .map(String::toUpperCase)                                      // mapowanie na wielkie litery
                    .sorted()                                                      // sortowanie alfabetyczne
                    .collect(Collectors.groupingBy(name -> name, Collectors.counting())); // agregacja

            long endStream = System.nanoTime();

            results1.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
            double streamDuration = (endStream - startStream) / 1_000_000.0;
            System.out.printf("Czas (stream): %.2f ms%n", streamDuration);


            System.out.println("\n parallelStream");
            long startParallelStream = System.nanoTime();

            Map<String, Long> results2 = votes.parallelStream()
                    .filter(Objects::nonNull)
                    .filter(name -> !name.equals("Nie głosuję"))
                    .map(String::toUpperCase)
                    .sorted()
                    .collect(Collectors.groupingBy(name -> name, Collectors.counting()));

            long endParallelStream = System.nanoTime();

            results2.entrySet().stream()
                    .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                    .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
            double parallelStreamDuration = (endParallelStream - startParallelStream) / 1_000_000.0;
            System.out.printf("Czas (parallelStream): %.2f ms%n", parallelStreamDuration);


            // Zapis do pliku z którego poźniej zrobie wykres
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/results.txt", true))) {
                writer.write(totalVotes + " " + streamDuration + " " + parallelStreamDuration + "\n");
            } catch (IOException e) {
                System.err.println("Błąd podczas zapisu do pliku: " + e.getMessage());
            }
        }
    }
}