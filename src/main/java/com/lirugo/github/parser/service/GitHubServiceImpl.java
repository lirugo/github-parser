package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.FileType;
import com.lirugo.github.parser.model.GitTree;
import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
import com.lirugo.github.parser.model.Word;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GitHubServiceImpl implements GitHubService {

  private static final String DEFAULT_BRANCH = "master";
  RestTemplate restTemplate;
  ParserService parserService;

  @Override
  public List<Repo> getRepos(String owner) {
    List<Repo> res = new ArrayList<>();

    try {
      ResponseEntity<Repo[]> response = restTemplate.getForEntity(String.format(REPO_URL, owner),
          Repo[].class);
      if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
        res = Arrays.asList(response.getBody());
      } else {
        log.warn("Failed to retrieve repos for owner: {}, code: {}, body: {}", owner,
            response.getStatusCode(), response.getBody());
      }
    } catch (RestClientException e) {
      log.warn("Failed to retrieve repos for owner: {}, ex: {}", owner, e.getMessage());
    }

    return res;
  }

  @Override
  public List<RepoFile> getFiles(String owner, String fileRegExp, Integer fileLimit) {
    var files = new ArrayList<RepoFile>();

    getRepos(owner)
        .stream()
        .map(repo -> {
          ResponseEntity<GitTree> response = null;
          var repoFiles = new ArrayList<RepoFile>();

          try {
            response = restTemplate.getForEntity(
                String.format(FILE_URL, owner, repo.name(), DEFAULT_BRANCH),
                GitTree.class);

            if (response.getStatusCode().is2xxSuccessful()
                && response.getBody() != null
                && response.getBody().getTree() != null) {
              response.getBody().getTree()
                  .stream()
                  .filter(file -> file.getType().equals(FileType.blob))
                  .peek(file -> {
                        var path = file.getPath();
                        var fileName = path.substring(path.lastIndexOf("/") + 1);
                        file.setName(fileName);
                      }
                  )
                  .filter(file -> file.getName().matches(fileRegExp))
                  .peek(file -> file.setContent(
                      getFileContent(file.getUrl())))
                  .forEach(repoFiles::add);
            } else {
              log.warn("Failed to retrieve files for repo: {}, code: {}, body: {}", repo.name(),
                  response.getStatusCode(), response.getBody());
            }

          } catch (RestClientException e) {
            log.warn("Failed to retrieve files for repo: {}", repo.name());
          }

          return repoFiles;
        })
        .limit(fileLimit)
        .forEach(files::addAll);

    return files;
  }

  @Override
  public Optional<String> getFileContent(String url) {
    var response = restTemplate.getForEntity(url, RepoFile.class);
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

  @Override
  public List<Word> getWordFrequency(String owner, String fileRegExp, Integer fileLimit,
      Integer minLetter,
      Integer topLimit) {
    var files = getFiles(owner, fileRegExp, fileLimit);

    return parserService.countWordFrequency(files, minLetter, topLimit);
  }

  private Optional<String> decodeBase64(Optional<String> base64) {
    if (base64.isEmpty()) {
      return Optional.empty();
    }

    try {
      var contentBase64 = base64.get().replace("\r", "").replace("\n", "");
      var decodedBytes = Base64.getDecoder().decode(contentBase64);

      return Optional.of(new String(decodedBytes));
    } catch (IllegalArgumentException e) {
      log.error("Failed to decode base64 content: {}", e.getMessage());
      return Optional.empty();
    }
  }
}
