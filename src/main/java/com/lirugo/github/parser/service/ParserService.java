package com.lirugo.github.parser.service;

import java.util.List;

public interface ParserService {
  List<String> parse(String username, String fileReqExp, Integer fileLimit);
}
