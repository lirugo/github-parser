package com.lirugo.github.parser.model;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class RepoFile {

  Repo repo;
  String name;
  String path;
  Integer size;
  Optional<String> content;
}
