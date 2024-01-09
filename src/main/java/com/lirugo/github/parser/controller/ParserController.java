package com.lirugo.github.parser.controller;


import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
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
  public List<Repo> repo(@RequestParam String owner) {
    return gitHubService.getRepos(owner);
  }


//  @RequestParam(required = false, defaultValue = "100") Integer fileLimit
  @GetMapping("/files")
  public List<RepoFile> parse(
      @RequestParam String owner,
      @RequestParam String fileReqExp) {
    return gitHubService.getFiles(owner, fileReqExp);
  }

}
