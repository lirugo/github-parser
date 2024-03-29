package com.lirugo.github.parser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.lirugo.github.parser.model.FileType;
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

    var str1 = "hello world hello";
    RepoFile file1 = new RepoFile("name", "path1", FileType.blob, str1.length(), "url", Optional.of(str1));
    RepoFile file2 = new RepoFile("name", "path2", FileType.blob, readme1Content.length(), "url", Optional.of(readme1Content));
    List<RepoFile> files = List.of(file1, file2);

    List<Word> result = service.countWordFrequency(files, 4, 3);

    assertEquals(3, result.size());
    assertEquals("foobar", result.get(0).word());
    assertEquals(8, result.get(0).frequency());
    assertEquals("returns", result.get(1).word());
    assertEquals(3, result.get(1).frequency());
  }
}
