package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.RepoFile;
import com.lirugo.github.parser.model.Word;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

      var words = splitTextToWords(file.getContent());

      for (var word : words) {
        if (word.length() <= letterLimit && !word.isBlank()) {
          frequencyMap.merge(word, 1, Integer::sum);
        }
      }
    }

    return frequencyMap.entrySet().stream()
        .map(entry -> new Word(entry.getKey(), entry.getValue()))
        .sorted(Comparator.comparingInt(Word::frequency).reversed())
        .filter(word -> word.word().length() <= letterLimit)
        .limit(topLimit)
        .toList();
  }

  private List<String> splitTextToWords(Optional<String> content) {
    return content
        .map(this::getWords)
//        .map(this::splitWords)
        .orElse(List.of());
  }

  /**
   * Here i collect all words from text. I use StringBuilder to collect letters from word. When i
   * meet non-letter symbol i add word to list and clear StringBuilder. I use pointer to iterate
   * through text.
   *
   * @param content is just a text from file
   * @return list of words
   */
  private List<String> getWords(String content) {
    List<String> words = new ArrayList<>();
    var sb = new StringBuilder();

    for (var index = 0; index < content.length(); index++) {
      while (index < content.length() && Character.isLetter(content.charAt(index))) {
        sb.append(content.charAt(index++));
      }

      if (!sb.isEmpty()) {
        words.add(sb.toString().toLowerCase());
        sb = new StringBuilder();
      }

    }

    return words;
  }

  /**
   * Regex split is slower than my method TODO check it
   *
   * @param content
   * @return
   */
  private List<String> splitWords(String content) {
    return Arrays.asList(content.toLowerCase().split("\\W+"));
  }
}
