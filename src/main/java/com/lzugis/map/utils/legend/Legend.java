package com.lzugis.map.utils.legend;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Legend {

	/**
	 * 传数据
	 * 
	 * @param data
	 *            数组 按顺序存放 ["0xffffff,0-2","0x000000,3-4"]
	 * @param width
	 *            宽度
	 * @param lr
	 *            左边 "L" 右边 "R"
	 */

	public Legend(String[] data, int width, String lr) {
		this.data = data;
		this.width = width;
		this.lr = lr;
	}

	private int width = 0;
	private int height = 0;
	private int x = 0;
	private int y = 0;
	private String[] data = null;
	private String lr = "L";

	/**
	 * 绘制
	 * 
	 * @param img
	 *            图片对象
	 */
	public boolean draw(BufferedImage img) {
		try {
			if (data != null && data.length > 0) {
                int strokAl = 0;
                int gay = 0;
                int fillAl = 255;
				int imgWidth = img.getWidth();
				int imgHeight = img.getHeight();
				this.height = 20 + data.length * 16 + 5 + 10;

				if (this.lr.toLowerCase().equals("r")) {
					this.x = imgWidth - this.width*2-10;
					strokAl =255;
					fillAl = 0;
					gay = 0;
				}else if(this.lr.toLowerCase().equals("or")){
					this.x = imgWidth-this.width-5 ;
					strokAl =0;
					fillAl = 255;
					gay = 5;
				} 
//				else if(this.lr.toLowerCase().equals("ol")){
//					this.x = -this.width-5;
//					strokAl =0;
//					fillAl = 255;
//					gay = 5;
//				} 
				else {
					this.x = 0;
					strokAl =255;
					fillAl = 0;
					gay = 0;
				}
               // System.err.println(this.x);
				this.y = imgHeight - this.height;

				Graphics2D graphics = (Graphics2D) img.getGraphics();
				//边框
				graphics.setColor(new Color(0, 0, 0,strokAl));
				graphics.drawRect(this.x+2, this.y, width+2, height-2);
				// 背景
				graphics.setColor(new Color(255, 255, 255, fillAl));
				graphics.fillRect(x, y, width+gay, height);

				// 标题
				Font f1 = new Font("Helvetica", Font.PLAIN, 16);
				graphics.setFont(f1);
				graphics.setColor(new Color(00, 00, 00));
				int tx = x + (width - 2 * 12) / 2 - 10;
				
				graphics.drawString("图例", tx, y + 25);

				y = y + 20 + 10;

				int i = 0;
				// 绘制图例
				for (int index = 0; index < data.length; index++) {
					String[] key_value = data[index].split(",");
					String key = key_value[0];
					String value = key_value[1];
					int iWidth = this.width - 10;
					int iHeight = 11;
					int iX = x + 5;
					int iY = y + i * iHeight + (i + 1) * 5;

					 //System.err.println(iWidth +" "+iHeight+"  "+ iX +"  "+ iY
					// );
					LegendItem item = new LegendItem(iWidth, iHeight, iX, iY,
							key, value);
					item.draw(graphics);
					i++;
				}

			}

			return true;
		} catch (Exception e) {
			this.width = 0;
			this.height = 0;
			this.x = 0;
			this.y = 0;
			this.data = null;
			e.printStackTrace();
			return false;
		}
	}

}
