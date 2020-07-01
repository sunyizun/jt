package com.jt.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jt.vo.ImageVO;




@Service
@PropertySource("classpath:/properties/image.properties")
public class FileServiceImpl implements FileService {
	//定义上传的路径
	@Value("${image.localDirPath}")
	private String localDirPath;
	//定义虚拟的路径
	@Value("${image.urlPath}")
	private String urlPath;
	@Override
	public ImageVO updateFile(MultipartFile uploadFile) {
		ImageVO imageVO = new ImageVO();
		//1.获取文件名称 a.jpg,A.jpg
		String fileName =uploadFile.getOriginalFilename();
		System.out.println("fileName="+fileName);
		//将字符转化为小写
		fileName=fileName.toLowerCase();
		System.out.println("fileName="+fileName);
		
		System.out.println("走到了");
		//2.判断是否问文件类型(jpg|png|gif)
		if(!fileName.matches("^.*(jpg|png|gif)$")){
			imageVO.setError(1);//上传有误
			return imageVO;
		}
		System.out.println("走到了11");
		//3.判断是否为恶意程序
		try {
			BufferedImage bufferedImage  =ImageIO.read(uploadFile.getInputStream());
			int width = bufferedImage.getWidth();
			int height = bufferedImage.getHeight();
			System.out.println("width="+width);
			if(width==0||height==0) {
				imageVO.setError(1);//上传有误
				return imageVO;
			}
			System.out.println("也到这儿");
			//4时间转化成字符串
			String dateDir = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
			System.out.println("dateDir="+dateDir);
			//5准备文件夹
			String localDir =localDirPath+dateDir;
			File fileDir = new File(localDir);
			if(!fileDir.exists()) {
				//如果不存在创建文件夹
				fileDir.mkdirs();
			}
			System.out.println("也到这儿1");
			//6使用UUID定义文件名称uuid.jpg
			String uuid =UUID.randomUUID().toString().replace("-", "");
			//图片类型a.jpg动态获取.jpg
			String fileType=fileName.substring(fileName.lastIndexOf("."));
			//拼接新的文件名称
			//D:/tts9/image/yyyy/MM/dd/文件名称.类型
			String realLocalPath=localDir+"/"+uuid+fileType;
			//7完成文件上传
			uploadFile.transferTo(new File(realLocalPath));
			System.out.println("也到这儿2");
			//8拼接url路径
			String realUrlPath=urlPath+dateDir+"/"+uuid+fileType;
			//将文件回传页面
			imageVO.setError(0).setHeight(height).setWidth(width).setUrl(realUrlPath);
			System.out.println("也到这儿3");
		} catch (IOException e) {
			e.printStackTrace();
			imageVO.setError(1);//上传有误
			return imageVO;
		}
		System.out.println("也到这儿4");
		return imageVO;
	}

}
