package myCompany.BackEnd.generators;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGen {
	
	public static BufferedImage generate_qr(Long produktid,String modell,String serienummer) {
		try {
			int tThumbWidth = 200;
			int tThumbHeight = 200;
			Font h = new Font("monospaced.0", Font.PLAIN, 18);
			
			QRCodeWriter qrCodeEncoder = new QRCodeWriter();
			BitMatrix result = qrCodeEncoder.encode(produktid+"", BarcodeFormat.QR_CODE, 200, 200);
			BufferedImage image = MatrixToImageWriter.toBufferedImage(result);
			
			BufferedImage tThumbImage = new BufferedImage( tThumbWidth*3, tThumbHeight, BufferedImage.TYPE_INT_RGB );
			  Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
			  tGraphics2D.setBackground( Color.WHITE );
			  tGraphics2D.setPaint( Color.WHITE );
			  tGraphics2D.fillRect( 0, 0, tThumbWidth*3, tThumbHeight );
			  tGraphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
			  tGraphics2D.drawImage( image, 0, 0, tThumbWidth, tThumbHeight, null );
			  tGraphics2D.setPaint( Color.BLACK );
			  tGraphics2D.setFont(h);
			  tGraphics2D.drawString("Model code: "+modell.toUpperCase(), tThumbWidth, tThumbHeight/3);
			  tGraphics2D.drawString("S/N: "+serienummer.toUpperCase(), tThumbWidth, tThumbHeight/2);
			  tGraphics2D.drawString("REF: "+produktid, tThumbWidth, tThumbHeight-38);
			  
			  return tThumbImage;
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}
	
}
