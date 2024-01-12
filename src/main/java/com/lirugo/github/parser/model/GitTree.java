package com.lirugo.github.parser.model;

import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GitTree {
  String sha;
  String url;
  List<RepoFile> tree;
}
