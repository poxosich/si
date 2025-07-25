package com.example.si.controller;


import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class PictureController {

    @Value("${si.pic.package.url}")
    private String path;

    @GetMapping(value = "/grtImage", produces = MediaType.IMAGE_JPEG_VALUE)///
    @ResponseBody
    public byte[] getImage(@RequestParam(name = "picture") String picture) throws IOException {
        File file = new File(path, picture);
        if (file.exists()) {
            return IOUtils.toByteArray(new FileInputStream(file));
        }
        return new byte[0];
    }
}
