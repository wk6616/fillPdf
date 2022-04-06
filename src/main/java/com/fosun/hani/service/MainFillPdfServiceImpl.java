package com.fosun.hani.service;

import cn.hutool.core.io.IoUtil;
import com.fosun.hani.vo.Result;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.*;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class MainFillPdfServiceImpl implements MainFillPdfService {

    @Override
    public Result fillPdf(HttpServletRequest request, HttpServletResponse response) throws Exception{
        MultipartHttpServletRequest params=((MultipartHttpServletRequest) request);
        Map<String, MultipartFile> multiFileMap = params.getFileMap();
        MultipartFile file = params.getFile("file");
        if(file==null||!file.getContentType().endsWith("/pdf")){
            return Result.fail("请上传pdf文件");
        }
        //参数
        Map<String, String[]> parameterMap = params.getParameterMap();
        if(parameterMap.isEmpty()){
            return Result.fail("填充数据为空");
        }
        PdfReader reader = null;
        PdfWriter writer = null;
        PdfDocument pdf = null;
        InputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            String fileName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
            byte [] byteArr = file.getBytes();
            //获取输入流
            inputStream = new ByteArrayInputStream(byteArr);
            //读取输入
            reader = new PdfReader(inputStream);
            //写输出文件
            writer = new PdfWriter(outputStream);
            pdf = new PdfDocument(reader, writer);
            PdfAcroForm form = PdfAcroForm.getAcroForm(pdf, true);
            Map<String, PdfFormField> fields = form.getFormFields();
            //图片
            Map imgMap = new HashMap();
            for(Map.Entry<String, MultipartFile> im:multiFileMap.entrySet()){
                MultipartFile paramsFile = params.getFile(im.getKey());
                // 文件原名
                String fileNameOriginal = paramsFile.getOriginalFilename();
                String mimeType = request.getServletContext().getMimeType(fileNameOriginal);
                if (mimeType.startsWith("image/")) {
                    imgMap.put(im.getKey(),paramsFile.getBytes());

                }
            }
            fields.forEach((name,formField)->{
                String[] value = parameterMap.get(name);
                //文本域
                if (formField instanceof PdfTextFormField) {
                    if(value!=null&&value.length>0){
                        formField.setValue(value[0]);
                    }
                }
                //选项
                if( formField instanceof PdfButtonFormField){
                    if(value!=null&&value.length>0){
                        formField.setValue(value[0]);
                    }
                }
                //下拉框
                if( formField instanceof PdfChoiceFormField){
                    if(value!=null&&value.length>0){
                        formField.setValue(value[0]);
                    }
                }
                //图片
                if( formField instanceof  PdfSignatureFormField){
                    if(imgMap.containsKey(name)){
                        fillPdfImg(formField, (byte[]) imgMap.get(name));
                    }
                }
            });
            //设置文本域不能修改
            form.flattenFields();
            //设置响应头信息
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            //刷新
            outputStream.flush();
        } catch (Exception e) {
            return Result.fail("生成文件异常"+e);
        } finally {
            //先声明的流后关掉
            IoUtil.close(inputStream);
            IoUtil.close(pdf);
            IoUtil.close(outputStream);
            IoUtil.close(reader);
            IoUtil.close(writer);
        }
        return null;
    }

    public static void fillPdfImg( PdfFormField formField,byte[] imagedata){
        Rectangle rectangle = formField.getWidgets().get(0).getRectangle().toRectangle();
        //画布
        PdfCanvas canvas = new PdfCanvas(formField.getWidgets().get(0).getPage());
        //存储pdfdocument
        canvas.saveState();
        PdfExtGState pdfExtGState =new PdfExtGState();
        //设置pdf笔画透明度
        pdfExtGState.setFillOpacity(1f);

        canvas.setExtGState(pdfExtGState);
        ImageData imageData = ImageDataFactory.create(imagedata);
        //获取 签名域的长度宽
        float rectangleWidth = rectangle.getWidth();
        float rectangleHeight = rectangle.getHeight();
        //图画大小
        float imageWidth = imageData.getWidth();
        float imageHeight = imageData.getHeight();

        float tempWidth = 0;
        float tempHeight = 0;
        // 压缩宽度
        int result = 1;
        if (imageWidth > rectangleWidth) {
            if(imageHeight < rectangleHeight){
                tempWidth= rectangleWidth;
                tempHeight =  imageHeight;
            }else{
                result = 2;
                tempWidth= imageWidth;
                tempHeight= rectangleHeight;
            }

        } else {
            if (imageHeight > rectangleHeight) {
                //压缩高度
                tempHeight = rectangleHeight;
                result = 2;
            } else {
                result = 3;
            }
        }
        float y = 0;
        if (result == 1) { // 压缩宽度
            y = rectangleHeight - tempHeight;
        } else if (result == 3) { // 不压缩
            y = rectangleHeight - imageHeight;
        }
        // y/=2; // 如果想要图片在表单域的上下对齐，这个值除以2就行。同理可以计算x的偏移
        if (result == 1) {
            canvas.addImage(imageData, rectangle.getX(), rectangle.getY() + y, tempWidth, false);
        } else if (result == 2) {
            canvas.addImage(imageData, rectangle.getX(), rectangle.getY(), tempHeight, false, false);
        } else if (result == 3) {
            canvas.addImage(imageData, rectangle.getX(), rectangle.getY() + y, false);
        }
        canvas.restoreState();
    }

}
