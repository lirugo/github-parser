package com.lirugo.github.parser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lirugo.github.parser.model.Repo;
import com.lirugo.github.parser.model.RepoFile;
import com.lirugo.github.parser.model.Word;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ParserServiceImplTest {

  @Test
  void testCountWordFrequency() {
    File readme1File = new File(
        getClass().getClassLoader().getResource("data/README-1.md").getFile());
    var readme1Content = "";
    try {
      readme1Content = Files.readString(readme1File.toPath());
    } catch (IOException e) {
      log.warn("Failed to read file: {}", readme1File.getAbsolutePath());
    }

    ParserServiceImpl service = new ParserServiceImpl();

    Repo repo = new Repo(1, "testOwner");
    RepoFile file1 = new RepoFile(repo, "name", "path1", 1, Optional.of("hello world hello"));
    RepoFile file2 = new RepoFile(repo, "name", "path2", 1, Optional.of(readme1Content));
    List<RepoFile> files = List.of(file1, file2);

    List<Word> result = service.countWordFrequency(files, 10, 3);

    assertEquals(3, result.size());
    assertEquals("foobar", result.get(0).word());
    assertEquals(8, result.get(0).frequency());
    assertEquals("to", result.get(1).word());
    assertEquals(4, result.get(1).frequency());
  }
}
