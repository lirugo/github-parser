package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
import com.lirugo.github.parser.model.Word;
import java.util.List;
import java.util.Optional;

public interface GitHubService {

  String API_BASE_URL = "https://api.github.com";
  String REPO_URL = API_BASE_URL + "/users/%s/repos"; // owner
  // https://docs.github.com/ru/rest/git/trees?apiVersion=2022-11-28#get-a-tree
  String FILE_URL = API_BASE_URL + "/repos/%s/%s/git/trees/%s?recursive=1"; // owner, repo, branch

  List<Repo> getRepos(String owner);

  List<RepoFile> getFiles(String owner, String fileRegExp, Integer fileLimit);

  Optional<String> getFileContent(String url);

  List<Word> getWordFrequency(String owner, String fileRegExp, Integer fileLimit,
      Integer minLetter, Integer topLimit);

}
