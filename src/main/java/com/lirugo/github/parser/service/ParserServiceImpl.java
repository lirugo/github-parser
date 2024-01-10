package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.Word;
import java.util.List;
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

  GitHubService gitHubService;

  @Override
  public List<Word> getWordFrequency(String owner, String fileRegExp, Integer fileLimit,
      Integer letterLimit,
      Integer topLimit) {
    var files = gitHubService.getFiles(owner, fileRegExp, fileLimit);

    files.forEach(file -> {
      var content = gitHubService.getFileContent(owner, file.getRepo().name(), file.getPath());
      content.ifPresent(log::debug);
      // TODO implement frequency map
    });

    return List.of();
  }
}
