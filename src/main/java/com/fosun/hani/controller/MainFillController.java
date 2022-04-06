package com.fosun.hani.controller;

import com.fosun.hani.service.MainFillPdfService;
import com.fosun.hani.vo.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/main")
public class MainFillController {

    @Resource
    private MainFillPdfService fillPDFService;

    @RequestMapping(value = "/fillPdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST)
    public Result fillPdf(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Result result = fillPDFService.fillPdf(request,response);
        return result;
    }

}
