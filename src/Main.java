import java.io.*;
import java.util.Objects;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.io.File;

public class Main {

    public static void main(String[] args) {

        // Getting the title
            int incorrectGuessesLeft = 10;
            int lettersRemaining = 100;
            int turn = 0;
            String guessedCharacters = "";
            String incorrectCharacters = "";
            String correctCharacters= "";
            String selectedCharacter = "";


        processTurn(
                lettersRemaining,
                guessedCharacters,
                correctCharacters,
                incorrectCharacters,
                incorrectGuessesLeft,
                turn
        );

    }

    private static String chooseTitle() {
        String chosenTitle = "";
        try {
            File f = new File (new File("movie-titles.txt").getAbsolutePath());
            Scanner scanner = new Scanner(f);

            int lines= 0;
            String [] allTitles = new String[1000];
            while(scanner.hasNextLine()) {
                allTitles[lines] = scanner.nextLine();
                lines++;
            }

            int randomNumber = (int)(Math.random() * lines) + 1;
            chosenTitle = allTitles[randomNumber];
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return chosenTitle;
    }

    /**
     * This takes a lot of info, manipulates it, and ONLY returns workingTitle
     * @param selectedCharacter String, is the character selected for this turn
     * @param correctCharacters String, is a list of correct characters guessed by user
     * @param turn int, is the current round of the game
     * @return workingTitle
     */
    private static String createWorkingTitle(
            int turn,
            String currentTitle,
            String workingTitle,
            String selectedCharacter,
            String correctCharacters
    ) {
        String [] currentTitleArray = currentTitle.toLowerCase().split("");
        String [] workingTitleArray = workingTitle.toLowerCase().split("");

        if (turn == 0) {
            workingTitle = currentTitle.replaceAll("\\S", "_");
        } else {
            // use previous version of workingTitle and just fill it in with selectedCharacter
            for(int i = 0; i < currentTitleArray.length; i++) {
                if(currentTitleArray[i].equals(selectedCharacter)) {
                    workingTitleArray[i] = selectedCharacter;
                    correctCharacters += selectedCharacter;
                }
            }

            workingTitle = String.join("", workingTitleArray);
        }

        return workingTitle;
    }

    /** NOTES: need to refine input handling
     * (don't allow user to repeat characters or select whitespace, non-letter characters)
     * and lettersRemaining (don't include whitespace)
     * Handles game logic, calls chooseTitle and createWorkingTitle
     * @param lettersRemaining int, number of letters in title that are left to guess
     * @param guessedCharacters String, characters guessed so far
     * @param correctCharacters String, characters guessed that are in the title
     * @param incorrectCharacters String, characters guessed that are not in the title
     * @param incorrectGuessesLeft int, number of incorrect guesses remaining
     * @param turn int, the current round number
     * @return something, don't know what yet
     */
    public static String[] processTurn(
            int lettersRemaining,
            String guessedCharacters,
            String correctCharacters,
            String incorrectCharacters,
            int incorrectGuessesLeft,
            int turn
    ) {
        // on first turn, explain game
        // on every turn after, IF there is >= 1 incorrect guess and 1 letter left,
        // display this information:
        // working title, incorrect guesses left, incorrect letters, "guess" interface
        // then, take user input, pass it to createWorkingTitle to generate the working title,
        // run the game logic, display it again

        String chosenTitle = "";
        String intro = "RULES \n\n" +
                "Guess the secret movie title!\n" +
                "Each turn, you may guess ONE (1) letter. If that letter is present in the secret movie title, it will be displayed in the title.\n" +
                "If that letter is NOT present in the secret movie title, you will lose a point. If you lose 10 points, you lose the game!\n" +
                "At each turn, you will be presented with the secret title, including any correct letters you've guessed, and a list of incorrect letters you've guessed." +
                "\n\n";

        String guessLine = "Guess a letter.\n";
        String selectedCharacter = "";
        String incorrectLettersLine = "Incorrect letters: " + incorrectCharacters + "\n";
        String incorrectGuessesLeftLine = "Points remaining: " + incorrectGuessesLeft + "\n";
        String workingTitle = "";


        while(incorrectGuessesLeft > 1) {
            // if it's the first turn, explain the game
            if(turn == 0) {
                chosenTitle = chooseTitle();
                System.out.println(chosenTitle);
                lettersRemaining = chosenTitle.length();
                workingTitle = createWorkingTitle(
                    turn,
                    chosenTitle,
                    workingTitle,
                    selectedCharacter,
                    correctCharacters
                );

                System.out.println(intro);
                System.out.println(workingTitle);
                turn++;
            } else if (lettersRemaining > 0) {
             /* FLOW
                After game is explained,
                Show working title, information, and guess a letter prompt
                User guesses letter
                Results are shown,
                New working title and information is shown,
                Turn is incremented
             */
                System.out.println(incorrectGuessesLeftLine);
                System.out.println(incorrectLettersLine);

                // GUESS
                System.out.println(guessLine);
                Scanner scanner = new Scanner(System.in);
                selectedCharacter = scanner.nextLine();
                guessedCharacters += selectedCharacter;
                System.out.println("You guessed: " + selectedCharacter + "\n");

                // check if guessed letter is correct
                if(!chosenTitle.toLowerCase().contains(selectedCharacter)) {
                    System.out.println("Sorry, " + selectedCharacter + " is not in the title.");
                    incorrectCharacters += selectedCharacter;
                    incorrectGuessesLeft--;
                } else {
                    System.out.println("Good guess: " + selectedCharacter + " is in the title!");
                    correctCharacters += selectedCharacter;
                }

                workingTitle = createWorkingTitle(
                        turn,
                        chosenTitle,
                        workingTitle,
                        selectedCharacter,
                        correctCharacters
                );
                System.out.println("Line 177: " + workingTitle);
                System.out.println("Turn: " + turn);
                turn++;
            } else {
                // YOU WON!
                System.out.println("Congratulations! You've won!");
            }
        }


        String [] results = {};
        return results;
    }
}
