package org.sopt.pawkey.backendapi.global.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.springframework.web.multipart.MultipartFile;

public class ImageDimensionUtil {

	public static List<Integer> getImageDimensions(MultipartFile file) throws IOException {
		try (ImageInputStream in = ImageIO.createImageInputStream(file.getInputStream())) {
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext()) {
				ImageReader reader = readers.next();
				try {
					reader.setInput(in);
					return List.of(reader.getWidth(0), reader.getHeight(0));
				} finally {
					reader.dispose();
				}
			}
		}
		throw new IOException("Unsupported image format or no reader found");
	}
}
