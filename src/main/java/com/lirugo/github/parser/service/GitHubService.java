package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
import java.util.List;
import java.util.Optional;

public interface GitHubService {

  String API_BASE_URL = "https://api.github.com";
  String REPO_URL = API_BASE_URL + "/users/%s/repos"; // owner
  String FILE_URL = API_BASE_URL + "/repos/%s/%s/contents"; // owner, repo
  String FILE_CONTENT_URL = API_BASE_URL + "/repos/%s/%s/contents/%s"; // owner, repo, filePath

  List<Repo> getRepos(String owner);

  List<RepoFile> getFiles(String owner, String fileRegExp, Integer fileLimit);
  Optional<String> getFileContent(String owner, String repo, String filePath);

}
