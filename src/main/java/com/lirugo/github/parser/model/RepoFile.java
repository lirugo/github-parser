package com.lirugo.github.parser.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class RepoFile {

  Repo repo;
  String name;
  Integer size;
}
