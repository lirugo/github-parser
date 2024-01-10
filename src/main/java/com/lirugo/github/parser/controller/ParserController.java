package com.lirugo.github.parser.controller;


import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
import com.lirugo.github.parser.model.Word;
import com.lirugo.github.parser.service.GitHubService;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/github-parser")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParserController {

  GitHubService gitHubService;

  @GetMapping("/repos")
  public List<Repo> getRepos(@RequestParam String owner) {
    return gitHubService.getRepos(owner);
  }

  @GetMapping("/files")
  public List<RepoFile> getFiles(
      @RequestParam String owner,
      @RequestParam String fileRegExp,
      @RequestParam(required = false, defaultValue = "100") Integer fileLimit) {
    return gitHubService.getFiles(owner, fileRegExp, fileLimit);
  }

  @GetMapping("/words-frequency")
  public List<Word> getWordFrequency(
      @RequestParam String owner,
      @RequestParam String fileRegExp,
      @RequestParam(required = false, defaultValue = "100") Integer fileLimit,
      @RequestParam(required = false, defaultValue = "4") Integer minLetter,
      @RequestParam(required = false, defaultValue = "3") Integer topLimit) {
    return gitHubService.getWordFrequency(owner, fileRegExp, fileLimit, minLetter, topLimit);
  }

}
