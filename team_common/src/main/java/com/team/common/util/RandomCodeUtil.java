package com.team.common.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// TODO: Auto-generated Javadoc
/**
 * 随机校验码生成工具.
 * 
 * @author david
 * @version 1.0
 */
public class RandomCodeUtil {

	/**
	 * 生成随机码存入session中，并将随机码以图像的方式输出至客户端.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @author david
	 * 
	 *         完成日期: 2013-7-17
	 */
	public static void genRandom(HttpServletRequest request, HttpServletResponse response) throws IOException {
		genRandom(request, response, 100, 37);
	}

	/**
	 * 生成随机码存入session中，并将随机码以图像的方式输出至客户端.// 定义图片的宽度和高度
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @author david
	 * 
	 *         完成日期: 2013-7-17
	 */
	public static void genRandom(HttpServletRequest request, HttpServletResponse response, int width, int height) throws IOException {
		// 首先设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 创建一个图像对象
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 得到图像的环境对象
		Graphics2D g = image.createGraphics();

		Random random = new Random();
		// 用随机颜色填充图像背景

		Color color = new Color(123, 173, 19);
		Color color2 = new Color(254, 254, 254);

		g.setColor(color);
		g.fillRect(0, 0, width, 22);

		g.setColor(color);
		g.fillRect(0, 22, width, 18);

		for (int i = 0; i < 10; i++) {
			g.setColor(color2);
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			// g.drawOval(x, y, 4, 4);
		}

		char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
				'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
		// 随机字符串
		String sRand = "";

		int fontHeight = height - 4;
		int fontX = (width - 4) / (4);
		int fontY = (height - 4) - 4;
		// 设置字体，下面准备画随机数
		g.setFont(new Font("", Font.PLAIN, fontHeight));
		for (int i = 0; i < 4; i++) {
			// 生成四个随机字符
			String rand = String.valueOf(codeSequence[random.nextInt(36)]);
			sRand += rand;

			AffineTransform trans = new AffineTransform();
			if (i != 0 && i != 3) {
				// 想文字旋转一定的角度
				// trans.rotate(random.nextInt(30) * 3.14 / 180, 15 * i + 8, 7);
			} else {
				// 缩放文字
				float scaleSize = random.nextFloat() + 0.96f;
				if (scaleSize > 1f) {
					scaleSize = 1f;
				}
				trans.scale(scaleSize, scaleSize);
			}

			g.setTransform(trans);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// 将随机字符画在图像上
			g.drawString(rand, fontX * i, fontY);
			// g.setBackground(color);
			g.setColor(color2);
			g.setStroke(new BasicStroke(3.5f));

		}
		HttpSession session = request.getSession();
		// 将生成的随机数字字符串写入Session
		session.setAttribute("randcode", sRand);
		// 使图像生效
		g.dispose();
		// 输出图像到页面
		ImageIO.write(image, "JPEG", response.getOutputStream());
	}

	/**
	 * 产生一个随机的颜色.
	 * 
	 * @param fc
	 *            颜色分量最小值
	 * @param bc
	 *            颜色分量最大值
	 * @return the rand color
	 * @return
	 */
	private static Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255) {
			fc = 255;
		}
		if (bc > 255) {
			bc = 255;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}
}
