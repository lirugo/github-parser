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

  String path;
  String name;
  FileType type;
  Integer size;
  String url;
  Optional<String> content;
}
