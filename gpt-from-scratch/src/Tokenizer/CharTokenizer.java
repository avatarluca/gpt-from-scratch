package src.Tokenizer;

import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.List;


/*
A simple character level tokenizer for out character level language model.
*/
public class CharTokenizer extends Tokenizer {
    public CharTokenizer(final String input) {
        setUpVocabulary(input);
    }

    private void setUpVocabulary(final String input) {
        for(int i = 0; i < input.length(); i++) {
            final char symbol = input.charAt(i);    
            vocabulary.add(symbol + "");
        }

        vocabulary.build();
    }

    @Override public List<Token> tokenize(final String input) throws NotActiveException {
        final List<Token> tokens = new ArrayList<>();
        for(int i = 0; i < input.length(); i++) {
            final char symbol = input.charAt(i);
            tokens.add(vocabulary.getTokenBySymbol(symbol + ""));
        }
        
        return tokens;
    }
}
