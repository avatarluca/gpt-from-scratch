package src.Tokenizer;

import java.io.NotActiveException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public abstract class Tokenizer {
    Vocabulary vocabulary = new Vocabulary();
    
    abstract List<Token> tokenize(final String input) throws NotActiveException;
    
    public List<Integer> encode(final String input) throws NotActiveException {
        List<Token> tokens = tokenize(input);
        return tokens.stream().map(Token::ID).toList();
    }
    public String decode(final List<Integer> input) throws NotActiveException {
        String output = "";
        for(final int id : input) output += vocabulary.getTokenById(id).value();

        return output;
    }
    
    public String tokenizeToString(final String input) throws NotActiveException {
        final List<Token> tokens = tokenize(input);
        String output = "";
        for(final Token token : tokens) output += token.ID() + " : " + token.value() + "\n";

        return output;
    }

    public static class Vocabulary {
        private Set<String> stream = new HashSet<>();
        private List<Token> tokens;

        public void add(final String lexem) {
            stream.add(lexem);
        }

        public Token getTokenBySymbol(final String symbol) throws NotActiveException {
            if(this.tokens == null) throw new NotActiveException("Vocabulary not build");

            for(final Token token : this.tokens) if(token.value().equals(symbol)) return token;

            // we now return an unknown token
            return new Token(this.tokens.size(), "<UNK>");
        }

        public Token getTokenById(final int id) throws NotActiveException {
            if(this.tokens == null) throw new NotActiveException("Vocabulary not build");

            if(id >= tokens.size()) {
                // we now return an unknown token
                return new Token(this.tokens.size(), "<UNK>");
            }

            return tokens.get(id);
        }

        public void build() {
            final List<Token> tokens = new ArrayList<>();
            int id = 0;
            for(String lexem : stream) tokens.add(new Token(id++, lexem));
            
            this.tokens = tokens;
        }
    }
    
    public static record Token(int ID, String value) {}
}
