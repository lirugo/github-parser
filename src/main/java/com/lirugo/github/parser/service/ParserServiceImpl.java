package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.RepoFile;
import com.lirugo.github.parser.model.Word;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParserServiceImpl implements ParserService {

  public List<Word> countWordFrequency(List<RepoFile> files, Integer letterLimit,
      Integer topLimit) {
    Map<String, Integer> frequencyMap = new HashMap<>();

    for (var file : files) {
      if (file.getContent().isEmpty()) {
        log.warn("File content is null. Repo: {}, Path: {}",
            file.getRepo().name(), file.getPath());
        continue;
      }

      var content = file.getContent().get();
      // TODO implement my own algorithm
      // TODO
      var words = content
          .replaceAll("[^a-zA-Z ]", "")
          .split("\\s+");
      for (var word : words) {
        if (word.length() <= letterLimit && !word.isBlank()) {
          frequencyMap.merge(word, 1, Integer::sum);
        }
      }
    }

    return frequencyMap.entrySet().stream()
        .map(entry -> new Word(entry.getKey(), entry.getValue()))
        .sorted(Comparator.comparingInt(Word::frequency).reversed())
        .limit(topLimit)
        .toList();
  }
}
