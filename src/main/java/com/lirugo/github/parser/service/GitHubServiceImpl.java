package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GitHubServiceImpl implements GitHubService {

  RestTemplate restTemplate;

  @Override
  public List<Repo> getRepos(String owner) {
    var res = restTemplate.getForEntity(String.format(REPO_URL, owner), Repo[].class);
    return List.of(Objects.requireNonNull(res.getBody()));
  }

  @Override
  //TODO
  public List<RepoFile> getFiles(String owner, String fileReqExp) {
    var res = new ArrayList<RepoFile>();

    getRepos(owner)
        .stream()
        .limit(2) // TODO remove it
        .map(repo -> {
          var repoFiles = restTemplate.getForEntity(String.format(FILE_URL, owner, repo.name()),
              RepoFile[].class).getBody();

          return Arrays.stream(Objects.requireNonNull(repoFiles))
              .peek(r -> r.setRepo(repo))
              .toList();
        }).forEach(res::addAll);

    return res;
  }

}
