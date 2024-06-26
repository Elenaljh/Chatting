package Server;

import java.util.Arrays;
import java.util.List;

public class BlacklistedWord {

    private final List<String> blacklistedWords = Arrays.asList("시발", "병신", "개새끼");

    public BlacklistedWord() {

    }

    public boolean containBlacklistedWord(String msg) {
        for (String word : blacklistedWords) {
            if (msg.contains(word)) {
                return true;
            }
        }
        return false;
    }
}
