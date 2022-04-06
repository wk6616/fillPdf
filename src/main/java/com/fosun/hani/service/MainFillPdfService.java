package com.fosun.hani.service;

import com.fosun.hani.vo.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public interface MainFillPdfService {

    Result fillPdf(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
