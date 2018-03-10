import java.io.*;
import java.util.Scanner;
import java.util.Random;
import java.io.File;

public class Main {

    public static void main(String[] args) {

        // Getting the title
            String chosenTitle = "";
            String workingTitle = "";
            int incorrectGuessesLeft = 10;
            int lettersRemaining = 100;
            String [] guessedCharacters = new String[100];
            String [] incorrectCharacters = new String[100];
            String [] correctCharacters= new String[100];
            String selectedCharacter = "";

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
                lettersRemaining = chosenTitle.length();
            }
            //catch the exception
            catch(FileNotFoundException e) {
                e.printStackTrace();
            }
            System.out.println(chosenTitle);

        // Generating blank title
        String thisTitle = createWorkingTitle(selectedCharacter,
                chosenTitle,
                workingTitle,
                lettersRemaining,
                guessedCharacters,
                correctCharacters,
                incorrectCharacters,
                incorrectGuessesLeft
        );

        System.out.println(thisTitle);

        // call createWorkingTitle on every interaction
        // set up interactivity next
    }

    /**
     * This takes a lot of info, manipulates it, and ONLY returns workingTitle
     * @param selectedCharacter
     * @param chosenTitle
     * @param workingTitle
     * @param lettersRemaining
     * @param guessedCharacters
     * @param correctCharacters
     * @param incorrectCharacters
     * @param incorrectGuessesLeft
     * @return workingTitle
     */
    public static String createWorkingTitle(String selectedCharacter,
                                        String chosenTitle,
                                        String workingTitle,
                                        int lettersRemaining,
                                        String [] guessedCharacters,
                                        String [] correctCharacters,
                                        String [] incorrectCharacters,
                                        int incorrectGuessesLeft
    ) {
        // Initial title return
        if(lettersRemaining == chosenTitle.length()) {
            String[] arrayifiedTitle = chosenTitle.split("");
            for (int i = 0; i < arrayifiedTitle.length; i++) {
                if (arrayifiedTitle[i].equals(" ")) {
                    workingTitle = workingTitle + " ";
                } else {
                    workingTitle = workingTitle + "_";
                }
            }
        } else {
            boolean selectedIsCorrect = false;
            // check if chosenTitle contains guessed character
            // if so, replace that index in workingTitle with that character,
            // decrement charactersRemaining, and put that character in
            // guessedCharacters and correctCharacters
            // if not, decrement incorrectGuessesLeft and put that character
            // in guessedCharacters and incorrectCharacters

            String newTitle = workingTitle;
            String[] arrayifiedTitle = chosenTitle.split("");
            for (int i = 0; i < arrayifiedTitle.length + 1; i++) {
                for(int j = 0; j < guessedCharacters.length; j++ ) {
                    if(guessedCharacters[i].equals(arrayifiedTitle[i])) {
                        newTitle = newTitle + guessedCharacters[i];

                        if(guessedCharacters[i].equals(selectedCharacter)) {
                            selectedIsCorrect = true;
                        }
                    }
                }


                if(i == arrayifiedTitle.length) {
                    if(selectedIsCorrect) {
                        correctCharacters[correctCharacters.length] = selectedCharacter;
                    } else {
                        incorrectCharacters[incorrectCharacters.length] = selectedCharacter;
                        incorrectGuessesLeft--;
                    }
                    guessedCharacters[guessedCharacters.length] = selectedCharacter;
                    break;
                }
            }
        }
        return workingTitle;
    }
}
