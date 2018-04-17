package com.playtika.test;

import com.google.common.collect.Maps;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {FileWriter.class})
public class FileWriterTest {

  @MockBean
  private Configuration configuration;
  @Autowired
  private FileWriter writer;

  @Before
  public void init(){
    Mockito.doReturn("test-result.tmp").when(configuration).getSavePath();
  }

  @Test
  public void test_writeFile_success(){
//    File tmpFile = null;
//    try {
//      tmpFile = File.createTempFile("test-result", ".tmp");
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
    Map<String, String> pathes = Maps.newHashMap();
    pathes.put("Hello", "");
    FileWriter writer = new FileWriter();
    writer.write(pathes);
  }
}