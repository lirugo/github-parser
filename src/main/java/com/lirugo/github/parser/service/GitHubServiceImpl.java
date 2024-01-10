package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
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
  public List<RepoFile> getFiles(String owner, String fileRegExp, Integer fileLimit) {
    var res = new ArrayList<RepoFile>();

    getRepos(owner)
        .stream()
        .map(repo -> {
          var repoFiles = restTemplate.getForEntity(String.format(FILE_URL, owner, repo.name()),
              RepoFile[].class).getBody();

          return Arrays.stream(Objects.requireNonNull(repoFiles))
              .filter(repoFile -> repoFile.getName().matches(fileRegExp))
              .peek(r -> r.setRepo(repo))
              .toList();
        })
        .limit(fileLimit)
        .forEach(res::addAll);

    return res;
  }

  @Override
  public Optional<String> getFileContent(String owner, String repo, String filePath) {
    var response = restTemplate.getForEntity(String.format(FILE_CONTENT_URL, owner, repo, filePath),
        RepoFile.class);
    Optional<String> content = Optional.empty();

    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
      var contentBase64 = response.getBody().getContent();

      content = decodeBase64(contentBase64);
    } else {
      log.warn("File content not found, code: {}, body: {}", response.getStatusCode(),
          response.getBody());
    }

    return content;
  }

  private Optional<String> decodeBase64(String contentBase64) {
    try {
      contentBase64 = contentBase64
          .replace("\n", "");

      var decodedBytes = Base64.getDecoder().decode(contentBase64);

      return Optional.of(new String(decodedBytes));
    } catch (IllegalArgumentException e) {
      log.error("Failed to decode base64 content: {}", e.getMessage());
      return Optional.empty();
    }
  }
}
