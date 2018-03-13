import java.io.*;
import java.util.Objects;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
import java.io.File;


public class Main {

    public static void main(String[] args) {

        // Setting up
            String result = "";
            int points = 0;


        // Playing the game
            result = processTurn();
            System.out.println(result);

    }

    /***
     * Selects random title from movie-titles.txt file
     * @return String chosenTitle, string containing the full title that will be guessed
     */
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
        String [] currentTitleArray = currentTitle.split("");
        String [] workingTitleArray = workingTitle.toLowerCase().split("");
        selectedCharacter = selectedCharacter.toLowerCase();

        if (turn == 0) {
            workingTitle = currentTitle.replaceAll("\\S", "_");
        } else {
            // use previous version of workingTitle and just fill it in with selectedCharacter
            for(int i = 0; i < currentTitleArray.length; i++) {
                if(currentTitleArray[i].toLowerCase().equals(selectedCharacter)) {
                    workingTitleArray[i] = currentTitleArray[i];
                    correctCharacters += selectedCharacter;
                }
            }

            workingTitle = String.join("", workingTitleArray);
        }

        return workingTitle;
    }

    /** Handles game logic, calls chooseTitle and createWorkingTitle
     * @return String results, holds the result of the game (win or lose)
     */
    private static String processTurn(

    ) {
        // This is what gets returned
            String results = "";
        // Set up
            String chosenTitle = "";
            int lettersRemaining = 100;
            String guessedCharacters = "";
            String correctCharacters = "";
            String incorrectCharacters = "";
            int incorrectGuessesLeft = 11;
            int turn = 0;
            String intro = "RULES \n\n" +
                    "Guess the secret movie title!\n" +
                    "Each turn, you may guess ONE (1) letter. If that letter is present in the secret movie title, it will be displayed in the title.\n" +
                    "If that letter is NOT present in the secret movie title, you will lose a point. If you lose 10 points, you lose the game!\n" +
                    "At each turn, you will be presented with the secret title, including any correct letters you've guessed, and a list of incorrect letters you've guessed." +
                    "\n\n";

            String guessLine = "Guess a letter.\n";
            String selectedCharacter = "";
            String workingTitle = "";

        /* FLOW
            Run while the user still has points remaining.
            on first turn, explain game
            on every turn after, IF there is >= 1 incorrect guess and 1 letter left,
            display this information:
            working title, incorrect guesses left, incorrect letters, "guess" interface
            then, take user input, pass it to createWorkingTitle to generate the working title,
            run the game logic, display it again
         */
        while(incorrectGuessesLeft > 0) {
            // If the user still has points left
            if(incorrectGuessesLeft > 1) {
                String incorrectLettersLine = "Incorrect letters: " + incorrectCharacters + "\n";
                String incorrectGuessesLeftLine = "Points remaining: " + (incorrectGuessesLeft - 1) + "\n";

                // If it's the first turn, explain the game
                if(turn == 0) {
                    chosenTitle = chooseTitle();

                    System.out.println(chosenTitle);
                    // Don't count spaces in chosenTitle when initializing value for lettersRemaining
                    lettersRemaining = chosenTitle.replace(" ", "").length();
                    System.out.println("original length: " + chosenTitle.length() + " | test length: " + lettersRemaining);
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
                } else if (lettersRemaining != 0) {
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
                    String testChar = scanner.nextLine();

                    /* CHECK INPUT:
                        If it's the same as a previously-guessed character, don't allow it
                        If it's whitespace, don't allow it
                     */
                    if(guessedCharacters.contains(testChar.toLowerCase())) {
                        System.out.println("It looks like you've already guessed " + selectedCharacter +".  Please choose another letter. \n");
                        System.out.println(guessLine);

                    } else if (testChar.matches("[^A-Za-z]+")) {
                        System.out.println("That's a non-letter character. Please choose a letter.\n");
                        System.out.println(guessLine);

                    } else if (testChar.length() > 1) {
                        System.out.println("That's too long. Please choose only ONE letter.\n");
                        System.out.println(guessLine);

                    } else {
                        selectedCharacter = testChar;
                        guessedCharacters += selectedCharacter.toLowerCase();

                        // Check if guessed letter is correct
                        if(!chosenTitle.toLowerCase().contains(selectedCharacter.toLowerCase())) {
                            System.out.println("Sorry, " + selectedCharacter + " is not in the title.");
                            incorrectCharacters += selectedCharacter;
                            incorrectGuessesLeft -= 1;
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
                        System.out.println(workingTitle);
                        System.out.println("Turn: " + turn);
                        // Count number of underscores in workingTitle to get lettersRemaining
                        lettersRemaining = workingTitle.length() - workingTitle.replace("_", "").length();
                        turn++;
                    }

                } else {
                    // YOU WON!
                    results = "Congratulations! You've won!";
                    break;
                }
            } else {
                // YOU LOSE.
                results = "Sorry, you've lost.";
                break;
            }

        }

        return results;
    }
}
