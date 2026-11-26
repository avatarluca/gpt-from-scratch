package src;

import java.io.NotActiveException;

import src.Tokenizer.CharTokenizer;
import src.Tokenizer.Tokenizer;

public class GPT {
    public static void main(final String... args) throws NotActiveException {
        System.out.println("GPT from Scratch");

        Tokenizer tokenizer = new CharTokenizer("Hallo Welt");
        System.out.println(tokenizer.tokenizeToString("Hallo mein Name ist Luca"));
    }
}
