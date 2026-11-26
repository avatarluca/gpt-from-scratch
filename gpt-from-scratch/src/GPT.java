package src;

import java.io.IOException;
import java.io.NotActiveException;

import src.Tokenizer.CharTokenizer;
import src.Tokenizer.Tokenizer;
import src.Tokenizer.FileReader;

public class GPT {
    public static void main(final String... args) throws NotActiveException, IOException {
        String text = FileReader.readText("data/input.txt");
        System.out.println(text);       

        Tokenizer tokenizer = new CharTokenizer(text);
        // System.out.println(tokenizer.tokenizeToString("Hallo mein Name ist Luca"));
        System.out.println(tokenizer.decode(tokenizer.encode("Hallo Luca")));
    }
}
