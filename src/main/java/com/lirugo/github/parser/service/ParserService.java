package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.Word;
import java.util.List;

public interface ParserService {
    List<Word> getWordFrequency(String owner, String fileRegExp, Integer fileLimit, Integer letterLimit, Integer topLimit);
}
