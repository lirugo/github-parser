package com.lirugo.github.parser.service;

import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
import java.util.List;

public interface GitHubService {

  String API_BASE_URL = "https://api.github.com";
  String REPO_URL = API_BASE_URL + "/users/%s/repos";
  String FILE_URL = API_BASE_URL + "/repos/%s/%s/contents";

  List<Repo> getRepos(String owner);

  List<RepoFile> getFiles(String owner, String fileReqExp);

}
