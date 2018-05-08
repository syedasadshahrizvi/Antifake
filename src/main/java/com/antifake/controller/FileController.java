package com.antifake.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.antifake.VO.ResultVO;
import com.antifake.enums.ResultEnum;
import com.antifake.exception.AntiFakeException;
import com.antifake.utils.FileUtil;
import com.antifake.utils.ResultVOUtil;
import com.antifake.utils.UUIDUtil;

@RestController
@RequestMapping("/file")
public class FileController {

	@PostMapping
	@RequestMapping("/upload")
	public ResultVO fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
		String contentType = file.getContentType();
		if(contentType.indexOf("image/")==-1) {
			throw new AntiFakeException(ResultEnum.IMG_TYPE_ERROR.getCode(), ResultEnum.IMG_TYPE_ERROR.getMessage());
		}
		String imgType = contentType.replaceAll("image/", ".");
		String filePath = request.getSession().getServletContext().getRealPath("imgupload/");
		String fileName = UUIDUtil.get32UUID()+imgType;
		try {
			FileUtil.uploadFile(file.getBytes(), filePath, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultVOUtil.success("imgupload/" + fileName);
	}

}
