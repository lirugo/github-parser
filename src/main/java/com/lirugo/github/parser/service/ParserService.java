package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.RepoFile;
import com.lirugo.github.parser.model.Word;
import java.util.List;

public interface ParserService {
    List<Word> countWordFrequency(List<RepoFile> files, Integer minLetter,
        Integer topLimit);
}
